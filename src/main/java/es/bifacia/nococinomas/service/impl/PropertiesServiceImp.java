package es.bifacia.nococinomas.service.impl;

import es.bifacia.nococinomas.service.PropertiesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class PropertiesServiceImp implements PropertiesService {
    private static final String PROPERTIES_FILE = "./test.properties";
    private final Logger logger = LogManager.getLogger(PropertiesServiceImp.class);
    private Map<String, String> properties = new HashMap<>();

    public PropertiesServiceImp() {
        super();
        loadProperties();
    }

    private void loadProperties() {
        try {
            final Properties prop = new Properties();
            try (final InputStream inputStream = new FileInputStream(PROPERTIES_FILE)) {
                prop.load(inputStream);
                for (final Map.Entry<Object, Object> entry : prop.entrySet()) {
                    properties.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        } catch (Exception ex) {
            logger.error("Error trying to load the properties file {}. {}", PROPERTIES_FILE, ex.getMessage());
        }
    }

    /**
     * Gets the value of a property.
     * @param propertyName Name of the property we want to retrieve.
     * @return Value of the property.
     */
    public String getProperty(final String propertyName) {
        String value = null;
        if (properties.containsKey(propertyName)) {
            value = properties.get(propertyName) ;
        }
        return value;
    }

    /**
     * Gets the integer value of a property.
     * @param propertyName Name of the property we want to retrieve.
     * @return Integer value of the property.
     */
    public int getIntegerProperty(final String propertyName) {
        int value = 0;
        if (properties.containsKey(propertyName)) {
            try {
                value = Integer.parseInt(properties.get(propertyName));
            } catch (Exception ex) {
                logger.error("Error trying to retrieve property {}", propertyName);
            }
        }
        return value;
    }
}
