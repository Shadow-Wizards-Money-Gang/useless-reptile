package nordmods.uselessreptile.datagen.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.init.URItems;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class URModelProvider extends FabricModelProvider {
    public URModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(URItems.WYVERN_SPAWN_EGG, item("template_spawn_egg"));
        itemModelGenerator.register(URItems.LIGHTNING_CHASER_SPAWN_EGG, item("template_spawn_egg"));
        itemModelGenerator.register(URItems.MOLECLAW_SPAWN_EGG, item("template_spawn_egg"));
        itemModelGenerator.register(URItems.RIVER_PIKEHORN_SPAWN_EGG, item("template_spawn_egg"));

        itemModelGenerator.register(URItems.WYVERN_SKIN, Models.GENERATED);
        itemModelGenerator.register(URItems.VORTEX_HORN, item("goat_horn", TextureKey.LAYER0));
        itemModelGenerator.register(URItems.IRON_VORTEX_HORN, item("goat_horn", TextureKey.LAYER0));
        itemModelGenerator.register(URItems.GOLD_VORTEX_HORN, item("goat_horn", TextureKey.LAYER0));
        itemModelGenerator.register(URItems.DIAMOND_VORTEX_HORN, item("goat_horn", TextureKey.LAYER0));
        itemModelGenerator.register(URItems.NETHERITE_VORTEX_HORN, item("goat_horn", TextureKey.LAYER0));

        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_CHESTPLATE_IRON, UselessReptile.id("item/armor/dragon/armor_iron_body"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_CHESTPLATE_GOLD, UselessReptile.id("item/armor/dragon/armor_gold_body"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_CHESTPLATE_DIAMOND, UselessReptile.id("item/armor/dragon/armor_diamond_body"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_TAIL_ARMOR_IRON, UselessReptile.id("item/armor/dragon/armor_iron_tail"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_TAIL_ARMOR_GOLD, UselessReptile.id("item/armor/dragon/armor_gold_tail"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_TAIL_ARMOR_DIAMOND, UselessReptile.id("item/armor/dragon/armor_diamond_tail"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_HELMET_IRON, UselessReptile.id("item/armor/dragon/armor_iron_head"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_HELMET_GOLD, UselessReptile.id("item/armor/dragon/armor_gold_head"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.DRAGON_HELMET_DIAMOND, UselessReptile.id("item/armor/dragon/armor_diamond_head"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.MOLECLAW_HELMET_IRON, UselessReptile.id("item/armor/dragon/armor_iron_head_moleclaw"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.MOLECLAW_HELMET_GOLD, UselessReptile.id("item/armor/dragon/armor_gold_head_moleclaw"));
        registerDragonArmorModel(itemModelGenerator.writer, URItems.MOLECLAW_HELMET_DIAMOND, UselessReptile.id("item/armor/dragon/armor_diamond_head_moleclaw"));
    }

    protected static Model item(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    protected JsonObject generateDragonArmor(Item item, Map<TextureKey, Identifier> textureMap) {
        JsonArray translation = new JsonArray();
        translation.add(0);
        translation.add(-0.4);
        translation.add(0);

        JsonArray scale = new JsonArray();
        scale.add(0.55);
        scale.add(0.55);
        scale.add(0.55);

        JsonObject thirdpersonRighthand = new JsonObject();
        thirdpersonRighthand.add("translation", translation);
        thirdpersonRighthand.add("scale", scale);

        JsonObject display = new JsonObject();
        display.add("thirdperson_righthand", thirdpersonRighthand);

        Identifier itemID = ModelIds.getItemModelId(item);
        JsonObject root = Models.GENERATED.createJson(Identifier.of(itemID.getNamespace(), itemID.getPath()), textureMap);
        root.add("display", display);

        return root;
    }

    protected void registerDragonArmorModel(BiConsumer<Identifier, Supplier<JsonElement>> writer, Item item, Identifier texture) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(texture), writer, ((id, textures) -> generateDragonArmor(item, textures)));
    }
}
