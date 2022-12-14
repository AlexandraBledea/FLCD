package LR0;

import State.State;
import Utils.Pair;

import java.util.*;

public class CanonicalCollection {

    private List<State> states;
    private Map<Pair<Integer, String>, Integer> adjacencyList;

    public CanonicalCollection(){
        this.states = new ArrayList<>();
        this.adjacencyList = new LinkedHashMap<>();
    }

    public CanonicalCollection(List<State> states, Map<Pair<Integer, String>, Integer> adjacencyList) {
        this.states = states;
        this.adjacencyList = adjacencyList;
    }

    /**
     * With this method we keep track of which states are created by another state and through what symbol
     * @param indexFirstState - the index of the state we start from
     * @param symbol - the symbol we goTo
     * @param indexSecondState - the index of the state we obtained
     */

    public void connectStates(Integer indexFirstState, String symbol, Integer indexSecondState){
        this.adjacencyList.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }

    /**
     * With this method we add a new state to the list of states
     * @param state - the state we are about to add
     */
    public void addState(State state){
        this.states.add(states.size(), state);
    }

    public List<State> getStates(){
        return this.states;
    }

    public Map<Pair<Integer, String>, Integer> getAdjacencyList(){
        return this.adjacencyList;
    }

}
