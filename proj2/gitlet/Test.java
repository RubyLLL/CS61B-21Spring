package gitlet;

import java.io.File;

import static gitlet.Repository.GITLET_REFS;

public class Test {

    public static void main(String[] args) {
        File[] files = GITLET_REFS.listFiles(File::isDirectory);
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
    }
}
