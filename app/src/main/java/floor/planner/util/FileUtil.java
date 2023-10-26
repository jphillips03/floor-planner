package floor.planner.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Writes the given content to the given file.
     *
     * @param file The file to write to.
     * @param content The content to write.
     * @return HashCode of content for checking changes.
     */
    public static int save(File file, String content) {
        PrintWriter writer = null;
        try {
            String fileName = file.getCanonicalPath();
            writer = new PrintWriter(new FileOutputStream(fileName));
            writer.print(content);
        } catch(IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        return content.hashCode();
    }

    /**
     * Reads the given file line by line and returns the string contents.
     *
     * @param file The file to read.
     * @return The contents of the file.
     */
    public static String read(File file) {
        BufferedReader reader = null;
        String content = "";
		try {
			if(file != null) {
                String fileName = file.getCanonicalPath();
				fileName = file.getCanonicalPath();
				reader = new BufferedReader(new FileReader(fileName));
				String line;
				while((line = reader.readLine()) != null) {
					content += line + "\n";
				}
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return content;
    }
}
