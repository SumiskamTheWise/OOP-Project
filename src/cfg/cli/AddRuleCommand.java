package cfg.cli;

import cfg.model.Grammar;
import cfg.model.Rule;

/**
 * Adds a production rule to an existing grammar.
 * <p>
 * Usage: {@code addRule <id> <A> -> <rhs>}
 * <p>
 * Example: {@code addRule 1 S -> aSb}
 */

public class AddRuleCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        // args: [id, LHS, "->", RHS]  or  [id, "LHS->RHS"] — we join and parse
        if (args.length < 3) { System.out.println("Usage: " + usage()); return; }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        // Join remaining args and parse as rule
        String rulePart = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        Rule rule = parseRule(rulePart);
        if (rule == null) return;
        g.addRule(rule);
        System.out.println("Successfully added rule: " + rule + " to grammar #" + g.getId());
    }

    @Override
    public String usage() {
        return "addRule <id> <A> -> <rhs>  adds rule to grammar #<id>";
    }

    private Rule parseRule(String text) {
        String[] parts = text.split("->", 2);
        if (parts.length != 2) {
            System.out.println("Error: rule must be in the form 'A -> rhs'");
            return null;
        }
        String lhs = parts[0].trim();
        String rhs = parts[1].trim();
        if (lhs.length() != 1 || !Character.isUpperCase(lhs.charAt(0))) {
            System.out.println("Error: LHS must be a single non-terminal (uppercase letter).");
            return null;
        }
        if (rhs.isEmpty()) {
            System.out.println("Error: RHS cannot be empty. Use 'eps' for epsilon.");
            return null;
        }
        try {
            return new Rule(lhs.charAt(0), rhs);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
