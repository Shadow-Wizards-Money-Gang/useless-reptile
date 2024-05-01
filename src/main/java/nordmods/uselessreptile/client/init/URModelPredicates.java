package nordmods.uselessreptile.client.init;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.common.init.URItems;

public class URModelPredicates {
    public static void init() {
        ModelPredicateProviderRegistry.register(URItems.FLUTE, new Identifier("mode"),
                (stack, world, entity, seed) -> {
                    if (stack.get(URItems.FLUTE_MODE_COMPONENT) == null) stack.set(URItems.FLUTE_MODE_COMPONENT, 0);
                    return stack.get(URItems.FLUTE_MODE_COMPONENT)/2f;
                });
    }
}
