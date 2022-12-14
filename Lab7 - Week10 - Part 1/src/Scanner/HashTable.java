package Scanner;

import Utils.Pair;

import java.util.ArrayList;

public class HashTable {
    private Integer size;
    private ArrayList<ArrayList<String>> table;


    public HashTable(Integer size){
        this.size = size;
        this.table = new ArrayList<>();
        for(int i = 0; i < size; i++){
            this.table.add(new ArrayList<>());
        }
    }

    /**
     * With this method we return an element based on its position
     * @param pos - represents the position of the element from the hashtable
     * @return - returns the elements
     */
    public String findByPos(Pair<Integer, Integer> pos){
        if(this.table.size() <= pos.getFirst() || this.table.get(pos.getFirst()).size() <= pos.getSecond()){
            throw new IndexOutOfBoundsException("Invalid position");
        }

        return this.table.get(pos.getFirst()).get(pos.getSecond());
    }

    /**
     * This method returns the size of the haspmap
     * @return the size of the hashmap
     */
    public Integer getSize(){
        return size;
    }

    /**
     * With this method we look for the position of an element and return it
     * @param elem - the element for which we want to find the position
     * @return - the position of the element
     */
    public Pair<Integer, Integer> findPositionOfTerm(String elem){
        int pos = hash(elem);

        if(!table.get(pos).isEmpty()){
            ArrayList<String> elems = this.table.get(pos);
            for(int i = 0; i < elems.size(); i++){
                if(elems.get(i).equals(elem)){
                    return new Pair<>(pos, i);
                }
            }
        }

        return null;
    }

    /**
     * This method represents the hash function (used to compute the hash for an element)
     * @param key - the element for which we compute the hash
     * @return - the hash of the element
     */
    private Integer hash(String key){
        int sum_chars = 0;
        char[] key_characters = key.toCharArray();
        for(char c: key_characters){
            sum_chars += c;
        }
        return sum_chars % size;
    }

    /**
     * This method looks whether the hashmap contains a specific element
     * @param elem - the element we are looking for in the hashmap
     * @return - returns true if the element is in the hashmap and false otherwise
     */
    public boolean containsTerm(String elem){
        return this.findPositionOfTerm(elem) != null;
    }

    /**
     * This method adds a new element in the hashmap
     * @param elem - the element we want to add in the hashmap
     * @return - return true if the element was added successfully in the hashmap and false if the element was already in the hashmap
     */
    public boolean add(String elem){
        if(containsTerm(elem)){
            return false;
        }

        Integer pos = hash(elem);

        ArrayList<String> elems = this.table.get(pos);
        elems.add(elem);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        for(int i = 0; i < this.table.size(); i++){
            if(this.table.get(i).size() > 0){
                computedString.append(i);
                computedString.append(" - ");
                computedString.append(this.table.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }

}
