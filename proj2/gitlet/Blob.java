package gitlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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


    // ----------------------------------------------------------------
    // Blob Operations (get/save/remove/copy)
    // ----------------------------------------------------------------
    /**
     * get the blob from the specified directory
     * @param id
     * @param from
     * @return
     */
    public static Blob get(String id, File from) {
        File f = new File(from, id);
        return Utils.readObject(f, Blob.class);
    }

    /**
     * Save the blob to the specified directory
     * @param b
     */
    public static void save(Blob b, File to) {
        if (!to.exists()) {
            to.mkdirs();
        }
        File f = new File(to, b.id);
        Utils.writeObject(f, b);
    }

    /**
     * Remove the blob from the specified directory
     */
    public static void remove(Blob b, File from) {
        File f = new File(from, b.id);
        f.delete();
    }

    /**
     * Copy blobs from folder to folder
     * @param from the source directory
     * @param to the destination directory
     */
    public static void copy(File from, File to) {
        if (!to.exists()) {
            to.mkdir();
        }
        File[] files = from.listFiles(File::isFile);
        if (files != null) {
            for (File f: files) {
                Blob.save(Blob.get(f.getName(), from), to);
            }
        }
    }

    /**
     * Copy blobs in HashMap from folder to folder
     * @param hashMap blobs to be copied
     * @param from the source directory
     * @param to the destination directory
     */
    public static void copy(HashMap<String, String> hashMap, File from, File to) {
        for (String blobId : hashMap.values()) {
            Blob.save(Blob.get(blobId, from), to);
        }
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
        byte[] bytes = Utils.readContents(f);
        Blob b = new Blob(f.getName(),f.getPath(), bytes);
        b.id = b.generateBlobId();
        return b;
    }


    // ----------------------------------------------------------------
    //             Blob Generator & Setters and Getters
    // ----------------------------------------------------------------

    public static List<Blob> generateBlobs(List<File> files) {
        List<Blob> blobs = new ArrayList<>();
        for (File f : files) {
            blobs.add(generateBlob(f));
        }
        return blobs;
    }

    /**
     * Recover the file from the Blob, save to the given filepath
     * if the file exists, the content would be overwritten
     * @return
     */
    public void toFile(File filepath) {
        String content = new String(bytes);
        filepath.delete();
        try {
            filepath.createNewFile();
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateBlobId() {
        return Utils.sha1("B" + this.filename + Arrays.toString(this.bytes));
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

    public byte[] getBytes() {
        return this.bytes;
    }
}


