package Scanner;

import Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {

    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;

    private List<Integer> types;

    /**
     * We initialize the two lists from the class
     */
    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    /**
     * We add a token/identifier/constant to its list + their position in the symbol table and we also add the category in the list of types
     * @param pair - Is a pair which is composed of the token/constant/idenfitier + its position in the symbol table
     * @param type - The category of the token (2, 3, 4) or constant (0) or identifier (1)
     */
    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type){
        this.tokenPositionPair.add(pair);
        this.types.add(type);
    }

    @Override
    public String toString(){
        StringBuilder computedString = new StringBuilder();
        for(int i = 0; i < this.tokenPositionPair.size(); i++) {
            computedString.append(this.tokenPositionPair.get(i).getFirst())
                    .append(" - ")
                    .append(this.tokenPositionPair.get(i).getSecond())
                    .append(" -> ")
                    .append(types.get(i))
                    .append("\n");
        }

        return computedString.toString();
    }
}
