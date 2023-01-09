package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Product Not Found Exception";
    }
}
