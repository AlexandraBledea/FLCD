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
            List.of("read", "write", "if", "else", "for", "while", "int", "string", "char", "return", "start", "array")
    );

    private final String filePath;

    private SymbolTable symbolTable;

    private ProgramInternalForm pif;


    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new ProgramInternalForm();
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while(scanner.hasNextLine()){
            fileContent.append(scanner.nextLine()).append("\n");
        }

        return fileContent.toString().replace("\t", "");
    }

    private List<Pair<String, Integer>> createTokens(){
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

    private List<Pair<String, Integer>> tokenize(List<String> tokensToBe){

        List<Pair<String, Integer>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
        StringBuilder createdString = new StringBuilder();
        int numberLine = 1;

        for(String t: tokensToBe){
            switch (t) {
                case "\"":
                    if (isStringConstant) {
                        resultedTokens.add(new Pair<>(createdString.toString(), numberLine));
                        createdString = new StringBuilder();
                    }
                    isStringConstant = !isStringConstant;
                    break;
                case "'":
                    if (isCharConstant) {
                        resultedTokens.add(new Pair<>(createdString.toString(), numberLine));
                        createdString = new StringBuilder();
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    numberLine++;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (isCharConstant) {
                        createdString.append(t);
                    } else if (!t.equals(" ")) {
                        resultedTokens.add(new Pair<>(t, numberLine));
                    }
                    break;
            }
        }
        return resultedTokens;
    }

    public void scan(){

        List<Pair<String, Integer>> tokens = createTokens();
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
            } else if(Pattern.compile("^0|[-|+][1-9]([0-9])*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z]*\"$").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findPositionOfTerm(token)), 0);
            }
            else if(Pattern.compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findPositionOfTerm(token)), 1);
            } else {
                System.out.println("Error at line: " + t.getSecond() +", invalid token: " + t.getFirst());
                lexicalErrorExists.set(true);
            }
        });

        if(!lexicalErrorExists.get()){
            System.out.println("Program is lexically correct!");
        }

    }

    public ProgramInternalForm getPif(){
        return this.pif;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

}
