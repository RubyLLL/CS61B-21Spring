package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gitlet.Repository.CWD;
public class MyUtils {

    /**
     * Takes more than one HashMap, returns a new HashMap containing items that are only present in the first HashMap
     * @param hashMaps
     * @return
     */
    @SafeVarargs
    public static HashMap<String, String> compareMap(HashMap<String, String>... hashMaps) {
        if (hashMaps.length == 1) {
            return hashMaps[0];
        }
        else {
            HashMap<String, String> a = new HashMap<>(hashMaps[0]);
            HashMap<String, String> result = new HashMap<String, String>();
            for (int i = 1; i < hashMaps.length; i++) {
                HashMap<String,String> b = hashMaps[i];
                for (String key : b.keySet()) {
                    a.remove(key);
                }
            }
            return a;
        }

    }

    /**
     * Generate HashMap
     * Key: Blob.id, Value: filename
     * BlobID: Utils.sha1("B" + this.filename + Arrays.toString(this.bytes))
     * @param files
     * @return
     */
    public static HashMap<String, String> generateHashMap(List<File> files) {
        List<Blob> blobs = Blob.generateBlobs(files);
        HashMap<String, String> hashMap = new HashMap<>();
        for (Blob blob : blobs) {
            hashMap.put(blob.getFilename(), blob.generateBlobId());
        }
        return hashMap;
    }

    /**
     * Scans the working directory and returns *.txt
     * @return
     */
    public static List<File> scandir() {
        List<String> filenames = Utils.plainFilenamesIn(CWD);
        List<File> files = new ArrayList<File>();
        if (filenames != null) {
            for (String s: filenames) {
                if (s.split("\\.").length > 1 && s.split("\\.")[1].equals("txt")) {
                    files.add(new File(CWD, s));
                }
            }
        }
        return files;
    }

    /**
     * Clear the directory f, including all files and nested directories
     * @param f the directory to be cleaned
     */
    public static void cleanDirectory(File f) {
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                    file.delete();
                } else {file.delete();}
            }
        }
    }

    /**
     * Clear the files of the given type from the directory f
     * @param f the directory to be cleaned
     * @param type the type of files to be cleaned
     */
    public static void cleanDirectory(File f, String type) {
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith("."+type)) {
                    file.delete();
                }
            }
        }
    }
}