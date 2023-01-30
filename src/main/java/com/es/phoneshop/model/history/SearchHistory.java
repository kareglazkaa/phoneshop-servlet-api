package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory {
    private List<Product> products = new ArrayList<>();

    public SearchHistory() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
