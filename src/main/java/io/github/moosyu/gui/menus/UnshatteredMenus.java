package io.github.moosyu.gui.menus;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);

    public static final Supplier<MenuType<TalismansMenu>> TALISMAN_MENU_TYPE = MENUS.register("talisman_menu_type", () ->
            new MenuType<>(TalismansMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<StorageMenu>> STORAGE_MENU_TYPE = MENUS.register("storage_menu_type", () ->
            new MenuType<>(StorageMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
}