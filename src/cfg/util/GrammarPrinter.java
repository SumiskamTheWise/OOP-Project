package cfg.util;

import cfg.model.Grammar;
import cfg.model.Rule;

import java.util.List;

/** Grammar Printer
 * <p>How grammars are presented to the user
 */
public class GrammarPrinter {

    public String format(Grammar grammar) {
        StringBuilder sb = new StringBuilder();
        sb.append("Grammar #").append(grammar.getId())
                .append("  (start: ").append(grammar.getStartSymbol()).append(")\n");

        List<Rule> rules = grammar.getRules();
        if (rules.isEmpty()) {
            sb.append("  (no rules)\n");
        } else {
            for (int i = 0; i < rules.size(); i++) {
                sb.append(String.format("  %2d. %s%n", i + 1, rules.get(i)));
            }
        }
        return sb.toString();
    }

    public void print(Grammar grammar) {
        System.out.print(format(grammar));
    }
}