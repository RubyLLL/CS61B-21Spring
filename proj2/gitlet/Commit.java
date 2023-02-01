package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

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
    private LinkedList<String> parentIds; // the 40-digit sha1 code of the last commit

    public Commit() {
        this.message = "initial commit";
        Date date = new Date(0);
        this.timestamp = dateToTimeStamp(date);
        this.parentIds = new LinkedList<>();
        this.id = "";
        this.branch = "master";
        this.committedFiles = new HashMap<String, String>();
    }

    public Commit(String message, String timestamp, LinkedList<String> parentIds,
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
        if (s.noChange()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        //TODO: change branch
        HashMap<String, String> filesRemained = MyUtils.compareMap(c.committedFiles, s.getRemovedFiles());
        HashMap<String, String> committedFiles = new HashMap<String, String>();
        committedFiles.putAll(filesRemained);
        committedFiles.putAll(s.getStagedFiles());
        String timestamp = dateToTimeStamp(new Date());
        LinkedList<String> parentIds = c.getParentIDs();
        parentIds.addFirst(c.id);
        Commit commit = new Commit(message, timestamp, parentIds, committedFiles, branch);
        commit.id = commit.generateCommitId();

        save(commit);
        moveHead(commit);
        HEAD(commit);
        Stage.removeStage();
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    private static void HEAD(Commit c) {
        if (GITLET_HEADS.listFiles() != null) {
            MyUtils.cleanDirectory(GITLET_HEADS);
        }
        File f = new File(GITLET_HEADS, c.id);
        f.mkdirs();
        Utils.writeObject(Utils.join(f, c.id), c);
        File source = Utils.join(GITLET_OBJ, c.id, "blobs");
        Blob.copy(source, Utils.join(f, "blobs"));
    }

    /**
     * Save the commit object to the directory
     * Returns a new commit object, set its variable files the copy of this one
     * and parent to be this commit object
     * @return
     */
    public static void save(Commit c) {
        File commitFolder = new File(GITLET_OBJ, c.id);
        commitFolder.mkdir();
        String parentId = c.getParentIDs().peek();
        if (parentId != null) { //TODO: should delete those present in s.removedFiles
            File parentCommit = Utils.join(GITLET_OBJ, parentId, "blobs");
            Blob.copy(parentCommit, Utils.join(commitFolder, "blobs"));
        }
        Stage s = Stage.get();
        Blob.copy(s.getStagedFiles(), STAGE_BLOBS, Utils.join(commitFolder, "blobs"));
        File f = new File(commitFolder, c.id);
        Utils.writeObject(f, c);
    }

    /**
     * Get the commit from .gitlet/HEAD
     * @return
     */
    public static Commit get() {
        File[] files = GITLET_HEADS.listFiles(File::isDirectory);
        if (files != null && files.length > 0) {
            File file = files[0];
            File[] f = file.listFiles(File::isFile);
            if (f != null && f.length > 0) {
                return Utils.readObject(f[0], Commit.class);
            }   
        }
        return null;
    }

    /**
     * Replace the commit object of the branch with the new commit object
     * @param commit
     */
    //TODO: generalize this to copying
    public static void moveHead(Commit c) {
        File target = Utils.join(GITLET_REFS, c.branch, c.id);
        target.mkdir();
        Utils.writeObject(Utils.join(target, c.id), c);
        File source = new File(GITLET_OBJ, c.id);
        Blob.copy(Utils.join(source, "blobs"),
                Utils.join(target, "blobs"));
        String parentId = c.getParentIDs().peek();
        if (parentId != null) {
            File parent = new File(Utils.join(GITLET_REFS, c.branch), parentId);
            MyUtils.cleanDirectory(parent);
            parent.delete();
        }
    }

    private String generateCommitId() {
        return Utils.sha1("C" + this.timestamp + parentIds.toString() + committedFiles.toString());
    }

    public HashMap<String, String> getCommittedFiles() {
        return committedFiles;
    }

    public LinkedList<String> getParentIDs() {
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
