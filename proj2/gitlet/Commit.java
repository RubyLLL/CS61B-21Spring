package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;

/** Represents a gitlet commit object.
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

public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String id; // the 40-digit sha1 code
    private String branch; // the name of the branch this commit is in
    private HashMap<String, String> committedFiles; // all blobs associated with this commit
    private List<String> parentIds; // the 40-digit sha1 code of the last commit

    public Commit() {
        this.message = "initial commit";
        Date date = new Date(0);
        this.timestamp = dateToTimeStamp(date);
        this.parentIds = new ArrayList<String>();
        this.id = "";
        this.branch = "master";
        this.committedFiles = new HashMap<String, String>();
    }

    public Commit(String message, String timestamp,
                  List<String> parentIds, String id,
                  HashMap<String, String> committedFiles, String branch) {
        this.message = message;
        this.timestamp = timestamp;
        this.parentIds = parentIds;
        this.id = id;
        this.committedFiles = committedFiles;
        this.branch = branch;
    }

    /**
     * initial commit
     * by default at master branch
     */
    public static void init() {
        newBranch("master");
        Commit commit = new Commit();
        commit.id =  commit.generateCommitId();
        save(commit);
        commit.moveHead();
        HEAD(commit);
    }


    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public static boolean branchExists(String name){
        File[] files = GITLET_REFS.listFiles(File::isFile);
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void newBranch(String name) {
        if (branchExists(name)) {
            throw new GitletException("A branch with that name already exists.");
        }
        File newBranch = new File(GITLET_REFS, name);
        newBranch.mkdir();
    }

    public static Commit retriveFromHEAD() {
        File f = new File(GITLET_HEADS, "head");
        return Utils.readObject(f, Commit.class);
    }

    private static void HEAD(Commit c) {
        File f = new File(GITLET_HEADS, "head");
        Utils.writeObject(f, c);
    }

    /**
     * Save the commit object to the directory
     * Returns a new commit object, set its variable files the copy of this one
     * and parent to be this commit object
     * @return
     */
    public static void save(Commit c) {
        //TODO: save the commit object somewhere in .gitlet
        // after done so, empty .gitlet/stage
        File f = new File(GITLET_OBJ, c.id);
        Utils.writeObject(f, c);
    }

    private String generateCommitId() {
        return Utils.sha1("C" + this.timestamp + parentIds.toString() + committedFiles.toString());
    }

    /**
     *
     * @param commit
     */
    private void moveHead() {
        File folder = new File(GITLET_REFS, this.branch);
        File f = new File(folder, this.id);
        Utils.writeObject(f, this);
    }

    public HashMap<String, String> getCommittedFiles() {
        return committedFiles;
    }

    public List<String> getParentIDs() {
        return parentIds;
    }

    /* TODO: fill in the rest of this class. */
}
