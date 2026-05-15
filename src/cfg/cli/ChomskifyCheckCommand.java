package cfg.cli;

import cfg.model.Grammar;

/**
 * Checks whether a grammar is already in Chomsky Normal Form.
 * <p>
 * Usage: {@code chomsky <id>}
 */
public class ChomskifyCheckCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        boolean result = context.getIsChomsky().check(g);
        System.out.println("Grammar #" + g.getId() + " is " + (result ? "" : "NOT ") + "in Chomsky Normal Form.");
    }

    @Override
    public String usage() {
        return "chomsky <id> checks if grammar #<id> is in Chomsky Normal Form";
    }
}
