package es.bifacia.nococinomas.service;

public interface PropertiesService {

    /**
     * Gets the value of a property.
     * @param propertyName Name of the property we want to retrieve.
     * @return Value of the property.
     */
    String getProperty(final String propertyName);

    /**
     * Gets the integer value of a property.
     * @param propertyName Name of the property we want to retrieve.
     * @return Integer value of the property.
     */
    int getIntegerProperty(final String propertyName);
}
