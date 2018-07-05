package cache;

import cachestrategy.Strategy;
import cachestrategy.StrategyBuilder;
import cachestrategy.StrategyType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Two-level cache implementation
 */
public class TwoLevelCache<KeyType extends Serializable, ValueType extends Serializable> extends Cache<KeyType, ValueType> {
    private MemoryCache<KeyType, ValueType> memoryCache;
    private FileCache<KeyType, ValueType> fileCache;
    private Strategy<KeyType> cacheStrategy;

    /**
     * @param cacheStrategy - cache strategy and replacement algorithm
     * @param memoryCacheCapacity - memory cache size
     * @param fileCacheCapacity - file cache size
     * @param fileCacheFilePath - path to a file that will be used as a cache storage for file cache
     */
    public TwoLevelCache(StrategyType cacheStrategy, long memoryCacheCapacity, long fileCacheCapacity, String fileCacheFilePath) throws Exception {
        memoryCache = new MemoryCache<>(cacheStrategy, memoryCacheCapacity);
        fileCache = new FileCache<>(cacheStrategy, fileCacheFilePath, fileCacheCapacity);
        this.cacheStrategy = (new StrategyBuilder<KeyType>(cacheStrategy)).createStrategy();
    }

    @Override
    public void put(KeyType key, ValueType data) {
        if(!memoryCache.contains(key)) {
            if(!memoryCache.isFull()) {
                putToMemoryCache(key, data);
            } else if(!fileCache.contains(key)) {
                if(!fileCache.isFull()) {
                    putToFileCache(key, data);
                } else {
                    KeyType keyToReplace = cacheStrategy.getReplacingKey();
                    remove(keyToReplace);
                    put(key, data);
                }
            }
        }
    }

    /**
     * Stores data to memory cache
     * @param key - data key
     * @param data - data value
     */
    private void putToMemoryCache(KeyType key, ValueType data) {
        memoryCache.put(key, data);
        cacheStrategy.notifyAdd(key);
        updateObjectsPositionInCache();
        if(fileCache.contains(key)) {
            fileCache.remove(key);
        }
    }

    /**
     * Stores data to file cache
     * @param key - data key
     * @param data - data value
     */
    private void putToFileCache(KeyType key, ValueType data) {
        fileCache.put(key, data);
        cacheStrategy.notifyAdd(key);
        updateObjectsPositionInCache();
    }

    @Override
    public ValueType get(KeyType key) {
        ValueType value = null;

        if(memoryCache.contains(key)) {
            value = memoryCache.get(key);
            cacheStrategy.notifyGet(key);
            updateObjectsPositionInCache();
        } else if(fileCache.contains(key)) {
            value = fileCache.get(key);
            cacheStrategy.notifyGet(key);
            updateObjectsPositionInCache();
        }

        return value;
    }

    @Override
    public void remove(KeyType key) {
        if(memoryCache.contains(key)) {
            memoryCache.remove(key);
            cacheStrategy.notifyRemove(key);
            updateObjectsPositionInCache();
        } else if(fileCache.contains(key)) {
            fileCache.remove(key);
            cacheStrategy.notifyRemove(key);
            updateObjectsPositionInCache();
        }
    }

    @Override
    public void clear() {
        memoryCache.clear();
        fileCache.clear();
        cacheStrategy.notifyClear();
        updateObjectsPositionInCache();
    }

    @Override
    public boolean contains(KeyType key) {
        return memoryCache.contains(key) || fileCache.contains(key);
    }

    @Override
    public boolean isEmpty() {
        return memoryCache.isEmpty() && fileCache.isEmpty();
    }

    @Override
    public boolean isFull() {
        return memoryCache.isFull() && fileCache.isFull();
    }

    @Override
    public Set<KeyType> keySet() {
        Set<KeyType> combinedKeySet = new HashSet<>();
        combinedKeySet.addAll(memoryCache.keySet());
        combinedKeySet.addAll(fileCache.keySet());
        return combinedKeySet;
    }

    /**
     * Delete file cache storage file
     */
    public void destroy() {
        fileCache.destroy();
    }

    /**
     * Moves objects between memory and file cache according to cache strategy
     */
    private void updateObjectsPositionInCache() {
        for(int i = cacheStrategy.getElementsOrderList().size(); i>0; i--) {
            KeyType strategyKey = cacheStrategy.getElementsOrderList().get(i-1);
            if(fileCache.contains(strategyKey)) {
                if(!memoryCache.isFull()) {
                    memoryCache.put(strategyKey, fileCache.get(strategyKey));
                    fileCache.remove(strategyKey);
                } else {
                    for(int j=0; j<i-1; j++) {
                        KeyType replaceKey = cacheStrategy.getElementsOrderList().get(j);
                        if(memoryCache.contains(replaceKey)) {
                            ValueType replaceValue = memoryCache.get(replaceKey);
                            memoryCache.remove(replaceKey);
                            memoryCache.put(strategyKey, fileCache.get(strategyKey));
                            fileCache.remove(strategyKey);
                            fileCache.put(replaceKey, replaceValue);
                            break;
                        }
                    }
                }
            }
        }
    }
}
