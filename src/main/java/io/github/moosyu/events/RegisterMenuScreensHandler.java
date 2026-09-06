package io.github.moosyu.events;

import io.github.moosyu.gui.menus.UnshatteredMenus;
import io.github.moosyu.gui.screens.StorageScreen;
import io.github.moosyu.gui.screens.TalismansScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterMenuScreensHandler {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(UnshatteredMenus.TALISMAN_MENU_TYPE.get(), TalismansScreen::new);
        // event.register(UnshatteredMenus.INVENTORY_MENU.get(), UnshatteredInventoryScreen::new);
        event.register(UnshatteredMenus.STORAGE_MENU_TYPE.get(), StorageScreen::new);
    }
}