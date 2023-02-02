package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Test {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void main(String[] args) {
        List<String> tests = new ArrayList<String>();
        tests.add("abc");
        tests.add("bshd");
        tests.add("sjh");

        System.out.println(tests);
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

//        Commit c = Commit.get();
//        System.out.println(c);


    }
}
