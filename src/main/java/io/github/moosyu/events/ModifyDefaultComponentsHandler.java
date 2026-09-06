package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ModifyDefaultComponentsHandler {
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.WOODEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 1))
        );

        event.modify(Items.GOLDEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 6))
        );

        event.modify(Items.STONE_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 2))
        );

        event.modify(Items.IRON_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 4))
        );

        event.modify(Items.DIAMOND_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.SELL_VALUE, 12))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true).set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.STONE_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.IRON_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 3))
        );

        event.modify(Items.GOLDEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 4))
        );

        event.modify(Items.DIAMOND_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 8))
        );

        event.modify(Items.WOODEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 1));
        event.modify(Items.STONE_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 2));
        event.modify(Items.IRON_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 4));
        event.modify(Items.GOLDEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 6));
        event.modify(Items.DIAMOND_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.UNCOMMON, 12));
        modifyVanillaItem(event, Items.END_STONE, ItemTypes.MATERIAL, 2, true);
        modifyVanillaItem(event, Items.DIAMOND, ItemTypes.MATERIAL, 8, false);
        modifyVanillaItem(event, Items.COBBLESTONE, ItemTypes.MATERIAL, 1, false);
        modifyVanillaItem(event, Items.IRON_HELMET, ItemTypes.HELMET, 2, false);
        modifyVanillaItem(event, Items.IRON_CHESTPLATE, ItemTypes.CHESTPLATE, 3, false);
        modifyVanillaItem(event, Items.IRON_LEGGINGS, ItemTypes.LEGGINGS, 3, false);
        modifyVanillaItem(event, Items.IRON_BOOTS, ItemTypes.BOOTS, 2, false);
        modifyVanillaItem(event, Items.DIAMOND_HELMET, ItemTypes.HELMET, UnshatteredRarities.UNCOMMON, 3, false);
        modifyVanillaItem(event, Items.DIAMOND_CHESTPLATE, ItemTypes.CHESTPLATE, UnshatteredRarities.UNCOMMON, 5, false);
        modifyVanillaItem(event, Items.DIAMOND_LEGGINGS, ItemTypes.LEGGINGS, UnshatteredRarities.UNCOMMON, 4, false);
        modifyVanillaItem(event, Items.DIAMOND_BOOTS, ItemTypes.BOOTS, UnshatteredRarities.UNCOMMON, 3, false);
        modifyVanillaItem(event, Items.GOLD_INGOT, ItemTypes.MATERIAL, 4);
        modifyVanillaItem(event, Items.GOLD_BLOCK, ItemTypes.MATERIAL, 27);
        modifyVanillaItem(event, Items.DIAMOND, ItemTypes.MATERIAL, 8);
        modifyVanillaItem(event, Items.DIAMOND_BLOCK, ItemTypes.MATERIAL, 72);
        modifyVanillaItem(event, Items.EMERALD, ItemTypes.MATERIAL, 4);
        modifyVanillaItem(event, Items.EMERALD_BLOCK, ItemTypes.MATERIAL, 36);
    }

    /**
     * removes axe's data components to convert them into battleaxes
     * @param components data component map builder
     * @param rarity axe rarity
     * @param sellValue sell value
     */
    private static void modifyVanillaAxeComponents(DataComponentMap.Builder components, UnshatteredRarities rarity, int sellValue) {
        components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.BATTLE_AXE)
                .set(UnshatteredDataComponents.RARITY, rarity)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
                .set(DataComponents.TOOL, new Tool(List.of(), 1.0f, 0, false));
    }

    /**
     * modify a vanilla item by adding unshattered components
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param rarity the rarity
     * @param sellValue the sell value
     * @param description whether the item has a description (item.description.unshattered.item_id) set in lang.
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemTypes itemType, UnshatteredRarities rarity, int sellValue, boolean description) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.RARITY, rarity)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), description)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }

    /**
     * modify a vanilla item by adding unshattered components (assuming it's common)
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param sellValue the sell value
     * @param description whether the item has a description (item.description.unshattered.item_id) set in lang.
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemTypes itemType, int sellValue, boolean description) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), description)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }

    /**
     * modify a vanilla item by adding unshattered components (assuming it's common)
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param sellValue the sell value
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemTypes itemType, int sellValue) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }
}
