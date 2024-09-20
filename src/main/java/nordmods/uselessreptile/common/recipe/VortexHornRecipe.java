package nordmods.uselessreptile.common.recipe;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import nordmods.uselessreptile.common.init.URRecipeSerializers;

public class VortexHornRecipe extends SpecialCraftingRecipe {
    public VortexHornRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (!fits(input.getWidth(), input.getHeight())) return false;
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++) {
                ItemStack stack = input.getStackInSlot(x, y);
                switch (x) {
                    case 0, 2 -> {
                        switch (y) {
                            case 0, 2 -> {
                                if (!stack.isEmpty()) return false;
                            }
                            case 1 -> {
                                if (!stack.isOf(Items.BREEZE_ROD)) return false;
                            }
                        }
                    }
                    case 1 -> {
                        switch (y) {
                            case 0, 2 -> {
                                if (!stack.isOf(Items.BREEZE_ROD)) return false;
                            }
                            case 1 -> {
                                if (!stack.contains(DataComponentTypes.INSTRUMENT)) return false;
                            }
                        }
                    }
                }
            }

        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack instrument = getInstrumentStack(input);
        if (!instrument.isEmpty()) {
            ItemStack result = Items.STICK.getDefaultStack();
            result.set(DataComponentTypes.INSTRUMENT, instrument.get(DataComponentTypes.INSTRUMENT));
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width > 2 && height > 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return URRecipeSerializers.VORTEX_HORN;
    }

    protected ItemStack getInstrumentStack(CraftingRecipeInput craftingRecipeInput) {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++) {
                ItemStack stack = craftingRecipeInput.getStackInSlot(x, y);
                if (stack.contains(DataComponentTypes.INSTRUMENT)) return stack;
            }
        return ItemStack.EMPTY;
    }
}
