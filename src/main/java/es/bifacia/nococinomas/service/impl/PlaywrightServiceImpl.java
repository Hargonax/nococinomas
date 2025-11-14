package es.bifacia.nococinomas.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Proxy;
import es.bifacia.nococinomas.model.Order;
import es.bifacia.nococinomas.service.PlaywrightService;
import es.bifacia.nococinomas.service.PropertiesService;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PlaywrightServiceImpl implements PlaywrightService {
    private static final String LOGIN_URL = "https://www.nococinomas.es/iniciar-sesion?back=my-account";
    private static final String NO_COCINO_MAS_URL = "https://www.nococinomas.es/";
    private static final String SEARCH_URL = "https://www.nococinomas.es/busqueda?controller=search&s=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:115.0) Gecko/20100101 Firefox/141.0";
    private final Logger logger = LogManager.getLogger(PlaywrightServiceImpl.class);

    @Autowired
    private PropertiesService propertiesService;

    private Playwright playwright;
    private Browser browser;
    private Page webPage;

    public PlaywrightServiceImpl() {
        super();
    }

    /**
     * Loads the main page of No Cocino Más.
     */
    public void startPlaywright() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setSlowMo(1000)
        );
        Browser.NewContextOptions ctx = new Browser.NewContextOptions();
        ctx.userAgent = USER_AGENT;
        BrowserContext bcxt = browser.newContext(ctx);

        webPage = bcxt.newPage();

        webPage.setViewportSize(ThreadLocalRandom.current().nextInt(1500, 1600), ThreadLocalRandom.current().nextInt(800, 840));
        webPage.setDefaultTimeout(30000);
        webPage.navigate(LOGIN_URL);
        closeCookiesModal();
    }

    /**
     * Request all the orders to No Cocino Más web page.
     * @param orders List of orders.
     */
    public void launchFullOrder(final List<Order> orders) {
        try {
            startPlaywright();
//            login();
            orders.forEach(order -> {
                addMealOrder(order);
            });
            logger.info("Finish adding orders. Please, proceed to end the order.");
            final int waitTime = propertiesService.getIntegerProperty("wait.in.millis");
            Thread.sleep(waitTime);
        } catch (Exception ex) {
            logger.error("There was an error launching the full order. {}", ex.getMessage());
        }
    }

    private void login() {
        final String user = propertiesService.getProperty("user.mail");
        final String password = propertiesService.getProperty("password");
        webPage.locator("input#field-email").fill(user);
        webPage.locator("input#field-password").fill(password);
        webPage.locator("#submit-login").click();
    }

    private void closeCookiesModal() {
        final Locator closeButton = webPage.locator("button.cookiesplus-reject");
        if (closeButton.count() > 0) {
            closeButton.click();
        }
    }

    private void addMealOrder(final Order order) {
        Locator meal = null;
        final String mealName = order.getMealName().toLowerCase();
        final String url = SEARCH_URL + mealName;
        webPage.navigate(url);
        Locator meals = webPage.locator("div.product");
        for (int i = 0; i < meals.count(); i++) {
            final String name = meals.nth(i).locator(".product-title").textContent().trim().toLowerCase();
            if (StringUtils.isNotEmpty(mealName) && mealName.equals(name)) {
                meal = meals.nth(i);
                break;
            }
        }
        if (meal != null) {
            int start = 0;
            final String ordered = meal.locator("input[name=qty]").getAttribute("value");
            if (!meal.locator(".bootstrap-touchspin-up").isVisible()) {
                meal.locator(".btn-secondary").click();
                start = 1;
            }
            for (int j = start; j < order.getNumberOfMeals(); j++) {
                meal.locator(".bootstrap-touchspin-up").click();
            }
        } else {
            logger.error("Meal " + mealName + " not found.");
        }
    }

}
