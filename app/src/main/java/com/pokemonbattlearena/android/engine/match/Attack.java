package com.pokemonbattlearena.android.engine.match;

import android.util.Log;

import com.pokemonbattlearena.android.engine.database.Move;
import com.pokemonbattlearena.android.engine.database.StatusEffect;

public class Attack implements Command {

    private static final String TAG = Attack.class.getName();

    private Move move;
    private BattlePokemon attacker;
    private BattlePokemon target;

    private static DamageCalculator damageCalculator = DamageCalculator.getInstance();
    private static StatusEffectCalculator statusEffectCalculator = StatusEffectCalculator.getInstance();

    public Attack(BattlePokemon attacker, Move move, BattlePokemon target) {
        this.move = move;
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {

        // TODO: Actually use real damage/effect calculations
        int damage = damageCalculator.calculateDamage(attacker, move, target);
        int remainingHp = target.getCurrentHp() - damage;
        target.setCurrentHp(remainingHp);
        boolean flinched = statusEffectCalculator.doesApplyFlinch(move);
        boolean applyStatusEffect = statusEffectCalculator.doesApplyStatusEffect(move, target);

        Log.i(TAG, move.getName() + " caused flinch? " + flinched);
        Log.i(TAG, move.getName() + " applied status effect? " + applyStatusEffect);

        target.setFlinched(true);

        if (applyStatusEffect) {
            StatusEffect effect = move.getStatusEffect();
            int turns = statusEffectCalculator.getStatusEffectTurns(effect);

            // Confusion can be applied separately from other status effects
            if (effect == StatusEffect.CONFUSION) {
                target.setConfused(true);
                target.setConfusedTurns(turns);
            } else {
                target.setStatusEffect(effect);
                target.setStatusEffectTurns(turns);
            }

            Log.i(TAG, "Effect: " + effect + " applied for " + turns + " turns");
        }

        if (target.getCurrentHp() <= 0) {
            Log.d(TAG, target.getOriginalPokemon().getName() + " fainted! (" + target.getCurrentHp() + " hp)");
            target.setFainted(true);
        }
    }
}
