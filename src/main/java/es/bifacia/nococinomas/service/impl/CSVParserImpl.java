package es.bifacia.nococinomas.service.impl;

import es.bifacia.nococinomas.model.Order;
import es.bifacia.nococinomas.service.CSVParser;
import es.bifacia.nococinomas.service.PropertiesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVParserImpl implements CSVParser {
    private static final int NAME_COLUMN = 0;
    private static final int NUMBER_COLUMN = 1;
    private static final String COMMA_DELIMITER = ",";

    private final Logger logger = LogManager.getLogger(CSVParserImpl.class);

    @Autowired
    private PropertiesService propertiesService;

    public CSVParserImpl() {
        super();
    }

    /**
     * Parses a list of cards from a CSV file.
     * @return List of cards contained in the file.
     */
    public List<Order> parseOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            final String filePath = propertiesService.getProperty("orders.file");
            final File file = ResourceUtils.getFile(filePath);
            try (final BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                int counter = 1;
                while ((line = br.readLine()) != null) {
                    final String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    final Order order = parseOrder(values, counter);
                    if (order != null) {
                        orders.add(order);
                    }
                    counter++;
                }
            }
        } catch (Exception ex) {
            logger.error("Error reading CSV file. {}", ex.getMessage());
        }
        return orders;
    }

    public Order parseOrder(final String[] values, final int rowNumber) {
        final Order order = new Order();
        try {
            order.setMealName(values[NAME_COLUMN].replaceAll("\"", ""));
            order.setNumberOfMeals(Integer.parseInt(values[NUMBER_COLUMN]));
        } catch (Exception ex) {
            final String message = "Error trying to parse row " + rowNumber  + " from the songs Excel.";
            logger.error(message);
            throw ex;
        }
        return order;
    }

}
