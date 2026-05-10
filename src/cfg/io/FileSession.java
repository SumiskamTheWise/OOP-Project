package cfg.io;

import cfg.model.GrammarStore;

import java.io.IOException;

/**
 * Manages the lifecycle of a grammar file session.
 * <p>
 * Responsible for: tracking whether a file is open, the current file path,
 * and delegating load/save operations to a GrammarFileFormat.
 * <p>
 * This class follows the Single Responsibility Principle by only handling
 * the open/close/save session state, not grammar logic.
 */
public class FileSession {

    private final GrammarFileFormat format;
    private final GrammarStore store;

    private String currentFilePath;
    private boolean isOpen;

    /**
     * Constructs a FileSession with the given format and grammar store.
     *
     * @param format the file format to use for reading/writing
     * @param store  the grammar store to populate and persist
     */
    public FileSession(GrammarFileFormat format, GrammarStore store) {
        this.format = format;
        this.store = store;
        this.isOpen = false;
    }

    /**
     * Opens a file, loading its grammars into the store.
     * If the file does not exist, an empty session is started with that path.
     *
     * @param filePath path to the grammar file
     * @throws IOException              if the file exists but cannot be read
     * @throws IllegalArgumentException if the file content is malformed
     * @throws IllegalStateException    if a file is already open
     */
    public void open(String filePath) throws IOException {
        if (isOpen) {
            throw new IllegalStateException("A file is already open. Close it first.");
        }
        store.clear();
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        if (java.nio.file.Files.exists(path)) {
            format.load(filePath, store);
        }
        // If file doesn't exist: create an empty session (file will be created on save)
        currentFilePath = filePath;
        isOpen = true;
    }

    /**
     * Closes the current session without saving. Clears the in-memory store.
     *
     * @return the name of the closed file
     * @throws IllegalStateException if no file is currently open
     */
    public String close() {
        requireOpen();
        String name = java.nio.file.Paths.get(currentFilePath).getFileName().toString();
        store.clear();
        currentFilePath = null;
        isOpen = false;
        return name;
    }

    /**
     * Saves the current store back to the original file.
     *
     * @return the name of the saved file
     * @throws IOException           if the file cannot be written
     * @throws IllegalStateException if no file is currently open
     */
    public String save() throws IOException {
        requireOpen();
        format.saveAll(currentFilePath, store);
        return java.nio.file.Paths.get(currentFilePath).getFileName().toString();
    }

    /**
     * Saves the current store to a different file path.
     *
     * @param filePath the target file path
     * @return the name of the saved file
     * @throws IOException           if the file cannot be written
     * @throws IllegalStateException if no file is currently open
     */
    public String saveAs(String filePath) throws IOException {
        requireOpen();
        format.saveAll(filePath, store);
        return java.nio.file.Paths.get(filePath).getFileName().toString();
    }

    /**
     * Returns whether a file is currently open.
     *
     * @return true if a file is open
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Returns the current file path.
     *
     * @return the current file path, or {@code null} if no file is open
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Returns the grammar store managed by this session.
     *
     * @return the grammar store
     */
    public GrammarStore getStore() {
        return store;
    }

    /**
     * Returns the file format used by this session.
     *
     * @return the file format
     */
    public GrammarFileFormat getFormat() {
        return format;
    }

    private void requireOpen() {
        if (!isOpen) {
            throw new IllegalStateException("No file is currently open.");
        }
    }
}
