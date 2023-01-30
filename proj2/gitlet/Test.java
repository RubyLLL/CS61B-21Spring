package gitlet;

import java.io.File;

import static gitlet.Repository.GITLET_HEADS;

public class Test {

    public static void main(String[] args) {
        File f = new File(GITLET_HEADS, "head");
        Commit c = Commit.get();
        System.out.println(c);
//        Stage s = Stage.get();
//        System.out.println(s);
    }
}
