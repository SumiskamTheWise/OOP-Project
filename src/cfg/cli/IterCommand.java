package cfg.cli;

import cfg.model.Grammar;

/**
 * Computes the Kleene star (iteration) of a grammar and stores the result.
 * <p>
 * Usage: {@code iter <id>}
 * </p>
 */
public class IterCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        Grammar result = context.getIteration().apply(g, context.getStore());
        System.out.println("Iteration (Kleene star) of grammar #" + g.getId() + " stored as grammar #" + result.getId());
    }

    @Override
    public String usage() {
        return "iter <id> Kleene star of grammar #<id>; stores new grammar";
    }
}
