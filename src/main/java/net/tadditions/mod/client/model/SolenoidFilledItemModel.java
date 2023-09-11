package net.tadditions.mod.client.model;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.tadditions.mod.QolMod;
import net.tadditions.mod.items.AnimatedBlockItem;
import net.tadditions.mod.tileentity.SolenoidFilledTile;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.List;

public class SolenoidFilledItemModel extends AnimatedGeoModel<AnimatedBlockItem> {
    private static final ResourceLocation modelResource = new ResourceLocation(QolMod.MOD_ID, "geo/filled_electromagnetic_solenoid_container.geo.json");
    private static final ResourceLocation animationResource = new ResourceLocation(QolMod.MOD_ID, "animations/filled_electromagnetic_solenoid_container.animation.json");
    private static final List<ResourceLocation> animo = Lists.newArrayList(new ResourceLocation(QolMod.MOD_ID, "textures/blockanim/solenoid/solenoid_container_frame_1.png"), new ResourceLocation(QolMod.MOD_ID, "textures/blockanim/solenoid/solenoid_container_frame_2.png"), new ResourceLocation(QolMod.MOD_ID, "textures/blockanim/solenoid/solenoid_container_frame_3.png"), new ResourceLocation(QolMod.MOD_ID, "textures/blockanim/solenoid/solenoid_container_frame_4.png"), new ResourceLocation(QolMod.MOD_ID, "textures/blockanim/solenoid/solenoid_container_frame_5.png"));
    private int tickCount = 0;
    private int currentIndex = 0;


    @Override
    public ResourceLocation getModelLocation(AnimatedBlockItem object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatedBlockItem object) {
        return animateTexture(animo);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AnimatedBlockItem object) {
        return animationResource;
    }

    public ResourceLocation animateTexture(List<ResourceLocation> list){
        tickCount++;
        if (tickCount >= 70) {
            tickCount = 0;
            currentIndex++;
            if (currentIndex >= list.size()) {
                currentIndex = 0;
            }
        }

        return list.get(currentIndex);
    }
}
