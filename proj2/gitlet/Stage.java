package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.join;

public class Stage implements Serializable {

    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_STAGE = join(GITLET_DIR, "stage");

    HashMap<String, String> untrackedFiles;
    HashMap<String, String> stagedFiles;
    HashMap<String, String> removedFiles;
    HashMap<String, String> modifiedFiles;
    HashMap<String, String> deletedFiles;

    Stage() {
        untrackedFiles = new HashMap<String, String>();
        stagedFiles = new HashMap<String, String>();
    }
    /**
     * Returns the existing stage or creates a new one.
     * @return Stage
     */
    public static Stage stage() {
        File f = new File(GITLET_STAGE, "current");
        if (!f.exists()) {
            // TODO: untracked files should include everything in the folder but not in the previous commit
            return new Stage();
        }
    }

    public void addFile(File file) {
        String filename = file.getName();
        String sha1 = Utils.sha1(file);
        stage.put(filename, sha1);
    }

    public void removeFile(File file) {
        String filename = file.getName();
        stage.remove(filename);
    }

    public void serialize() {
        File f = new File(GITLET_STAGE, "current");
        Utils.writeObject(f, this);
    }

    public Stage deserialize() {
        File f = new File(GITLET_STAGE, "current");
        try {
            Stage s = Utils.readObject(f, Stage.class);
            return s;
        } catch (Exception e) {
            throw new GitletException(e.getMessage());
        }
    }
}
