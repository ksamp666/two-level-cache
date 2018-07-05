package tests.module;

import cache.MemoryCache;
import cache.TwoLevelCache;
import cachestrategy.StrategyType;
import main.BaseJUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.fileutils.FileSystem.isFileExist;

class MemoryCacheAndStrategiesTests extends BaseJUnitTest {
    @Test
    @DisplayName("Check that MEMORY cache works correctly with FIFO strategy")
    void testMemoryCacheAndFifoStrategy() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 3);
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
    @DisplayName("Check that MEMORY cache works correctly with LIFO strategy")
    void testMemoryCacheAndLifoStrategy() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_LIFO, 3);
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
    @DisplayName("Check that MEMORY cache works correctly with LRU strategy")
    void testMemoryCacheAndLruStrategy() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_LRU, 3);
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
    @DisplayName("Check that MEMORY cache works correctly with MRU strategy")
    void testMemoryCacheAndMruStrategy() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_MRU, 3);
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
    @DisplayName("Check that MEMORY cache works correctly with LFU strategy")
    void testMemoryCacheAndLfuStrategy() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_LFU, 3);
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
