package com.pokemonbattlearena.android.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.pokemonbattlearena.android.activity.SplashActivity;

/**
 * Created by Spencer Amann on 12/2/16.
 */

public class PokemonUtils {
    public static final int BATTLE_REQUEST = 42247;
    public static final String PROFILE_NAME_KEY = "profile_name";
    public static final String PREFS_KEY = "Pokemon Battle Prefs";
    public static final String AI_BATTLE_KEY = "AI_battle";
    public static String CHAT_TYPE_KEY = "chat_type";
    public static final String CHAT_ALLOW_IN_GAME = "allows_switch_chat";
    public static String POKEMON_TEAM_KEY = "pokemon_team";
    public static final int typePokemon = 0;
    public static final int typeStatus = 1;


    public static Drawable getDrawableForPokemon(Context c, String name, int type) {
        String middle = "";
        switch (type) {
            case typePokemon:
                middle = "_pokemon_";
                break;
            case typeStatus:
                middle = "_status_";
                break;
            default:
                break;
        }
        String key = "ic" + middle + name.toLowerCase();
        int id = c.getResources().getIdentifier(key, "drawable", c.getPackageName());
        return c.getDrawable(id);
    }
}
