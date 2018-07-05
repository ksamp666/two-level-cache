package cache;

import java.util.*;

/**
 * Abstract cache class
 */
public abstract class Cache<KeyType, ValueType> {

    private KeyType currentElementKey = null;

    /**
     * Insert object to cache
     * @param key - Object key
     * @param data - Object data
     */
    public abstract void put(KeyType key, ValueType data) throws Exception;

    /**
     * Get object from cache
     * @param key - Object key to search for an object
     * @return Object found by key
     */
    public abstract ValueType get(KeyType key);

    /**
     * Remove object by key
     * @param key - Object key
     */
    public abstract void remove(KeyType key);

    /**
     * Clear cache
     */
    public abstract void clear();

    /**
     * Check if cache contains object
     * @param key - Object key
     * @return True if cache contains object with key
     */
    public abstract boolean contains(KeyType key);

    /**
     * Check if cache empty
     * @return True if cache is empty
     */
    public abstract boolean isEmpty();

    /**
     * Check if cache full
     * @return True if cache is full
     */
    public abstract boolean isFull();

    /**
     * Get cache keys set
     * @return Cache keys set
     */
    public abstract Set<KeyType> keySet();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Cache.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Cache objectToCompareWith = (Cache) obj;

        if(!this.keySet().equals(objectToCompareWith.keySet())) {
            return false;
        }
        for (KeyType key: this.keySet()) {
            if(!this.get(key).equals(objectToCompareWith.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (KeyType key: this.keySet()) {
            ValueType value = this.get(key);
            result = 31 * result + key.hashCode();
            result = 31 * result + value.hashCode();
        }
        return result;
    }
}
