package com.pokemonbattlearena.android.engine.match;

public abstract class CommandResult {

    TargetInfo targetInfo;

    public CommandResult() { }

    public TargetInfo getTargetInfo() {
        return targetInfo;
    }

    public abstract CommandResult makeCopy();
}
