package com.pokemonbattlearena.android.engine.match;

import java.util.UUID;

public class BattlePokemonPlayer {

    // Unique id to determine source/target player when applying CommandResult
    private String id;
    private PokemonPlayer pokemonPlayer;

    private transient BattlePokemonTeam battlePokemonTeam;

    public BattlePokemonPlayer(BattlePokemonPlayer other) {
        this.id = other.id;
        this.pokemonPlayer = new PokemonPlayer(other.pokemonPlayer);
        this.battlePokemonTeam = new BattlePokemonTeam(other.battlePokemonTeam);

    }

    public BattlePokemonPlayer(PokemonPlayer player) {
        this.id = player.getPlayerId();
        this.battlePokemonTeam = new BattlePokemonTeam(player.getPokemonTeam());
        this.pokemonPlayer = player;
    }

    public String getId() {
        return id;
    }

    public BattlePokemonTeam getBattlePokemonTeam() {
        return battlePokemonTeam;
    }

    public PokemonPlayer getPokemonPlayer() {
        return this.pokemonPlayer;
    }
}
