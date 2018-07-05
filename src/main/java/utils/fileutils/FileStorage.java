package utils.fileutils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.fileutils.FileSystem.createFile;
import static utils.fileutils.FileSystem.deleteFile;
import static utils.Serializer.deserializeObject;
import static utils.Serializer.serializeObject;

/**
 * This class implements file storage
 */
public class FileStorage<KeyType extends Serializable, ValueType extends Serializable> {

    private String storageFilePath;

    /**
     * File storage constructor
     * @param fullStorageFileName - full path to storage file
     */
    public FileStorage(String fullStorageFileName) {
        storageFilePath = fullStorageFileName;
        createFile(storageFilePath);
    }

    /**
     * Returns previously stored value by its key
     * @param key - a key to search for a value
     * @return previousle stored value found by its key
     */
    public ValueType getValueByKey(KeyType key) {
        ValueType value = null;

        try {
            String keyString = serializeObject(key);
            String valueString = getStringValueForKey(keyString);
            if (valueString != null) {
                value = (ValueType) deserializeObject(valueString);
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to get value by Key: " + storageFilePath, e);
        }

        return value;
    }

    /**
     * Removes previously stored value from storage by its key
     * @param key - a key to search for a value to be removed
     */
    public void removeValueByKey(KeyType key) {
        try {
            String keyString = serializeObject(key);
            (new TextFile(storageFilePath)).removeLine(getFileLineFromKeyAndValue(keyString, getStringValueForKey(keyString)));
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to remove value by Key: " + storageFilePath, e);
        }
    }

    /**
     * Adds a new value to file storage
     * @param key - new value key
     * @param value - new value data
     */
    public void addValue(KeyType key, ValueType value) {
        try {
            if (getValueByKey(key) != null) {
                removeValueByKey(key);
            }
            (new TextFile(storageFilePath)).addLine(getFileLineFromKeyAndValue(serializeObject(key), serializeObject(value)));
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to add value to file: " + storageFilePath, e);
        }
    }

    /**
     * Clears file storage
     */
    public void removeAllValues() {
        (new TextFile(storageFilePath)).clear();
    }

    /**
     * Returns all stored values keys
     * @return previousle stored values keys
     */
    public Set<KeyType> getKeys() {
        Set<KeyType> keys = new HashSet<>();

        try {
            TextFile textFile = new TextFile(storageFilePath);
            textFile.initLineReader();
            String currentLine;
            while ((currentLine = textFile.getNextLine()) != null) {
                keys.add((KeyType) deserializeObject(currentLine.substring(0, currentLine.indexOf("*"))));
            }
            textFile.closeLineReader();
        } catch(Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.WARNING, "Failed to get keys from a file: " + storageFilePath, e);
        }

        return keys;
    }

    /**
     * Deletes storage file
     */
    public void destroy() {
        deleteFile(storageFilePath);
    }

    /**
     * Returns serialized value for serialized key
     * @param key serialized key
     * @return serialized value data
     */
    private String getStringValueForKey(String key) {

        String value = null;

        TextFile textFile = new TextFile(storageFilePath);
        textFile.initLineReader();
        String currentLine;
        while ((currentLine = textFile.getNextLine()) != null) {
            if (currentLine.startsWith(key)) {
                value = currentLine.substring(key.length() + 1);
            }
        }
        textFile.closeLineReader();

        return value;
    }


    /**
     * Generates a string to store to file
     * @param key serialized key
     * @param value serialized value
     * @return data string to store to a file
     */
    private String getFileLineFromKeyAndValue(String key, String value) {
        return key + "*" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!FileStorage.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final FileStorage objectToCompareWith = (FileStorage) obj;

        if(!storageFilePath.equals(objectToCompareWith.storageFilePath)) {
            return false;
        }
        File curObjectFile = new File(this.storageFilePath);
        File otherObjectFile = new File(objectToCompareWith.storageFilePath);
        if(!curObjectFile.equals(otherObjectFile)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + storageFilePath.hashCode();
        result = 31 * result + (new File(storageFilePath)).hashCode();
        return result;
    }
}
