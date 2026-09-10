package io.github.moosyu.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

import static io.github.moosyu.Unshattered.LOGGER;

public enum UnshatteredAttributeValues {
    HEALTH("health", "❤", 20.0, 0.0, 2147483647.0, 0xFFFC3A3A, true, false, false),
    HEALTH_REGEN("health_regen", "❣", 100.0, 0.0, 2048.0, 0xFFFC3A3A, true, false, true),
    DEFENCE("defence", "❈", 0.0, 0.0, 131072.0, 0xFF55FF55, true, false, false),
    TRUE_DEFENCE("true_defence", "❂", 0.0, 0.0, 1024.0, 0xFFFFFFFF, true, false, false),
    STRENGTH("strength", "❁", 0.0, 0.0, 4096.0, 0xFFFC3A3A, true, true, false),
    CRITICAL_CHANCE("critical_chance", "☣", 0.0, 0.0, 400.0, 0xFFB451D6, true, false, true),
    CRITICAL_DAMAGE("critical_damage", "☠", 0.0, 0.0, 4096.0, 0xFFB451D6, true, true, true),
    FEROCITY("ferocity", "⫽", 0.0, 0.0, 200.0, 0xFFFF5555, true, true, true),
    MANA("mana", "✎", 20.0, 0.0, 131072.0, 0xFF55D5FF, true, false, false),
    MANA_REGEN("mana_regen", "✎", 100.0, 0.0, 2048.0, 0xFF55D5FF, true, false, true),
    MINING_SPEED("mining_speed", "⸕", 0.0, 0.0, 128.0, 0xFFFFAA00, true, false, false),
    MINING_FORTUNE("mining_fortune", "☘", 0.0, 0.0, 2048.0, 0xFFFFAA00, true, false, false),
    MINING_SPREAD("mining_spread", "▚", 0.0, 0.0, 1024.0, 0xFFFFFF55, true, false, false),
    PRISTINE("pristine", "✧", 0.0, 0.0, 32.0, 0xFFAA00AA, true, false, false),
    FARMING_FORTUNE("farming_fortune", "☘", 0.0, 0.0, 2048.0, 0xFFFFAA00, true, false, false),
    FORAGING_FORTUNE("foraging_fortune", "\uD83E\uDE93", 0.0, 0.0, 2048.0, 0xFFFFAA00, true, false, false),
    SWEEP("sweep", "∮", 0.0, 0.0, 1024.0, 0xFF00AA00, true, false, false),
    COMBAT_FORTUNE("combat_fortune", "✯", 0.0, 0.0, 900.0, 0xFF55FFFF, true, false, false),
    FISHING_SPEED("fishing_speed", "☂", 0.0, 0.0, 448.0, 0xFF55FFFF, true, false, false),
    FISHING_FORTUNE("fishing_fortune", "⛃", 0.0, 0.0, 2048.0, 0xFF00AAAA, true, false, false),
    DAMAGE("damage", 0.0, 0.0, 2147483647.0),
    FINAL_DAMAGE_MODIFIER("final_damage_modifier", 1, 0, 10.0),
    BREAKING_POWER("breaking_power", "Ⓟ", 0, 0, 20, 0xFF00AA00, true, false, false);

    private static final Map<Attribute, UnshatteredAttributeValues> ATTRIBUTE_MAP = new HashMap<>();
    public final String id;
    public final String symbol;
    public final double def, min, max;
    public final int color;
    public DeferredHolder<Attribute, Attribute> holder;
    public final boolean visible, offensive, percentage;

    UnshatteredAttributeValues(String id, String symbol, double def, double min, double max, int color, boolean visible, boolean offensive, boolean percentage) {
        this.id = id;
        this.symbol = symbol;
        this.def = def;
        this.min = min;
        this.max = max;
        this.color = color;
        this.visible = visible;
        this.offensive = offensive;
        this.percentage = percentage;
    }

    UnshatteredAttributeValues(String id, double def, double min, double max) {
        this(id, "", def, min, max, 0x00000000, false, true, false);
    }

    public String getTranslationKey() {
        return "attribute.name.unshattered." + id;
    }

    // for accessing attributes using holder values
    public static void buildLookup() {
        ATTRIBUTE_MAP.clear();
        for (UnshatteredAttributeValues modAttribute : values()) {
            if (modAttribute.holder != null) {
                ATTRIBUTE_MAP.put(modAttribute.holder.value(), modAttribute);
            }
        }
    }

    public static UnshatteredAttributeValues fromAttribute(Attribute attribute) {
        return ATTRIBUTE_MAP.get(attribute);
    }

    public static void modifyAttributeBaseValue(Player player, UnshatteredAttributeValues attribute, double amount) {
        if (player.level().isClientSide()) return;
        AttributeInstance playerAttribute = player.getAttribute(attribute.holder);
        if (playerAttribute == null) {
            LOGGER.error("player didn't have attribute: {}", attribute.id);
            return;
        }
        playerAttribute.setBaseValue(playerAttribute.getBaseValue() + amount);
    }
}