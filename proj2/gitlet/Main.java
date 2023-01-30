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
                String fileName = args[1];
                Repository.add(fileName);
                // TODO: handle the `add [filename]` command
                break;
            default:
                throw new GitletException("No command with that name exists.");
            // TODO: FILL THE REST IN
        }
    }
}
