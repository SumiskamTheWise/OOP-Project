package cfg.operations;

import cfg.model.Grammar;
import cfg.model.GrammarStore;
import cfg.model.Rule;
import cfg.util.SymbolAllocator;

import java.util.*;

/**
 * Computes the Kleene star (iteration) of a Context-Free Grammar.
 * <p>
 * Given G with start symbol S, produces G* with:
 * <ul>
 *   <li>A fresh start symbol S'</li>
 *   <li>Rules: S' -> eps | S S'</li>
 *   <li>All original rules</li>
 * </ul>
 * This represents the language {@code L(G)*}.
 */
public class IterationOperation implements GrammarOperation {

    /**
     * Computes the Kleene star of the given grammar and registers it in the store.
     *
     * @param grammar the grammar to iterate
     * @param store   the grammar store
     * @return the new iteration grammar
     */
    public Grammar apply(Grammar grammar, GrammarStore store) {
        SymbolAllocator allocator = new SymbolAllocator();
        allocator.reserve(grammar.getNonTerminals());

        char newStart = allocator.next();
        List<Rule> rules = new ArrayList<>();

        // S' -> eps (zero repetitions)
        rules.add(new Rule(newStart, Rule.EPSILON));
        // S' -> S S' (one or more repetitions)
        rules.add(new Rule(newStart, String.valueOf(grammar.getStartSymbol()) + newStart));

        rules.addAll(grammar.getRules());

        return store.registerNew(newStart, rules);
    }
}
