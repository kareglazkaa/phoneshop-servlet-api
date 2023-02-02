package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.List;

public interface ProductDao {
    List<Product> findProducts(String query, SortField sortFiled, SortOrder sortOrder);

    void delete(Long id) throws ProductNotFoundException;
}
