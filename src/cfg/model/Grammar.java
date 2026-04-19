package cfg.model;

import java.util.*;

public class Grammar {
    private final int id;
    private char startSymbol;
    private final List<Rule> rules;

    public Grammar(int id, char startSymbol, List<Rule> rules) {
        this.id = id;
        this.startSymbol = startSymbol;
        this.rules = new ArrayList<>(rules);
    }

    public int getId() {
        return id;
    }

    public char getStartSymbol() {
        return startSymbol;
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }
    public void removeRule(int index) {
        rules.remove(index - 1);
    }
    public Set<Character> getNonTerminals() {
        Set<Character> nonTerminals = new LinkedHashSet<>();
        nonTerminals.add(startSymbol);
        for (Rule rule : rules) {
            nonTerminals.add(rule.getLeftSide());
            for (char c : rule.getRightSide().toCharArray()) {
                if (Character.isUpperCase(c)) nonTerminals.add(c);
            }
        }
        return nonTerminals;
    }
    public Set<Character> getTerminals() {
        Set<Character> terminals = new LinkedHashSet<>();
        for (Rule rule : rules) {
            for (char c : rule.getRightSide().toCharArray()) {
                if (Character.isLowerCase(c) || Character.isDigit(c)) {
                    terminals.add(c);
                }
            }
        }
        return terminals;
    }
    /**
     * Creates a deep copy of this grammar with a new id.
     * @param newId the id for the copied grammar
     * @return a new Grammar with the same rules and start symbol
     */
    public Grammar copyWithId(int newId) {
        List<Rule> copiedRules = new ArrayList<>();
        for (Rule r : rules) {
            copiedRules.add(new Rule(r.getLeftSide(), r.getRightSide()));
        }
        return new Grammar(newId, startSymbol, copiedRules);
    }

    @Override
    public String toString() {
        return "Grammar#" + id + " (start=" + startSymbol + ", rules=" + rules.size() + ")";
    }
}
