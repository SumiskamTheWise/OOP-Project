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
    public Grammar getGrammar(int id) {
        return grammars.get(id);
    }

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
    /**
     * Registers a new grammar in the store, assigning it the next available id.
     * @param startSymbol the start symbol for the new grammar
     * @param rules the production rules
     * @return the newly created and registered Grammar
     */
    public Grammar register(char startSymbol, List<Rule> rules) {
        Grammar g = new Grammar(nextId++, startSymbol, rules);
        grammars.put(g.getId(), g);
        return g;
    }
    /**
     * Registers a pre-built grammar (used when loading from file, assigning a fresh id).
     * @param startSymbol the start symbol
     * @param rules the rules
     * @return the registered Grammar
     */
    public Grammar registerNew(char startSymbol, List<Rule> rules) {
        return register(startSymbol, rules);
    }
    /**
     * Stores a grammar directly (e.g. result of an operation).
     * @param g the grammar to store; its existing id is ignored, a new one is assigned
     * @return the grammar stored with a new id
     */
    public Grammar store(Grammar g) {
        return register(g.getStartSymbol(), new ArrayList<>(g.getRules()));
    }


}
