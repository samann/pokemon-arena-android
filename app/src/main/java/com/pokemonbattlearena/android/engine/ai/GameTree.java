package com.pokemonbattlearena.android.engine.ai;

import com.pokemonbattlearena.android.engine.match.Battle;
import com.pokemonbattlearena.android.engine.match.BattlePhase;
import com.pokemonbattlearena.android.engine.match.BattlePhaseResult;

/**
 * Created by nathan on 10/2/16.
 * Will edit and optimize once basic functionality is working.
 */

public final class GameTree {

    private Node root;

    public GameTree() {
        setRoot(null);
    }

    public GameTree(Node n) {
        setRoot(n);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node n) {
        root = n;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Battle getData(Node n) {
        if (!isEmpty()) {
            return root.getValue();
        }
        return null;
    }

    public void setData(Battle res) {
        if(!isEmpty()){
            getRoot().setValue(res);
        }
    }

    public void insertNode (Node prev, Node cur){
        if (prev != null) {
         //   prev.setChild(cur);
        }
    }

    protected void pretrav(Node t){
        if(t == null)
            return;
        System.out.println(t.toString()+" \n");
        for(int i=0; i<t.numberOfChildren(); i++)
            pretrav(t.getChild(i));
    }


}
