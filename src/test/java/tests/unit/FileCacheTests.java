package tests.unit;

import cache.FileCache;
import cache.MemoryCache;
import cachestrategy.StrategyType;
import main.BaseJUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static constants.CommonConstants.RESOURSES_FOLDER_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.fileutils.FileSystem.isFileExist;

class FileCacheTests extends BaseJUnitTest {
    private static final String cacheFilePath = CACHE_FILES_FOLDER + "file_cache.txt";

    @Test
    @DisplayName("[File cache] Check add, get and remove operations")
    void testAddGetAndRemove() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 100);
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
    @DisplayName("[File cache] Check clear operation")
    void testClear() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 100);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        testCache.clear();
        assertEquals(0, testCache.keySet().size());
        testCache.destroy();
    }

    @Test
    @DisplayName("[File cache] Check 'is full' and 'is empty' methods")
    void testIsFullAndIsEmpty() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 5);
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
    @DisplayName("[File cache] Check that cache returns key set correctly")
    void testKeySet() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 100);
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
    @DisplayName("[File cache] Check 'contains' method")
    void testContains() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 100);
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
    @DisplayName("[File cache] Check that cache destroys correctly")
    void testDestroy() {
        assertFalse(isFileExist(cacheFilePath));
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, cacheFilePath, 100);
        assertTrue(isFileExist(cacheFilePath));
        testCache.put(1, "one");
        testCache.clear();
        assertTrue(isFileExist(cacheFilePath));
        testCache.destroy();
        assertFalse(isFileExist(cacheFilePath));
    }

    @Test
    @DisplayName("[File cache] Check that cache is created correctly when file path does not exist")
    void testNonExistingPath() {
        assertFalse(isFileExist(cacheFilePath));
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, TESTS_FOLDER + "new_folder" + File.separator + "file_cache.txt", 100);
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
    @DisplayName("[File cache] Check that cache works correctly if overflown")
    void testOverflow() {
        FileCache<Integer, String> testCache = createFileCache(StrategyType.STRATEGY_FIFO, CACHE_FILES_FOLDER + "new_folder" + File.separator + "file_cache.txt", 3);
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
