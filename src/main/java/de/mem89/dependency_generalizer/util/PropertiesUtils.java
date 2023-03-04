package de.mem89.dependency_generalizer.util;

import lombok.NonNull;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils extends AbstractLogEnabled {

    public Properties load(@NonNull String propertiesFileName) {
        Properties properties = new Properties();

        InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(is);
        } catch (IOException e) {
            getLogger().error(String.format("There was an error loading properties file %s", propertiesFileName));
            throw new RuntimeException(e);
        }

        return properties;
    }
}
