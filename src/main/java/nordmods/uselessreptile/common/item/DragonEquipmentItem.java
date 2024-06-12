package nordmods.uselessreptile.common.item;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;

import java.util.UUID;
import java.util.function.Supplier;

public class DragonEquipmentItem extends Item {

    private final Supplier<AttributeModifiersComponent> dragonEquipmentAttributes;
    private final EquipmentSlot equipmentSlot;

    public DragonEquipmentItem(EquipmentSlot equipmentSlot, Supplier<AttributeModifiersComponent> dragonEquipmentAttributes, Settings settings) {
        super(settings);
        this.equipmentSlot = equipmentSlot;
        this.dragonEquipmentAttributes = dragonEquipmentAttributes;
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return dragonEquipmentAttributes.get();
    }

    public static UUID equipmentModifierUUID(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HEAD -> UUID.fromString("0f146331-9912-4c4b-b3f2-bb98fba0916e");
            case LEGS -> UUID.fromString("0983b5bf-6f93-40cb-a52f-bf0ff6b49703");
            case CHEST -> UUID.fromString("ac582768-20fe-4b44-955e-5f1ab02bb110");
            case FEET -> UUID.fromString("02ac9cf7-817b-4416-a3e3-ab148ebbb787");
            case BODY -> UUID.fromString("b4c78426-f69f-40e5-a8c8-dedb7ee5f712");
            case OFFHAND -> UUID.fromString("38deadcb-38a2-4afc-82a4-d7b729986200");
            case MAINHAND -> UUID.fromString("4790d80d-5d74-4672-bbcd-1fea761a2036");
        };
    }

    public EquipmentSlot getSlotType() {
        return equipmentSlot;
    }
}
