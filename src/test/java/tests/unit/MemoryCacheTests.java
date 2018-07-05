package tests.unit;

import cache.MemoryCache;
import cachestrategy.StrategyType;
import main.BaseJUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryCacheTests extends BaseJUnitTest {
    @Test
    @DisplayName("[Memory cache] Check add, get and remove operations")
    void testAddGetAndRemove() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 100);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        assertEquals("two", testCache.get(2));
        testCache.remove(2);
        assertEquals(2, testCache.keySet().size());
    }

    @Test
    @DisplayName("[Memory cache] Check clear operation")
    void testClear() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 100);
        testCache.put(1, "one");
        testCache.put(2, "two");
        testCache.put(3, "three");
        assertEquals(3, testCache.keySet().size());
        testCache.clear();
        assertEquals(0, testCache.keySet().size());
    }

    @Test
    @DisplayName("[Memory cache] Check 'is full' and 'is empty' methods")
    void testIsFullAndIsEmpty() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 5);
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
    }

    @Test
    @DisplayName("[Memory cache] Check that cache returns key set correctly")
    void testKeySet() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 100);
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
    }

    @Test
    @DisplayName("[Memory cache] Check 'contains' method")
    void testContains() {
        MemoryCache<Integer, String> testCache = createMemoryCache(StrategyType.STRATEGY_FIFO, 100);
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
    }

    @Test
    @DisplayName("[Memory cache] Check that cache works correctly if overflown")
    void testOverflow() {
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
}
