package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import static gitlet.Repository.GITLET_STAGE;

/* Structure inside our .gitlet directory
 *   .gitlet
 *      |--objects
 *      |     |--commit and blob
 *      |--refs
 *      |     |--master
 *      |--HEAD
 *      |--stage
 */
public class Stage implements Serializable {

    HashMap<String, String> untrackedFiles;
    HashMap<String, String> stagedFiles;
    HashMap<String, String> removedFiles;
    HashMap<String, String> modifiedFiles;
    HashMap<String, String> currentCommit;

    /**
     * Returns the existing stage or creates a new one.
     * @return Stage
     */
    public static Stage get() {
        File f = new File(GITLET_STAGE, "current");
        Stage s;
        if (f.exists()) {
            s = Utils.readObject(f, Stage.class);
        }
        else {
            s = new Stage();
            // stagedFiles ? - add() should take care of this
            // removedFiles ? - getRemoved()
            // modifiedFiles ? - getModified()
            // untrackedFiles ? - getUntracked()
            Commit c = Commit.retrieveFromHEAD();
            s.currentCommit = c.getCommittedFiles();
            s.stagedFiles = new HashMap<>();
            s.getUntracked();
            s.getModified();
            s.getRemoved();
        }
        return s;
    }

    /**
     * remove the stage, usually happens after a commit
     */
    public static void removeStage() {
        File f = new File(GITLET_STAGE, "current");
        f.delete();
    }

    /**
     * add a new file to staging
     * @param f
     */
    public static void add(File f) {
        if (!f.exists()) {
            System.out.println("File does not exist.");
        }
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        if (!s.alreadyCommitted(b) && !s.alreadyTracked(b)) {
            s.stagedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.stagedFiles.put(b.getId(), f.getName());
            s.removedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.untrackedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.modifiedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
        } else if (s.alreadyCommitted(b)) {
            // if identical to the version in the current commit
            // remove it from the staging area
            s.stagedFiles.remove(b.getId());
        }
        save(s);
    }

    public static void remove(File f) {
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        //Unstage the file if it is currently staged for addition.
        if (s.alreadyTracked(b)) {
            s.stagedFiles.remove(b.getId());
        } else if (s.alreadyCommitted(b)) {
            s.removedFiles.put(b.getId(), f.getName());
            f.delete();
        } else {
            System.out.println("No reason to remove the file.");
        }
        save(s);
    }

    /**
     * save the stage object in GITLET_STAGE/current
     */
    public static void save(Stage s) {
        File f = new File(GITLET_STAGE, "current");
        Utils.writeObject(f, s);
    }

    /**
     * Get all modified files
     * Modified: files whose names present in currentCommit but the blob ids are different
     * @return
     */
    private void getModified() {
        List<File> files = MyUtils.scandir();
        HashMap<String, String> hashMap = MyUtils.generateHashMap(files);
        this.modifiedFiles = new HashMap<String, String>();
    }

    /**
     * Get all untracked files
     * Untracked: files that are NOT present in currentCommit or StagedFiles but in the directory
     *            OR files that are in currentCommit but in removedFiles
     * @return
     */
    private void getUntracked() {
        List<File> files = MyUtils.scandir();
        HashMap<String, String> hashMap = MyUtils.generateHashMap(files);
        this.untrackedFiles = MyUtils.compareMap(hashMap, currentCommit, stagedFiles);
        //TODO: files that are in currentCommit but also in removedFiles
    }

    private void getRemoved() {
        //TODO: how to deal with this?
        this.removedFiles = new HashMap<String, String>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Untracked Files: ");
        sb.append(System.getProperty("line.separator"));
        sb.append(untrackedFiles.toString());
        sb.append(System.getProperty("line.separator"));
        sb.append("Staged Files: ");
        sb.append(System.getProperty("line.separator"));
        sb.append(stagedFiles.toString());
        sb.append(System.getProperty("line.separator"));
        sb.append("Modified Files: ");
        sb.append(System.getProperty("line.separator"));
        sb.append(modifiedFiles.toString());
        sb.append(System.getProperty("line.separator"));
        sb.append("Removed Files: ");
        sb.append(System.getProperty("line.separator"));
        sb.append(removedFiles.toString());
        return sb.toString();
    }
    /**
     * check if the Blob is in the current commit
     * @param b
     * @return
     */
    private boolean alreadyCommitted(Blob b) {
        return currentCommit.containsKey(b.getId());
    }

    private boolean alreadyTracked(Blob b) {
        return stagedFiles.containsKey(b.getId());
    }



}
