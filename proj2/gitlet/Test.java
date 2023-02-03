package gitlet;

import java.io.File;


public class Test {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void main(String[] args) {
        Commit c = Commit.get();
        System.out.println(c);
        Stage s = Stage.get();
        System.out.println(s);

//        File f = new File(CWD, "test.txt");
//        System.out.println(f.getAbsoluteFile());
//        try {
//              f.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Commit c = Commit.get();
//        System.out.println(c);


    }
}
