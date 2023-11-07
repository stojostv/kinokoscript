package kinoko.provider.map;

public enum FieldLimit {
    UNABLE_TO_JUMP(0x1),
    UNABLE_TO_USE_SKILL(0x2),
    UNABLE_TO_USE_SUMMON_LIMIT(0x4),
    UNABLE_TO_USE_MYSTIC_DOOR(0x8),
    UNABLE_TO_MIGRATE(0x10),
    UNABLE_TO_USE_PORTAL_SCROLL(0x20),
    UNABLE_TO_USE_TELEPORT_ITEM(0x40),
    UNABLE_TO_OPEN_MINIGAME(0x80),
    UNABLE_TO_USE_SPECIFIC_PORTAL_SCROLL(0x100),
    UNABLE_TO_USE_TAMING_MOB(0x200),
    UNABLE_TO_CONSUME_STAT_CHANGE_ITEM(0x400),
    UNABLE_TO_CHANGE_PARTY_BOSS(0x800),
    NO_MONSTER_CAPACITY_LIMIT(0x1000),
    UNABLE_TO_USE_WEDDING_INVITATION_ITEM(0x2000),
    UNABLE_TO_USE_CASH_WEATHER(0x4000),
    UNABLE_TO_USE_PET(0x8000),
    UNABLE_TO_USE_ANTI_MACRO_ITEM(0x10_000),
    UNABLE_TO_FALL_DOWN(0x20_000),
    UNABLE_TO_SUMMON_NPC(0x40_000),
    NO_EXP_DECREASE(0x80_000),
    NO_DAMAGE_ON_FALLING(0x100_000),
    PARCEL_OPEN_LIMIT(0x200_000),
    DROP_LIMIT(0x400_000),
    UNABLE_TO_USE_ROCKET_BOOST(0x800_000),
    NO_ITEM_OPTION_LIMIT(0x1_000_000),
    NO_QUEST_ALERT(0x2_000_000),
    NO_ANDROID(0x4_000_000),
    AUTO_EXPAND_MINIMAP(0x8_000_000),
    MOVE_SKILL_ONLY(0x10_000_000);

    private final int value;

    FieldLimit(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
