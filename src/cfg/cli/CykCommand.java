package cfg.cli;

import cfg.model.Grammar;

/**
 * Tests whether a word belongs to the language of a grammar using the CYK algorithm.
 * <p>
 * Usage: {@code cyk <id> <word>}
 * <p>
 * Use {@code eps} as the word to test the empty string.
 */
public class CykCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 2) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        String word = args[1].equals("eps") ? "" : args[1];
        boolean accepted = context.getCyk().accepts(g, word);

        String wordDisplay = word.isEmpty() ? "eps (empty string)" : "\"" + word + "\"";
        System.out.println(wordDisplay + " is " + (accepted ? "" : "NOT ") + "in the language of grammar #" + g.getId() + ".");
    }

    @Override
    public String usage() {
        return "cyk <id> <word> tests if <word> is in L(grammar #<id>) via CYK";
    }
}
