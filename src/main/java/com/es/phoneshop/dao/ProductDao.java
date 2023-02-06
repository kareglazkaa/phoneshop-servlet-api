package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SearchMethod;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> findProducts(String query, SortField sortFiled, SortOrder sortOrder);
    List<Product> advancedFindProducts(String description, BigDecimal minPrice, BigDecimal maxPrice, SearchMethod searchMethod);

    void delete(Long id) throws ProductNotFoundException;
}
