package tests.module;

import cache.FileCache;
import cache.MemoryCache;
import cachestrategy.StrategyType;
import main.BaseJUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileCacheAndStrategiesTests extends BaseJUnitTest {
    private static final String cacheFilePath = CACHE_FILES_FOLDER + "two_level_cache.txt";

    @Test
    @DisplayName("Check that FILE cache works correctly with FIFO strategy")
    void testMemoryCacheAndFifoStrategy() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 3);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        testCache.put(4, "two");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        assertTrue(testCache.contains(4));
    }

    @Test
    @DisplayName("Check that FILE cache works correctly with LIFO strategy")
    void testMemoryCacheAndLifoStrategy() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_LIFO, cacheFilePath, 3);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        testCache.put(4, "two");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(4));
    }

    @Test
    @DisplayName("Check that FILE cache works correctly with LRU strategy")
    void testMemoryCacheAndLruStrategy() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_LRU, cacheFilePath, 3);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        testCache.put(4, "two");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        assertTrue(testCache.contains(4));
        testCache.get(2);
        testCache.put(5, "five");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(4));
        assertTrue(testCache.contains(5));
    }

    @Test
    @DisplayName("Check that FILE cache works correctly with MRU strategy")
    void testMemoryCacheAndMruStrategy() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_MRU, cacheFilePath, 3);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        testCache.put(4, "two");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(4));
        testCache.get(2);
        testCache.put(5, "five");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(4));
        assertTrue(testCache.contains(5));
    }

    @Test
    @DisplayName("Check that FILE cache works correctly with LFU strategy")
    void testMemoryCacheAndLfuStrategy() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_LFU, cacheFilePath, 3);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        testCache.put(4, "two");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(3));
        assertTrue(testCache.contains(4));
        testCache.get(2);
        testCache.get(2);
        testCache.get(2);
        testCache.get(4);
        testCache.get(4);
        testCache.get(3);
        testCache.put(5, "five");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.contains(2));
        assertTrue(testCache.contains(4));
        assertTrue(testCache.contains(5));
    }
}
