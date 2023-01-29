package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_COMMIT = join(GITLET_DIR, "commits");

    /** The message of this Commit. */
    private String message;
    private Instant timestamp;
    private Commit parent;
    private String id; // the sha1 for all files included in this commit
    private HashMap<String, Blob> blobs; //TODO: type depends on BSTMap.saveFiles return type

    public Commit(String message, Instant epoch, Commit parent, HashMap<String, Blob> blobs) {
        this.message = message;
        this.timestamp = epoch;
        this.parent = parent;
        this.blobs = blobs;
    }

    public Commit(Commit parent, HashMap blobs) {
        this.parent = parent;
        this.blobs = blobs;
    }

    public static void initCommit() {
        // init commit
        Commit c = new Commit("initial commit", Instant.EPOCH, null, new HashMap<String, Blob>());
        String id = Utils.sha1(GITLET_DIR);
        HashMap<String, Commit> commits = new HashMap<String, Commit>();
        commits.put(id, c);
        CommitTree tree = new CommitTree("master", commits);
        Branch master = new Branch("master", tree);

    }

    public Commit getParent() {
        return parent;
    }

    /**
     * Save the commit object to the directory
     * Returns a new commit object, set its variable files the copy of this one
     * and parent to be this commit object
     * @return
     */
    public Commit save() {
        //TODO: save the commit object somewhere in .gitlet
        // after done so, empty .gitlet/stage

    }

    /* TODO: fill in the rest of this class. */
}
