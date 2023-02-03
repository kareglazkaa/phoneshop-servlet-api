package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericDao<Product> implements ProductDao {
    private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortFiled, SortOrder sortOrder) {
        readLock.lock();
        try {
            List<Product> products = super.getObjects();
            Comparator<Product> comparatorFiled = Comparator.comparing(product -> {
                if (SortField.DESCRIPTION == sortFiled)
                    return (Comparable) product.getDescription();
                else if (SortField.PRICE == sortFiled)
                    return (Comparable) product.getPrice();
                else
                    return (Comparable) containsQuery(query, product.getDescription());
            });

            if (!query.isEmpty()) {
                comparatorFiled = comparatorFiled.reversed();
            }
            if (SortOrder.DESC == sortOrder) {
                comparatorFiled = comparatorFiled.reversed();
            }
            return products.stream().
                    filter(product -> query.isEmpty() || containsQuery(query, product.getDescription()) != 0)
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparatorFiled)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        List<Product> products = super.getObjects();
        Product productToDelete = get(id);
        products.remove(productToDelete);
        writeLock.unlock();
    }

    private Long containsQuery(String query, String productDescription) {
        return Arrays.stream(query.split(" "))
                .filter(productDescription::contains)
                .count();
    }

}
