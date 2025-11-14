package es.bifacia.nococinomas.service.impl;

import es.bifacia.nococinomas.model.Order;
import es.bifacia.nococinomas.service.CSVParser;
import es.bifacia.nococinomas.service.PlaywrightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.bifacia.nococinomas.service.MainService;

import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private PlaywrightService playwrightService;

    public MainServiceImpl() {
        super();
    }

    public void runApplication() {
        final List<Order> orders = csvParser.parseOrders();
        playwrightService.launchFullOrder(orders);
    }
}
