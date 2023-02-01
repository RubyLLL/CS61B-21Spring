package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new GitletException("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                if (args.length != 2) {
                    throw new GitletException("Usage: java gitlet.Main add [file name].");
                }
                Repository.add(args[1]);
                break;
            case "rm":
                if (args.length != 2) {
                    throw new GitletException("Usage: java gitlet.Main remove [file name].");
                }
                Repository.remove(args[1]);
                break;
            case "commit":
                if (args.length != 2) {
                    throw new GitletException("Please enter a commit message.");
                }
                String message = args[1];
                Repository.commit(message, "master");
                break;
            case "branch":
                if (args.length != 2) {
                    throw new GitletException("Usage: java gitlet.Main branch [branch name].");
                }
                String branch = args[1];
                Repository.branch(branch);
                break;
            case "rm-branch":
                if (args.length != 2) {
                    throw new GitletException("Usage: java gitlet.Main rm-branch [branchname].");
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
                }
                break;
            default:
                throw new GitletException("No command with that name exists.");
            // TODO: FILL THE REST IN
        }
    }
}
