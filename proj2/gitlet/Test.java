package gitlet;

import java.io.File;


public class Test {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void main(String[] args) {
        Stage s = Stage.get();
        System.out.println(s);
    }
}
