package cache;

import cachestrategy.Strategy;
import cachestrategy.StrategyBuilder;
import cachestrategy.StrategyType;
import utils.fileutils.FileStorage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * File cache implementation
 */
public class FileCache<KeyType extends Serializable, ValueType extends Serializable> extends Cache<KeyType, ValueType> {

    private HashSet<KeyType> cacheIndex = new HashSet<>();
    private FileStorage<KeyType, ValueType> fileStorage;
    private long cacheCapacity;
    private Strategy<KeyType> cacheStrategy;

    /**
     * @param strategyType - cache strategy and replacement algorithm
     * @param fullCacheFileName - path to a file that will be used as a cache storage
     * @param cacheCapacity - cache size
     */
    public FileCache(StrategyType strategyType, String fullCacheFileName, long cacheCapacity) throws Exception {
        if (cacheCapacity <= 0) {
            throw new Exception("Cache capacity should be positive.");
        }
        this.cacheCapacity = cacheCapacity;
        fileStorage = new FileStorage<>(fullCacheFileName);
        this.cacheStrategy = (new StrategyBuilder<KeyType>(strategyType)).createStrategy();
    }

    @Override
    public void put(KeyType key, ValueType data) {
        if(!isFull()) {
            cacheIndex.add(key);
            fileStorage.addValue(key, data);
            cacheStrategy.notifyAdd(key);
        } else {
            remove(cacheStrategy.getReplacingKey());
            put(key, data);
        }
    }

    @Override
    public ValueType get(KeyType key) {
        ValueType value = fileStorage.getValueByKey(key);
        if(value!=null) {
            cacheStrategy.notifyGet(key);
        }
        return value;
    }

    @Override
    public void remove(KeyType key) {
        fileStorage.removeValueByKey(key);
        cacheIndex.remove(key);
        cacheStrategy.notifyRemove(key);
    }

    @Override
    public void clear() {
        fileStorage.removeAllValues();
        cacheIndex.clear();
        cacheStrategy.notifyClear();
    }

    @Override
    public boolean contains(KeyType key) {
        return cacheIndex.contains(key);
    }

    @Override
    public boolean isEmpty() {
        return cacheIndex.isEmpty();
    }

    @Override
    public boolean isFull() {
        return cacheIndex.size()>=cacheCapacity;
    }

    @Override
    public Set<KeyType> keySet() {
        return cacheIndex;
    }

    /**
     * Delete cache storage file
     */
    public void destroy() {
        fileStorage.destroy();
    }
}
