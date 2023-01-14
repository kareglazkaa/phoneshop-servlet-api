package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Product {
    private Long id;
    private String code;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> histories = new ArrayList<>();
    ;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price,
                   Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(String code, String description, BigDecimal price, Currency currency,
                   int stock, String imageUrl, List<PriceHistory> priceHistory) {
        this(null, code, description, price, currency, stock, imageUrl, priceHistory);
    }

    public Product(String code, String description, BigDecimal price,
                   Currency currency, int stock, String imageUrl) {
        this(null, code, description, price, currency, stock, imageUrl, null);
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency,
                   int stock, String imageUrl, List<PriceHistory> priceHistory) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.histories = priceHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceHistory> getPriceHistory() {
        return histories;
    }

    public void setPriceHistory(List<PriceHistory> priceHistory) {
        this.histories = priceHistory;
    }
}
