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
        requireOpen(context);
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
        return "lists identifiers of all loaded grammars";
    }

    private void requireOpen(AppContext context) {
        if (!context.getSession().isOpen()) {
            throw new IllegalStateException("No file is currently open.");
        }
    }
}
