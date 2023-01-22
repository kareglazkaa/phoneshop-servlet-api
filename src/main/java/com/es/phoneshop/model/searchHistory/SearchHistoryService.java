package com.es.phoneshop.model.searchHistory;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;

public interface SearchHistoryService {
    void addRecentProduct(SearchHistory searchHistory, Product product);
    SearchHistory getSearchHistory(HttpServletRequest request);
}
