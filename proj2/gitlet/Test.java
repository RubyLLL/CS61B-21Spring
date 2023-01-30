package gitlet;

import java.io.File;

import static gitlet.Repository.CWD;

public class Test {

    public static void main(String[] args) {
        Stage s = Stage.get();
        File f = new File(CWD, "test.txt");
        System.out.println(s);
    }
}
