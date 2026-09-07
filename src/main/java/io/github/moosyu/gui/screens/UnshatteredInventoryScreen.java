package io.github.moosyu.gui.screens;

import io.github.moosyu.packets.OpenCraftingPacket;
import io.github.moosyu.packets.OpenStoragePacket;
import io.github.moosyu.packets.OpenTalismanBagPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredInventoryScreen extends AbstractContainerScreen<InventoryMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/inventory.png");
    public final int IMAGE_WIDTH = 176;
    public final int IMAGE_HEIGHT = 166;
    private float xMouse;
    private float yMouse;

    public UnshatteredInventoryScreen(InventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new ImageButton(this.leftPos + 76,
                this.topPos + 7,
                20,
                18,
                new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("inventory_buttons/crafting")),
                _ -> ClientPacketDistributor.sendToServer(new OpenCraftingPacket())
        )).setTooltip(Tooltip.create(Component.translatable("container.unshattered.crafting")));

        this.addRenderableWidget(new ImageButton(this.leftPos + 76,
                this.topPos + 25,
                20,
                18,
                new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("inventory_buttons/storage")),
                _ -> ClientPacketDistributor.sendToServer(new OpenStoragePacket())
        )).setTooltip(Tooltip.create(Component.translatable("container.unshattered.storage")));

        this.addRenderableWidget(new ImageButton(this.leftPos + 76,
                this.topPos + 43,
                20,
                18,
                new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("inventory_buttons/talisman_bag")),
                _ -> ClientPacketDistributor.sendToServer(new OpenTalismanBagPacket())
        )).setTooltip(Tooltip.create(Component.translatable("container.unshattered.talisman_bag")));

        this.addRenderableWidget(new ImageButton(this.leftPos + 96,
                this.topPos + 61,
                20,
                18,
                new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("inventory_buttons/stats")),
                _ -> Minecraft.getInstance().setScreen(new StatsScreen(Component.translatable("screen.unshattered.stats")))
        )).setTooltip(Tooltip.create(Component.translatable("screen.unshattered.stats")));
    }


    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = (this.width - IMAGE_WIDTH) / 2;
        int y = (this.height - IMAGE_HEIGHT) / 2;

        super.extractBackground(graphics, mouseX, mouseY, a);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);

        if (this.minecraft.player != null) {
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, this.leftPos + 26, this.topPos + 8, this.leftPos + 75, this.topPos + 78, 30, 0.0625F, this.xMouse, this.yMouse, this.minecraft.player);
        }
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {}

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.xMouse = mouseX;
        this.yMouse = mouseY;
    }
}