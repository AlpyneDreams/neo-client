// copyright lololol
package lol.neoclient.mixin;

// See: https://github.com/Tom-The-Geek/DebugRenderers/blob/master/src/main/java/me/geek/tom/debugrenderers/mixins/mixins/MixinDebugRenderer.java

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lol.neoclient.DebugRendererSettings;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.BeeDebugRenderer;
import net.minecraft.client.render.debug.BreezeDebugRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.GameEventDebugRenderer;
import net.minecraft.client.render.debug.GoalSelectorDebugRenderer;
import net.minecraft.client.render.debug.LightDebugRenderer;
import net.minecraft.client.render.debug.PathfindingDebugRenderer;
import net.minecraft.client.render.debug.RaidCenterDebugRenderer;
import net.minecraft.client.render.debug.StructureDebugRenderer;
import net.minecraft.client.render.debug.VillageDebugRenderer;
import net.minecraft.client.render.debug.VillageSectionsDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Final @Shadow public PathfindingDebugRenderer pathfindingDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer waterDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer heightmapDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer collisionDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer supportingBlockDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer neighborUpdateDebugRenderer;
	@Final @Shadow public StructureDebugRenderer structureDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer skyLightDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer worldGenAttemptDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer blockOutlineDebugRenderer;
	@Final @Shadow public DebugRenderer.Renderer chunkLoadingDebugRenderer;
	@Final @Shadow public VillageDebugRenderer villageDebugRenderer;
	@Final @Shadow public VillageSectionsDebugRenderer villageSectionsDebugRenderer;
	@Final @Shadow public BeeDebugRenderer beeDebugRenderer;
	@Final @Shadow public RaidCenterDebugRenderer raidCenterDebugRenderer;
	@Final @Shadow public GoalSelectorDebugRenderer goalSelectorDebugRenderer;
	@Final @Shadow public GameEventDebugRenderer gameEventDebugRenderer;
	@Final @Shadow public LightDebugRenderer lightDebugRenderer;
	@Final @Shadow public BreezeDebugRenderer breezeDebugRenderer;

	//@Final @Shadow public DebugRenderer.Renderer chunkBorderDebugRenderer;
	//@Final @Shadow public GameTestDebugRenderer gameTestDebugRenderer;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V")
    public void onRender(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo info) {
        if (!DebugRendererSettings.enabled.get()) return;
        if (DebugRendererSettings.showPathfindingDebugRenderer.get()) pathfindingDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showWaterDebugRenderer.get()) waterDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showHeightmapDebugRenderer.get()) heightmapDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showCollisionDebugRenderer.get()) collisionDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showSupportingBlockDebugRenderer.get()) supportingBlockDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showNeighborUpdateDebugRenderer.get()) neighborUpdateDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showStructureDebugRenderer.get()) structureDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showSkyLightDebugRenderer.get()) skyLightDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showWorldGenAttemptDebugRenderer.get()) worldGenAttemptDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showBlockOutlineDebugRenderer.get()) blockOutlineDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showChunkLoadingDebugRenderer.get()) chunkLoadingDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showVillageDebugRenderer.get()) villageDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showVillageSectionsDebugRenderer.get()) villageSectionsDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showBeeDebugRenderer.get()) beeDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showRaidCenterDebugRenderer.get()) raidCenterDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showGoalSelectorDebugRenderer.get()) goalSelectorDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showGameEventDebugRenderer.get()) gameEventDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showLightDebugRenderer.get()) lightDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        if (DebugRendererSettings.showBreezeDebugRenderer.get()) breezeDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);    
    }

}
