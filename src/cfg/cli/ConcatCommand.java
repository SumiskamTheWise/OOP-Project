package cfg.cli;

import cfg.model.Grammar;

/**
 * Computes the concatenation of two grammars and stores the result.
 * <p>
 * Usage: {@code concat <id1> <id2>}
 */
public class ConcatCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 2) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g1 = PrintCommand.resolveGrammar(args[0], context);
        Grammar g2 = PrintCommand.resolveGrammar(args[1], context);
        if (g1 == null || g2 == null) return;

        Grammar result = context.getConcat().apply(g1, g2, context.getStore());
        System.out.println("Concatenation of grammar #" + g1.getId() + " and #" + g2.getId() + " stored as grammar #" + result.getId());
    }

    @Override
    public String usage() {
        return "concat <id1> <id2>          concatenation of grammars #<id1> and #<id2>";
    }
}
