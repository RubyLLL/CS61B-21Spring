package gitlet;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;

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
    public static final File GITLET_STAGE = join(GITLET_DIR, "stage");

    public static void init() {
        //TODO: init
        /**
         * This system will automatically start with one commit:
         * a commit that contains no files and has the commit message initial commit
         */
        initDirectory();
        HashMap files = new HashMap();
        // Commit commit = new Commit("initial commit", Instant.EPOCH);
    }

    private static void initDirectory() {
        if (GITLET_DIR.exists()) {
            System.out.println("Reinitialized existing Git repository in " + GITLET_DIR);
        } else {
            GITLET_DIR.mkdir();
            GITLET_COMMIT.mkdir();
            GITLET_STAGE.mkdir();
            System.out.println("Initialized empty Git repository in" + GITLET_DIR);
        }
    }

    private static String initCommit() {
        HashMap f = new HashMap<String, String>();
        String sha1 = Utils.sha1(f);
        Commit commit = new Commit("initial commit", Instant.EPOCH, null, sha1);
        commit.save();
        return sha1;
    }


    public static void add(String fileName) {
        File file = new File(GITLET_DIR, fileName);
        if (!GITLET_DIR.exists()) {
            throw new GitletException("Not in an initialized Gitlet directory.");
        }
        if (!file.exists()) {
            throw new GitletException("File does not exist.");
        }
        //TODO: check file exists in the previous Commit object

        // TODO: check if there is already a STage object in use


    }
}
