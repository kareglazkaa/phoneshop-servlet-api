package com.es.phoneshop.model.product;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts(String query, SortField sortFiled, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id) throws ProductNotFoundException;
}
