package cachestrategy;

import java.util.List;

/**
 * Cache strategy interface
 */
public interface Strategy<KeyType> {
    /**
     * Returns cache object key which should be deleted according to cache strategy to free a place for a new object
     */
    KeyType getReplacingKey();

    /**
     * This method is used to notify cache strategy about adding new element to cache
     */
    void notifyAdd(KeyType key);

    /**
     * This method is used to notify cache strategy about removing and element from cache
     */
    void notifyRemove(KeyType key);

    /**
     * This method is used to notify cache strategy about getting element from cache
     */
    void notifyGet(KeyType key);

    /**
     * This method is used to notify cache strategy about clearing cache
     */
    void notifyClear();

    /**
     * Return cache elements keys list, representing the correct elements order in cache
     */
    List<KeyType> getElementsOrderList();
}
