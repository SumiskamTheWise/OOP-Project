package cfg.operations;

import cfg.model.Grammar;
import cfg.model.Rule;

/**
 * Checks whether a Context-Free Grammar is in Chomsky Normal Form (CNF).
 * <p>
 * CNF requires every rule to be one of:
 * <ul>
 *   <li>A -> BC  (exactly two non-terminals)</li>
 *   <li>A -> a   (exactly one terminal)</li>
 *   <li>S -> eps (only the start symbol may produce epsilon)</li>
 * </ul>
 */
public class IsChomsky implements GrammarOperation {

    /**
     * Checks whether the given grammar is in CNF.
     *
     * @param grammar the grammar to check
     * @return true if the grammar is in CNF
     */
    public boolean check(Grammar grammar) {
        char start = grammar.getStartSymbol();
        for (Rule r : grammar.getRules()) {
            if (r.isEpsilon()) {
                if (r.getLeftSide() != start) return false;
            } else if (r.isSingleTerminal()) {
                // OK
            } else if (r.isTwoNonTerminals()) {
                // OK
            } else {
                return false;
            }
        }
        return true;
    }
}
