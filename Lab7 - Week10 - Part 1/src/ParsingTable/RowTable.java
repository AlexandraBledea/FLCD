package ParsingTable;

import State.StateActionType;
import Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class RowTable {

    public int stateIndex;

    public StateActionType action;

    public String reduceNonTerminal;

    public List<String> reduceContent = new ArrayList<>();

    public List<Pair<String, Integer>> shifts = new ArrayList<>();

    public String reduceProductionString(){
        return this.reduceNonTerminal + " -> " + this.reduceContent;
    }

    @Override
    public String toString(){
        return "Row: " +
                "stateIndex= " + stateIndex +
                ", action='" + action + '\'' +
                ", reduceNonTerminal='" + reduceNonTerminal + '\'' +
                ", reduceContent = " + reduceContent +
                ", shifts = " + shifts;
    }

}
