package com.pokemonbattlearena.android.engine.match;

class Switch implements Command {

    private transient static final String TAG = Switch.class.getName();

    private BattlePokemonPlayer attackingPlayer;
    private int positionToSwitchTo;

    public Switch(Switch other) {
        this.attackingPlayer = new BattlePokemonPlayer(other.attackingPlayer);
        this.positionToSwitchTo = other.positionToSwitchTo;
    }

    Switch(BattlePokemonPlayer attacker, int positionToSwitchTo) {
        this.attackingPlayer = attacker;
        this.positionToSwitchTo = positionToSwitchTo;
    }

    @Override
    public SwitchResult execute() {

        TargetInfo targetInfo = new TargetInfo(attackingPlayer);
        SwitchResult.Builder builder = new SwitchResult.Builder(targetInfo);

        builder.setPositionOfPokemon(positionToSwitchTo);

        return builder.build();
    }

    @Override
    public Command makeCopy() {
        return new Switch(this);
    }
}
