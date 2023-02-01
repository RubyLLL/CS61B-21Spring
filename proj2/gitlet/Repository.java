package gitlet;

import java.io.File;
import java.util.Objects;

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
 *      |     |--commitID
 *      |         |--commit object
 *                |-- blob objects
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
    public static final File STAGE_BLOBS = join(GITLET_STAGE, "blobs");

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
            STAGE_BLOBS.mkdir();
        }
    }

    public static void add(String fileName) {
        File file = new File(CWD, fileName);
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Stage.add(file);
    }

    public static void remove(String fileName) {
        File file = new File(CWD, fileName);
        Stage.remove(file);
    }

    public static void checkout(String fileName, String commitID) {
        File file = new File(CWD, fileName);
        Commit.checkout(file, commitID);
    }

    private static boolean isBranch(String branch) {
        File[] files = GITLET_REFS.listFiles(File::isDirectory);
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(branch)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     */
    public static void branch(String branch) {
        if (isBranch(branch)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        File b = new File(GITLET_REFS, branch);
        b.mkdir();
        Commit c = Commit.get();
        c.setBranch(branch);
        Commit.moveHead(c);
    }

    public static void removeBranch(String branch) {
        Commit c = Commit.get();
        File b = new File(GITLET_REFS, branch);
        if (c.getBranch().equals(branch)) {
            System.out.println("Cannot remove the current branch.");
        } else if (!b.exists()) {
            System.out.println("A branch with that name does not exist.");
        } else {
            for (File f: Objects.requireNonNull(b.listFiles())) {
                f.delete();
            }
            b.delete();
        }
    }

    /**
     *
     * @param message
     * @param branch
     */
    public static void commit(String message, String branch) {
        Commit.commit(message, branch);
    }

    public static void log() {
        Commit.log();
    }
}
