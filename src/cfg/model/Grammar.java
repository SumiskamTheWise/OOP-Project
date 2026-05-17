package cfg.model;

import java.util.*;

/**
 * Represents a Context-Free Grammar (CFG).
 * <p>
 * A CFG consists of:
 * <ul>
 *   <li>A set of non-terminal symbols (uppercase Latin letters)</li>
 *   <li>A set of terminal symbols (lowercase Latin letters and digits)</li>
 *   <li>A list of production rules</li>
 *   <li>A start symbol</li>
 * </ul>
 * Each grammar has a unique identifier assigned when it is loaded.
 */
public class Grammar {

    private final int id;
    private char startSymbol;
    private final List<Rule> rules;

    /**
     * Constructs a Grammar with the given id, start symbol, and rules.
     * @param id the unique identifier for this grammar
     * @param startSymbol the start symbol (must be a non-terminal)
     * @param rules the list of production rules
     */
    public Grammar(int id, char startSymbol, List<Rule> rules) {
        this.id = id;
        this.startSymbol = startSymbol;
        this.rules = new ArrayList<>(rules);
    }

    /** @return the unique identifier of this grammar */
    public int getId() {
        return id;
    }

    /** @return the start symbol of this grammar */
    public char getStartSymbol() {
        return startSymbol;
    }

    /** @return an unmodifiable view of the production rules */
    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /**
     * Adds a production rule to this grammar.
     * @param rule the rule to add
     */
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Removes a production rule by its 1-based index.
     * @param index the 1-based index of the rule to remove
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void removeRule(int index) {
        rules.remove(index - 1);
    }

    /**
     * Returns the set of all non-terminal symbols used in this grammar.
     * @return set of non-terminal characters
     */
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

    /**
     * Returns the set of all terminal symbols used in this grammar.
     * @return set of terminal characters
     */
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