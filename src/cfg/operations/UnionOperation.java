package cfg.operations;

import cfg.model.Grammar;
import cfg.model.GrammarStore;
import cfg.model.Rule;
import cfg.util.SymbolAllocator;

import java.util.*;

/**
 * Computes the union of two Context-Free Grammars.
 * <p>
 * Given G1 (start S1) and G2 (start S2), produces a new grammar G with:
 * <ul>
 *   <li>A fresh start symbol S</li>
 *   <li>Rules: S -> S1 | S2, plus all rules from G1 and G2 (with renamed symbols
 *       if needed to avoid conflicts)</li>
 * </ul>
 */
public class UnionOperation implements GrammarOperation {

    /**
     * Computes the union grammar of g1 and g2, registers it in the store, and returns it.
     *
     * @param g1    the first grammar
     * @param g2    the second grammar
     * @param store the grammar store to register the result in
     * @return the new union grammar
     */
    public Grammar apply(Grammar g1, Grammar g2, GrammarStore store) {
        SymbolAllocator allocator = new SymbolAllocator();
        allocator.reserve(g1.getNonTerminals());
        allocator.reserve(g2.getNonTerminals());

        // Rename G2 to avoid conflicts with G1
        Map<Character, Character> remap = buildRemap(g2, g1, allocator);

        char newStart = allocator.next();
        List<Rule> rules = new ArrayList<>();

        // New start rules
        char g2Start = remap.getOrDefault(g2.getStartSymbol(), g2.getStartSymbol());
        rules.add(new Rule(newStart, String.valueOf(g1.getStartSymbol())));
        rules.add(new Rule(newStart, String.valueOf(g2Start)));

        // All rules from G1 unchanged
        rules.addAll(g1.getRules());

        // All rules from G2 with renamed symbols
        for (Rule r : g2.getRules()) {
            char newLhs = remap.getOrDefault(r.getLeftSide(), r.getLeftSide());
            String newRhs = remapRhs(r.getRightSide(), remap);
            rules.add(new Rule(newLhs, newRhs));
        }

        return store.register(newStart, rules);
    }

    private Map<Character, Character> buildRemap(Grammar g2, Grammar g1, SymbolAllocator allocator) {
        Map<Character, Character> remap = new HashMap<>();
        Set<Character> g1Symbols = g1.getNonTerminals();
        for (char c : g2.getNonTerminals()) {
            if (g1Symbols.contains(c)) {
                remap.put(c, allocator.next());
            }
        }
        return remap;
    }

    private String remapRhs(String rhs, Map<Character, Character> remap) {
        if (Rule.EPSILON.equals(rhs)) return rhs;
        StringBuilder sb = new StringBuilder();
        for (char c : rhs.toCharArray()) {
            sb.append(Character.isUpperCase(c) ? remap.getOrDefault(c, c) : c);
        }
        return sb.toString();
    }
}
