package cfg.operations;

import cfg.model.Grammar;
import cfg.model.Rule;

import java.util.*;

/**
 * Checks whether the language of a Context-Free Grammar is empty.
 * <p>
 * A CFG's language is empty if the start symbol cannot derive any terminal string.
 * This is determined by computing the set of <em>productive</em> non-terminals:
 * those that can eventually derive a string of terminals.
 */
public class EmptyLanguageCheck implements GrammarOperation {

    /**
     * Determines whether the language of the given grammar is empty.
     *
     * @param grammar the grammar to check
     * @return true if L(grammar) is empty, false otherwise
     */
    public boolean isEmpty(Grammar grammar) {
        Set<Character> productive = findProductive(grammar);
        return !productive.contains(grammar.getStartSymbol());
    }

    // Private helpers

    /**
     * Computes the set of productive non-terminals.
     * A non-terminal A is productive if there exists a rule A -> w where
     * every symbol in w is either a terminal or a productive non-terminal.
     */
    private Set<Character> findProductive(Grammar grammar) {
        Set<Character> productive = new HashSet<>();
        boolean changed = true;

        while (changed) {
            changed = false;
            for (Rule r : grammar.getRules()) {
                if (!productive.contains(r.getLeftSide()) && isProductive(r, productive)) {
                    productive.add(r.getLeftSide());
                    changed = true;
                }
            }
        }
        return productive;
    }

    /**
     * Returns true if all symbols on the right-hand side of the rule are
     * terminals or already known to be productive non-terminals.
     */
    private boolean isProductive(Rule r, Set<Character> productive) {
        if (r.isEpsilon()) return true;
        for (char c : r.getRightSide().toCharArray()) {
            if (Character.isUpperCase(c) && !productive.contains(c)) return false;
        }
        return true;
    }
}