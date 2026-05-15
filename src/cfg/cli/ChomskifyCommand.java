package cfg.cli;

import cfg.model.Grammar;

/**
 * Converts a grammar to Chomsky Normal Form and stores the result as a new grammar.
 * <p>
 * Usage: {@code chomskify <id>}
 */
public class ChomskifyCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        Grammar result = context.getChomskify().apply(g, context.getStore());
        System.out.println("Chomsky Normal Form of grammar #" + g.getId() + " stored as grammar #" + result.getId());
    }

    @Override
    public String usage() {
        return "chomskify <id> converts grammar #<id> to CNF; stores new grammar";
    }
}
