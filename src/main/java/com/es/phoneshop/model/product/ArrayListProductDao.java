package com.es.phoneshop.model.product;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static final ProductDao INSTANCE = new ArrayListProductDao();
    private List<Product> products = new ArrayList<>();

    private Long maxId = Long.valueOf(0);
    private Object lock = new Object();

    private ArrayListProductDao() {
    }

    public static ProductDao getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Product getProduct(Long id) {
        synchronized (lock) {
            return products.stream().
                    filter(product -> id.equals(product.getId())).
                    findAny().
                    orElseThrow(() -> new ProductNotFoundException());
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortFiled, SortOrder sortOrder) {
        synchronized (lock) {
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
            return products.stream()
                    .filter(product -> query.isEmpty() || containsQuery(query, product.getDescription()) != 0)
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparatorFiled)
                    .collect(Collectors.toList());
        }
    }

    public Long containsQuery(String query, String productDescription) {
        return Arrays.stream(query.split(" "))
                .filter(productDescription::contains)
                .count();
    }

    @Override
    public void save(Product product) {
        synchronized (lock) {
            Long id = product.getId();
            if (id != null) {
                for (int i = 0; i < products.size(); i++) {
                    if (id.equals(products.get(i).getId())) {
                        products.set(i, product);
                        break;
                    }
                }
            } else {
                product.setId(maxId++);
                products.add(product);
            }
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (lock) {
            Product productToDelete = products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException());
            products.remove(productToDelete);
        }
    }

}
