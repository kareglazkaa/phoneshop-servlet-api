package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchHistoryServiceImpl implements SearchHistoryService {
    private static final SearchHistoryServiceImpl INSTANCE = new SearchHistoryServiceImpl();

    private static final String SEARCH_HISTORY_SESSION_ATTRIBUTE =
            SearchHistoryServiceImpl.class.getName() + ".products";
    private ReadWriteLock lock=new ReentrantReadWriteLock();
    private Lock readLock=lock.readLock();
    private Lock writeLock=lock.writeLock();

    private SearchHistoryServiceImpl() {
    }

    public static SearchHistoryServiceImpl getInstance() {
        return INSTANCE;
    }

    public void addRecentProduct(SearchHistory searchHistory, Product product) {
        writeLock.lock();
        List<Product> products = searchHistory.getProducts();
        int index = productContains(products, product.getId());
        if (index != -1) {
            products.remove(index);
        }
        products.add(0, product);

        searchHistory.setProducts(products.stream()
                .limit(3)
                .collect(Collectors.toList()));
        writeLock.unlock();
    }

    public SearchHistory getSearchHistory(HttpServletRequest request) {
        readLock.lock();
        try {
            SearchHistory searchHistory =
                    (SearchHistory) request.getSession().getAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE);
            if (searchHistory == null) {
                searchHistory = new SearchHistory();
                request.getSession().setAttribute(SEARCH_HISTORY_SESSION_ATTRIBUTE, searchHistory);
            }

            return searchHistory;
        }
        finally {
            readLock.unlock();
        }

    }

    private int productContains(List<Product> products, Long productId) {
            int size = products.size();
            return IntStream.range(0, size)
                    .filter(i -> productId.equals(products.get(i).getId()))
                    .findFirst()
                    .orElse(-1);
    }
}
