import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Main {
    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        MyScanner scanner = new MyScanner(filePath);
        scanner.scan();
        printToFile(filePath.replace(".txt", "ST.txt"), scanner.getSymbolTable());
        printToFile(filePath.replace(".txt", "PIF.txt"), scanner.getPif());
    }

    private static void printMenu() {
        System.out.println("1. Print states.");
        System.out.println("2. Print alphabet.");
        System.out.println("3. Print final states.");
        System.out.println("4. Print transitions.");
        System.out.println("5. Print initial state.");
        System.out.println("6. Print is deterministic.");
        System.out.println("7. Check if sequence is accepted by DFA.");
    }

    private static void optionsForDFA() {

        FiniteAutomaton finiteAutomaton = new FiniteAutomaton("Input_Output/FA.txt");

        System.out.println("FA read from file.");
        printMenu();
        System.out.println("Your option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while (option != 0) {

            switch (option) {
                case 1:
                    System.out.println("Final states: ");
                    System.out.println(finiteAutomaton.getStates());
                    System.out.println();
                    break;

                case 2:
                    System.out.println("Alphabet: ");
                    System.out.println(finiteAutomaton.getAlphabet());
                    System.out.println();
                    break;

                case 3:
                    System.out.println("Final states: ");
                    System.out.println(finiteAutomaton.getFinalStates());
                    System.out.println();
                    break;

                case 4:
                    System.out.println(finiteAutomaton.writeTransitions());
                    break;

                case 5:
                    System.out.println("Initial state: ");
                    System.out.println(finiteAutomaton.getInitialState());
                    System.out.println();
                    break;

                case 6:
                    System.out.println("Is deterministic?");
                    System.out.println(finiteAutomaton.checkIfDeterministic());
                    break;

                case 7: {
                    System.out.println("Your sequence: ");
                    Scanner scanner2 = new Scanner(System.in);
                    String sequence = scanner2.nextLine();

                    if (finiteAutomaton.checkSequence(sequence))
                        System.out.println("Sequence is valid");
                    else
                        System.out.println("Invalid sequence");
                }
                break;

                default:
                    System.out.println("Invalid command!");
                    break;

            }
            System.out.println();
            printMenu();
            System.out.println("Your option: ");
            option = scanner.nextInt();
        }
    }

    public static void runScanner(){
        run("Input_Output/p1.txt");
        run("Input_Output/p2.txt");
        run("Input_Output/p3.txt");
        run("Input_Output/p1err.txt");
    }

    public static void main(String[] args) {
        System.out.println("1. FA");
        System.out.println("2. Scanner");
        System.out.println("Your option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                optionsForDFA();
                break;
            case 2:
                runScanner();
                break;

            default:
                System.out.println("Invalid command!");
                break;

        }

    }
}
