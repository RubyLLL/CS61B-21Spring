package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Xiaoyue Lyu
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

public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    private final String timestamp;
    private final HashMap<String, String> committedFiles; // all blobs associated with this commit
    private final LinkedList<String> parentIds; // the 40-digit sha1 code of the previous commits
    private final String id; // the 40-digit sha1 code
    private String branch; // the name of the branch this commit is in

    public Commit() {
        this.message = "initial commit";
        Date date = new Date(0);
        this.timestamp = MyUtils.dateToTimeStamp(date);
        this.parentIds = new LinkedList<>();
        this.branch = "master";
        this.committedFiles = new HashMap<>();
        this.id = Utils.sha1("C" + timestamp + parentIds + committedFiles);
    }

    public Commit(String message, String timestamp, LinkedList<String> parentIds,
                  HashMap<String, String> committedFiles, String branch) {
        this.message = message;
        this.timestamp = timestamp;
        this.parentIds = parentIds;
        this.committedFiles = committedFiles;
        this.branch = branch;
        this.id = Utils.sha1("C" + timestamp + parentIds.toString() + committedFiles.toString());
    }

    /**
     * initial commit
     * by default at master branch
     */
    public static void init() {
        File master = new File(GITLET_REFS, "master");
        master.mkdir();
        Commit commit = new Commit();
        save(commit);
        moveFolder(new File(GITLET_REFS, commit.branch), commit);
        moveFolder(GITLET_HEADS, commit);
    }

    public static void commit(String message, String branch) {
        Commit c = get();
        Stage s = Stage.get();
        if (s.noChange()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        HashMap<String, String> filesRemained =
                MyUtils.compareMap(c.committedFiles, s.getRemovedFiles());
        HashMap<String, String> committedFiles = new HashMap<String, String>();
        committedFiles.putAll(filesRemained);
        committedFiles.putAll(s.getStagedFiles());
        String timestamp = MyUtils.dateToTimeStamp(new Date());
        LinkedList<String> parentIds = c.getParentIDs();
        parentIds.addFirst(c.id);
        Commit commit = new Commit(message, timestamp, parentIds, committedFiles, branch);
        save(commit);
        moveFolder(new File(GITLET_REFS, commit.branch), commit);
        moveFolder(GITLET_HEADS, commit);
        Stage.removeStage();
    }

    /**
     * Takes the version of the file as it exists in the head commit
     * and puts it in the working directory,
     * overwriting the file that’s already there
     * The new version of the file is not staged.
     * @param f
     */
    public static void checkout(File f, String commitId) {
        Commit commit;
        File source;
        if (commitId.equals("HEAD")) {
            commit = get();
            source = GITLET_HEADS;
        } else {
            commit = get(commitId);
            if (commit == null) {
                System.out.println("No commit with that id exists.");
                return;
            }
            source = GITLET_OBJ;
        }
        HashMap<String, String> commits = commit.getCommittedFiles();
        if (commits != null) {
            String blobId = commits.get(f.getName());
            if (blobId == null) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            Blob blob = Blob.get(blobId, Utils.join(source, commit.id, "blobs"));
            blob.toFile(f.getAbsoluteFile());
        }
    }

    /**
     * Checkout branch
     * @param branch
     */
    public static void checkout(String branch) {
        // Take all files from the given branch
        File commitFolder = getHead(branch);
        reset(commitFolder, branch);
    }

    public static void reset(File commitFolder, String branch) {
        Commit c = Commit.get(commitFolder.getName());
        if (branch != null) {
            c.setBranch(branch);
        } else {
            File dest = Utils.join(GITLET_REFS, c.getBranch());
            moveFolder(dest, c);
        }
        Stage s = Stage.get();
        List<Blob> blobs = new ArrayList<>();
        HashMap<String, String> untrackedFiles = s.getUntrackedFiles();
        File[] newHEADBlobs = new File(commitFolder, "blobs").listFiles(File::isFile);
        if (newHEADBlobs != null) {
            for (File newBlob : newHEADBlobs) {
                Blob nb = Utils.readObject(newBlob, Blob.class);
                blobs.add(nb);
                if (untrackedFiles.containsKey(nb.getFilename())
                        && !untrackedFiles.containsValue(nb.getId())) {
                    // untracked files that will be overwritten
                    // if the contents didn't change, then it won't be overwritten
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    return;
                }
            }
        }
        moveFolder(GITLET_HEADS, c);
        MyUtils.cleanDirectory(CWD, "txt");
        if (branch == null) {
            for (Map.Entry<String, String> entry: s.getStagedFiles().entrySet()) {
                // Checks out all the files tracked by the given commit.
                Blob b = Blob.get(entry.getValue(), new File(GITLET_STAGE, "blobs"));
                b.toFile(new File(b.getFilepath()));
            }
        }
        Stage.removeStage();
        for (Blob b: blobs) {
            b.toFile(new File(b.getFilepath()));
        }
    }

    /**
     * Get the head from the given branch
     * @param branch
     */
    private static File getHead(String branch) {
        File head = new File(GITLET_REFS, branch);
        if (!head.exists()) {
            return null;
        }
        File[] commits = head.listFiles(File::isDirectory);
        if (commits == null) {
            return null;
        }
        return commits[0];
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
        if (parentId != null) {
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
    /**
     * Get the commit from .gitlet/HEAD
     * @return
     */
    public static Commit get() {
        File[] files =
                GITLET_HEADS.listFiles(File::isDirectory);
        if (files != null && files.length > 0) {
            File file = files[0];
            File[] f = file.listFiles(File::isFile);
            if (f != null && f.length > 0) {
                return Utils.readObject(f[0], Commit.class);
            }
        }
        return null;
    }

    public static Commit get(String commitID) {
        File[] files =
                GITLET_OBJ.listFiles(File::isDirectory);
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(commitID)) {
                    File[] f = file.listFiles(File::isFile);
                    if (f != null && f.length > 0) {
                        return Utils.readObject(f[0], Commit.class);
                    }
                }
            }
        }
        return null;
    }

    public static void log() {
        Commit commit = get();
        commit.printCommit();
        LinkedList<String> parentIds = commit.getParentIDs();
        for (String parentId : parentIds) {
            Commit parent = get(parentId);
            parent.printCommit();
        }
    }

    public static void globalLog() {
        File[] commitFolders = GITLET_OBJ.listFiles(File::isDirectory);
        for (File folder: commitFolders) {
            Commit c = get(folder.getName());
            c.printCommit();
        }
    }

    /**
     *
     * @param dest
     * @param c
     */
    public static void moveFolder(File dest, Commit c) {
            // dest = Utils.join(GITET_REFS, c.branch);
        MyUtils.cleanDirectory(dest);
        File newFolder = new File(dest, c.id);
        newFolder.mkdir();
        File commitFolder = Utils.join(GITLET_OBJ, c.id);
        Blob.copy(Utils.join(commitFolder, "blobs"),
                Utils.join(newFolder, "blobs"));
        Utils.writeObject(new File(newFolder, c.id), c);
    }

    public static void find(String message) {
        File[] commitFolders = GITLET_OBJ.listFiles(File::isDirectory);
        List<String> commits = new ArrayList<>();
        for (File folder: commitFolders) {
            Commit c = get(folder.getName());
            if (c.message.equals(message)) {
                commits.add(c.id);
            }
        }
        if (commits.size() == 0) {
            System.out.println("Found no commit with that message.");
            return;
        }
        for (String commit: commits) {
            System.out.println(commit);
        }
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

    public void printCommit() {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + timestamp);
        System.out.println(message);
        System.out.println();
    }
}
