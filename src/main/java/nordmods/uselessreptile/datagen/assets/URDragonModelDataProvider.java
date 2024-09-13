package nordmods.uselessreptile.datagen.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;
import nordmods.uselessreptile.client.util.model_data.base.ModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.UREntities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class URDragonModelDataProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    public URDragonModelDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.pathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "dragon_model_data");
        this.registryLookupFuture = registryLookupFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture.thenCompose((registryLookupFuture) -> {
            addEntries();
            List<CompletableFuture<?>> list = new ArrayList<>();
            DragonModelData.getEntries().forEach(entry -> {
                String dragon = entry.getKey();
                Map<String, DragonModelData> dragonModelDataMap = entry.getValue();
                dragonModelDataMap.forEach((variant, dragonModelData) -> {
                    Path path = this.pathResolver.resolveJson(UselessReptile.id(dragon + "/" + variant));
                    list.add(DataProvider.writeCodecToPath(writer, registryLookupFuture, DragonModelData.CODEC, dragonModelData, path));
                });
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    protected void addEntries() {
        addEntry(UREntities.WYVERN_ENTITY, "green", true);
        addEntry(UREntities.WYVERN_ENTITY, "brown", true);
        addEntry(UREntities.WYVERN_ENTITY, "jeb_", true, true);

        addEntry(UREntities.MOLECLAW_ENTITY, "black", false);
        addEntry(UREntities.MOLECLAW_ENTITY, "brown", false);
        addEntry(UREntities.MOLECLAW_ENTITY, "grey", false);
        addEntry(UREntities.MOLECLAW_ENTITY, "albino", false);

        addEntry(UREntities.LIGHTNING_CHASER_ENTITY, "blue", true);
        addEntry(UREntities.LIGHTNING_CHASER_ENTITY, "grey", true);
        addEntry(UREntities.LIGHTNING_CHASER_ENTITY, "brown", true);
        addEntry(UREntities.LIGHTNING_CHASER_ENTITY, "purple", true);

        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "green", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_green", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "blue", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_blue", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "purple", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_purple", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "teal", true);
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, "dark_teal", true);
    }

    protected void addEntry(EntityType<? extends URDragonEntity> type, String variant, boolean cull, boolean nametagAccessibe) {
        Identifier id = EntityType.getId(type);
        Identifier texture = Identifier.of(id.getNamespace(), "textures/entity/" + id.getPath() + "/" + variant +".png");
        Identifier model = Identifier.of(id.getNamespace(), "geo/entity/" + id.getPath() + "/" + id.getPath() +".geo.json");
        Identifier animation = Identifier.of(id.getNamespace(), "animations/entity/" + id.getPath() + "/" + id.getPath() +".animation.json");
        DragonModelData dragonModelData = new DragonModelData(new ModelData(texture, Optional.of(model), Optional.of(animation), cull, false), Optional.empty(), nametagAccessibe);
        DragonModelData.add(EntityType.getId(type).getPath(), variant, dragonModelData);
    }

    protected void addEntry(EntityType<? extends URDragonEntity> type, String variant, boolean cull) {
        addEntry(type, variant, cull, false);
    }

    @Override
    public String getName() {
        return "Dragon Model Data";
    }
}
