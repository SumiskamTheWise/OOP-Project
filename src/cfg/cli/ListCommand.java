package cfg.cli;

import cfg.model.Grammar;

/**
 * Lists all grammar identifiers currently loaded in the store.
 * <p>
 * Usage: {@code list}
 */
public class ListCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (context.getStore().isEmpty()) {
            System.out.println("No grammars loaded.");
            return;
        }
        System.out.println("Loaded grammars:");
        for (Grammar g : context.getStore().getAll()) {
            System.out.println("  #" + g.getId() + "  (start: " + g.getStartSymbol() + ", rules: " + g.getRules().size() + ")");
        }
    }
    @Override
    public String usage() {
        return "list lists identifiers of all loaded grammars";
    }
}
