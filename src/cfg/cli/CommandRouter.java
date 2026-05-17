package cfg.cli;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses raw user input and dispatches to the appropriate {@link Command}.
 * <p>
 * Follows the Single Responsibility Principle: this class is only responsible
 * for tokenising input and routing to registered commands.
 * <p>
 * Commands that require an open file session are guarded here so that
 * individual command classes stay clean.
 */
public class CommandRouter {

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final AppContext context;

    /**
     * Constructs the router and registers all supported commands.
     *
     * @param context the shared application context
     */
    public CommandRouter(AppContext context) {
        this.context = context;
        registerAll();
    }

    /**
     * Parses and executes one line of user input.
     *
     * @param line the raw input line
     */
    public void dispatch(String line) {
        if (line == null || line.isBlank()) return;

        // Tokenise, honouring quoted strings (for paths with spaces)
        String[] tokens = tokenise(line.trim());
        if (tokens.length == 0) return;

        String name = tokens[0].toLowerCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        // "save as" is a two-word command
        if (name.equals("save") && args.length > 0 && args[0].equalsIgnoreCase("as")) {
            args = Arrays.copyOfRange(args, 1, args.length);
            runCommand("save as", args);
            return;
        }

        runCommand(name, args);
    }

    /** Returns the command map (used by HelpCommand). */
    public Map<String, Command> getCommands() {
        return commands;
    }


    // Private helpers
    private void runCommand(String name, String[] args) {
        Command cmd = commands.get(name);
        if (cmd == null) {
            System.out.println("Unknown command: '" + name + "'. Type 'help' for a list of commands.");
            return;
        }

        // Commands that need an open file session declare it via requiresOpenSession()
        if (cmd.requiresOpenSession() && !context.getSession().isOpen()) {
            System.out.println("Error: no file is open. Use 'open <file>' first.");
            return;
        }

        cmd.execute(args, context);
    }

    private void registerAll() {
        HelpCommand help = new HelpCommand(commands);

        register("open",       new OpenCommand());
        register("close",      new CloseCommand());
        register("save as",    new SaveAsCommand());
        register("exit",       new ExitCommand());
        register("help",       help);
        register("list",       new ListCommand());
        register("print",      new PrintCommand());
        register("addRule",    new AddRuleCommand());
        register("removerule", new RemoveRuleCommand());
        register("union",      new UnionCommand());
        register("concat",     new ConcatCommand());
        register("iter",       new IterCommand());
        register("chomsky",    new ChomskifyCheckCommand());
        register("chomskify",  new ChomskifyCommand());
        register("cyk",        new CykCommand());
        register("empty",      new EmptyCommand());

        // "save" dispatches to SaveCommand (no args) or SaveGrammarCommand (id + file)
        register("save", new SmartSaveCommand(new SaveCommand(), new SaveGrammarCommand()));
    }

    private void register(String name, Command cmd) {
        commands.put(name.toLowerCase(), cmd);
    }

    /**
     * Tokenises a line respecting double-quoted strings.
     * e.g. {@code save as "C:\My Dir\file.cfg"} -> ["save", "as", "C:\My Dir\file.cfg"]
     */
    private String[] tokenise(String line) {
        java.util.List<String> tokens = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }
}