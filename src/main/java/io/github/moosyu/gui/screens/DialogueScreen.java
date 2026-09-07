package io.github.moosyu.gui.screens;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueNode;
import io.github.moosyu.packets.QueueNewFlagsPacket;
import io.github.moosyu.packets.ResetFlagQueuePacket;
import io.github.moosyu.packets.TriggerEventPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class DialogueScreen extends Screen {
    private static final int DIALOGUE_TEXTBOX_WIDTH = 300;
    private static final int DIALOGUE_TEXTBOX_HEIGHT = 87;
    private static final int DIALOGUE_TEXTBOX_PADDING = 5;
    private static final int DIALOGUE_TEXTBOX_MARGIN_BOTTOM = 2;
    private static final int BOTTOM_Y_OFFSET = 35;
    private static final int BUTTON_SPACING = 6;
    private static final int BUTTON_HEIGHT = 20;

    Component talkableName;
    Player player;
    DialogueNode selectedDialogueNode;

    public DialogueScreen(Component talkableName, DialogueNode selectedDialogueNode, Player player) {
        super(Component.translatable("screen.unshattered.dialogue"));

        this.talkableName = talkableName;
        this.selectedDialogueNode = selectedDialogueNode;
        this.player = player;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int centerXPos = graphics.guiWidth() / 2;
        graphics.fill(centerXPos - (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - DIALOGUE_TEXTBOX_HEIGHT - BOTTOM_Y_OFFSET,
                centerXPos + (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - BOTTOM_Y_OFFSET,
                UnshatteredUtils.getOpacityColor(0x1F1F21, 0.55f)
        );
    }

    @Override
    protected void init() {
        super.init();

        List<DialogueChoice> availableDialogueChoices = selectedDialogueNode.dialogueChoices().stream().filter(dialogueChoice -> dialogueChoice.dialogueFlagRequirements().isPresent()
                && dialogueChoice.dialogueFlagRequirements().get().isSatisfied(player.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS))
                || dialogueChoice.dialogueFlagRequirements().isEmpty()).toList();
        int dialogueChoiceCount = availableDialogueChoices.size();

        if (dialogueChoiceCount > 0) {
            int[] widths = new int[dialogueChoiceCount];
            int totalWidth = (dialogueChoiceCount - 1) * BUTTON_SPACING;

            for (int i = 0; i < dialogueChoiceCount; i++) {
                widths[i] = font.width(availableDialogueChoices.get(i).text()) + 10;
                totalWidth += widths[i];
            }

            int x = (this.width / 2) - (totalWidth / 2);

            for (int i = 0; i < dialogueChoiceCount; i++) {
                DialogueChoice dialogueChoice = availableDialogueChoices.get(i);
                int width = widths[i];

                this.addRenderableWidget(Button.builder(dialogueChoice.text(), _ -> {
                    if (dialogueChoice.triggeredEvent().isPresent()) {
                        ClientPacketDistributor.sendToServer(new TriggerEventPacket(dialogueChoice.triggeredEvent().get()));
                    }

                    // todo: have this carry an identifier for choice picked so it can't be cheated (id hope)
                    ClientPacketDistributor.sendToServer(new QueueNewFlagsPacket(dialogueChoice.setFlags()));

                    if (dialogueChoice.targetNode().isPresent()) {
                        selectedDialogueNode = dialogueChoice.targetNode().get();
                        this.rebuildWidgets();
                    } else {
                        closeDialogueScreen();
                    }
                }).pos(x, this.height - BOTTOM_Y_OFFSET + DIALOGUE_TEXTBOX_MARGIN_BOTTOM).size(width, BUTTON_HEIGHT).build());

                x += width + BUTTON_SPACING;
            }
        } else {
            int width = 22;

            this.addRenderableWidget(Button.builder(Component.literal("..."), _ -> closeDialogueScreen())
                    .pos((this.width / 2) - (width / 2), this.height - BOTTOM_Y_OFFSET + DIALOGUE_TEXTBOX_MARGIN_BOTTOM)
                    .size(width, BUTTON_HEIGHT)
                    .build());
        }
    }

    @Override
    public void onClose() {
        ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(false));
        super.onClose();
    }

    /**
     * for closing the dialogue screen "properly" with the flag queue being added to the player's flags
     */
    private static void closeDialogueScreen() {
        ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(true));
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.text(font,
                talkableName,
                (graphics.guiWidth() / 2) - (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - DIALOGUE_TEXTBOX_HEIGHT - BOTTOM_Y_OFFSET - font.lineHeight,
                0xFFFFFFFF
        );

        int fontSpacing = font.lineHeight + 2;
        int availableHeight = DIALOGUE_TEXTBOX_HEIGHT - (DIALOGUE_TEXTBOX_PADDING * 2);
        int maxVisibleLines = availableHeight / fontSpacing;
        List<FormattedCharSequence> dialogueText = font.split(
                selectedDialogueNode.text(),
                DIALOGUE_TEXTBOX_WIDTH - (DIALOGUE_TEXTBOX_PADDING * 2)
        );

        for (int i = 0; i < dialogueText.size(); i++) {
            if (i >= maxVisibleLines) break;

            graphics.text(font,
                    dialogueText.get(i),
                    (graphics.guiWidth() / 2) - (DIALOGUE_TEXTBOX_WIDTH / 2) + DIALOGUE_TEXTBOX_PADDING,
                    (graphics.guiHeight() - DIALOGUE_TEXTBOX_HEIGHT - BOTTOM_Y_OFFSET + DIALOGUE_TEXTBOX_PADDING) + (fontSpacing * i),
                    0xFFFFFFFF
            );
        }

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}