package main;

import cache.FileCache;
import cache.MemoryCache;
import cache.TwoLevelCache;
import cachestrategy.StrategyType;
import org.junit.jupiter.api.AfterAll;

import java.io.File;

import static constants.CommonConstants.RESOURSES_FOLDER_PATH;
import static junit.framework.TestCase.fail;
import static utils.fileutils.FileSystem.deleteFolder;

public abstract class BaseJUnitTest {
    protected static final String TESTS_FOLDER = RESOURSES_FOLDER_PATH+"tests"+ File.separator;
    protected static final String CACHE_FILES_FOLDER = TESTS_FOLDER + "cache" + File.separator;

    @AfterAll
    static void tearDownAll() {
        deleteFolder(TESTS_FOLDER);
    }

    protected MemoryCache<Integer, String> createMemoryCache(StrategyType strategyType, long cacheCapacity) {
        try {
            return new MemoryCache<Integer, String>(strategyType, cacheCapacity);
        } catch (Exception e) {
            fail("Failed to create memory cache");
        }
        return null;
    }

    protected FileCache<Integer, String> createFileCache(StrategyType strategyType, String pathToCacheFile, long cacheCapacity) {
        try {
            return new FileCache<>(strategyType, pathToCacheFile, cacheCapacity);
        } catch (Exception e) {
            fail("Failed to create file cache");
        }
        return null;
    }

    protected TwoLevelCache<Integer, String> createTwoLevelCache(StrategyType strategyType, long memoryCacheCapacity, long fileCacheCapacity, String pathToCacheFile) {
        try {
            return new TwoLevelCache<>(strategyType, memoryCacheCapacity, fileCacheCapacity, pathToCacheFile);
        } catch (Exception e) {
            fail("Failed to create two-level cache");
        }
        return null;
    }
}
