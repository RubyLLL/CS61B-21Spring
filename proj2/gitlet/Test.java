package gitlet;

import java.io.File;



public class Test {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void main(String[] args) {
//        PrintStream o = null;
//        try {
//            o = new PrintStream(Utils.join(CWD, "log", "stage.txt"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        PrintStream console = System.out;
//        System.setOut(o);
        Stage s = Stage.get();
        System.out.println(s);

    }
}
