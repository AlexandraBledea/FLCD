import java.lang.reflect.Array;
import java.util.*;

public class LR {

    private final Grammar grammar;

    private final Grammar workingGrammar;
    private List<Pair<String, List<String>>> orderedProductions;

    public LR(Grammar grammar) throws Exception {
        this.grammar = grammar;

        if(this.grammar.getIsEnriched()){
            this.workingGrammar = this.grammar;
        } else {
            this.workingGrammar = this.grammar.getEnrichedGrammar();
        }

        orderedProductions = this.grammar.getOrderedProductions();
    }

    /**
     * With this method we get the non-terminal which is preceded by dot
     * @param item - the item in which we look for the non-terminal
     * @return - the non-terminal if it is found or null otherwise
     */
    public String getNonTerminalPrecededByDot(Item item){
        try {
            String term = item.getRightHandSide().get(item.getPositionForDot());
            if(!grammar.getNonTerminals().contains(term)){
                return null;
            }

            return term;
        }
        catch (Exception e){
            return null;
        }

    }


    /**
     * With this method we compute the closure for an item (an item being of the form [A->alpha.beta])
     * @param item - the analysis element
     * @return - the closure for item given as input
     */
    public State closure(Item item){

        Set<Item> oldClosure;
        Set<Item> currentClosure = Set.of(item);

        do {
            oldClosure = currentClosure;
            Set<Item> newClosure = new LinkedHashSet<>(currentClosure);
            for(Item i: currentClosure){
                String nonTerminal = getNonTerminalPrecededByDot(i);
                if(nonTerminal != null){
                    for(List<String> prod:  grammar.getProductionsForNonTerminal(nonTerminal)){
                        Item currentItem = new Item(nonTerminal, prod, 0);
                        newClosure.add(currentItem);
                    }
                }
            }
            currentClosure = newClosure;
        } while(!oldClosure.equals(currentClosure));

        return new State(currentClosure);
    }

    /**
     * With this method, in state S, we search LR(0) item that has dot in front of symbol X.
     * Move the dot after symbol X and call closure for this new item.
     * @param state - the state S from which we want to move
     * @param elem - the symbol after we look
     * @return - returns a State containing  a list of states
     * composed of the states for each computer closure
     */
    public State goTo(State state, String elem) {
        Set<Item> result = new LinkedHashSet<>();

        for (Item i : state.getItems()) {
            try {
                String nonTerminal = i.getRightHandSide().get(i.getPositionForDot());
                if (Objects.equals(nonTerminal, elem)) {
                    Item nextItem = new Item(i.getLeftHandSide(), i.getRightHandSide(), i.getPositionForDot() + 1);
                    State newState = closure(nextItem);
                    result.addAll(newState.getItems());
                }
            } catch(Exception ignored) {
            }
        }

        return new State(result);
    }

    /**
     * With this method we compute the canonical collection for the grammar.
     * @return - the formed canonical collection
     */
    public CanonicalCollection canonicalCollection(){
        CanonicalCollection canonicalCollection = new CanonicalCollection();

        canonicalCollection.addState(
                closure(
                        new Item(
                                workingGrammar.getStartingSymbol(),
                                workingGrammar.getProductionsForNonTerminal(workingGrammar.getStartingSymbol()).get(0),
                                0
                        )
                )
        );

        int index = 0;
        while(index < canonicalCollection.getStates().size()){
            for(String symbol: canonicalCollection.getStates().get(index).getSymbolsSucceedingTheDot()) {
                State newState = goTo(canonicalCollection.getStates().get(index), symbol);
                if (newState.getItems().size() != 0) {
                    int indexState = canonicalCollection.getStates().indexOf(newState);
                    if (indexState == -1) {
                        canonicalCollection.addState(newState);
                    }
                }
            }
            ++index;
        }
        return canonicalCollection;

    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Grammar getWorkingGrammar() {
        return workingGrammar;
    }
}
