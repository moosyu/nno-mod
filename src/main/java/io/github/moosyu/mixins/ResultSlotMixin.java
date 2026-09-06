package io.github.moosyu.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.moosyu.recipes.CraftingCountClientCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class ResultSlotMixin {
    @Shadow
    @Final
    private CraftingContainer craftSlots;

    @Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"), cancellable = true)
    private void replaceCraftingRemainderClientside(Player player, ItemStack carried, CallbackInfo ci, @Local(name = "slot") int slot) {
        if (player.level().isClientSide()) {
            ci.cancel();
            craftSlots.removeItem(slot, CraftingCountClientCache.requiredFor(slot));
        }
    }
}
