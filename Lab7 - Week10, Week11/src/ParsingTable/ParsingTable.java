package ParsingTable;

import java.util.ArrayList;
import java.util.List;

public class ParsingTable {

    public List<RowTable> entries;


    public ParsingTable() {
        this.entries = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Parsing Table: \n");

        for (RowTable entry : entries) {
            result.append(entry);
            result.append("\n");
        }

        return result.toString();
    }

}
