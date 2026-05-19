package cfg.operations;

import cfg.model.Grammar;
import cfg.model.Rule;

import java.util.*;

/**
 * Implements the CYK (Cocke-Younger-Kasami) algorithm to determine whether a
 * word belongs to the language of a Context-Free Grammar.
 * <p>
 * The grammar <b>must be in Chomsky Normal Form</b> before running CYK.
 * If it is not, this operation will automatically apply {@link ChomskifyOperation}
 * on an internal copy (without storing the converted grammar).
 */
public class CykOperation implements GrammarOperation {

    /** Creates a new CykOperation. */
    public CykOperation() {}

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
        // Handle empty word BEFORE converting to CNF, because the conversion
        // introduces a new start symbol (S₀) and may rewrite the epsilon rule.
        // Epsilon membership depends solely on whether the original start symbol
        // can derive ε — which ChomskifyOperation preserves via S₀ → eps, but
        // checking on the original is simpler and always correct.
        if (word.isEmpty()) {
            return canDeriveEpsilonOriginal(grammar);
        }

        // Convert to CNF internally if needed (for non-empty words)
        Grammar cnf = ensureCnf(grammar);

        int n = word.length();
        // table[i][j] = set of non-terminals that derive word[i..j]
        @SuppressWarnings("unchecked")
        Set<Character>[][] table = new Set[n][n];
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

    private boolean canDeriveEpsilonOriginal(Grammar grammar) {
        // Check transitively: collect all nullable non-terminals, then see if
        // the start symbol is among them.
        Set<Character> nullable = new HashSet<>();
        // Seed: any non-terminal with a direct eps rule
        for (Rule r : grammar.getRules()) {
            if (r.isEpsilon()) nullable.add(r.getLeftSide());
        }
        // Propagate: A -> B₁B₂...Bₙ where every Bᵢ is nullable ⟹ A is nullable
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule r : grammar.getRules()) {
                if (nullable.contains(r.getLeftSide())) continue;
                if (!r.isEpsilon() && !r.isSingleTerminal()) {
                    boolean allNullable = true;
                    for (char c : r.getRightSide().toCharArray()) {
                        if (!Character.isUpperCase(c) || !nullable.contains(c)) {
                            allNullable = false;
                            break;
                        }
                    }
                    if (allNullable) {
                        nullable.add(r.getLeftSide());
                        changed = true;
                    }
                }
            }
        }
        return nullable.contains(grammar.getStartSymbol());
    }
}