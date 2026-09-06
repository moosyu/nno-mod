package io.github.moosyu.items;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum ItemTypes implements StringRepresentable {
    ITEM("item", false),
    FISH("fish", false),
    LOG("log", false),
    SWORD("sword", true),
    DAGGER("dagger", true, 4),
    AXE("axe", true),
    BATTLE_AXE("battle_axe", true),
    BOW("bow", true),
    SHORTBOW("shortbow", true),
    HELMET("helmet", true),
    CHESTPLATE("chestplate", true),
    LEGGINGS("leggings", true),
    BOOTS("boots", true),
    WAND("wand", true),
    TALISMAN("talisman", false),
    STAFF("staff", true),
    GRIMOIRES("grimoire", true),
    LONGSWORD("longsword", true),
    WARHAMMER("warhammer", true),
    WHIP("whip", true),
    FISHING_ROD("fishing_rod", true),
    MATERIAL("material", false),
    CLEAVER("cleaver", true),
    PICKAXE("pickaxe", true);

    private final String serializedName;
    private final boolean reforgeable;
    private final int invulnerability;

    ItemTypes(String serializedName, boolean reforgeable, int invulnerability) {
        this.serializedName = serializedName;
        this.reforgeable = reforgeable;
        this.invulnerability = invulnerability;
    }

    ItemTypes(String serializedName, boolean reforgeable) {
        this.serializedName = serializedName;
        this.reforgeable = reforgeable;
        this.invulnerability = 10;
    }

    @Override
    public @NonNull String getSerializedName() {
        return serializedName;
    }

    public boolean reforgeable() {
        return reforgeable;
    }

    public int getInvulnerability() {
        return invulnerability;
    }

    public static final Codec<ItemTypes> CODEC = Codec.STRING.xmap(ItemTypes::valueOf, ItemTypes::name);

}
