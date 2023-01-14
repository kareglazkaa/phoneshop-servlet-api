package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.List;
import java.util.stream.IntStream;

public class DefaultCartService implements CartService{
    private ProductDao productDao= ArrayListProductDao.getInstance();
    private Cart cart=new Cart();
    private static final DefaultCartService INSTANCE=
                new DefaultCartService();

    private DefaultCartService(){}
    public static DefaultCartService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity)throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        int index = getItemIndex(productId);

        if (index != -1 && quantity + cart.getItems().get(index).getQuantity() <= product.getStock()) {
            cart.getItems().get(index).increaseQuantity(quantity);
        } else if (index == -1 && quantity <= product.getStock()) {
            cart.getItems().add(new CartItem(product, quantity));
        } else {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

    }
    private int getItemIndex(Long productId){
        List<CartItem> cartItems=cart.getItems();
        int size=cartItems.size();

        return IntStream.range(0,size)
                .filter(i -> productId.equals(cartItems.get(i).getProduct().getId()))
                .findFirst()
                .orElse(-1);
    }
}
