package cfg.model;

import java.util.*;

/**
 * In-memory store for all loaded grammars.
 * <p><p/>
 * Manages unique grammar identifiers and provides access to stored grammars.
 * This is a singleton-style registry used by the application session.
 */
public class GrammarStore {

    private final Map<Integer, Grammar> grammars = new LinkedHashMap<>();
    private int nextId = 1;

    /**
     * Registers a new grammar in the store, assigning it the next available id.
     *
     * @param startSymbol the start symbol for the new grammar
     * @param rules       the production rules
     * @return the newly created and registered Grammar
     */
    public Grammar register(char startSymbol, List<Rule> rules) {
        Grammar g = new Grammar(nextId++, startSymbol, rules);
        grammars.put(g.getId(), g);
        return g;
    }

    /**
     * Copies a grammar into the store, assigning it a fresh id.
     * The original grammar's id is not preserved.
     *
     * @param g the grammar whose start symbol and rules will be copied
     * @return the newly registered copy with a new id
     */
    public Grammar add(Grammar g) {
        return register(g.getStartSymbol(), new ArrayList<>(g.getRules()));
    }

    /**
     * Retrieves a grammar by its unique identifier.
     * @param id the grammar id
     * @return the Grammar, or {@code null} if not found
     */
    public Grammar get(int id) {
        return grammars.get(id);
    }

    /**
     * Returns all stored grammars in insertion order.
     * @return unmodifiable collection of all grammars
     */
    public Collection<Grammar> getAll() {
        return Collections.unmodifiableCollection(grammars.values());
    }

    public boolean isEmpty() {
        return grammars.isEmpty();
    }

    public void clear() {
        grammars.clear();
        nextId = 1;
    }

    public int size() {
        return grammars.size();
    }
}