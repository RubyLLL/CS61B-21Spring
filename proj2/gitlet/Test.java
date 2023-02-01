package gitlet;

import java.io.File;

import static gitlet.Repository.GITLET_DIR;

public class Test {

    public static void main(String[] args) {
        File source = new File(GITLET_DIR, "test1");
        MyUtils.cleanDirectory(source);
    }
}
