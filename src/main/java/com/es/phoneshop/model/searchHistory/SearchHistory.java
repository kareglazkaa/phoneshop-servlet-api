package com.es.phoneshop.model.searchHistory;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchHistory {
    private static final SearchHistory INSTANCE = new SearchHistory();

    private static final String SEARCH_HISTORY_SESSION_ATTRIBUTE=
                SearchHistory.class.getName()+".products";
    private Object lock=new Object();
    private SearchHistory() {}

    public static SearchHistory getINSTANCE() {
        return INSTANCE;
    }

    public void addRecentProduct(List<Product> products,Product product) {
        synchronized (lock) {
            int index = productContains(products, product.getId());
            if (index != -1) {
                products.remove(index);
            }
            products.add(0, product);
            products = products.stream()
                    .limit(3)
                    .collect(Collectors.toList());
        }
    }

    public List<Product> getProducts(HttpServletRequest request) {
        synchronized (lock) {
            List<Product> products = (List<Product>) request.getSession().getAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE);
            if (products == null) {
                products = new ArrayList<>();
                request.getSession().setAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE, products);
            }
            return products;
        }
    }

    private int productContains(List<Product> products,Long productId) {
        synchronized (lock) {
            int size = products.size();
            return IntStream.range(0, size)
                    .filter(i -> productId.equals(products.get(i).getId()))
                    .findFirst()
                    .orElse(-1);
        }
    }
}
