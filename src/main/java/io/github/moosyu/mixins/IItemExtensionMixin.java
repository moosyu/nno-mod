package io.github.moosyu.mixins;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true)
    private void canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.isEquippableInSlot(stack, armorType) && entity instanceof Player player) {
            SkillRequirement itemSkillRequirement = stack.get(UnshatteredDataComponents.SKILL_REQUIREMENT.get());

            PlayerSkillsAttachment playerSkills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            if (itemSkillRequirement == null || itemSkillRequirement.level() <= playerSkills.getLevel(playerSkills.getExp(itemSkillRequirement.skill()))) {
                cir.setReturnValue(true);
            } else {
                if (!player.level().isClientSide()) {
                    player.sendSystemMessage(Component.literal(Component.translatable(itemSkillRequirement.skill().getTranslationKey()).getString()
                            + " level "
                            + itemSkillRequirement.level()
                            + " is required to equip this armour piece!").withColor(UnshatteredUtils.ERROR_COLOR)
                    );
                }

                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }
}
