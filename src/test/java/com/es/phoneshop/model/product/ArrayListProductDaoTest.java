package com.es.phoneshop.model.product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private Product product;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = new ArrayListProductDao();

        Currency usd = Currency.getInstance("USD");
        product= new Product("test", "Samsung Galaxy S",
                new BigDecimal(100), usd, 100, "https://raw.githubusercontent." +
                "com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        saveProductTest();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void saveProductTest() throws ProductNotFoundException {
        productDao.save(product);
        assertTrue(product.getId()>0);
    }

    @Test
    public void deleteProductTest() throws ProductNotFoundException {
        productDao.delete(product.getId());
        assertNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void getProductTest() throws ProductNotFoundException {
        Product result=productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test",result.getCode());
    }

    @Test
    public void findProductsTest(){
        List<Product> products=productDao.findProducts();
        assertTrue(products.stream().
                filter(prd->prd.getPrice()==null && prd.getStock()>0).
                collect(Collectors.toList()).
                isEmpty());

    }

}
