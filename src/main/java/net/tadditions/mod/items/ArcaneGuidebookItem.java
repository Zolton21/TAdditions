package net.tadditions.mod.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import javax.annotation.Nonnull;

public class ArcaneGuidebookItem extends Item implements IAnimatable, ISyncable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String controllerName = "controller";
    public boolean open = false;
    Multimap<Attribute, AttributeModifier> map;

    public ArcaneGuidebookItem(Properties props) {
        super(props);
        GeckoLibNetwork.registerSyncable(this);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 6, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier("Weapon modifier", 6, AttributeModifier.Operation.ADDITION));
        this.map = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        if(equipmentSlot.equals(EquipmentSlotType.MAINHAND)){
            return map;
        } else return super.getAttributeModifiers(equipmentSlot);
    }

    public static boolean isOpen() {
        return ModItems.ARCANE_GUIDEBOOK.getId().equals(PatchouliAPI.get().getOpenBookGui());
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Book book = BookRegistry.INSTANCE.books.get(ModItems.ARCANE_GUIDEBOOK.getId());

        if (playerIn instanceof ServerPlayerEntity) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, this.getRegistryName());
            playerIn.playSound(PatchouliSounds.getSound(book.openSound, PatchouliSounds.book_open), 1F, (float) (0.7 + Math.random() * 0.4));
            int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld)worldIn);
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                return playerIn;
            });
            this.open = true;
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }



    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote()) {
            if (this.open && !isOpen()) {
                int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
                PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                    return entityIn;
                });
                GeckoLibNetwork.syncAnimation(target, this, id, 0);
            } else if(!this.open && isOpen()){
                int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
                PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                    return entityIn;
                });
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }
            if (this.open != isOpen()) {
                this.open = isOpen();
            }
            super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controllerloop", 0.001f, this::predicateIdle));
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    public <P extends Item & IAnimatable> PlayState predicateIdle(AnimationEvent<P> event) {
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("close", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, this.controllerName);
        controller.markNeedsReload();
        AnimationBuilder builder = new AnimationBuilder();
        if (state == 0) {
            controller.transitionLengthTicks = 0.1;
            builder.addAnimation("close", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME).addAnimation("idle", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
            controller.clearAnimationCache();
        } else if (state == 1) {
            controller.clearAnimationCache();
            controller.transitionLengthTicks = 35;
            builder.addAnimation("open", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME).clearAnimations().addAnimation("loop", ILoopType.EDefaultLoopTypes.LOOP);
        }
        controller.setAnimation(builder);
    }
}
