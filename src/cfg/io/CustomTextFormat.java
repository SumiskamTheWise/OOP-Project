package cfg.io;

import cfg.model.Grammar;
import cfg.model.GrammarStore;

import java.io.IOException;

public class CustomTextFormat implements GrammarFileFormat {
    @Override
    public void load(String filePath, GrammarStore store) throws IOException {

    }

    @Override
    public void saveAll(String filePath, GrammarStore store) throws IOException {

    }

    @Override
    public void saveSingle(String filePath, Grammar grammar) throws IOException {

    }
}
