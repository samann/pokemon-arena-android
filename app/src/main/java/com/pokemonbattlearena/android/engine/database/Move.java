package com.pokemonbattlearena.android.engine.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.StringBytesType;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "moves")
public class Move {

    protected final static String ID_FIELD_NAME = "id";
    protected final static String NAME_FIELD_NAME = "name";
    protected final static String DESCRIPTION_FIELD_NAME = "description";
    protected final static String TYPE_1_FIELD_NAME = "type_1";
    protected final static String CATEGORY_FIELD_NAME = "category";
    protected final static String POWER_FIELD_NAME = "power";
    protected final static String ACCURACY_FIELD_NAME = "accuracy";
    protected final static String POWER_POINTS_FIELD_NAME = "power_points";
    protected final static String STATUS_EFFECT_FIELD_NAME = "status_effect";
    protected final static String STATUS_EFFECT_CHANCE_FIELD_NAME = "status_effect_chance";
    protected final static String STAGE_CHANGE_STAT_FIELD_NAME = "stage_change_stat";
    protected final static String STAGE_CHANGE_FIELD_NAME = "stage_change";
    protected final static String STAGE_CHANGE_CHANCE_FIELD_NAME = "stage_change_chance";
    protected final static String CAN_FLINCH_FIELD_NAME = "can_flinch";
    protected final static String MIN_HITS_FIELD_NAME = "min_hits";
    protected final static String MAX_HITS_FIELD_NAME = "max_hits";
    protected final static String CHARGING_TURNS_FIELD_NAME = "charging_turns";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
    private String description;
    @DatabaseField(columnName = TYPE_1_FIELD_NAME)
    private String type1;
    @DatabaseField(columnName = CATEGORY_FIELD_NAME)
    private String category;
    @DatabaseField(columnName = POWER_FIELD_NAME)
    private int power;
    @DatabaseField(columnName = ACCURACY_FIELD_NAME)
    private int accuracy;
    @DatabaseField(columnName = POWER_POINTS_FIELD_NAME)
    private int powerPoints;
    @DatabaseField(columnName = STATUS_EFFECT_FIELD_NAME)
    private String statusEffect;
    @DatabaseField(columnName = STATUS_EFFECT_CHANCE_FIELD_NAME)
    private int statusEffectChance;
    @DatabaseField(columnName = STAGE_CHANGE_STAT_FIELD_NAME)
    private String stageChangeStat;
    @DatabaseField(columnName = STAGE_CHANGE_FIELD_NAME)
    private int stageChange;
    @DatabaseField(columnName = STAGE_CHANGE_CHANCE_FIELD_NAME)
    private int stageChangeChance;
    @DatabaseField(columnName = CAN_FLINCH_FIELD_NAME)
    private boolean canFlinch;
    @DatabaseField(columnName = MIN_HITS_FIELD_NAME)
    private int minHits;
    @DatabaseField(columnName = MAX_HITS_FIELD_NAME)
    private int maxHits;
    @DatabaseField(columnName = CHARGING_TURNS_FIELD_NAME)
    private int chargingTurns;

    public Move() {
        // Constructor for ORMLite
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType1() {
        return type1;
    }

    public ElementalType getElementalType1() {
        return ElementalType.valueOf(this.type1.toUpperCase());
    }

    public String getCategory() {
        return category;
    }

    public MoveType getMoveType() {

        return MoveType.valueOf(this.category.toUpperCase());
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPowerPoints() {
        return powerPoints;
    }

    public String getStatusEffectString() {
        return statusEffect;
    }

    public StatusEffect getStatusEffect() {
        try {
            return StatusEffect.valueOf(this.statusEffect.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public int getStatusEffectChance() {
        return statusEffectChance;
    }

    public String getStageChangeStat() {
        return stageChangeStat;
    }

    public StatType getStageChangeStatType() {
        return StatType.valueOf(this.stageChangeStat.toUpperCase());
    }

    public int getStageChange() {
        return stageChange;
    }

    public int getStageChangeChance() {
        return stageChangeChance;
    }

    public boolean canFlinch() { return canFlinch; }

    public int getMinHits() {
        return minHits;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public int getChargingTurns() {
        return chargingTurns;
    }

    public boolean isChargingMove() {
        return chargingTurns > 0;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Move: (" + id + ") " + name + " [" + type1 + "]");
        sb.append("\n");
        sb.append(" - Description: " + description + "\n");
        sb.append(" - Category: " + category + "\n");
        sb.append(" - Power: " + power + "\n");
        sb.append(" - Accuracy: " + accuracy + "\n");
        sb.append(" - PowerPoints: " + powerPoints + "\n");
        sb.append(" - StatusEffect: " + statusEffect + "\n");
        sb.append(" - StatusEffectChance: " + statusEffectChance + "\n");
        sb.append(" - StageChangeStat: " + stageChangeStat + "\n");
        sb.append(" - StageChange: " + stageChange + "\n");
        sb.append(" - StageChangeChance: " + stageChangeChance + "\n");
        sb.append(" - CanFlinch: " + canFlinch + "\n");
        sb.append(" - MaxHits: " + maxHits + "\n");
        sb.append(" - MinHits: " + minHits + "\n");


        return sb.toString();
    }
}
