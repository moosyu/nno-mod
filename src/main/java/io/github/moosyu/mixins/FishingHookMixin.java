package io.github.moosyu.mixins;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.util.UnshatteredUtils;
import io.github.moosyu.util.damage.DamageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
    protected FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Shadow
    private int timeUntilLured;

    @Shadow
    private int timeUntilHooked;

    @Shadow
    private int nibble;

    @Shadow
    private float fishAngle;

    @Shadow
    private static EntityDataAccessor<Boolean> DATA_BITING;

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    protected void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        if (this.getOwner() instanceof Player player) {
            Entity entity = hitResult.getEntity();
            if (entity.is(EntityType.ARMOR_STAND) && !player.isCreative()) {
                ci.cancel();
            } else {
                if (entity instanceof LivingEntity livingEntity && !player.level().isClientSide()) {
                    DamageUtil.playerDealDamage(player,
                            livingEntity,
                            UnshatteredUtils.triggerInstantPassiveAbility((ServerPlayer) player,
                                    livingEntity,
                                    player.getItemInHand(InteractionHand.MAIN_HAND).getItem()
                            ),
                            ItemTypes.FISHING_ROD
                    );
                }
            }
        }
    }

    @Inject(method = "catchingFish", at = @At("HEAD"), cancellable = true)
    private void catchingFish(CallbackInfo ci) {
        ci.cancel();
        FishingHook hook = (FishingHook)(Object)this;
        if (!(hook.getOwner() instanceof Player player)) return;
        ServerLevel level = (ServerLevel)hook.level();
        RandomSource random = hook.getRandom();
        double fishingSpeed = player.getAttributeValue(UnshatteredAttributeValues.FISHING_SPEED.holder);
        double fishingSpeedPercentage = (UnshatteredAttributeValues.FISHING_SPEED.max + 20 - fishingSpeed) / UnshatteredAttributeValues.FISHING_SPEED.max;
        if (this.nibble > 0) {
            --this.nibble;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                hook.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= 1;
            if (this.timeUntilHooked > 0) {
                this.fishAngle = this.fishAngle + (float)random.triangle(0.0, 9.188);
                float angle = this.fishAngle * (float) (Math.PI / 180.0);
                float angleSin = Mth.sin(angle);
                float angleCos = Mth.cos(angle);
                double fishX = hook.getX() + angleSin * this.timeUntilHooked * 0.1F;
                double fishY = Mth.floor(hook.getY()) + 1.0F;
                double fishZ = hook.getZ() + angleCos * this.timeUntilHooked * 0.1F;
                BlockState splashBlockState = level.getBlockState(BlockPos.containing(fishX, fishY - 1.0, fishZ));
                if (splashBlockState.is(Blocks.WATER)) {
                    if (random.nextFloat() < 0.15F) {
                        level.sendParticles(ParticleTypes.BUBBLE, fishX, fishY - 0.1F, fishZ, 1, angleSin, 0.1, angleCos, 0.0);
                    }

                    float particleXMovement = angleSin * 0.04F;
                    float particleZMovement = angleCos * 0.04F;
                    level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, particleZMovement, 0.01, -particleXMovement, 1.0);
                    level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, -particleZMovement, 0.01, particleXMovement, 1.0);
                }
            } else {
                level.playSound(null, hook.getX(), hook.getY(), hook.getZ(), SoundEvents.FISHING_BOBBER_SPLASH, hook.getSoundSource(), 0.25f, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
                double y = hook.getY() + 0.5;
                level.sendParticles(ParticleTypes.BUBBLE, hook.getX(), y, hook.getZ(), (int)(1.0F + hook.getBbWidth() * 20.0F), hook.getBbWidth(), 0.0, hook.getBbWidth(), 0.2F);
                level.sendParticles(
                        ParticleTypes.FISHING,
                        hook.getX(),
                        y,
                        hook.getZ(),
                        (int)(1.0F + hook.getBbWidth() * 20.0F),
                        hook.getBbWidth(),
                        0.0,
                        hook.getBbWidth(),
                        0.2F
                );
                this.nibble = Mth.nextInt(random, 20, 40);
                hook.getEntityData().set(DATA_BITING, true);
                System.out.println("Nibbling");
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= 1;
            float teaseChance = 0.15F;
            if (this.timeUntilLured < 20) {
                teaseChance += (20 - this.timeUntilLured) * 0.05F;
            } else if (this.timeUntilLured < 40) {
                teaseChance += (40 - this.timeUntilLured) * 0.02F;
            } else if (this.timeUntilLured < 60) {
                teaseChance += (60 - this.timeUntilLured) * 0.01F;
            }

            if (random.nextFloat() < teaseChance) {
                float angle = Mth.nextFloat(random, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
                float dist = Mth.nextFloat(random, 25.0F, 60.0F);
                double fishX = hook.getX() + Mth.sin(angle) * dist * 0.1;
                double fishY = Mth.floor(hook.getY()) + 1.0F;
                double fishZ = hook.getZ() + Mth.cos(angle) * dist * 0.1;
                BlockState splashBlockState = level.getBlockState(BlockPos.containing(fishX, fishY - 1.0, fishZ));
                if (splashBlockState.is(Blocks.WATER)) {
                    level.sendParticles(ParticleTypes.SPLASH, fishX, fishY, fishZ, 2 + random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
                }
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(random, 0.0F, 360.0F);
                this.timeUntilHooked = random.nextInt(10, Math.max(20, (int) (100 * fishingSpeedPercentage)));
                System.out.println("Fish approaching");
            }
        } else {
            this.timeUntilLured = random.nextInt(20, Math.max(40, (int) (160 * fishingSpeedPercentage)));
        }
    }
}
