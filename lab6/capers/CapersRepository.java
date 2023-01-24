package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 *
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        CAPERS_FOLDER.mkdir();
        File dogsFolder = new File(CAPERS_FOLDER, "dogs");
        File storyFolder = new File(CAPERS_FOLDER, "story");
        storyFolder.mkdir();
        dogsFolder.mkdir();
        File storyFile = new File(storyFolder, "story.txt");
        try {
            storyFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File storyFolder = new File(CAPERS_FOLDER, "story");
        File storyFile = new File(storyFolder, "story.txt");
        // read existing story
        String story = readContentsAsString(storyFile);
        // concatenate the story
        String newStory = story.concat(text).concat("\n");
        Utils.writeContents(storyFile, newStory);
        System.out.println(newStory);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        File dogFolder = new File(CAPERS_FOLDER, "dogs");
        File dogFile = new File(dogFolder, name);
        writeObject(dogFile, d);
        System.out.println(d);
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        File dogFolder = new File(CAPERS_FOLDER, "dogs");
        Dog d = Dog.fromFile(name);
        if (d == null) { return; }
        d.haveBirthday();
        File dogFile = new File(dogFolder, name);
        writeObject(dogFile, d);
    }

    public static void main(String[] args) {
       //setupPersistence();
        makeDog("pluto", "poodle", 4);
    }
}
