package gitlet;

import java.io.File;

import static gitlet.Utils.join;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */

/* Structure inside our .gitlet directory
 *   .gitlet
 *      |--objects
 *      |     |--commit and blob
 *      |--refs
 *      |     |--master
 *      |--HEAD
 *      |--stage
 */

// --------------------------------
//
// --------------------------------
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_OBJ = join(GITLET_DIR, "objects");
    public static final File GITLET_REFS = join(GITLET_DIR, "refs");
    public static final File GITLET_HEADS = join(GITLET_DIR, "HEAD");
    public static final File GITLET_STAGE = join(GITLET_DIR, "stage");

    public static void init() {
        //TODO: init
        /**
         * This system will automatically start with one commit:
         * a commit that contains no files and has the commit message initial commit
         */
        initDirectory();
        Commit.init();
    }

    private static void initDirectory() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            GITLET_OBJ.mkdir();
            GITLET_REFS.mkdir();
            GITLET_HEADS.mkdir();
            GITLET_STAGE.mkdir();
            System.out.println("Initialized empty Git repository in" + GITLET_DIR);
        }
    }

    public static void add(String fileName) {
        File file = new File(CWD, fileName);
        if (!GITLET_DIR.exists()) {
            throw new GitletException("Not in an initialized Gitlet directory.");
        }
        Stage.add(file);
    }
}
