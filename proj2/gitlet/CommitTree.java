package gitlet;

import java.util.HashMap;

public class CommitTree {
    String head;
    HashMap<String,Commit> commits;

    CommitTree(String head, HashMap<String,Commit> commits) {
        this.head = head;
        this.commits = commits;
    }
}
