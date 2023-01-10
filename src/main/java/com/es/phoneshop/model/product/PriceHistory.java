package com.es.phoneshop.model.product;

import java.math.BigDecimal;

public class PriceHistory {
    private String date;
    private BigDecimal price;

    public PriceHistory(){}
    public PriceHistory(String date,BigDecimal price){
        this.date=date;
        this.price=price;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String  date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
       return price;
   }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
