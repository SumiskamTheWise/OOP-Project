package cfg.model;
/**
 * Represents a single production rule in a Context-Free Grammar.
 * <p></p>
 * A rule has the form: {@code A -> w} where {@code A} is a non-terminal (uppercase)
 * and {@code w} is a string of terminals and/or non-terminals, or the empty string
 * represented by {@code "eps"}.
 */
public class Rule {
    /** Sentinel string representing the empty production (epsilon). */
    public static final String EPSILON = "eps";

    private final char leftSide;
    private final String rightSide;

    public Rule(char leftSide, String rightSide) {
        if (!Character.isUpperCase(leftSide)) {
            throw new IllegalArgumentException("Left side of rule must be a non-terminal (uppercase): " + leftSide);
        }
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public char getLeftSide() {
        return leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }
    /**
     * Returns whether this rule is an epsilon (empty) production.
     * @return true if this rule produces epsilon
     */
    public boolean isEpsilon() {
        return EPSILON.equals(rightSide);
    }

    /**
     * Returns whether the right-hand side is a single terminal symbol.
     * @return true if right side is exactly one terminal character
     */
    public boolean isSingleTerminal() {
        return rightSide.length() == 1 && (Character.isLowerCase(rightSide.charAt(0)) || Character.isDigit(rightSide.charAt(0)));
    }
    /**
     * Returns whether the right-hand side is exactly two non-terminals (CNF form).
     * @return true if right side is two uppercase letters
     */
    public boolean isTwoNonTerminals() {
        return rightSide.length() == 2
                && Character.isUpperCase(rightSide.charAt(0))
                && Character.isUpperCase(rightSide.charAt(1));
    }

    @Override
    public String toString() {
        return leftSide + " -> " + rightSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        return leftSide == rule.leftSide && rightSide.equals(rule.rightSide);
    }

    @Override
    public int hashCode() {
        return 31 * leftSide + rightSide.hashCode();
    }
}
