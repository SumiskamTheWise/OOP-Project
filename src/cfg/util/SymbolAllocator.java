package cfg.util;

import java.util.Set;

public class SymbolAllocator {
    private final boolean[] used =  new boolean[26]; //A-Z
    public void reserve(Set<Character> symbols) {
        for (char c : symbols) {
            if(c >= 'A' && c <= 'Z') {
                used[c - 'A'] = true;
            }
        }
    }
    public void reserve(char c) {
        if(c >= 'A' && c <= 'Z') {
            used[c - 'A'] = true;
        }
    }
    public char next() {
        for(int i = 0; i < 26; i++) {
            if(!used[i]) {
                used[i] = true;
                return (char) ('A' + i);
            }
        }
        throw new IllegalStateException("Used up all 26 non-terminal symbols.");
    }
}
