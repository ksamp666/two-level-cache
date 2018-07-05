package tests.unit;

import cache.FileCache;
import cache.TwoLevelCache;
import cachestrategy.StrategyType;
import main.BaseJUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.fileutils.FileSystem.isFileExist;

class TwoLevelCacheTests extends BaseJUnitTest {
    private static final String cacheFilePath = CACHE_FILES_FOLDER + "two_level_cache.txt";

    @Test
    @DisplayName("[Two-level cache] Check add, get and remove operations")
    void testAddGetAndRemove() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertEquals("two", testCache.get(2));
        testCache.remove(2);
        assertEquals(2, testCache.keySet().size());
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check clear operation")
    void testClear() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        testCache.clear();
        assertEquals(0, testCache.keySet().size());
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check 'is full' and 'is empty' methods")
    void testIsFullAndIsEmpty() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 2, 3, cacheFilePath);
        assertEquals(false, testCache.isFull());
        assertEquals(true, testCache.isEmpty());
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(false, testCache.isFull());
        assertEquals(false, testCache.isEmpty());
        testCache.put(4, "four");
        testCache.put(5, "five");
        assertEquals(true, testCache.isFull());
        assertEquals(false, testCache.isEmpty());
        testCache.clear();
        assertEquals(false, testCache.isFull());
        assertEquals(true, testCache.isEmpty());
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check that cache returns key set correctly")
    void testKeySet() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertTrue(testCache.keySet().contains(1));
        assertTrue(testCache.keySet().contains(2));
        assertTrue(testCache.keySet().contains(3));
        testCache.remove(2);
        assertEquals(2, testCache.keySet().size());
        assertTrue(testCache.keySet().contains(1));
        assertTrue(testCache.keySet().contains(3));
        testCache.clear();
        assertEquals(0, testCache.keySet().size());
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check 'contains' method")
    void testContains() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        testCache.put(1, "one");
        testCache.put(2, "two");
        assertTrue(testCache.contains(1));
        assertTrue(testCache.contains(2));
        assertFalse(testCache.contains(3));
        testCache.remove(2);
        assertTrue(testCache.contains(1));
        assertFalse(testCache.contains(2));
        testCache.clear();
        assertFalse(testCache.contains(1));
        assertFalse(testCache.contains(2));
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check that cache destroys correctly")
    void testDestroy() {
        assertFalse(isFileExist(cacheFilePath));
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        assertTrue(isFileExist(cacheFilePath));
        testCache.put(1, "one");
        testCache.clear();
        assertTrue(isFileExist(cacheFilePath));
        testCache.destroy();
        assertFalse(isFileExist(cacheFilePath));
    }

    @Test
    @DisplayName("[Two-level cache] Check that cache is created correctly when file path does not exist")
    void testNonExistingPath() {
        assertFalse(isFileExist(cacheFilePath));
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 100, 100, cacheFilePath);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertEquals("two", testCache.get(2));
        testCache.remove(2);
        assertEquals(2, testCache.keySet().size());
        testCache.destroy();
    }

    @Test
    @DisplayName("[Two-level cache] Check that cache works correctly if overflown")
    void testOverflow() {
        TwoLevelCache<Integer, String> testCache = createTwoLevelCache(StrategyType.STRATEGY_FIFO, 1, 2, cacheFilePath);
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
        testCache.destroy();
    }
}
