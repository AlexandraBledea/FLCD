package ParsingTree;

import LR0.Grammar;
import Utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputTree {

    private ParsingTreeRow root;

    private final Grammar grammar;

    private int currentIndex = 1;

    private int indexInInput = 1;

    private int maxLevel = 0;

    private List<ParsingTreeRow> treeList;

    public OutputTree(Grammar grammar){
        this.grammar = grammar;
    }

    public ParsingTreeRow getRoot(){
        return this.root;
    }

    /**
     * With this method we start the generation of the parse tree
     * @param inputSequence - the list which contains the production numbers (represents actually the output band from the parse algorithm)
     * @return - the root of the tree
     */
    public ParsingTreeRow generateTreeFromSequence(List<Integer> inputSequence){
        int productionIndex = inputSequence.get(0);

        Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(productionIndex);

        this.root = new ParsingTreeRow(productionString.getFirst());
        this.root.setIndex(0);
        this.root.setLevel(0);

        this.root.setLeftChild(buildRecursive(1, this.root, productionString.getSecond(), inputSequence));

        return this.root;
    }


    /**
     * With this method we build recursively each node from the parse tree
     * If it is a terminal, we try to set its right sibling as well
     * If it is a non-terminal, we try to set its right sibling and its left child as well
     * @param level - the level in the tree
     * @param parent - the parent of the current node from the tree
     * @param currentContent - the current elements which compose the production (So if we had A -> a b, then the currentContent is [a, b])
     * @param inputSequence - the list with the production numbers from the output band of the parse algorithm
     * @return - the newly created node
     */
    public ParsingTreeRow buildRecursive(int level, ParsingTreeRow parent, List<String> currentContent, List<Integer> inputSequence){
        if(currentContent.isEmpty() || this.indexInInput >= inputSequence.size() + 1){
            return null;
        }

        // We take the current symbol from the current production content
        String currentSymbol = currentContent.get(0);

        // If the symbol is a terminal, then we only need to set for it the rightSibling
        // It cannot be a parent for any other node in the tree
        if(this.grammar.getTerminals().contains(currentSymbol)){

            // We create a new node in our tree with the current symbol
            ParsingTreeRow node = new ParsingTreeRow(currentSymbol);


            // We set its index, level and parent
            // Increase the index
            node.setIndex(this.currentIndex);
            this.currentIndex++;

            node.setLevel(level);
            node.setParent(parent);

            // We create a new list which contains the elems from currentContent
            // But from it, we remove the first element, so more exactly
            // We remove the symbol we already added to our tree
            List<String> newList = new ArrayList<>(currentContent);
            newList.remove(0);

            // Then we call the recursion again in order to set the right sibling
            node.setRightSibling(buildRecursive(level, parent, newList, inputSequence));

            return node;
        }
        // If the symbol is a non-terminal, then it means it may have a right sibling,
        // and it will be a parent too for another node
        else if(this.grammar.getNonTerminals().contains(currentSymbol)){


            // We get the production index corresponding to the non-terminal
            int productionIndex = inputSequence.get(this.indexInInput);

            // We take the production string corresponding to the index
            Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(productionIndex);

            // We create a ew note in our tree with the current symbol
            ParsingTreeRow node = new ParsingTreeRow(currentSymbol);

            // We set the index, level and parent for the node
             node.setIndex(this.currentIndex);
             node.setLevel(level);
             node.setParent(parent);

            // We create a new variable for the new level, which we are going to use to set the left child
             int newLevel = level + 1;
             if(newLevel > this.maxLevel)
                 this.maxLevel = newLevel;

             this.currentIndex++;
             this.indexInInput++;

             // We call the recursion to set the left child
             node.setLeftChild(buildRecursive(newLevel, node, productionString.getSecond(), inputSequence));

             List<String> newList = new ArrayList<>(currentContent);
             newList.remove(0);

             // We call the recursion to set the right sibling
             node.setRightSibling(buildRecursive(level, parent, newList, inputSequence));

             return node;
        }
        else {
            System.out.println("ERROR");
            return null;
        }
    }

    public void printTree(ParsingTreeRow node, String filePath) throws IOException {
        this.treeList = new ArrayList<>();
        createList(node);

        for(int i = 0; i <= this.maxLevel; i++){
            for(ParsingTreeRow n: this.treeList){
                if(n.getLevel() == i){
                    System.out.println(n);
                    writeToFile(filePath, n.toString());
                }
            }
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    /**
     * With this method we compute the order in which the nodes should be in the parsing tree
     * And we effectively create it
     * @param node - the node from which we start the construction (the root in our case)
     */
    public void createList(ParsingTreeRow node){
        if(node == null)
            return;

        while(node != null){

            this.treeList.add(node);
            if(node.getLeftChild() != null){
                createList(node.getLeftChild());
            }

            node = node.getRightSibling();
        }

    }



}
