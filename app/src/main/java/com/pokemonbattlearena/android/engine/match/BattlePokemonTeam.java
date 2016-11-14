package com.pokemonbattlearena.android.engine.match;

import com.pokemonbattlearena.android.engine.database.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattlePokemonTeam {

    public List<BattlePokemon> battlePokemons;

    //Deep copy constructor
    public BattlePokemonTeam(BattlePokemonTeam other) {
        this.battlePokemons = new ArrayList<>();
        for (BattlePokemon bPokemon: other.getBattlePokemons()) {
            this.battlePokemons.add(new BattlePokemon(bPokemon));

        }
    }

    public BattlePokemonTeam(PokemonTeam pokemonTeam) {

        this.battlePokemons = new ArrayList<>();
        for (Pokemon p : pokemonTeam.getPokemons()) {
            this.battlePokemons.add(new BattlePokemon(p));
        }
    }

    public List<BattlePokemon> getBattlePokemons() {
        return battlePokemons;
    }

    public BattlePokemon getCurrentPokemon() {
        return battlePokemons.get(0);
    }

    public void switchPokemonAtPosition(int position) {
        Collections.swap(battlePokemons, 0, position);
    }

    public boolean allFainted() {

        for (BattlePokemon pokemon : battlePokemons) {
            if (!pokemon.isFainted()) {
                return false;
            }
        }

        return true;
    }
}
