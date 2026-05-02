package cfg.operations;

import cfg.model.Grammar;
import cfg.model.GrammarStore;
import cfg.model.Rule;
import cfg.util.SymbolAllocator;

import java.util.*;

public class ChomskifyOperation implements GrammarOperation {
    public Grammar apply(Grammar grammar, GrammarStore store) {
        SymbolAllocator allocator = new SymbolAllocator();
        allocator.reserve(grammar.getNonTerminals());

        List<Rule> rules = new ArrayList<>(grammar.getRules());
        char start = grammar.getStartSymbol();

        //Step 1: new start symbol
        char newStart = allocator.next();
        rules.add(0, new Rule(newStart, String.valueOf(start)));

        //Step 2: eliminate epsipon productions
        rules = eliminateEpsilob(rules, newStart);

        //Step 3: eliminate unit productions
        rules = eliminateUnit(rules);

        //Step 4/5: replace terminals
        rules = binarize(rules, allocator);

        //Duplicate
        List<Rule> deduped = new ArrayList<>(new LinkedHashSet<>(rules));

        return store.registerNew(newStart, deduped);
    }



    // Step 2
    private List<Rule> eliminateEpsilob(List<Rule> rules, char protectedStart) {
        Set<Character> nullable = findNullable(rules);
        List<Rule> result = new ArrayList<>();

        for (Rule r : rules) {
            if (r.isEpsilon()) {
                // Keep only the start epsilon rule
                if (r.getLeftSide() == protectedStart) result.add(r);
                continue;
            }
            // Generate all combinations with nullable symbols omitted
            List<String> expansions = expandNullable(r.getRightSide(), nullable);
            for (String exp : expansions) {
                if (!exp.isEmpty()) {
                    result.add(new Rule(r.getLeftSide(), exp));
                }
            }
        }
        return result;
    }

    private List<String> expandNullable(String rightSide, Set<Character> nullable) {
        List<String> result = new ArrayList<>();
        result.add("");
        for (char c :  rightSide.toCharArray()) {
            List<String> next = new ArrayList<>();
            for (String prefix : result) {
                next.add(prefix + c); // keep this symbol
                if(Character.isUpperCase(c) && nullable.contains(c)) {
                    next.add(prefix); // omit this symbol because it's nullable
                }
            }
            result = next;
        }
        return result;
    }

    private Set<Character> findNullable(List<Rule> rules) {
        Set<Character> nullable = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule r : rules) {
                if (!nullable.contains(r.getLeftSide())) {
                    if(r.isEpsilon() || allNullable(r.getRightSide(), nullable)) {
                        nullable.add(r.getLeftSide());
                        changed = true;
                    }
                }
            }
        }
        return nullable;
    }

    private boolean allNullable(String rightSide, Set<Character> nullable) {
        if (Rule.EPSILON.equals(rightSide)) return true;
        for (char c : rightSide.toCharArray()) {
            if (!Character.isUpperCase(c) || !nullable.contains(c)) return false;
        }
        return true;
    }

    // Step 3
    private List<Rule> eliminateUnit(List<Rule> rules) {
        // For each non-terminal, compute the set of non-terminals reachable via unit rules
        Set<Character> nonTerminals = new HashSet<>();
        for (Rule r : rules) {
            nonTerminals.add(r.getLeftSide());
            for (char c : r.getRightSide().toCharArray()) {
                if (Character.isUpperCase(c)) nonTerminals.add(c);
            }
        }

        List<Rule> result = new ArrayList<>();
        for (char A : nonTerminals) {
            Set<Character> reachable = unitReachable(A, rules);
            for (char B : reachable) {
                for (Rule r : rules) {
                    if (r.getLeftSide() == B && !isUnitRule(r)) {
                        result.add(new Rule(A, r.getRightSide()));
                    }
                }
            }
        }
        return result;
    }

    private Set<Character> unitReachable(char start, List<Rule> rules) {
        Set<Character> reachable = new HashSet<>();
        reachable.add(start);
        Queue<Character> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            char curr = queue.poll();
            for (Rule r : rules) {
                if (r.getLeftSide() == curr && isUnitRule(r)) {
                    char target = r.getRightSide().charAt(0);
                    if (reachable.add(target)) queue.add(target);
                }
            }
        }
        return reachable;
    }

    private boolean isUnitRule(Rule r) {
        return r.getRightSide().length() == 1 && Character.isUpperCase(r.getRightSide().charAt(0));
    }

    // Step 4/5
    private List<Rule> binarize(List<Rule> rules, SymbolAllocator allocator) {
        // Map terminal -> helper non-terminal (e.g. 'a' -> Ta)
        Map<Character, Character> termMap = new HashMap<>();
        List<Rule> result = new ArrayList<>();

        for (Rule r : rules) {
            String rightSide = r.getRightSide();

            // Leave single-terminal and epsilon rules as-is
            if (r.isEpsilon() || r.isSingleTerminal()) {
                result.add(r);
                continue;
            }

            // Replace terminals in RHS with helper symbols (only in rules of length >= 2)
            String replaced = replacedRhs(rightSide, termMap, allocator, result);
            // Binarize: split replaced RHS into binary rules
            binarizeRule(r.getLeftSide(), replaced, allocator, result);
        }

        // Add terminal helper rules
        for (Map.Entry<Character, Character> e : termMap.entrySet()) {
            result.add(new Rule(e.getValue(), String.valueOf(e.getKey())));
        }

        return result;
    }

    private String replacedRhs(String rightSide, Map<Character, Character> termMap,
                               SymbolAllocator allocator, List<Rule> result) {
        if (rightSide.length() < 2) return rightSide;
        StringBuilder sb = new StringBuilder();
        for (char c : rightSide.toCharArray()) {
            if (Character.isLowerCase(c) || Character.isDigit(c)) {
                if (!termMap.containsKey(c)) {
                    termMap.put(c, allocator.next());
                }
                sb.append(termMap.get(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void binarizeRule(char lhs, String rightSide, SymbolAllocator allocator, List<Rule> result) {
        if (rightSide.length() <= 2) {
            result.add(new Rule(lhs, rightSide));
            return;
        }
        // Split: lhs -> rightSide[0] + NEW, NEW -> rightSide[1..]
        char newSym = allocator.next();
        result.add(new Rule(lhs, String.valueOf(rightSide.charAt(0)) + newSym));
        binarizeRule(newSym, rightSide.substring(1), allocator, result);
    }
}
