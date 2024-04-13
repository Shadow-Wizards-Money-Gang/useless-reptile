package nordmods.uselessreptile.client.util.model_data;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;
import nordmods.uselessreptile.client.util.model_data.base.EquipmentModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModelDataUtil {
    @Nullable
    public static DragonModelData getDragonModelData(URDragonEntity dragon, boolean viaNametag) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        String dragonID = dragon.getDragonID();
        Map<String, DragonModelData> dragonModelDataMap = DragonModelData.dragonModelDataHolder.get(dragonID);
        DragonModelData dragonModelData;
        if (!viaNametag || URClientConfig.getConfig().disableNamedEntityModels) dragonModelData = dragonModelDataMap.get(dragon.getVariant());
        else {
            DragonModelData temp = dragonModelDataMap.get(ResourceUtil.parseName(dragon));
            if (temp != null && temp.nametagAccessible()) dragonModelData = temp;
            else dragonModelData = dragonModelDataMap.get(dragon.getVariant());
        }
        dragon.getAssetCache().setNametagModel(viaNametag);
        return dragonModelData;
    }

    @Nullable
    public static DragonModelData getNametagDragonModelData(URDragonEntity dragon) {
        DragonModelData dragonModelData = getDragonModelData(dragon, true);
        if (dragonModelData == null) dragonModelData = getVariantDragonModelData(dragon);
        return dragonModelData;
    }

    @Nullable
    public static DragonModelData getVariantDragonModelData(URDragonEntity dragon) {
        return getDragonModelData(dragon, false);
    }

    @Nullable
    public static EquipmentModelData getEquipmentModelData(URDragonEntity dragon, Item item) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        Identifier id = Registries.ITEM.getId(item);
        DragonModelData dragonModelData = getNametagDragonModelData(dragon);
        if (dragonModelData != null && dragonModelData.equipmentModelDataOverrides() != null)
            for (EquipmentModelData data : dragonModelData.equipmentModelDataOverrides()) {
                if (data.item().equals(id)) return data;
            }
        return getDefaultEquipmentModelData(dragon, id);
    }

    @Nullable
    private static EquipmentModelData getDefaultEquipmentModelData(URDragonEntity dragon, Identifier id) {
        for (EquipmentModelData data : EquipmentModelData.equipmentModelDataHolder.get(dragon.getDragonID())) {
            if (data.item().equals(id)) return data;
        }
        return null;
    }
}
