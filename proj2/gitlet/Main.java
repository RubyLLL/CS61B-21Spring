package gitlet;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (!firstArg.equals("init") && !GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                if (args.length != 2) {
                    System.out.println("Usage: java gitlet.Main add [file name].");
                    break;
                }
                Repository.add(args[1]);
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Usage: java gitlet.Main remove [file name].");
                    break;
                }
                Repository.remove(args[1]);
                break;
            case "commit":
                if (args.length != 2) {
                    System.out.println("Please enter a commit message.");
                    break;
                }
                String message = args[1];
                Repository.commit(message, "master");
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Usage: java gitlet.Main branch [branch name].");
                    break;
                }
                String branch = args[1];
                Repository.branch(branch);
                break;
            case "rm-branch":
                if (args.length != 2) {
                    System.out.println("Usage: java gitlet.Main rm-branch [branchname].");
                    break;
                }
                branch = args[1];
                Repository.removeBranch(branch);
                break;
            case "checkout":
                String commands = args[1];
                if (args.length == 3) {
                    Repository.checkout(args[2], "HEAD");
                } else if (args.length == 4) {
                    Repository.checkout(args[3], args[1]);
                } else if (args.length == 2) {
                    break;
                }
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                String target = args[1];
                Repository.find(target);
                break;
            case "status":
                Repository.status();
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
