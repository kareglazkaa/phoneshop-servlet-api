package com.es.phoneshop.model.searchHistory;

import com.es.phoneshop.model.history.SearchHistoryServiceImpl;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.web.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchHistoryServiceImplTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private SearchHistoryServiceImpl historyService;
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    List<Product> products;

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);

        demo.setSampleProducts();

        historyService = SearchHistoryServiceImpl.getInstance();
        products = historyService.getSearchHistory(request).getProducts();
    }

    @Test
    public void getProductsTest() {
        List<Product> testProducts = historyService.getSearchHistory(request).getProducts();
        assertNotNull(testProducts);
    }

    @Test
    public void addRecentProductTest() {
        assertNull(products.stream()
                .filter(prd -> prd.getId().equals(0L))
                .findAny().orElse(null));
    }
}
