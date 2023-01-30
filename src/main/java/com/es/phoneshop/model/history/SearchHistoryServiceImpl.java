package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchHistoryServiceImpl implements SearchHistoryService {
    private static final SearchHistoryServiceImpl INSTANCE = new SearchHistoryServiceImpl();

    private static final String SEARCH_HISTORY_SESSION_ATTRIBUTE =
            SearchHistoryServiceImpl.class.getName() + ".products";
    private Object lock = new Object();

    private SearchHistoryServiceImpl() {
    }

    public static SearchHistoryServiceImpl getInstance() {
        return INSTANCE;
    }

    public void addRecentProduct(SearchHistory searchHistory, Product product) {
        synchronized (lock) {
            List<Product> products = searchHistory.getProducts();
            int index = productContains(products, product.getId());
            if (index != -1) {
                products.remove(index);
            }
            products.add(0, product);

            searchHistory.setProducts(products.stream()
                    .limit(3)
                    .collect(Collectors.toList()));
        }
    }

    public SearchHistory getSearchHistory(HttpServletRequest request) {
        synchronized (lock) {
            SearchHistory searchHistory =
                    (SearchHistory) request.getSession().getAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE);
            if (searchHistory == null) {
                searchHistory = new SearchHistory();
                request.getSession().setAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE, searchHistory);
            }
            return searchHistory;
        }
    }

    private int productContains(List<Product> products, Long productId) {
        synchronized (lock) {
            int size = products.size();
            return IntStream.range(0, size)
                    .filter(i -> productId.equals(products.get(i).getId()))
                    .findFirst()
                    .orElse(-1);
        }
    }
}
