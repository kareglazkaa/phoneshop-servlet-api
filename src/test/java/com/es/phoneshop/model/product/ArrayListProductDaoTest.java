package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ArrayListProductDaoTest {
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private Product product;
    private String query;

    @Before
    public void setup() throws ProductNotFoundException {
        query = "Samsung";

        Currency usd = Currency.getInstance("USD");
        product = new Product("test", "Samsung Galaxy S",
                new BigDecimal(100), usd, 100, "https://raw.githubusercontent." +
                "com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);

    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts(query, SortField.PRICE, SortOrder.DESC).isEmpty());
    }

    @Test
    public void saveProductTest() throws ProductNotFoundException {
        productDao.save(product);

        assertTrue(product.getId() > 0);
    }

    @Test
    public void deleteProductTest() throws ProductNotFoundException {
        productDao.delete(product.getId());
        List<Product> products = productDao.findProducts(query, SortField.PRICE, SortOrder.DESC).
                stream().
                filter(prd -> product.getId().equals(prd.getId())).
                collect(Collectors.toList());

        assertTrue(products.isEmpty());
    }

    @Test
    public void getProductTest() throws ProductNotFoundException {
        Product result = productDao.getProduct(product.getId());

        assertNotNull(result);
        assertEquals("test", result.getCode());
    }

    @Test
    public void findProductsTest() {
        List<Product> products = productDao.findProducts(query, SortField.PRICE, SortOrder.DESC);

        assertTrue(products.stream().
                filter(prd -> prd.getPrice() == null || prd.getStock() < 0).
                collect(Collectors.toList()).
                isEmpty());
    }
}
