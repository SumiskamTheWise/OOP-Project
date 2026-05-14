package cfg.cli;

/**
 * Dispatches between two save behaviours based on argument count:
 * <ul>
 *   <li>save — saves all grammars to the current file</li>
 *   <li>save with ID to F — saves grammar #id to file f</li>
 * </ul>
 */
public class SmartSaveCommand implements Command {

    private final Command saveAll;
    private final Command saveOne;

    /**
     * Constructs the dispatcher.
     *
     * @param saveAll command to save everything to the open file
     * @param saveOne command to save a single grammar by id
     */
    public SmartSaveCommand(Command saveAll, Command saveOne) {
        this.saveAll = saveAll;
        this.saveOne = saveOne;
    }

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length == 0) {
            saveAll.execute(args, context);
        } else {
            saveOne.execute(args, context);
        }
    }

    @Override
    public String usage() {
        return "save [<id> <file>]   saves all grammars (or grammar #<id> to <file>)";
    }
}
