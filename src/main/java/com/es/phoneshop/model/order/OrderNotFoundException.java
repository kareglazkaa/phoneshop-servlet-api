package com.es.phoneshop.model.order;

public class OrderNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Order Not Found Exception";
    }
}
