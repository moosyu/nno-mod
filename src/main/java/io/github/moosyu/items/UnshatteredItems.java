package io.github.moosyu.items;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.regions.UnshatteredRegions;
import io.github.moosyu.items.talismans.BatTalisman;
import io.github.moosyu.items.tools.axes.RegionLockedFortuneAxe;
import io.github.moosyu.items.tools.axes.UnshatteredAxeTool;
import io.github.moosyu.items.tools.rods.UnshatteredRod;
import io.github.moosyu.items.weapons.axes.UnshatteredAxeWeapon;
import io.github.moosyu.items.weapons.cleavers.*;
import io.github.moosyu.items.weapons.daggers.IronDagger;
import io.github.moosyu.items.weapons.swords.*;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.ArmourMaterials.GLOW_SQUID_BOOTS_MATERIAL;
import static io.github.moosyu.items.ArmourMaterials.LEAFLET_ARMOUR_MATERIAL;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<UnshatteredAxeWeapon> MERCENARY_AXE = ITEMS.registerItem("mercenary_axe", props -> new UnshatteredAxeWeapon(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "mercenary_axe_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "mercenary_axe_strength"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<RegionLockedFortuneAxe> TREECAPITATOR = ITEMS.registerItem("treecapitator", props -> new RegionLockedFortuneAxe(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 10000)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true),
            50,
            5,
            12,
            UnshatteredRegions.PLAINS_REGION,
            Identifier.fromNamespaceAndPath(MODID, "treecapitator_park_enthusiast")
    ));

    public static final DeferredItem<RegionLockedFortuneAxe> SPRUCE_AXE = ITEMS.registerItem("spruce_axe", props -> new RegionLockedFortuneAxe(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 480)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true),
            25,
            2,
            6,
            UnshatteredRegions.PLAINS_REGION,
            Identifier.fromNamespaceAndPath(MODID, "spruce_axe_park_enthusiast")
    ));

    public static final DeferredItem<UnshatteredAxeTool> SERIOUSLY_DAMAGED_AXE = ITEMS.registerItem("seriously_damaged_axe", props -> new UnshatteredAxeTool(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "seriously_damaged_axe_sweep"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "seriously_damaged_axe_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            6.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> DECENT_AXE = ITEMS.registerItem("decent_axe", props -> new UnshatteredAxeTool(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "decent_axe_sweep"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "decent_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            8.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> FIG_HEW = ITEMS.registerItem("fig_hew", props -> new UnshatteredAxeTool(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fig_hew_sweep"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fig_hew_foraging_fortune"), 12, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            10.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> FIGSTONE_SPLITTER = ITEMS.registerItem("figstone_splitter", props -> new UnshatteredAxeTool(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "figstone_splitter_sweep"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "figstone_splitter_foraging_fortune"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            12.0f
    ));

    public static final DeferredItem<Item> LEAFLET_HELMET = ITEMS.registerItem("leaflet_helmet", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOUR_MATERIAL, ArmorType.HELMET)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.HELMET)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 2)
            .attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_helmet_health"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_helmet_foraging_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .build()
            )
    ));

    public static final DeferredItem<Item> LEAFLET_CHESTPLATE = ITEMS.registerItem("leaflet_chestplate", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOUR_MATERIAL, ArmorType.CHESTPLATE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.CHESTPLATE)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 4)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_chestplate_health"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_chestplate_foraging_fortune"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .build()
            )
    ));

    public static final DeferredItem<Item> LEAFLET_LEGGINGS = ITEMS.registerItem("leaflet_leggings", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOUR_MATERIAL, ArmorType.LEGGINGS)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.LEGGINGS)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 3)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_leggings_health"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_leggings_foraging_fortune"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
                    .build()
            )
    ));

    public static final DeferredItem<Item> LEAFLET_BOOTS = ITEMS.registerItem("leaflet_boots", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOUR_MATERIAL, ArmorType.BOOTS)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.BOOTS)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 2)
            .component(UnshatteredDataComponents.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_boots_health"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_boots_foraging_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .build()
            )
    ));

    public static final DeferredItem<EnchantedItem> BAT_THE_FISH = ITEMS.registerItem("bat_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> CENTURY_THE_FISH = ITEMS.registerItem("century_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> CHILL_THE_FISH = ITEMS.registerItem("chill_the_fish", props -> new EnchantedItem(props
            .stacksTo(1).component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> CLUNK_THE_FISH = ITEMS.registerItem("clunk_the_fish", props -> new EnchantedItem(props
            .stacksTo(1).component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> DIAMOND_THE_FISH = ITEMS.registerItem("diamond_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> DUST_THE_FISH = ITEMS.registerItem("dust_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> EGG_THE_FISH = ITEMS.registerItem("egg_the_fish", props -> new EnchantedItem(props
            .stacksTo(1).component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> EON_THE_FISH = ITEMS.registerItem("eon_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> FLAKE_THE_FISH = ITEMS.registerItem("flake_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH).component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> EXPERIMENT_THE_FISH = ITEMS.registerItem("experiment_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH).component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> FOSSIL_THE_FISH = ITEMS.registerItem("fossil_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH).component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> GABAGOOL_THE_FISH = ITEMS.registerItem("gabagool_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> GIFT_THE_FISH = ITEMS.registerItem("gift_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> HERRING_THE_FISH = ITEMS.registerItem("herring_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> NOPE_THE_FISH = ITEMS.registerItem("nope_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> OOPS_THE_FISH = ITEMS.registerItem("oops_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> PARTY_THE_FISH = ITEMS.registerItem("party_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> ROCK_THE_FISH = ITEMS.registerItem("rock_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> SHRIMP_THE_FISH = ITEMS.registerItem("shrimp_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> SKELETON_THE_FISH = ITEMS.registerItem("skeleton_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> SPOOK_THE_FISH = ITEMS.registerItem("spook_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> STEW_THE_FISH = ITEMS.registerItem("stew_the_fish", props -> new EnchantedItem(props
            .stacksTo(1).component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> SWAMP_THE_FISH = ITEMS.registerItem("swamp_the_fish", props -> new EnchantedItem(props
            .stacksTo(1).component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> ZOOP_THE_FISH = ITEMS.registerItem("zoop_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.SPECIAL)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_FIG_LOG = ITEMS.registerItem("enchanted_fig_log", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.LOG)
    ));

    public static final DeferredItem<Item> BEDROCK = ITEMS.registerItem("bedrock", props -> new Item(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.LEGENDARY)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.ITEM)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<Item> CAKE_SOUL = ITEMS.registerItem("cake_soul", props -> new Item(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.MYTHIC)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.ITEM)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<UnshatteredRod> CHALLENGING_ROD = ITEMS.registerItem("challenging_rod", props -> new UnshatteredRod(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISHING_ROD)
            .component(UnshatteredDataComponents.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.FISHING, 5))
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_strength"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FISHING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_fishing_fortune"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FISHING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_fishing_speed"), 9, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<UnshatteredRod> FISHING_ROD = ITEMS.registerItem("fishing_rod", props -> new UnshatteredRod(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.FISHING_ROD)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fishing_rod_damage"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fishing_rod_strength"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<BlockItem> FIG_LOG = ITEMS.registerItem("fig_log", props -> new BlockItem(FIG_LOG_BLOCK.get(), props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.COMMON)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.LOG)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_ROTTEN_FLESH = ITEMS.registerItem("enchanted_rotten_flesh", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<Item> ZOMBIE_HEART = ITEMS.registerItem("zombie_heart", props -> new Item(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> GOLDEN_POWDER = ITEMS.registerItem("golden_powder", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_GOLD_INGOT = ITEMS.registerItem("enchanted_gold_ingot", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_GOLD_BLOCK = ITEMS.registerItem("enchanted_gold_block", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_DIAMOND = ITEMS.registerItem("enchanted_diamond", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_DIAMOND_BLOCK = ITEMS.registerItem("enchanted_diamond_block", props -> new EnchantedItem(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<Item> HEALING_TISSUE = ITEMS.registerItem("healing_tissue", props -> new Item(props
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.MATERIAL)
    ));

    public static final DeferredItem<BlockItem> BREAKABLE_FIG_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_FIG_LOG_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_STONE = ITEMS.registerSimpleBlockItem(BREAKABLE_STONE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COAL_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_COAL_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_WHEAT = ITEMS.registerSimpleBlockItem(BREAKABLE_WHEAT_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COBBLESTONE = ITEMS.registerSimpleBlockItem(BREAKABLE_COBBLESTONE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_IRON_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_IRON_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COPPER_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_COPPER_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_GOLD_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_GOLD_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_REDSTONE_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_REDSTONE_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_EMERALD_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_EMERALD_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_DIAMOND_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_DIAMOND_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> PURE_DIAMOND = ITEMS.registerSimpleBlockItem(PURE_DIAMOND_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_OBSIDIAN = ITEMS.registerSimpleBlockItem(BREAKABLE_OBSIDIAN_BLOCK.getDelegate());

    public static final DeferredItem<Item> ROGUE_SWORD = ITEMS.registerItem("rogue_sword", RogueSword::new);

    public static final DeferredItem<Item> SQUIRE_SWORD = ITEMS.registerItem("squire_sword", props -> new Item(props
            .stacksTo(1)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.SWORD)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 2500)
            .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
            .component(UnshatteredDataComponents.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squire_sword_damage"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squire_sword_strength"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<Item> UNDEAD_SWORD = ITEMS.registerItem("undead_sword", UndeadSword::new);

    public static final DeferredItem<UnshatteredZombieSwordBase> ZOMBIE_SWORD = ITEMS.registerItem("zombie_sword",
            props -> new UnshatteredZombieSwordBase(props.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                    .component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(4, 300))
                    .component(UnshatteredDataComponents.SELL_VALUE.get(), 300000)
                    .component(UnshatteredDataComponents.DESCRIPTION.get(), true),
                    "zombie_sword",
                    28,
                    12,
                    20,
                    40,
                    24,
                    10
            )
    );

    public static final DeferredItem<UnshatteredZombieSwordBase> ORNATE_ZOMBIE_SWORD = ITEMS.registerItem("ornate_zombie_sword",
            props -> new UnshatteredZombieSwordBase(props.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
                    .component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(5, 300))
                    .component(UnshatteredDataComponents.SELL_VALUE.get(), 600000)
                    .component(UnshatteredDataComponents.DESCRIPTION.get(), true),
                    "ornate_zombie_sword",
                    35,
                    15,
                    25,
                    50,
                    30,
                    10
            )
    );

    public static final DeferredItem<UnshatteredZombieSwordBase> FLORID_ZOMBIE_SWORD = ITEMS.registerItem("florid_zombie_sword",
            props -> new UnshatteredZombieSwordBase(props.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.LEGENDARY)
                    .component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(5, 300))
                    .component(UnshatteredDataComponents.SELL_VALUE.get(), 2000000)
                    .component(UnshatteredDataComponents.DESCRIPTION.get(), true),
                    "florid_zombie_sword",
                    42,
                    20,
                    30,
                    60,
                    36,
                    10
            )
    );

    public static final DeferredItem<Item> RUSTY_CLEAVER = ITEMS.registerItem("rusty_cleaver", RustyCleaver::new);
    public static final DeferredItem<Item> GOLDEN_CLEAVER = ITEMS.registerItem("golden_cleaver", GoldenCleaver::new);
    public static final DeferredItem<Item> SUPER_CLEAVER = ITEMS.registerItem("super_cleaver", SuperCleaver::new);
    public static final DeferredItem<Item> HYPER_CLEAVER = ITEMS.registerItem("hyper_cleaver", HyperCleaver::new);
    public static final DeferredItem<Item> GIANT_CLEAVER = ITEMS.registerItem("giant_cleaver", GiantCleaver::new);
    public static final DeferredItem<Item> BAT_TALISMAN = ITEMS.registerItem("bat_talisman", BatTalisman::new);
    public static final DeferredItem<Item> IRON_DAGGER = ITEMS.registerItem("iron_dagger", IronDagger::new);

    public static final DeferredItem<Item> GLOW_SQUID_BOOTS = ITEMS.registerItem("glow_squid_boots", props -> new Item(props
            .humanoidArmor(GLOW_SQUID_BOOTS_MATERIAL, ArmorType.BOOTS)
            .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.BOOTS)
            .component(UnshatteredDataComponents.SELL_VALUE.get(), 15)
            .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.FISHING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squid_boots_fishing_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squid_boots_health"), 12, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .build()
            )
    ));

    public static final DeferredItem<BlockItem> ROCK_TALKABLE_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.ROCK_TALKABLE_BLOCK.getDelegate());
}
