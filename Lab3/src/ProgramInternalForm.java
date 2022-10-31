import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {

    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;

    private List<Integer> types;

    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

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
