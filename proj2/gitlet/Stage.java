package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static gitlet.Repository.GITLET_STAGE;
import static gitlet.Repository.STAGE_BLOBS;

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
public class Stage implements Serializable {

    private HashMap<String, String> untrackedFiles;
    private HashMap<String, String> stagedFiles;
    private HashMap<String, String> removedFiles;
    private HashMap<String, String> modifiedFiles;
    private HashMap<String, String> currentCommit;

    //----------------------------------------------------------------
    //          Stage Operations (get/add/save/remove)
    //----------------------------------------------------------------
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
            Commit c = Commit.get();
            s.currentCommit = c != null ?
                    c.getCommittedFiles() :
                    new HashMap<String, String>();
            s.stagedFiles = new HashMap<>();
            s.setUntracked();
            s.setModified();
            s.setRemoved();
        }
        return s;
    }

    /**
     * add a new file to staging
     * @param f
     */
    public static void add(File f) {
        //TODO: we are not saving the blobs
        if (!f.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        if (!s.alreadyCommitted(b) && !s.alreadyStaged(b)) {
            Blob.save(b, STAGE_BLOBS);
            s.stagedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.stagedFiles.put(b.getId(), f.getName());
            s.removedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.untrackedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
            s.modifiedFiles.entrySet().removeIf(entry -> entry.getValue().equals(f.getName()));
        } else if (s.alreadyCommitted(b)) {
            // if identical to the version in the current commit
            // remove it from the staging area
            s.stagedFiles.remove(b.getId());
            Blob.remove(b, STAGE_BLOBS);
        }
        save(s);
    }

    /**
     * Remove File f from the current stage
     * @param f
     */
    public static void remove(File f) {
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        if (s.alreadyStaged(b)) {
            s.stagedFiles.remove(b.getId());
            Blob.remove(b, STAGE_BLOBS);
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
     * remove the stage, usually happens after a commit
     */
    public static void removeStage() {
        for (File f: Objects.requireNonNull(STAGE_BLOBS.listFiles())) {
            f.delete();
        }
        File f = new File(GITLET_STAGE, "current");
        f.delete();
    }


    // ----------------------------------------------------------------
    //                       Stage Status
    // ----------------------------------------------------------------
    /**
     * check if the Blob is in the current commit
     * @param b
     * @return
     */
    private boolean alreadyCommitted(Blob b) {
        return currentCommit.containsKey(b.getId());
    }

    private boolean alreadyStaged(Blob b) {
        return stagedFiles.containsKey(b.getId());
    }

    /**
     * Return true if no changes since the last commit
     * @return
     */
    public boolean noChange() {
        return stagedFiles.isEmpty() && modifiedFiles.isEmpty() && removedFiles.isEmpty();
    }


    //----------------------------------------------------------------
    //                     Setters and Getters
    //----------------------------------------------------------------
    /**
     * Returns the staged files for the current stage
     * @return stagedFiles
     */
    public HashMap<String, String> getStagedFiles() {
        return this.stagedFiles;
    }

    /**
     * Returns the removed files for the current stage
     * @return
     */
    public HashMap<String, String> getRemovedFiles() {
        return this.removedFiles;
    }

    /**
     * Get all modified files
     * Modified: files whose names present in currentCommit but the blob ids are different
     * @return
     */
    private void setModified() {
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
    private void setUntracked() {
        List<File> files = MyUtils.scandir();
        HashMap<String, String> hashMap = MyUtils.generateHashMap(files);
        this.untrackedFiles = MyUtils.compareMap(hashMap, currentCommit, stagedFiles);
        //TODO: files that are in currentCommit but also in removedFiles
    }

    private void setRemoved() {
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



}
