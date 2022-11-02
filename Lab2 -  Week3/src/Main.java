public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(5);

        symbolTable.add("1");
        System.out.println(symbolTable.containsTerm("1"));
        Pair position = symbolTable.findPositionOfTerm("1");
        System.out.println(position);

        symbolTable.add("6");
        System.out.println(symbolTable.containsTerm("6"));
        System.out.println(symbolTable.findPositionOfTerm("6"));


        symbolTable.add("5");
        System.out.println(symbolTable.containsTerm("5"));
        System.out.println(symbolTable.findPositionOfTerm("5"));



        System.out.println(symbolTable.getHashTable());
    }
}