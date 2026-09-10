package io.github.moosyu.gui.screens;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.gui.widgets.ScrollerWidget;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class StatsScreen extends SimpleScreen {
    private static final int LINE_HEIGHT = 12;
    private final int viewportHeight;
    private double scrollOffset = 0;
    private final int VANILLA_ATTRIBUTE_ADDITIONS = 1;
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

        this.contentHeight = (visibleCount + VANILLA_ATTRIBUTE_ADDITIONS) * LINE_HEIGHT;
        int scissorTop = this.backgroundTopLeft.y + 6;
        int scissorBottom =  this.viewportHeight + (scissorTop + 8);

        graphics.enableScissor(this.backgroundTopLeft.x, scissorTop, this.backgroundTopLeft.x + imageWidth, scissorBottom);

        int visibleIndex = 0;
        for (UnshatteredAttributeValues currentAttribute : attributeValues) {
            if (currentAttribute.visible) {
                addAttributeToList(player,
                        graphics,
                        visibleIndex,
                        scissorTop,
                        scissorBottom,
                        currentAttribute.holder,
                        currentAttribute.symbol,
                        currentAttribute.id,
                        currentAttribute.percentage,
                        currentAttribute.color,
                        1
                );
                visibleIndex++;
            }
        }

        addAttributeToList(player,
                graphics,
                visibleIndex,
                scissorTop,
                scissorBottom,
                Attributes.MOVEMENT_SPEED,
                "✦",
                "speed",
                false,
                0xFFFFFFFF,
                10
        );
        visibleIndex++;

        graphics.disableScissor();
    }

    /**
     * add an attribute modifier to the stats screen
     * @param player the player
     * @param graphics GuiGraphicsExtractor
     * @param visibleIndex current visible index (needs to be incremented after adding)
     * @param scissorTop scissor top
     * @param scissorBottom scissor bottom
     * @param attributeHolder attribute holder
     * @param symbol string symbol for the attribute
     * @param currentAttributeId the attributes id (not key, just like a name as it adds the attribute.name.unshattered. for you)
     * @param isPercentage whether the attribute should be displayed as a percentage
     * @param attributeColour attribute colour
     * @param attributeModifier value to multiply the attribute's values by
     */
    private void addAttributeToList(Player player,
                                    GuiGraphicsExtractor graphics,
                                    int visibleIndex,
                                    int scissorTop,
                                    int scissorBottom,
                                    Holder<Attribute> attributeHolder,
                                    String symbol,
                                    String currentAttributeId,
                                    boolean isPercentage,
                                    int attributeColour,
                                    int attributeModifier
    ) {
        int lineY = this.backgroundTopLeft.y + LINE_HEIGHT + (visibleIndex * LINE_HEIGHT) - (int) scrollOffset;

        if (lineY + LINE_HEIGHT >= scissorTop && lineY <= scissorBottom) {
            double attributeBaseValue = player.getAttributeBaseValue(attributeHolder) * attributeModifier;
            double attributeValue = player.getAttributeValue(attributeHolder) * attributeModifier;
            String attributeBaseValueFormatted = UnshatteredUtils.oneDecimalFormat.format(attributeBaseValue);

            graphics.text(font,
                    symbol
                            + " "
                            + Component.translatable("attribute.name.unshattered." + currentAttributeId).getString()
                            + ": "
                            + (attributeValue == attributeBaseValue ? attributeBaseValueFormatted : attributeBaseValueFormatted + " (+" + UnshatteredUtils.oneDecimalFormat.format(attributeValue) + ")")
                            + (isPercentage ? "%" : ""),
                    this.backgroundTopLeft.x + 9,
                    lineY,
                    attributeColour
            );
        }
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
