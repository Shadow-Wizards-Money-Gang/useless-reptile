package nordmods.uselessreptile.datagen.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import nordmods.uselessreptile.common.init.URTags;

import java.util.concurrent.CompletableFuture;

public class URBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public URBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(URTags.DRAGON_UNBREAKABLE)
                .addOptionalTag(BlockTags.AIR);
        getOrCreateTagBuilder(URTags.LIGHTNING_BREATH_ALWAYS_BREAKS)
                .addOptionalTag(BlockTags.LEAVES)
                .addOptionalTag(BlockTags.REPLACEABLE)
                .addOptionalTag(BlockTags.FLOWERS)
                .addOptionalTag(BlockTags.WOOL_CARPETS)
                .addOptionalTag(BlockTags.WOOL)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.MOSS_CARPET)
                .addOptionalTag(BlockTags.SNOW)
                .add(Blocks.MUSHROOM_STEM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK);
    }
}
