package io.github.moosyu.creative;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.UnshatteredItems.*;

public class UnshatteredCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_ITEMS_TAB = CREATIVE_MODE_TABS.register("unshattered_items_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.items"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> BAT_THE_FISH.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(BAT_THE_FISH.get());
            output.accept(CENTURY_THE_FISH.get());
            output.accept(CHILL_THE_FISH.get());
            output.accept(CLUNK_THE_FISH.get());
            output.accept(DIAMOND_THE_FISH.get());
            output.accept(DUST_THE_FISH.get());
            output.accept(EGG_THE_FISH.get());
            output.accept(EON_THE_FISH.get());
            output.accept(FLAKE_THE_FISH.get());
            output.accept(EXPERIMENT_THE_FISH.get());
            output.accept(FOSSIL_THE_FISH.get());
            output.accept(GABAGOOL_THE_FISH.get());
            output.accept(GIFT_THE_FISH.get());
            output.accept(HERRING_THE_FISH.get());
            output.accept(NOPE_THE_FISH.get());
            output.accept(OOPS_THE_FISH.get());
            output.accept(PARTY_THE_FISH.get());
            output.accept(ROCK_THE_FISH.get());
            output.accept(SHRIMP_THE_FISH.get());
            output.accept(SKELETON_THE_FISH.get());
            output.accept(SPOOK_THE_FISH.get());
            output.accept(STEW_THE_FISH.get());
            output.accept(SWAMP_THE_FISH.get());
            output.accept(ZOOP_THE_FISH.get());
            output.accept(BEDROCK.get());
            output.accept(CAKE_SOUL.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_DECORATIVE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("unshattered_decorative_blocks_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.decorative_blocks"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> FIG_LOG.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(FIG_LOG.get());
            output.accept(ROCK_TALKABLE_BLOCK.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_BREAKABLE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("unshattered_breakable_blocks_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.breakable_blocks"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> BREAKABLE_COBBLESTONE.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(BREAKABLE_STONE.get());
            output.accept(BREAKABLE_COBBLESTONE.get());
            output.accept(BREAKABLE_FIG_LOG.get());
            output.accept(BREAKABLE_COAL_ORE.get());
            output.accept(BREAKABLE_IRON_ORE.get());
            output.accept(BREAKABLE_COPPER_ORE.get());
            output.accept(BREAKABLE_GOLD_ORE.get());
            output.accept(BREAKABLE_REDSTONE_ORE.get());
            output.accept(BREAKABLE_EMERALD_ORE.get());
            output.accept(BREAKABLE_DIAMOND_ORE.get());
            output.accept(PURE_DIAMOND.get());
            output.accept(BREAKABLE_OBSIDIAN.get());
            output.accept(BREAKABLE_WHEAT.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_RESOURCES_TAB = CREATIVE_MODE_TABS.register("unshattered_resources_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.unshattered_resources"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> ENCHANTED_FIG_LOG.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(ENCHANTED_FIG_LOG.get());
            output.accept(ENCHANTED_ROTTEN_FLESH);
            output.accept(ZOMBIE_HEART);
            output.accept(GOLDEN_POWDER);
            output.accept(ENCHANTED_GOLD_INGOT);
            output.accept(ENCHANTED_GOLD_BLOCK);
            output.accept(HEALING_TISSUE);
            output.accept(ENCHANTED_DIAMOND);
            output.accept(ENCHANTED_DIAMOND_BLOCK);
            output.accept(ENCHANTED_EMERALD);
            output.accept(ENCHANTED_EMERALD_BLOCK);
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_ARMOUR_TAB = CREATIVE_MODE_TABS.register("unshattered_armour_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.armour"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> LEAFLET_CHESTPLATE.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(LEAFLET_HELMET.get());
            output.accept(LEAFLET_CHESTPLATE.get());
            output.accept(LEAFLET_LEGGINGS.get());
            output.accept(LEAFLET_BOOTS.get());
            output.accept(GLOW_SQUID_BOOTS.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_TOOLS_TAB = CREATIVE_MODE_TABS.register("unshattered_tools_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.tools"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> TREECAPITATOR.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(TREECAPITATOR.get());
            output.accept(SPRUCE_AXE.get());
            output.accept(SERIOUSLY_DAMAGED_AXE.get());
            output.accept(DECENT_AXE.get());
            output.accept(FIG_HEW.get());
            output.accept(FIGSTONE_SPLITTER.get());
            output.accept(CHALLENGING_ROD.get());
            output.accept(FISHING_ROD.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_WEAPONS_TAB = CREATIVE_MODE_TABS.register("unshattered_weapons_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.unshattered.weapons"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> MERCENARY_AXE.get().getDefaultInstance())
        .displayItems((_, output) -> {
            output.accept(MERCENARY_AXE.get());
            output.accept(ROGUE_SWORD.get());
            output.accept(SQUIRE_SWORD.get());
            output.accept(UNDEAD_SWORD.get());
            output.accept(ZOMBIE_SWORD.get());
            output.accept(ORNATE_ZOMBIE_SWORD.get());
            output.accept(FLORID_ZOMBIE_SWORD.get());
            output.accept(RUSTY_CLEAVER.get());
            output.accept(GOLDEN_CLEAVER.get());
            output.accept(SUPER_CLEAVER.get());
            output.accept(HYPER_CLEAVER.get());
            output.accept(GIANT_CLEAVER.get());
            output.accept(IRON_DAGGER.get());
            output.accept(EMERALD_DAGGER.get());
        }).build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNSHATTERED_TALISMANS_TAB = CREATIVE_MODE_TABS.register("unshattered_talismans_tab",
            () -> CreativeModeTab.builder()
                .title(Component.translatable("item_group.unshattered.talismans"))
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .icon(() -> BAT_TALISMAN.get().getDefaultInstance())
                .displayItems((_, output) -> {
                    output.accept(BAT_TALISMAN.get());
                    output.accept(COINS_TALISMAN.get());
                }).build()
    );
}
