package io.github.moosyu.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.neoforged.neoforge.common.Tags;

import java.util.EnumMap;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredArmourMaterials {
    private static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static ResourceKey<EquipmentAsset> LEAFLET_KEY = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(MODID, "leaflet"));
    public static ResourceKey<EquipmentAsset> GLOW_SQUID_BOOTS_KEY = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(MODID, "glow_squid_boots"));

    public static final ArmorMaterial LEAFLET_ARMOUR_MATERIAL = new ArmorMaterial(
1,
        // Determines the defence value of this armor material, depending on what armor piece it is.
        Util.make(new EnumMap<>(ArmorType.class), map -> {
            map.put(ArmorType.BOOTS, 0);
            map.put(ArmorType.LEGGINGS, 0);
            map.put(ArmorType.CHESTPLATE, 0);
            map.put(ArmorType.HELMET, 0);
            map.put(ArmorType.BODY, 0);
        }),
        // Determines the enchantability of the tier. This represents how good the enchantments on this armor will be.
        // Gold uses 25, we put copper slightly below that.
        20,
        // Determines the sound played when equipping this armor.
        // This is wrapped with a Holder.
        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.AZALEA_LEAVES_PLACE),
0.0f,
0.0f,
        Tags.Items.NUGGETS,
        LEAFLET_KEY
    );

    public static final ArmorMaterial GLOW_SQUID_BOOTS_MATERIAL = new ArmorMaterial(
            1,
            // Determines the defence value of this armor material, depending on what armor piece it is.
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 0);
            }),
            // Determines the enchantability of the tier. This represents how good the enchantments on this armor will be.
            // Gold uses 25, we put copper slightly below that.
            20,
            // Determines the sound played when equipping this armor.
            // This is wrapped with a Holder.
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GLOW_SQUID_SQUIRT),
            0.0f,
            0.0f,
            Tags.Items.NUGGETS,
            GLOW_SQUID_BOOTS_KEY
    );

}
