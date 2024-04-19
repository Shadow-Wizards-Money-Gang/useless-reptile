package nordmods.uselessreptile.client.model.special;

import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.special.AcidBlastEntity;


public class AcidBlastEntityModel extends DefaultedEntityGeoModel<AcidBlastEntity> {
    public AcidBlastEntityModel() {
        super(new Identifier(UselessReptile.MODID, "acid_blast/acid_blast"));
    }
}
