package net.tadditions.mod.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.tadditions.mod.client.model.ZPFChamberBrokenModel;
import net.tadditions.mod.client.model.ZPFChamberModel;
import net.tadditions.mod.items.SubsysItem;
import net.tadditions.mod.tileentity.ZPFChamberBrokenTile;
import net.tadditions.mod.tileentity.ZPFChamberTile;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ZPFChamberBrokenRenderer extends GeoBlockRenderer<ZPFChamberBrokenTile> {
    public ZPFChamberBrokenRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new ZPFChamberBrokenModel());
    }

    @Override
    public RenderType getRenderType(ZPFChamberBrokenTile animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(textureLocation);
    }

    @Override
    public float getHeightScale(ZPFChamberBrokenTile entity) {
        return 0.65F;
    }

    @Override
    public float getWidthScale(ZPFChamberBrokenTile animatable2) {
        return 0.65F;
    }
}
