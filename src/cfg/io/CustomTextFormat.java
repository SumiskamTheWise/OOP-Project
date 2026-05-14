package cfg.io;

import cfg.model.Grammar;
import cfg.model.GrammarStore;
import cfg.model.Rule;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Reads and writes grammars in a custom plain-text format.
 * <p>
 * File format:
 * <pre>
 * GRAMMAR
 * START A
 * A -> aB
 * A -> eps
 * B -> b
 * END
 * GRAMMAR
 * START S
 * S -> AB
 * END
 * </pre>
 * Each grammar block starts with GRAMMAR, followed by START,
 * then one rule per line in the form LHS -> RHS, and ends with END.
 */
public class CustomTextFormat implements GrammarFileFormat {

    private static final String GRAMMAR_MARKER = "GRAMMAR";
    private static final String START_PREFIX = "START ";
    private static final String END_MARKER = "END";
    private static final String RULE_SEPARATOR = " -> ";

    @Override
    public void load(String filePath, GrammarStore store) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        parseGrammars(lines, store);
    }

    @Override
    public void saveAll(String filePath, GrammarStore store) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Grammar g : store.getAll()) {
                writeGrammar(pw, g);
            }
        }
    }

    @Override
    public void saveSingle(String filePath, Grammar grammar) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            writeGrammar(pw, grammar);
        }
    }


    // Private helpers

    private void parseGrammars(List<String> lines, GrammarStore store) {
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.equals(GRAMMAR_MARKER)) {
                i++;
                if (i >= lines.size() || !lines.get(i).trim().startsWith(START_PREFIX)) {
                    throw new IllegalArgumentException("Expected START <symbol> after GRAMMAR at line " + (i + 1));
                }
                char startSymbol = parseStartSymbol(lines.get(i).trim());
                i++;
                List<Rule> rules = new ArrayList<>();
                while (i < lines.size() && !lines.get(i).trim().equals(END_MARKER)) {
                    String ruleLine = lines.get(i).trim();
                    if (!ruleLine.isEmpty()) {
                        rules.add(parseRule(ruleLine, i + 1));
                    }
                    i++;
                }
                store.registerNew(startSymbol, rules);
                i++; // skip END
            } else {
                i++;
            }
        }
    }

    private char parseStartSymbol(String line) {
        String symbolStr = line.substring(START_PREFIX.length()).trim();
        if (symbolStr.length() != 1 || !Character.isUpperCase(symbolStr.charAt(0))) {
            throw new IllegalArgumentException("Start symbol must be a single uppercase letter, got: " + symbolStr);
        }
        return symbolStr.charAt(0);
    }

    private Rule parseRule(String line, int lineNumber) {
        int sep = line.indexOf(RULE_SEPARATOR);
        if (sep < 0) {
            throw new IllegalArgumentException("Invalid rule at line " + lineNumber + ": '" + line + "'");
        }
        String lhs = line.substring(0, sep).trim();
        String rhs = line.substring(sep + RULE_SEPARATOR.length()).trim();
        if (lhs.length() != 1 || !Character.isUpperCase(lhs.charAt(0))) {
            throw new IllegalArgumentException("LHS must be a single non-terminal at line " + lineNumber + ": '" + lhs + "'");
        }
        return new Rule(lhs.charAt(0), rhs);
    }

    private void writeGrammar(PrintWriter pw, Grammar g) {
        pw.println(GRAMMAR_MARKER);
        pw.println(START_PREFIX + g.getStartSymbol());
        for (Rule rule : g.getRules()) {
            pw.println(rule.getLeftSide() + RULE_SEPARATOR + rule.getRightSide());
        }
        pw.println(END_MARKER);
        pw.println();
    }
}
