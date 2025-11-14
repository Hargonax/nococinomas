package es.bifacia.nococinomas.service;

import es.bifacia.nococinomas.model.Order;

import java.util.List;

public interface CSVParser {

    /**
     * Parses a list of orders from a CSV file.
     * @return List of orders contained in the file.
     */
    List<Order> parseOrders();
}
