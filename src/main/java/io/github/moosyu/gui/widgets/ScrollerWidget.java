package io.github.moosyu.gui.widgets;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.function.DoubleConsumer;

public class ScrollerWidget extends AbstractWidget {
    /**
     * pointless but i reckon it makes things more clear. not even sure if right is 1 i just guessed lol.
     */
    enum MouseButton {
        LEFT(0),
        RIGHT(1);

        private final int button;

        MouseButton(int button) {
            this.button = button;
        }
    }

    private static final Identifier SCROLLER_SPRITE = UnshatteredUtils.getUnshatteredIdentifier("widgets/scroller");
    static final int SCROLLER_WIDTH = 12;
    public static final int SCROLLER_HEIGHT = 15;
    private final int trackTop;
    private final int trackHeight;
    private final DoubleConsumer onScroll;
    private boolean scrolling = false;

    public ScrollerWidget(int x, int y, int trackHeight, DoubleConsumer onScroll) {
        super(x, y, SCROLLER_WIDTH, SCROLLER_HEIGHT, Component.translatable("widget.unshattered.narration.scroller"));
        this.trackTop = y;
        this.trackHeight = trackHeight;
        this.onScroll = onScroll;
    }

    public void setScrollProgress(double progress) {
        this.setY(trackTop + (int) Math.round(Mth.clamp(progress, 0.0d, 1.0d) * trackHeight));
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, this.getX(), this.getY(), SCROLLER_WIDTH, SCROLLER_HEIGHT);
        if (this.isHovered || scrolling) {
            graphics.requestCursor(scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
        }
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (this.isActive()) {
            if (this.isValidClickButton(event.buttonInfo())) {
                boolean isMouseOver = this.isMouseOver(event.x(), event.y());
                if (isMouseOver) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(event, doubleClick);
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        // 0 is left click
        if (event.button() == MouseButton.LEFT.button) {
            scrolling = false;
        }
    }

    @Override
    protected void onDrag(@NonNull MouseButtonEvent event, double dragX, double dragY) {
        if (event.button() == MouseButton.LEFT.button) {
            int newY = Mth.clamp((int) Math.round(this.getY() + dragY), trackTop, trackTop + trackHeight);

            this.setY(newY);
            onScroll.accept(trackHeight == 0 ? 0.0d : (double) (newY - trackTop) / trackHeight);
            scrolling = true;
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("widget.unshattered.narration.scroller"));
    }

    @Override
    public void playDownSound(@NonNull SoundManager soundManager) {}
}