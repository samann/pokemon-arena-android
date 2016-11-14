package com.pokemonbattlearena.android.engine.ai;

import com.pokemonbattlearena.android.engine.match.Battle;
import com.pokemonbattlearena.android.engine.match.BattlePhase;
import com.pokemonbattlearena.android.engine.match.BattlePhaseResult;

/**
 * Created by nathan on 10/25/16.
 */

public class Node {

    protected Node[] children;
    Battle gameState;
    protected double hValue;


    public Node(Battle res) {

        children = new Node[256];
        this.gameState = res;

    }

    public Battle getValue() {
        return gameState;
    }

    public void setValue(Battle res) {
        gameState = res;
    }

    public void setChildAt(int i, Node n) {
        children[i] = n;
    }

    public Node getChild(int i){
        return children[i];
    }

    public int numberOfChildren() {
        return children.length;
    }

    public Node[] getChildren() {
        return children;
    }

    public boolean isLeaf() {
        if (children.length ==0) {
            return true;
        }
        return false;
    }

    public double getHValue() {
        return hValue;
    }

    public void setHValue(double h) {
       this.hValue = h;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
