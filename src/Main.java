import CFG.*;
import CLI.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentFilePath = null;
    private static boolean isFileOpen = false;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1 - CLI (File Operations)");
            System.out.println("2 - CFG (Grammar Operations)");
            System.out.println("3 - Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": handleCliMenu(); break;
                case "2": handleCfgMenu(); break;
                case "3":
                    System.out.println("Exiting program...");
                    System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }

    // CLI
    private static void handleCliMenu() {
        System.out.println("\nEntering CLI mode. Type 'help' for commands or 'back' to return.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1] : "";

            if (command.equals("back")) return;

            switch (command) {
                case "open": openFile(argument); break;
                case "close": closeFile(); break;
                case "save": saveFile(); break;
                case "saveas": saveFileAs(argument); break;
                case "help": showHelp(); break;
                case "exit":
                    System.out.println("Exiting program...");
                    System.exit(0);
                default: System.out.println("Unknown command. Type 'help' for info.");
            }
        }
    }

    protected static void openFile(String path) {
        if (path.isEmpty()) {
            System.out.println("Usage: open <path>");
            return;
        }
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                System.out.println("File did not exist. Created new empty file: " + path);
            } else {
                System.out.println("Successfully opened " + path);
            }
            currentFilePath = path;
            isFileOpen = true;
        } catch (IOException e) {
            System.out.println("Error: Could not open file.");
        }
    }

    protected static void closeFile() {
        if (!isFileOpen) {
            System.out.println("No file is currently open.");
            return;
        }
        System.out.println("Successfully closed " + currentFilePath);
        currentFilePath = null;
        isFileOpen = false;
    }

    protected static void saveFile() {
        if (!isFileOpen) {
            System.out.println("Error: No open file to save.");
            return;
        }
        System.out.println("Successfully saved " + currentFilePath);
    }

    protected static void saveFileAs(String newPath) {
        if (!isFileOpen) {
            System.out.println("Error: No open file to save.");
            return;
        }
        if (newPath.isEmpty()) {
            System.out.println("Usage: saveas <new path>");
            return;
        }
        currentFilePath = newPath;
        System.out.println("Successfully saved as " + newPath);
    }

    protected static void showHelp() {
        System.out.println("Supported commands:");
        System.out.println("open <file>  - Opens a file");
        System.out.println("close        - Closes the current file");
        System.out.println("save         - Saves changes to the current file");
        System.out.println("saveas <file>- Saves current data to a new file");
        System.out.println("help         - Shows this help");
        System.out.println("back         - Return to Main Menu");
        System.out.println("exit         - Exit the program");
    }

    // CFG
    private static void handleCfgMenu() {
        while (true) {
            System.out.println("\n--- CFG OPERATIONS ---");
            System.out.println("1 - List all      \t6 - Concatenation \t11 - Chomskify ");
            System.out.println("2 - Print Rules   \t7 - Chomsky Check \t12 - Back ");
            System.out.println("3 - Save          \t8 - CYK           \t13 - Exit ");
            System.out.println("4 - Remove Rule   \t9 - Iter ");
            System.out.println("5 - Union         \t10 - Empty ");
            System.out.print("Select CFG option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": new ListGrammarsCommand().execute(); break;
                case "2": new PrintRulesCommand().execute(); break;
                case "3": new SaveGrammarCommand().execute(); break;
                case "4": new RemoveRuleCommand().execute(); break;
                case "5": new UnionCommand().execute(); break;
                case "6": new ConcatCommand().execute(); break;
                case "7": new ChomskyCheckCommand().execute(); break;
                case "8": new CykCommand().execute(); break;
                case "9": new IterCommand().execute(); break;
                case "10": new EmptyCheckCommand().execute(); break;
                case "11": new ChomskifyCommand().execute(); break;
                case "12": return;
                case "13": System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }
}