package com.pokemonbattlearena.android.engine.match;

import com.pokemonbattlearena.android.engine.database.Move;
import com.pokemonbattlearena.android.engine.database.Pokemon;
import com.pokemonbattlearena.android.engine.database.StatusEffect;

public class BattlePokemon {

    private Pokemon originalPokemon;
    private int currentHp;
    private StatusEffect statusEffect;
    private int statusEffectTurns;
    private boolean confused;
    private int confusedTurns;
    private boolean flinched;
    private int chargingForTurns;
    private boolean fainted;
    private Move[] moveSet = new Move[4];

    public BattlePokemon(Pokemon pokemon) {

        this.originalPokemon = pokemon;
        this.currentHp = pokemon.getHp();
        this.confused = false;
        this.flinched = false;
        this.fainted = false;
    }

    public Pokemon getOriginalPokemon() {
        return originalPokemon;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
    }

    public void setStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }

    public boolean hasStatusEffect() {
        return statusEffect != null;
    }

    public int getStatusEffectTurns() {
        return statusEffectTurns;
    }

    public void setStatusEffectTurns(int statusEffectTurns) {
        this.statusEffectTurns = statusEffectTurns;
    }

    public boolean isConfused() {
        return confused;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public int getConfusedTurns() {
        return confusedTurns;
    }

    public void setConfusedTurns(int confusedTurns) {
        this.confusedTurns = confusedTurns;
    }

    public boolean isFlinched() {
        return flinched;
    }

    public void setFlinched(boolean flinched) {
        this.flinched = flinched;
    }

    public int getChargingForTurns() {
        return chargingForTurns;
    }

    public void setChargingForTurns(int chargingForTurns) {
        this.chargingForTurns = chargingForTurns;
    }

    public boolean isCharging() {
        return this.chargingForTurns > 0;
    }

    public boolean isFainted() { return this.fainted; }

    public void setFainted(boolean fainted) { this.fainted = fainted; }

    public Move[] getMoveSet() { return moveSet; }

    public void setMoveSet(Move[] chosenMoves) { this.moveSet = chosenMoves; }
}
