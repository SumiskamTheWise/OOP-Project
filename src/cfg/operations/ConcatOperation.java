package cfg.operations;

import cfg.model.Grammar;
import cfg.model.GrammarStore;
import cfg.model.Rule;
import cfg.util.SymbolAllocator;

import java.util.*;

/**
 * Computes the concatenation of two CFGs.
 * <p>
 * Given G1 (start S1) and G2 (start S2), produces a new grammar G with:
 * <ul>
 *   <li>A fresh start symbol S</li>
 *   <li>Rule: S -> S1 S2 (the start derives the concatenation of both languages)</li>
 *   <li>All rules from G1 and G2 (with renamed symbols if needed)</li>
 * </ul>
 */
public class ConcatOperation implements GrammarOperation {

    /**
     * Computes the concatenation of g1 and g2, registers it in the store.
     *
     * @param g1    first grammar
     * @param g2    second grammar
     * @param store grammar store
     * @return the concatenation grammar
     */
    public Grammar apply(Grammar g1, Grammar g2, GrammarStore store) {
        SymbolAllocator allocator = new SymbolAllocator();
        allocator.reserve(g1.getNonTerminals());
        allocator.reserve(g2.getNonTerminals());

        Map<Character, Character> remap = buildRemap(g2, g1, allocator);

        char newStart = allocator.next();
        List<Rule> rules = new ArrayList<>();

        char g2Start = remap.getOrDefault(g2.getStartSymbol(), g2.getStartSymbol());
        rules.add(new Rule(newStart, String.valueOf(g1.getStartSymbol()) + g2Start));

        rules.addAll(g1.getRules());

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
