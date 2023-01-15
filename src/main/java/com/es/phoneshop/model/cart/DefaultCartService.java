package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.IntStream;

public class DefaultCartService implements CartService{
    private ProductDao productDao= ArrayListProductDao.getINSTANCE();
    private Object lock=new Object();
    private static final String CART_SESSION_ATTRIBUTE=
            DefaultCartService.class.getName()+".cart";
    private static final DefaultCartService INSTANCE=
                new DefaultCartService();

    private DefaultCartService(){}
    public static DefaultCartService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (lock) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart,Long productId, int quantity)throws OutOfStockException {
        synchronized (lock) {
            Product product = productDao.getProduct(productId);
            int index = getItemIndex(cart, productId);

            if (index != -1 && quantity + cart.getItems().get(index).getQuantity() <= product.getStock()) {
                cart.getItems().get(index).increaseQuantity(quantity);
            } else if (index == -1 && quantity <= product.getStock()) {
                cart.getItems().add(new CartItem(product, quantity));
            } else {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
    }
    private int getItemIndex(Cart cart,Long productId){
        synchronized (lock) {
            List<CartItem> cartItems = cart.getItems();
            int size = cartItems.size();

            return IntStream.range(0, size)
                    .filter(i -> productId.equals(cartItems.get(i).getProduct().getId()))
                    .findFirst()
                    .orElse(-1);
        }
    }
}
