package cfg.cli;

import cfg.io.CustomTextFormat;
import cfg.io.FileSession;
import cfg.io.GrammarFileFormat;
import cfg.model.GrammarStore;
import cfg.operations.*;
import cfg.util.GrammarPrinter;

/**
 * Holds all shared application dependencies and state.
 * <p>
 * Passed to every Command as a context object.
 * Acts as a lightweight dependency injection container, making
 * all services available without global state or singletons.
 * </p>
 */
public class AppContext {

    private final GrammarStore store;
    private final FileSession session;
    private final GrammarPrinter printer;

    // Operations
    private final UnionOperation union;
    private final ConcatOperation concat;
    private final IterationOperation iteration;
    private final ChomskifyOperation chomskify;
    private final IsChomsky isChomsky;
    private final CykOperation cyk;
    private final EmptyLanguageCheck emptyCheck;

    private boolean running;

    /**
     * Constructs the application context, wiring all dependencies together.
     */
    public AppContext() {
        this.store = new GrammarStore();
        GrammarFileFormat format = new CustomTextFormat();
        this.session = new FileSession(format, store);
        this.printer = new GrammarPrinter();

        this.union = new UnionOperation();
        this.concat = new ConcatOperation();
        this.iteration = new IterationOperation();
        this.chomskify = new ChomskifyOperation();
        this.isChomsky = new IsChomsky();
        this.cyk = new CykOperation();
        this.emptyCheck = new EmptyLanguageCheck();

        this.running = true;
    }

    public GrammarStore getStore()             { return store; }
    public FileSession getSession()            { return session; }
    public GrammarPrinter getPrinter()         { return printer; }
    public UnionOperation getUnion()           { return union; }
    public ConcatOperation getConcat()         { return concat; }
    public IterationOperation getIteration()   { return iteration; }
    public ChomskifyOperation getChomskify()   { return chomskify; }
    public IsChomsky getIsChomsky()            { return isChomsky; }
    public CykOperation getCyk()               { return cyk; }
    public EmptyLanguageCheck getEmptyCheck()  { return emptyCheck; }
    public boolean isRunning()                 { return running; }
    public void stop()                         { this.running = false; }
}
