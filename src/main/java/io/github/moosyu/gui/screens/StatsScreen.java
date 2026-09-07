package io.github.moosyu.gui.screens;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.gui.widgets.ScrollerWidget;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class StatsScreen extends SimpleScreen {
    private static final int LINE_HEIGHT = 12;
    private final int viewportHeight;
    private double scrollOffset = 0;
    private int contentHeight = 0;
    private ScrollerWidget scroller;

    public StatsScreen(Component title) {
        super(title, 176, 166, "textures/gui/stats.png");
        this.viewportHeight = imageHeight - 24;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        UnshatteredAttributeValues[] attributeValues = UnshatteredAttributeValues.values();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int visibleCount = 0;
        for (UnshatteredAttributeValues attribute : attributeValues) {
            if (attribute.visible) {
                visibleCount++;
            }
        }

        this.contentHeight = visibleCount * LINE_HEIGHT;
        int scissorTop = this.backgroundTopLeft.y + 6;
        int scissorBottom =  this.viewportHeight + (scissorTop + 8);

        graphics.enableScissor(this.backgroundTopLeft.x, scissorTop, this.backgroundTopLeft.x + imageWidth, scissorBottom);

        int visibleIndex = 0;
        for (UnshatteredAttributeValues currentAttribute : attributeValues) {
            if (currentAttribute.visible) {
                int lineY = this.backgroundTopLeft.y + LINE_HEIGHT + (visibleIndex * LINE_HEIGHT) - (int) scrollOffset;

                if (lineY + LINE_HEIGHT >= scissorTop && lineY <= scissorBottom) {
                    double attributeBaseValue = player.getAttributeBaseValue(currentAttribute.holder);
                    double attributeValue = player.getAttributeValue(currentAttribute.holder);
                    String attributeBaseValueFormatted = UnshatteredUtils.oneDecimalFormat.format(attributeBaseValue);

                    graphics.text(font,
                            currentAttribute.symbol
                                    + " "
                                    + Component.translatable("attribute.name.unshattered." + currentAttribute.id).getString()
                                    + ": "
                                    + (attributeValue == attributeBaseValue ? attributeBaseValueFormatted : attributeBaseValueFormatted + " (+" + UnshatteredUtils.oneDecimalFormat.format(attributeValue) + ")")
                                    + (currentAttribute.percentage ? "%" : ""),
                            this.backgroundTopLeft.x + 9,
                            lineY,
                            currentAttribute.color
                    );
                }
                visibleIndex++;
            }
        }

        graphics.disableScissor();
    }

    @Override
    protected void init() {
        super.init();

        int trackTop = backgroundTopLeft.y() + 6;
        int trackHeight = imageHeight - 12 - ScrollerWidget.SCROLLER_HEIGHT;

        this.scroller = new ScrollerWidget(backgroundTopLeft.x() + 156, trackTop, trackHeight, progress -> this.scrollOffset = progress * Math.max(0, contentHeight - viewportHeight));
        this.addRenderableWidget(this.scroller);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (contentHeight > viewportHeight) {
            int maxScroll = contentHeight - viewportHeight;

            scrollOffset = Mth.clamp(scrollOffset - scrollY * 10, 0, maxScroll);
            if (scroller != null) {
                scroller.setScrollProgress(maxScroll == 0 ? 0.0 : scrollOffset / maxScroll);
            }

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
