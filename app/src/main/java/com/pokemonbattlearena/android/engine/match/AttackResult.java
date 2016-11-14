package com.pokemonbattlearena.android.engine.match;

import android.util.Log;

import com.pokemonbattlearena.android.engine.database.StatType;
import com.pokemonbattlearena.android.engine.database.StatusEffect;

public class AttackResult extends CommandResult {

    private transient static final String TAG = AttackResult.class.getName();

    private int moveUsedId;

    private int damageDone;
    private StatusEffect statusEffectApplied;
    private int statusEffectTurns;
    private boolean confused;
    private int confusedTurns;
    private boolean flinched;
    private int chargingTurns;
    private int rechargingTurns;
    private int healingDone;
    private int recoilTaken;
    private boolean fainted;
    private int attackStageChange;
    private int defenseStageChange;
    private int spAttackStageChange;
    private int spDefenseStageChange;
    private int speedStageChange;
    private int critStageChange;

    public AttackResult(AttackResult other) {
        this.moveUsedId = other.moveUsedId;
        this.damageDone = other.damageDone;
        this.statusEffectApplied = other.statusEffectApplied;
        this.statusEffectTurns = other.statusEffectTurns;
        this.confused = other.confused;
        this.confusedTurns = other.confusedTurns;
        this.flinched = other.flinched;
        this.chargingTurns = other.chargingTurns;
        this.rechargingTurns = other.rechargingTurns;
        this.healingDone = other.healingDone;
        this.recoilTaken = other.recoilTaken;
        this.fainted = other.fainted;
        this.attackStageChange = other.attackStageChange;
        this.defenseStageChange = other.defenseStageChange;
        this.spAttackStageChange = other.spAttackStageChange;
        this.spDefenseStageChange = other.spDefenseStageChange;
        this.speedStageChange = other.speedStageChange;
        this.critStageChange = other.critStageChange;
    }

    private AttackResult(Builder builder) {

        super();

        this.targetInfo = builder.targetInfo;
        this.moveUsedId = builder.moveUsedId;

        this.damageDone = builder.damageDone;
        this.statusEffectApplied = builder.statusEffectApplied;
        this.statusEffectTurns = builder.statusEffectTurns;
        this.confused = builder.confused;
        this.confusedTurns = builder.confusedTurns;
        this.flinched = builder.flinched;
        this.chargingTurns = builder.chargingTurns;
        this.rechargingTurns = builder.rechargingTurns;
        this.healingDone = builder.healingDone;
        this.recoilTaken = builder.recoilTaken;
        this.fainted = builder.fainted;
        this.attackStageChange = builder.attackStageChange;
        this.defenseStageChange = builder.defenseStageChange;
        this.spAttackStageChange = builder.spAttackStageChange;
        this.spDefenseStageChange = builder.spDefenseStageChange;
        this.speedStageChange = builder.speedStageChange;
        this.critStageChange = builder.critStageChange;
    }

    public int getMoveUsedId() {
        return moveUsedId;
    }

    public int getDamageDone() {
        return damageDone;
    }

    public StatusEffect getStatusEffectApplied() {
        return statusEffectApplied;
    }

    public boolean isConfused() {
        return confused;
    }

    public int getConfusedTurns() {
        return confusedTurns;
    }

    public boolean isFlinched() {
        return flinched;
    }

    public int getChargingTurns() {
        return chargingTurns;
    }

    public int getRechargingTurns() {
        return rechargingTurns;
    }

    public int getStatusEffectTurns() {
        return statusEffectTurns;
    }

    public int getHealingDone() {
        return healingDone;
    }

    public int getRecoilTaken() {
        return recoilTaken;
    }

    public boolean isFainted() {
        return fainted;
    }

    public int getAttackStageChange() {
        return attackStageChange;
    }

    public int getDefenseStageChange() {
        return defenseStageChange;
    }

    public int getSpAttackStageChange() {
        return spAttackStageChange;
    }

    public int getSpDefenseStageChange() {
        return spDefenseStageChange;
    }

    public int getSpeedStageChange() {
        return speedStageChange;
    }

    public int getCritStageChange() {
        return critStageChange;
    }

    protected static class Builder {

        private TargetInfo targetInfo;
        private int moveUsedId;

        private int damageDone;
        private StatusEffect statusEffectApplied;
        private int statusEffectTurns;
        private boolean confused;
        private int confusedTurns;
        private boolean flinched;
        private int chargingTurns;
        private int rechargingTurns;
        private int healingDone;
        private int recoilTaken;
        private boolean fainted;
        private int attackStageChange;
        private int defenseStageChange;
        private int spAttackStageChange;
        private int spDefenseStageChange;
        private int speedStageChange;
        private int critStageChange;

        protected Builder(TargetInfo targetInfo, int moveUsedId) {
            this.targetInfo = targetInfo;
            this.moveUsedId = moveUsedId;
        }

        protected Builder setDamageDone(int damageDone) {
            this.damageDone = damageDone;
            return this;
        }

        protected Builder setStatusEffectApplied(StatusEffect statusEffect) {
            this.statusEffectApplied = statusEffect;
            return this;
        }

        protected Builder setStatusEffectTurns(int statusEffectTurns) {
            this.statusEffectTurns = statusEffectTurns;
            return this;
        }

        protected Builder setConfused(boolean confused) {
            this.confused = confused;
            return this;
        }

        protected Builder setConfusedTurns(int confusedTurns) {
            this.confusedTurns = confusedTurns;
            return this;
        }

        protected Builder setFlinched(boolean flinched) {
            this.flinched = flinched;
            return this;
        }

        protected Builder setChargingTurns(int chargingTurns) {
            this.chargingTurns = chargingTurns;
            return this;
        }

        protected Builder setRechargingTurns(int rechargingTurns) {
            this.rechargingTurns = rechargingTurns;
            return this;
        }

        protected Builder setHealingDone(int healingDone) {
            this.healingDone = healingDone;
            return this;
        }

        protected Builder setRecoilTaken(int recoilTaken) {
            this.recoilTaken = recoilTaken;
            return this;
        }

        protected Builder setFainted(boolean fainted) {
            this.fainted = fainted;
            return this;
        }

        protected Builder setAttackStageChange(int stageChange) {
            this.attackStageChange = stageChange;
            return this;
        }

        protected Builder setDefenseStageChange(int stageChange) {
            this.defenseStageChange = stageChange;
            return this;
        }

        protected Builder setSpAttackStageChange(int stageChange) {
            this.spAttackStageChange = stageChange;
            return this;
        }

        protected Builder setSpDefenseStageChange(int stageChange) {
            this.spDefenseStageChange = stageChange;
            return this;
        }

        protected Builder setSpeedStageChange(int stageChange) {
            this.speedStageChange = stageChange;
            return this;
        }

        protected Builder setCritStageChange(int stageChange) {
            this.critStageChange = stageChange;
            return this;
        }

        protected AttackResult build() {

            Log.i(TAG, "Building AttackResult");
            return new AttackResult(this);
        }
    }

    @Override
    public CommandResult makeCopy() {
        return new AttackResult(this);
    }
}
