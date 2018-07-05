package constants;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class is used for storing common constants
 */
public final class CommonConstants {
    private CommonConstants() {}

    public static final String RESOURSES_FOLDER_PATH = System.getProperty("user.dir") + File.separator + Paths.get("src", "main", "resources") + File.separator;

}
