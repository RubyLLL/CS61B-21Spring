package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.join;

public class Branch implements Serializable {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_COMMIT = join(GITLET_DIR, "commits");

    String name;
    CommitTree commits;

    Branch(String name, CommitTree commits){
        this.name = name;
        this.commits = commits;
        if (!branchExists(name)) {
            newBranch(name);
        }
    }

    public static boolean branchExists(String name){
        File[] files = GITLET_COMMIT.listFiles(File::isFile);
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void newBranch(String name){
        if (branchExists(name)) {
            throw new GitletException("A branch with that name already exists.");
        }
        File newBranch = new File(GITLET_DIR, name);
        newBranch.mkdir();
        // TODO: should contain the latest commit from the currentWorkingBranch
        Branch b = new Branch(name, new CommitTree(name, new HashMap<>()));
        Utils.writeObject(newBranch, b);
    }

    /**
     * The most recently updated folder would be our current working branch.
     * @return
     */
    public static File getCurrentBranch() {
        File[] files = GITLET_COMMIT.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File branch = null;

        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    branch = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }
        return branch;
    }
}
