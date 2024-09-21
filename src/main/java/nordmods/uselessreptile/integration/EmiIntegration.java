package nordmods.uselessreptile.integration;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.init.URItems;
import nordmods.uselessreptile.common.recipe.VortexHornRecipe;

import java.util.List;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        for (RecipeEntry<CraftingRecipe> recipe : registry.getRecipeManager().listAllOfType(RecipeType.CRAFTING)) {
            if (!(recipe.value() instanceof VortexHornRecipe vortexHornRecipe)) continue;
            register(URItems.VORTEX_HORN, vortexHornRecipe, registry);
        }
    }

    private static void register(Item horn, VortexHornRecipe vortexHornRecipe, EmiRegistry registry) {
        EmiRecipe emiRecipe = new EmiCraftingRecipe(List.of(
                EmiStack.of(ItemStack.EMPTY), EmiStack.of(Items.BREEZE_ROD), EmiStack.of(ItemStack.EMPTY),
                EmiStack.of(Items.BREEZE_ROD), EmiStack.of(Items.GOAT_HORN), EmiStack.of(Items.BREEZE_ROD),
                EmiStack.of(ItemStack.EMPTY), EmiStack.of(Items.BREEZE_ROD), EmiStack.of(ItemStack.EMPTY)),
                EmiStack.of(horn), horn.getRegistryEntry().registryKey().getValue(), false);
        try {
            registry.addRecipe(emiRecipe);
        } catch (Throwable e) {
            UselessReptile.LOGGER.warn("Exception thrown when parsing recipes for Vortex Horn (EMI integration)");
            UselessReptile.LOGGER.error(String.valueOf(e));
        }
    }
}
