package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blob implements Serializable {
    private String filename;
    private String filepath;
    private byte[] bytes;
    private String id;

    public Blob(String filename, String filepath, byte[] bytes) {
        this.filename = filename;
        this.filepath = filepath;
        this.bytes = bytes;
        this.id = "";
    }

    public static Blob retrive(String id) {
        return null;
    }

    /**
     * Generate a Blob for the given file
     * @param f the file to generate Blob from
     * @return the Blob
     */
    public static Blob generateBlob(File f) {
        if (!f.exists()) {
            throw new GitletException("File does not exist.");
        }
        byte[] bytes = Utils.serialize(f);
        Blob b = new Blob(f.getName(),f.getPath(), bytes);
        b.id = b.generateBlobId();
        return b;
    }

    public static List<Blob> generateBlobs(List<File> files) {
        List<Blob> blobs = new ArrayList<>();
        for (File f : files) {
            blobs.add(generateBlob(f));
        }
        return blobs;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return this.filename;
    }

    public String generateBlobId() {
        return Utils.sha1("B" + this.filename + Arrays.toString(this.bytes));
    }
}


