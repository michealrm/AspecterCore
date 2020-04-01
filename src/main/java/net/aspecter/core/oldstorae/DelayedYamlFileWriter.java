package net.aspecter.core.oldstorae;

import net.aspecter.core.AspecterCore;

import java.io.File;

public class DelayedYamlFileWriter extends AbstractDelayedYamlFileWriter {

    private static final String DATA_PATH = "plugins/Core/guis";

    private static final File DATA_DIR = new File(DATA_PATH);

    public DelayedYamlFileWriter(AspecterCore ac, String subdir) {
    }

    @Override
    public StorageObject getObject() {
        return null;
    }

    @Override
    public void onFinish() {

    }
}
