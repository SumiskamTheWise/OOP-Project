package cfg.io;

import cfg.model.Grammar;
import cfg.model.GrammarStore;

import java.io.IOException;

/**
 * Abstraction for reading and writing grammars to/from files.
 * <p>
 * Implementing classes define the concrete file format.
 * This interface follows the Open/Closed Principle: new formats can be added
 * by implementing this interface without modifying existing code.
 */
public interface GrammarFileFormat {

    /**
     * Reads all grammars from the given file path and registers them in the store.
     *
     * @param filePath the path to the file
     * @param store    the grammar store to populate
     * @throws IOException              if the file cannot be read
     * @throws IllegalArgumentException if the file content is malformed
     */
    void load(String filePath, GrammarStore store) throws IOException;

    /**
     * Writes all grammars in the store to the given file path.
     *
     * @param filePath the path to the output file
     * @param store    the grammar store to persist
     * @throws IOException if the file cannot be written
     */
    void saveAll(String filePath, GrammarStore store) throws IOException;

    /**
     * Writes a single grammar to a file.
     *
     * @param filePath the path to the output file
     * @param grammar  the grammar to save
     * @throws IOException if the file cannot be written
     */
    void saveSingle(String filePath, Grammar grammar) throws IOException;
}
