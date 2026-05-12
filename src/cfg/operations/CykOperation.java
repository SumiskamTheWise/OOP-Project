package cfg.operations;

import cfg.model.Grammar;
import cfg.model.Rule;

import java.util.*;

/**
 * Implements the CYK (Cocke-Younger-Kasami) algorithm to determine whether a
 * word belongs to the language of a Context-Free Grammar.
 * <p>
 * The grammar MUST be in Chomsky Normal Form before running CYK.
 * If it is not, this operation will automatically apply ChomskifyOperation
 * on an internal copy (without storing the converted grammar).
 * </p>
 */
public class CykOperation implements GrammarOperation {

    private final ChomskifyOperation chomskify = new ChomskifyOperation();
    private final IsChomsky isChomsky = new IsChomsky();

    /**
     * Tests whether the given word is in the language of the grammar.
     *
     * @param grammar the grammar (converted to CNF internally if needed)
     * @param word    the input word (use empty string for epsilon)
     * @return true if {@code word} is in L(grammar)
     */
    public boolean accepts(Grammar grammar, String word) {
        // Convert to CNF internally if needed
        Grammar cnf = ensureCnf(grammar);

        // Handle empty word
        if (word.isEmpty()) {
            return canDeriveEpsilon(cnf);
        }

        int n = word.length();
        // table[i][j] = set of non-terminals that derive word[i..j]
        @SuppressWarnings("unchecked") Set<Character>[][] table = new Set[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                table[i][j] = new HashSet<>();

        // Fill diagonal: single characters
        for (int i = 0; i < n; i++) {
            char terminal = word.charAt(i);
            for (Rule r : cnf.getRules()) {
                if (r.isSingleTerminal() && r.getRightSide().charAt(0) == terminal) {
                    table[i][i].add(r.getLeftSide());
                }
            }
        }

        // Fill upper triangle: substrings of increasing length
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                for (int k = i; k < j; k++) {
                    for (Rule r : cnf.getRules()) {
                        if (r.isTwoNonTerminals()) {
                            char B = r.getRightSide().charAt(0);
                            char C = r.getRightSide().charAt(1);
                            if (table[i][k].contains(B) && table[k + 1][j].contains(C)) {
                                table[i][j].add(r.getLeftSide());
                            }
                        }
                    }
                }
            }
        }
        return table[0][n - 1].contains(cnf.getStartSymbol());
    }

    // Private helpers
    private Grammar ensureCnf(Grammar grammar) {
        if (isChomsky.check(grammar)) return grammar;
        // Use a temporary store just for the conversion
        cfg.model.GrammarStore tempStore = new cfg.model.GrammarStore();
        return chomskify.apply(grammar, tempStore);
    }

    private boolean canDeriveEpsilon(Grammar cnf) {
        for (Rule r : cnf.getRules()) {
            if (r.getLeftSide() == cnf.getStartSymbol() && r.isEpsilon()) return true;
        }
        return false;
    }
}
