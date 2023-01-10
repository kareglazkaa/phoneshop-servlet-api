package com.es.phoneshop.model.product;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.web.DemoDataServletContextListener;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;
    private List<Product> products;
    private Long maxId= Long.valueOf(0);
    private Object lock=new Object();
    public static ProductDao getInstance(){
        if(instance==null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }
    private ArrayListProductDao(){
        products=new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id)  {
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

            Comparator<Product> comparatorFiled=Comparator.comparing( product -> {
                if (SortField.description == sortFiled)
                    return (Comparable) product.getDescription();
                else if(SortField.price==sortFiled)
                    return (Comparable) product.getPrice();
                else
                    return (Comparable) containsQuery(query, product.getDescription());

            });

            if(query!=null || !query.isEmpty()){
                comparatorFiled=comparatorFiled.reversed();
            }
            if(SortOrder.desc==sortOrder){
                comparatorFiled=comparatorFiled.reversed();
            }

            return products.stream()
                    .filter(product -> query == null || query.isEmpty() || containsQuery(query,product.getDescription())!=0)
                    .filter(product -> product.getPrice()!=null)
                    .filter(product -> product.getStock()>0)
                    .sorted(comparatorFiled)
                    .collect(Collectors.toList());
        }
    }

    public Integer containsQuery(String query,String productDescription){
        int count = 0;
        if(query!=null && !query.isEmpty()) {
            String[] words = query.split(" ");
            for (String word : words) {
                if (productDescription.contains(word))
                    count++;
            }
        }
        return count;

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
