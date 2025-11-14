package es.bifacia.nococinomas.service;

import es.bifacia.nococinomas.model.Order;

import java.util.List;

public interface PlaywrightService {

    /**
     * Request all the orders to No Cocino Más web page.
     * @param orders List of orders.
     */
    void launchFullOrder(final List<Order> orders);
}
