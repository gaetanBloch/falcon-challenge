package io.gbloch.falcon.challenge.core.common;

import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public final class ResourceFileUtils {
        private ResourceFileUtils() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static File createTempFileFromResource(String resourcePath) throws IOException {
            File file;
            String fileName = FilenameUtils.getName(resourcePath);
            try (InputStream configStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName)) {
                file = File.createTempFile(fileName, FilenameUtils.getExtension(fileName));
                file.deleteOnExit();
                FileUtils.copyInputStreamToFile(configStream, file);
            }
            return file;
        }
}
