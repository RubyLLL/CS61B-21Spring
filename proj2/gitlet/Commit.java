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

    public Commit(String message, String timestamp, List<String> parentIds,
                  HashMap<String, String> committedFiles, String branch) {
        this.message = message;
        this.timestamp = timestamp;
        this.parentIds = parentIds;
        this.committedFiles = committedFiles;
        this.branch = branch;
    }

    /**
     * initial commit
     * by default at master branch
     */
    public static void init() {
        File master = new File(GITLET_REFS, "master");
        master.mkdir();
        Commit commit = new Commit();
        commit.id =  commit.generateCommitId();
        save(commit);
        moveHead(commit);
        HEAD(commit);
    }

    public static void commit(String message, String branch) {
        Commit c = get();
        Stage s = Stage.get();
        //TODO: change branch
        HashMap<String, String> filesRemained = MyUtils.compareMap(c.committedFiles, s.removedFiles);
        HashMap<String, String> committedFiles = new HashMap<String, String>();
        committedFiles.putAll(filesRemained);
        committedFiles.putAll(s.stagedFiles);
        String timestamp = dateToTimeStamp(new Date());
        List<String> parentIds = c.getParentIDs();
        parentIds.add(c.id);
        Commit commit = new Commit(message, timestamp, parentIds, committedFiles, branch);
        commit.id = commit.generateCommitId();
        save(commit);
        moveHead(commit );
        HEAD(commit);
        Stage.removeStage();
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public static Commit retrieveFromHEAD() {
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
        File f = new File(GITLET_OBJ, c.id);
        Utils.writeObject(f, c);
    }

    /**
     * Get the commit from .gitlet/HEAD
     * @return
     */
    public static Commit get() {
        File f = new File(GITLET_HEADS, "head");
        return Utils.readObject(f, Commit.class);
    }

    /**
     *
     * @param commit
     */
    public static void moveHead(Commit c) {
        File folder = new File(GITLET_REFS, c.branch);
        File f = new File(folder, c.id);
        Utils.writeObject(f, c);
    }

    private String generateCommitId() {
        return Utils.sha1("C" + this.timestamp + parentIds.toString() + committedFiles.toString());
    }

    public HashMap<String, String> getCommittedFiles() {
        return committedFiles;
    }

    public List<String> getParentIDs() {
        return parentIds;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     *     private String message;
     *     private String timestamp;
     *     private String id; // the 40-digit sha1 code
     *     private String branch; // the name of the branch this commit is in
     *     private HashMap<String, String> committedFiles; // all blobs associated with this commit
     *     private List<String> parentIds;
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Message: ").append(message);
        sb.append("\n");
        sb.append("Timestamp: ").append(timestamp);
        sb.append("\n");
        sb.append("Commit id: ").append(id);
        sb.append("\n");
        sb.append("Branch: ").append(branch);
        sb.append("\n");
        sb.append("Committed Files: ").append(committedFiles);
        sb.append("\n");
        sb.append("Parent IDs: ").append(parentIds);
        sb.append("\n");
        return sb.toString();
    }
}
