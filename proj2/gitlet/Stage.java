package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gitlet.Repository.*;

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

    public Stage() {
        untrackedFiles = new HashMap<String, String>();
        stagedFiles = new HashMap<String, String>();
        removedFiles = new HashMap<String, String>();
        modifiedFiles = new HashMap<String, String>();
        currentCommit = new HashMap<String, String>();
    }

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
        }
        s.update();
        return s;
    }

    /**
     * add a new file to staging
     * @param f
     */
    public static void add(File f) {
        if (!f.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        if (!s.alreadyCommitted(b)
                && !s.alreadyStaged(b)) {
            Blob.save(b, STAGE_BLOBS);
            s.stagedFiles.put(f.getName(), b.getId());
        } else if (s.alreadyCommitted(b)) {
            Blob.remove(b, STAGE_BLOBS);
            s.stagedFiles.remove(f.getName());
        }
        s.removedFiles.remove(f.getName());
        s.untrackedFiles.remove(f.getName());
        s.modifiedFiles.remove(f.getName());
        save(s);
    }

    /**
     * Remove File f from the current stage
     * @param f
     */
    public static void remove(File f) {
        if (!f.exists()) {
            return;
        }
        Stage s = get();
        Blob b = Blob.generateBlob(f);
        if (s.alreadyStaged(b)) {
            // Unstage the file if it is currently staged for addition.
            s.stagedFiles.remove(f.getName());
            Blob.remove(b, STAGE_BLOBS);
        } else if (s.alreadyCommitted(b)) {
            // If the file is tracked in the current commit, stage it for removal
            // and remove the file from the working directory
            s.removedFiles.put(f.getName(), b.getId());
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

    public static void status() {
        Stage s = get();
        Commit c = Commit.get();
        File[] branches = GITLET_REFS.listFiles(File::isDirectory);
        System.out.println("=== Branches ===");
        System.out.println("*" + c.getBranch());
        for (File branch : branches) {
            if (!branch.getName().equals(c.getBranch())) {
                System.out.println(branch.getName());
            }
        }
        System.out.println("");

        System.out.println("=== Staged Files ===");
        for (String filename: s.getStagedFiles().keySet()) {
            System.out.println(filename);
        }
        System.out.println("");

        System.out.println("=== Removed Files ===");
        for (String filename: s.getRemovedFiles().keySet()) {
            System.out.println(filename);
        }
        System.out.println("");

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String filename: s.getModifiedFiles().keySet()) {
            if (filename.startsWith("D")) {
                System.out.println(filename.substring(1) + " (deleted)");
            } else if (filename.startsWith("M")) {
                System.out.println(filename.substring(1) + " (modified)");
            }
        }
        System.out.println("");

        System.out.println("=== Untracked Files ===");
        for (String filename: s.getUntrackedFiles().keySet()) {
            System.out.println(filename);
        }
        System.out.println("");
    }

    public void update() {
        Commit c = Commit.get();
        currentCommit = c != null ?
                c.getCommittedFiles() :
                new HashMap<String, String>();
        setUntracked();
        setModified();
    }


    // ----------------------------------------------------------------
    //                       Stage Status
    // ----------------------------------------------------------------

    /**
     * check if the Blob is in the current commit
     * @param b
     * @return true if the Blob is already committed
     */
    private boolean alreadyCommitted(Blob b) {
        return currentCommit.containsValue(b.getId());
    }

    /**
     * check if the Blob has already staged
     * @param b
     * @return true if the Blob has already staged
     */
    private boolean alreadyStaged(Blob b) {
        return stagedFiles.containsValue(b.getId());
    }


    /**
     * Return true if no changes since the last commit
     * @return
     */
    public boolean noChange() {
        return stagedFiles.isEmpty()
                && modifiedFiles.isEmpty()
                && removedFiles.isEmpty();
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

    public HashMap<String, String> getModifiedFiles() {
        return this.modifiedFiles;
    }

    public HashMap<String, String> getUntrackedFiles() {
        return this.untrackedFiles;
    }

    /**
     * Get all modified files
     * Modified: files whose names present in currentCommit
     * but the blob ids are different and not in stagedFiles
     * @return
     */
    private void setModified() {

        // Tracked in the current commit, changed in the working directory, but not staged; or
        // Staged for addition, but with different contents than in the working directory; or
        List<File> files = MyUtils.scandir();
        HashMap<String, String> hashMap = MyUtils.generateHashMap(files);
        for (Map.Entry<String, String> entry: hashMap.entrySet()) {
            if (committedButChanged(entry) || stagedButChanged(entry)) {
                modifiedFiles.put("M" + entry.getKey(), entry.getValue());
            }
        }

        // Staged for addition, but deleted in the working directory
        // TODO: test22-remove-deleted-file
        HashMap<String, String> stagedButDeleted = MyUtils.compareMap(stagedFiles, hashMap);
        for (Map.Entry<String, String> entry: stagedButDeleted.entrySet()) {
            modifiedFiles.put("D" + entry.getKey(), entry.getValue());
        }

        // Not staged for removal, but tracked in the current commit and deleted from the working directory.
        HashMap<String, String> diffFromCommit = MyUtils.compareMap(currentCommit, hashMap);
        for (Map.Entry<String, String> entry : diffFromCommit.entrySet()) {
            if (!removedFiles.containsKey(entry.getKey())) {
                modifiedFiles.put("D" + entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Get all untracked files
     * Untracked: files that are NOT present in currentCommit or StagedFiles but in the directory
     * @return
     */
    private void setUntracked() {
        List<File> files = MyUtils.scandir();
        HashMap<String, String> hashMap = MyUtils.generateHashMap(files);
        untrackedFiles = MyUtils.compareMap(hashMap, currentCommit, stagedFiles);
    }


    private boolean committedButChanged(Map.Entry<String, String> entry) {
        return currentCommit.containsKey(entry.getKey())
                && !currentCommit.containsValue(entry.getValue());
    }

    private boolean stagedButChanged(Map.Entry<String, String> entry) {
        return stagedFiles.containsKey(entry.getKey())
                && !stagedFiles.containsValue(entry.getValue());
    }

    private void setRemoved() {
        //TODO: how to deal with this?
        removedFiles = removedFiles != null ?
                removedFiles :
                new HashMap<String, String>();
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
