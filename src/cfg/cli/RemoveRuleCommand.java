package cfg.cli;

import cfg.model.Grammar;

/**
 * Removes a production rule from a grammar by its 1-based index.
 * <p>
 * Usage: {@code removeRule <id> <n>}
 */
public class RemoveRuleCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 2) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;
        int n;
        try {
            n = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + args[1] + "' is not a valid rule number.");
            return;
        }
        if (n < 1 || n > g.getRules().size()) {
            System.out.println("Error: rule number " + n + " is out of range (grammar #" + g.getId() + " has " + g.getRules().size() + " rules).");
            return;
        }
        String removed = g.getRules().get(n - 1).toString();
        g.removeRule(n);
        System.out.println("Successfully removed rule " + n + " (" + removed + ") from grammar #" + g.getId());
    }

    @Override
    public String usage() {
        return "removeRule <id> <n>  removes rule number <n> from grammar #<id>";
    }
}
