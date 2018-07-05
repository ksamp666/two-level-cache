package utils.fileutils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.fileutils.FileSystem.createFile;
import static utils.fileutils.FileSystem.deleteFile;

/**
 * Methods to work with text files
 */
public class TextFile {

    private String inputFilePath;

    private FileReader fileLinesFileReader;
    private BufferedReader fileLinesBufferedReader;

    /**
     * Text file worker constructor
     * @param inputFilePath - path to a file to work with
     */
    public TextFile(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    /**
     * Removes a line from file
     * @param lineToRemove - line contents to remove
     */
    public void removeLine(String lineToRemove) {
        try {
            File inputFile = new File(inputFilePath);
            File tempFile = new File(inputFilePath + "_temp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(lineToRemove)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            deleteFile(inputFilePath);
            tempFile.renameTo(inputFile);
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to remove line from file: " + inputFilePath, e);
        }
    }

    /**
     * Adds line to the end of the file
     * @param line - line text to add
     */
    public void addLine(String line) {
        try {
            FileWriter fileWriter = new FileWriter(inputFilePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(line);

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to write to file: " + inputFilePath, e);
        }
    }

    /**
     * Deletes all file contents (not deletes the file itself)
     */
    public void clear() {
        try {
            File file = new File(inputFilePath);
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to clear the file: " + inputFilePath, e);
        }
    }

    /**
     * Initialize file reader (to read the next line use getNextLine() method)
     */
    public void initLineReader() {
        try {
            fileLinesFileReader = new FileReader(inputFilePath);
            fileLinesBufferedReader = new BufferedReader(fileLinesFileReader);
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to read from file: " + inputFilePath, e);
        }
    }

    /**
     * Reads the next line
     */
    public String getNextLine() {
        if(fileLinesFileReader == null || fileLinesBufferedReader ==null) {
            initLineReader();
        }
        String newLine = null;
        try {
            newLine = fileLinesBufferedReader.readLine();
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to read next line in file: " + inputFilePath, e);
        }
        return newLine;
    }

    /**
     * Closes file reader
     */
    public void closeLineReader() {
        try {
            if (fileLinesBufferedReader != null) {
                fileLinesBufferedReader.close();
            }
            if (fileLinesFileReader != null) {
                fileLinesFileReader.close();
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to close file reader for file: " + inputFilePath, e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!TextFile.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final TextFile objectToCompareWith = (TextFile) obj;

        if(!inputFilePath.equals(objectToCompareWith.inputFilePath)) {
            return false;
        }
        File curObjectFile = new File(this.inputFilePath);
        File otherObjectFile = new File(objectToCompareWith.inputFilePath);
        if(!curObjectFile.equals(otherObjectFile)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + inputFilePath.hashCode();
        result = 31 * result + (new File(inputFilePath)).hashCode();
        return result;
    }
}
