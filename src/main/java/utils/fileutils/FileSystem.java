package utils.fileutils;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some useful methods to work with file system
 */
public final class FileSystem {

    private FileSystem() {
    }

    /**
     * Creates a file by specified path
     * @param fullFileName full file path
     * @return created file
     */
    public static File createFile(String fullFileName) {
        if (isFileExist(fullFileName)) {
            deleteFile(fullFileName);
        }
        File file = new File(fullFileName);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch(Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to create a file: " + fullFileName, e);
        }
        return file;
    }

    /**
     * Deletes a file by its path
     * @param fullFileName file path to delete
     */
    public static void deleteFile(String fullFileName) {
        File file = new File(fullFileName);
        file.delete();
    }

    /**
     * Deletes a folder by its path
     * @param pathToFolder folder path to delete
     */
    public static void deleteFolder(String pathToFolder) {
        try {
            File tempFolder = new File(pathToFolder);
            org.apache.commons.io.FileUtils.deleteDirectory(tempFolder);
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to delete folder: " + pathToFolder, e);
        }
    }

    /**
     * Check if specified file exists in file system
     * @param filePath file path to check
     */
    public static boolean isFileExist(String filePath) {
        File f = new File(filePath);
        return (f.exists() && !f.isDirectory());
    }
}
