package nordmods.uselessreptile.client.init;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.renderer.base.DragonEquipmentRenderer;
import nordmods.uselessreptile.client.util.DragonAssetCache;
import nordmods.uselessreptile.client.util.DragonEquipmentAnimatable;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreBakedGeoModel;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.event.GeoRenderEvent;

import java.util.Map;

public class URClientEvents {
    public static void init() {
        GeoRenderEvent.Entity.Post.EVENT.register(event -> {
            if (!(event.getEntity() instanceof URDragonEntity dragon)) return;
            DragonAssetCache dragonAssetCache = dragon.getAssetCache();

            int i = 0;
            for (ItemStack itemStack : dragon.getArmorItems()) {
                int j = i;
                i++;
                if (itemStack.isEmpty() || !ResourceUtil.isResourceReloadFinished) {
                    dragonAssetCache.setEquipmentAnimatable(j, null);
                    continue;
                }

                DragonEquipmentRenderer dragonEquipmentRenderer = new DragonEquipmentRenderer();
                DragonEquipmentAnimatable dragonEquipmentAnimatable = dragonAssetCache.getEquipmentAnimatable(j);
                if (dragonEquipmentAnimatable == null || dragonEquipmentAnimatable.item != itemStack.getItem()) {
                    dragonEquipmentAnimatable = new DragonEquipmentAnimatable(dragon, itemStack.getItem());
                    dragonAssetCache.setEquipmentAnimatable(j, dragonEquipmentAnimatable);
                }

                Identifier id = dragonEquipmentRenderer.getGeoModel().getModelResource(dragonEquipmentAnimatable);
                if (!ResourceUtil.doesExist(id)) continue;
                BakedGeoModel bakedEquipmentModel = dragonEquipmentRenderer.getGeoModel().getBakedModel(id);
                id = dragonEquipmentRenderer.getGeoModel().getTextureResource(dragonEquipmentAnimatable);
                if (!ResourceUtil.doesExist(id)) continue;

                Map<String, CoreGeoBone> equipmentBones = dragonEquipmentAnimatable.equipmentBones;
                if (equipmentBones.isEmpty()) getSaddleBones(equipmentBones, bakedEquipmentModel);

                event.getRenderer().getGeoModel().getAnimationProcessor().getRegisteredBones().forEach(bone -> {
                    GeoBone equipmentBone = (GeoBone) equipmentBones.get(bone.getName());
                    if (equipmentBone != null) {
                        equipmentBone.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                        equipmentBone.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                        equipmentBone.updatePosition(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                    }
                });

                RenderLayer cameo = dragonEquipmentRenderer.getGeoModel().getRenderType(dragonEquipmentAnimatable, id);
                VertexConsumerProvider bufferSource = event.getBufferSource();
                VertexConsumer buffer = bufferSource.getBuffer(cameo);

                dragonEquipmentRenderer.render(event.getPoseStack(), dragonEquipmentAnimatable, bufferSource, cameo, buffer, event.getPackedLight());
            }
        });
    }

    private static void addChildren(Map<String, CoreGeoBone> equipmentBones, CoreGeoBone bone) {
        equipmentBones.put(bone.getName(), bone);
        for (CoreGeoBone child : bone.getChildBones()) addChildren(equipmentBones, child);
    }

    private static void getSaddleBones(Map<String, CoreGeoBone> equipmentBones, CoreBakedGeoModel model) {
        equipmentBones.clear();
        for (CoreGeoBone bone : model.getBones()) addChildren(equipmentBones, bone);
    }
}
