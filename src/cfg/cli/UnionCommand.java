package cfg.cli;

import cfg.model.Grammar;

/**
 * Computes the union of two grammars and stores the result as a new grammar.
 * <p>
 * Usage: union of ID1 and ID2
 */
public class UnionCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 2) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g1 = PrintCommand.resolveGrammar(args[0], context);
        Grammar g2 = PrintCommand.resolveGrammar(args[1], context);
        if (g1 == null || g2 == null) return;

        Grammar result = context.getUnion().apply(g1, g2, context.getStore());
        System.out.println("Union of grammar #" + g1.getId() + " and #" + g2.getId() + " stored as grammar #" + result.getId());
    }

    @Override
    public String usage() {
        return "union <id1> <id2> union of grammars #<id1> and #<id2>";
    }
}
