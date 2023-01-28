package gitlet;

import java.io.File;
import java.time.Instant;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_COMMIT = join(GITLET_DIR, "commits");

    public static void init() {
        //TODO: init
        /**
         * This system will automatically start with one commit:
         * a commit that contains no files and has the commit message initial commit
         */
        initDirectory();
        BSTMap files = new BSTMap();
        files.saveFiles(GITLET_COMMIT);
        // Commit commit = new Commit("initial commit", Instant.EPOCH);
    }

    private static void initDirectory() {
        File ref = new File(GITLET_DIR, "ref");
        if (GITLET_DIR.exists()) {
            System.out.println("Reinitialized existing Git repository in " + GITLET_DIR);
        } else {
            GITLET_DIR.mkdir();
            GITLET_COMMIT.mkdir();
            ref.mkdir();
            System.out.println("Initialized empty Git repository in" + GITLET_DIR);
        }
    }


    public static void add() {
        if (!GITLET_DIR.exists()) {
            throw new GitletException("Not in an initialized Gitlet directory.");
        }
    }
}
