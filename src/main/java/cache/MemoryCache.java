package cache;

import cachestrategy.Strategy;
import cachestrategy.StrategyBuilder;
import cachestrategy.StrategyType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memory cache implementation
 */
public class MemoryCache<KeyType, ValueType> extends Cache<KeyType, ValueType> {

    private volatile Map<KeyType, ValueType> cacheStorage = new ConcurrentHashMap<>();
    private long cacheCapacity;
    private Strategy<KeyType> cacheStrategy;

    /**
     * @param strategyType - cache strategy and replacement algorithm
     * @param cacheCapacity - cache size
     */
    public MemoryCache(StrategyType strategyType, long cacheCapacity) throws Exception {
        if (cacheCapacity <= 0) {
            throw new Exception("Cache capacity should be positive.");
        }
        this.cacheCapacity = cacheCapacity;
        this.cacheStrategy = (new StrategyBuilder<KeyType>(strategyType)).createStrategy();
    }

    @Override
    public void put(KeyType key, ValueType data) {
        if(!isFull()) {
            cacheStorage.put(key, data);
            cacheStrategy.notifyAdd(key);
        } else {
            remove(cacheStrategy.getReplacingKey());
            put(key, data);
        }
    }

    @Override
    public ValueType get(KeyType key) {
        if(cacheStorage.containsKey(key)) {
            cacheStrategy.notifyGet(key);
        }
        return cacheStorage.get(key);
    }

    @Override
    public void remove(KeyType key) {
        if(cacheStorage.containsKey(key)) {
            cacheStrategy.notifyRemove(key);
        }
        cacheStorage.remove(key);
    }

    @Override
    public void clear() {
        cacheStorage.clear();
        cacheStrategy.notifyClear();
    }

    @Override
    public boolean contains(KeyType key) {
        return cacheStorage.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return cacheStorage.isEmpty();
    }

    @Override
    public boolean isFull() {
        return cacheStorage.size()>=cacheCapacity;
    }

    @Override
    public Set<KeyType> keySet() {
        return cacheStorage.keySet();
    }
}
