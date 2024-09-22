package nordmods.uselessreptile.datagen.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import nordmods.uselessreptile.common.init.UREntities;
import nordmods.uselessreptile.common.init.URItems;

import java.util.concurrent.CompletableFuture;

public class URRecipeProvider extends FabricRecipeProvider {
    public URRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerDragonHelmetRecipe(exporter, URItems.DRAGON_HELMET_IRON,ConventionalItemTags.IRON_INGOTS);
        offerDragonHelmetRecipe(exporter, URItems.DRAGON_HELMET_GOLD, ConventionalItemTags.GOLD_INGOTS);
        offerDragonHelmetRecipe(exporter, URItems.DRAGON_HELMET_DIAMOND, ConventionalItemTags.DIAMOND_GEMS);

        offerDragonChestplateRecipe(exporter, URItems.DRAGON_CHESTPLATE_IRON,ConventionalItemTags.IRON_INGOTS);
        offerDragonChestplateRecipe(exporter, URItems.DRAGON_CHESTPLATE_GOLD, ConventionalItemTags.GOLD_INGOTS);
        offerDragonChestplateRecipe(exporter, URItems.DRAGON_CHESTPLATE_DIAMOND, ConventionalItemTags.DIAMOND_GEMS);

        offerDragonTailArmorRecipe(exporter, URItems.DRAGON_TAIL_ARMOR_IRON,ConventionalItemTags.IRON_INGOTS);
        offerDragonTailArmorRecipe(exporter, URItems.DRAGON_TAIL_ARMOR_GOLD, ConventionalItemTags.GOLD_INGOTS);
        offerDragonTailArmorRecipe(exporter, URItems.DRAGON_TAIL_ARMOR_DIAMOND, ConventionalItemTags.DIAMOND_GEMS);

        offerMoleclawHelmetRecipe(exporter, URItems.MOLECLAW_HELMET_IRON, URItems.DRAGON_HELMET_IRON);
        offerMoleclawHelmetRecipe(exporter, URItems.MOLECLAW_HELMET_GOLD, URItems.DRAGON_HELMET_GOLD);
        offerMoleclawHelmetRecipe(exporter, URItems.MOLECLAW_HELMET_DIAMOND, URItems.DRAGON_HELMET_DIAMOND);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, URItems.FLUTE)
                .input('R', ConventionalItemTags.RED_DYES)
                .input('G', ConventionalItemTags.GREEN_DYES)
                .input('B', ConventionalItemTags.BLUE_DYES)
                .input('W', ItemTags.PLANKS)
                .input('I', ConventionalItemTags.IRON_INGOTS)
                .pattern("RGI")
                .pattern("BW ")
                .pattern("W  ")
                .criterion("entity_tamed", AdvancementCriterions.entityTamedCondition(UREntities.RIVER_PIKEHORN_ENTITY))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LEATHER, 2)
                .input(URItems.WYVERN_SKIN)
                .criterion("has_material", conditionsFromItem(URItems.WYVERN_SKIN))
                .offerTo(exporter);

        VortexHornRecipeJsonBuilder.create(RecipeCategory.TOOLS, URItems.VORTEX_HORN)
                .input('R', Items.BREEZE_ROD)
                .input('H', Items.GOAT_HORN)
                .pattern(" R ")
                .pattern("RHR")
                .pattern(" R ")
                .criterion("has_material", conditionsFromItem(Items.GOAT_HORN))
                .offerTo(exporter);

        VortexHornRecipeJsonBuilder.create(RecipeCategory.TOOLS, URItems.IRON_VORTEX_HORN)
                .input('R', Items.BREEZE_ROD)
                .input('H', URItems.VORTEX_HORN)
                .input('I', ConventionalItemTags.IRON_INGOTS)
                .pattern("IRI")
                .pattern("RHR")
                .pattern("IRI")
                .criterion("has_material", conditionsFromItem(Items.GOAT_HORN))
                .offerTo(exporter);

        VortexHornRecipeJsonBuilder.create(RecipeCategory.TOOLS, URItems.GOLD_VORTEX_HORN)
                .input('R', Items.BREEZE_ROD)
                .input('H', URItems.IRON_VORTEX_HORN)
                .input('I', ConventionalItemTags.GOLD_INGOTS)
                .pattern("IRI")
                .pattern("RHR")
                .pattern("IRI")
                .criterion("has_material", conditionsFromItem(Items.GOAT_HORN))
                .offerTo(exporter);

        VortexHornRecipeJsonBuilder.create(RecipeCategory.TOOLS, URItems.DIAMOND_VORTEX_HORN)
                .input('R', Items.BREEZE_ROD)
                .input('H', URItems.GOLD_VORTEX_HORN)
                .input('I', ConventionalItemTags.DIAMOND_GEMS)
                .pattern("IRI")
                .pattern("RHR")
                .pattern("IRI")
                .criterion("has_material", conditionsFromItem(Items.GOAT_HORN))
                .offerTo(exporter);

        RecipeProvider.offerNetheriteUpgradeRecipe(exporter, URItems.DIAMOND_VORTEX_HORN, RecipeCategory.TOOLS, URItems.NETHERITE_VORTEX_HORN);
    }

    protected static void offerDragonHelmetRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MMM")
                .pattern("M M")
                .pattern("L L")
                .criterion("has_material", conditionsFromItem(input))
                .offerTo(exporter);
    }

    protected static void offerDragonChestplateRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MLM")
                .pattern("MMM")
                .pattern("LML")
                .criterion("has_material", conditionsFromItem(input))
                .offerTo(exporter);
    }

    protected static void offerDragonTailArmorRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MMM")
                .pattern(" L ")
                .criterion("has_material", conditionsFromItem(input))
                .offerTo(exporter);
    }

    protected static void offerDragonHelmetRecipe(RecipeExporter exporter, ItemConvertible output, TagKey<Item> input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MMM")
                .pattern("M M")
                .pattern("L L")
                .criterion("has_material", conditionsFromTag(input))
                .offerTo(exporter);
    }

    protected static void offerDragonChestplateRecipe(RecipeExporter exporter, ItemConvertible output, TagKey<Item> input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MLM")
                .pattern("MMM")
                .pattern("LML")
                .criterion("has_material", conditionsFromTag(input))
                .offerTo(exporter);
    }

    protected static void offerDragonTailArmorRecipe(RecipeExporter exporter, ItemConvertible output, TagKey<Item> input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('L', Items.LEATHER)
                .input('M', input)
                .pattern("MMM")
                .pattern(" L ")
                .criterion("has_material", conditionsFromTag(input))
                .offerTo(exporter);
    }

    protected static void offerMoleclawHelmetRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, output)
                .input('G', ConventionalItemTags.GLASS_BLOCKS_TINTED)
                .input('H', input)
                .pattern("GHG")
                .criterion("has_material", conditionsFromItem(input))
                .offerTo(exporter);
    }
}
