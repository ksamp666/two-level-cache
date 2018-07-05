package utils;

import java.io.*;
import java.util.Base64;

/**
 * This class is used to serialize and deserialize objects to/from string.
 */
public final class Serializer {

    private Serializer() {
    }

    /**
     * Deserializes an object using Base64 encoding
     * @param s - serialized object string representation
     * @return deserialized object
     */
    public static Object deserializeObject(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Serializes an object using Base64 encoding
     * @param o - object to serialize
     * @return serialized object string representation
     */
    public static String serializeObject(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
