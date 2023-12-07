package net.tadditions.mod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.tardis.mod.items.ArtronCapacitorItem;

public class SubspaceCapEnchant extends Enchantment {
    protected SubspaceCapEnchant(Rarity rarityIn, EquipmentSlotType[] slots) {
        super(rarityIn, EnchantmentType.create("artron", item -> item instanceof ArtronCapacitorItem), slots);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ArtronCapacitorItem;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean canVillagerTrade() {
        return true;
    }

    @Override
    public boolean canGenerateInLoot() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }


}
