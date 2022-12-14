package Scanner;

import Utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {

    private final ArrayList<String> operators = new ArrayList<>(
            List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "=")
    );

    private final ArrayList<String> separators = new ArrayList<>(
            List.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "'", "\"")
    );

    private final ArrayList<String> reservedWords = new ArrayList<>(
            List.of("mul", "div", "mod", "add", "sub", "read", "write", "if", "else", "for", "while", "int", "string", "char", "return", "start", "array")
    );

    private final String filePath;

    private SymbolTable symbolTable;

    private ProgramInternalForm pif;

    /**
     * This is the constructor and here we initialize the symbolTable, the pif and the filePath
     * @param filePath - represents the filePath from the while from where we are going to read the program
     */

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new ProgramInternalForm();
    }

    /**
     * In this method we read the content of the file and replace the tabs with ""
     * @return - We return the content of the read file
     * @throws FileNotFoundException if the file doesn't exist
     */
    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while(scanner.hasNextLine()){
            fileContent.append(scanner.nextLine()).append("\n");
        }

        return fileContent.toString().replace("\t", "");
    }


    /**
     * Practically, this method is some sort of wrapper for the real tokenize method, which prepares the array for the
     * real process of splitting the tokens.
     * In this method, we call the method for reading the content of the file, we concatenate the separators into a
     * simple string, we use that string to split the program into a list of string where we have stored the tokens +
     * identifiers + constants + the separators from the  created string. In the end, the tokenize method is called,
     * method which will create a List of pair which contains the token/idenfitier/constant + the number of the line
     * on which it was placed.
     * @return - the list of pairs composed of tokens/identifiers/constants + a pair which is composed from the number of the line and the number of column on which them were placed
     */
    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramsElems(){
        try{
            String content = this.readFile();
            String separatorsString = this.separators.stream().reduce("", (a,b)->(a + b));
            StringTokenizer tokenizer = new StringTokenizer(content, separatorsString, true);

            List<String> tokens = Collections.list(tokenizer)
                    .stream()
                    .map(t->(String) t)
                    .collect(Collectors.toList());

            return tokenize(tokens);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Within this method, we go through each string from tokensToBe and look in what case are we:
     * We can have 4 cases:
     * 1) the case when we are managing a string
     * -- a) where we are either at the start of the string and we start to create it
     * -- b) we found the end of the string so we add it to our final list + the line and the column on which it is situated
     * 2) the case when we are managing a char
     * -- a) where we are either at the start of the char and we start to create it
     * -- b) we found the end of the char so we add it to our final list + the line and the column on which it is situated
     * 3) the case when we have a new line
     * -- we simply increase the line number in this case
     * -- we make the number column 1 again because we start a new line
     * 4) the case when:
     * -- a) if we have a string, we keep compute the string
     * -- b) if we have a char, we compute the char
     * -- c) if the token is different from " " (space) it means we found a token and we add it to our final list + the line and the column on which it is situated and we increase the column number
     *
     * Basically, in this method we go through the elements of the program and for each of them, if they compose a token/identifier/constant we add it
     * to the final list and we compute also the line number on which each of the are situated. (we somehow tokenize the elems which compose the program)
     * @param tokensToBe - the List of program elements (strings) + the separators
     * @return - the list of pairs composed of tokens/identifiers/constants + a pair which is composed from the number of the line and the number of column on which them were placed
     */
    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokensToBe){

        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
        StringBuilder createdString = new StringBuilder();
        int numberLine = 1;
        int numberColumn = 1;

        for(String t: tokensToBe){
            switch (t) {
                case "\"":
                    if (isStringConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    }else {
                        createdString.append(t);
                    }
                    isStringConstant = !isStringConstant;
                    break;
                case "'":
                    if (isCharConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    }
                    else {
                        createdString.append(t);
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    numberLine++;
                    numberColumn = 1;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (isCharConstant) {
                        createdString.append(t);
                    } else if (!t.equals(" ")) {
                        resultedTokens.add(new Pair<>(t, new Pair<>(numberLine, numberColumn)));
                        numberColumn++;
                    }
                    break;
            }
        }
        return resultedTokens;
    }

    /**
     *  In this method, we scan the list of created tokens and we classify each of them in a category:
     *  a) 2 - for reservedWords
     *  b) 3 - for operators
     *  c) 4 - for separators
     *  d) 0 - for constants
     *  e) 1 - for identifiers
     *  If the token is a constant or an identifier we add it to the Symbol Table
     *  After figuring out the category, we add them to the Scanner.ProgramInternalForm + their position in the symbol table ( (-1, -1) for anything that is not a constant and an identifier ) + their category (0, 1, 2, 3, 4)
     *  If the token is not in any of the categories, we print a message with the line and the column of the error + the token which is invalid.
     */

    public void scan(){

        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramsElems();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if(tokens == null){
            return;
        }

        tokens.forEach(t -> {
            String token = t.getFirst();
            if(this.reservedWords.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
            } else if(this.operators.contains(token)){
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
            } else if(this.separators.contains(token)){
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
            } else if(Pattern.compile("^0|[-|+][1-9]([0-9])*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\"$").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>("CONST", symbolTable.findPositionOfTerm(token)), 0);
//                this.pif.add(new Utils.Pair<>("const", symbolTable.findPositionOfTerm(token)), 0);
            }
            else if(Pattern.compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>("IDENTIFIER", symbolTable.findPositionOfTerm(token)), 1);
//                this.pif.add(new Utils.Pair<>("ident", symbolTable.findPositionOfTerm(token)), 1);
            } else {
                Pair<Integer, Integer> pairLineColumn = t.getSecond();
                System.out.println("Error at line: " + pairLineColumn.getFirst() + " and column: " + pairLineColumn.getSecond() + ", invalid token: " + t.getFirst());
                lexicalErrorExists.set(true);
            }
        });

        if(!lexicalErrorExists.get()){
            System.out.println("Program is lexically correct!");
        }

    }

    /**
     *
     * @return the Scanner.ProgramInternalForm
     */
    public ProgramInternalForm getPif(){
        return this.pif;
    }

    /**
     *
     * @return the Scanner.SymbolTable
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

}
