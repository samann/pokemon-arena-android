package com.pokemonbattlearena.android.engine.ai;

import android.util.Log;

import com.pokemonbattlearena.android.engine.database.Move;
import com.pokemonbattlearena.android.engine.match.Battle;
import com.pokemonbattlearena.android.engine.match.BattlePhase;
import com.pokemonbattlearena.android.engine.match.BattlePhaseResult;
import com.pokemonbattlearena.android.engine.match.BattlePokemon;
import com.pokemonbattlearena.android.engine.match.BattlePokemonPlayer;
import com.pokemonbattlearena.android.engine.match.BattlePokemonTeam;
import com.pokemonbattlearena.android.engine.match.CommandResult;

import java.util.Random;

import static android.content.ContentValues.TAG;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Integer.MAX_VALUE;

/**
 * Created by nathan on 10/2/16.
 */

public class MiniMax {

    protected GameTree gamePossibilities;

    protected BattlePokemonPlayer ai;
    protected BattlePokemonPlayer human;
    protected BattlePokemonTeam aiTeam;
    protected BattlePokemonTeam huTeam;
    protected BattlePokemon aiCurrent;
    protected BattlePokemon huCurrent;
    protected Battle battle;
    protected int depth = 0;

    MiniMax(BattlePokemonPlayer aiPlayer, BattlePokemonPlayer humanPlayer, Battle battle) {
        this.gamePossibilities = new GameTree();

        this.battle = battle;

        boolean isAi = true;

        Battle state = battle;

        this.ai = state.getOpponent();
        this.human = state.getSelf();
        this.aiTeam = ai.getBattlePokemonTeam();
        this.huTeam = human.getBattlePokemonTeam();
        this.aiCurrent = aiTeam.getCurrentPokemon();
        this.huCurrent = huTeam.getCurrentPokemon();

        for (BattlePokemon pk: aiTeam.battlePokemons
             ) {
            Log.d(TAG, "MiniMax: " + pk.getOriginalPokemon().getName());
        }

        gamePossibilities.setRoot(buildTree(depth, new Node(state), isAi));

        for (int i = 0; i < 24; i++) {
           // Log.d(TAG, gamePossibilities.getRoot().getChild(i).toString());
        }
     //   Log.d(TAG, "" + chooseBestMove(gamePossibilities.getRoot(), depth, isAi));
    }


    public Node buildTree(int d, Node n, boolean isAi){
        if ( d < 0 ) {
            Log.e(TAG, "buildTree: Hit the depth" );
            return n;
        }
            int i = 0;
                // if (isAi) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        Battle childState = new Battle(n.getValue());
                        Log.e(TAG, "buildTree: New child");
                        childState.getCurrentBattlePhase().queueAction(ai, human, aiCurrent.getMoveSet().get(j));
                        childState.getCurrentBattlePhase().queueAction(human, ai, huCurrent.getMoveSet().get(k));
                        BattlePhaseResult res = childState.executeCurrentBattlePhase();
                        for (CommandResult cmd : res.getCommandResults()) {
                            childState.applyCommandResult(cmd);
                        }
                        Node ne = new Node(childState);
                        n.setChildAt(i, (buildTree(d - 1, ne, !isAi)));
                        i++;
                    }
                }
            return n;
    }

    public double hFunction(Node n) {
        //return new Random().nextInt(1000);
        return 10;
    }


    public Node choose() {
        //build tree
        return chooseBestMove(gamePossibilities.getRoot(), depth, true);
    }

    public Node chooseBestMove(Node n, int depth, boolean isAi) {
        if (depth == 0 || n.isLeaf()) {
                n.setHValue(hFunction(n));
                return n;
        }

        if (isAi) {
            n.setHValue(MIN_VALUE);
            double curValue;

            for (Node child : n.children) {
               // Log.d(TAG, child.toString());
                curValue = chooseBestMove(child, depth - 1, !isAi).getHValue();
                if (n.getHValue() > curValue) {
                } else {
                     n.setHValue(curValue);
                     n.setValue(child.getValue());
                }
            }
            return n;

        } else {
            n.setHValue(MAX_VALUE);
            double curValue;

            for (Node child : n.children) {
                curValue = chooseBestMove(child, depth - 1, !isAi).getHValue();
                if (n.getHValue() < curValue) {
                } else {
                    n.setHValue(curValue);
                    n.setValue(child.getValue());
                }
            }
            return n;
        }
    }


}
