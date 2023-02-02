package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class CartServiceImpl implements CartService {
    private ArrayListProductDao productDao = ArrayListProductDao.getInstance();
    private ReadWriteLock lock=new ReentrantReadWriteLock();
    private Lock readLock=lock.readLock();
    private Lock writeLock=lock.writeLock();
    private static final String CART_SESSION_ATTRIBUTE =
            CartServiceImpl.class.getName() + ".cart";
    private static final CartServiceImpl INSTANCE = new CartServiceImpl();

    private CartServiceImpl() {
    }

    public static CartServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Cart getCart(HttpSession session) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;

    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
            Product product = productDao.get(productId);
            int index = getItemIndex(cart, productId);
            if (quantity > 0 && index != -1 && quantity + cart.getItems().get(index).getQuantity() <= product.getStock()) {
                increaseCartQuantity(cart.getItems().get(index), quantity);
            } else if (quantity > 0 && index == -1 && quantity <= product.getStock()) {
                cart.getItems().add(new CartItem(product, quantity));
            } else {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            recalculateCart(cart);
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
            Product product = productDao.get(productId);
            int index = getItemIndex(cart, productId);
            if (quantity > 0 && quantity <= product.getStock()) {
                setCartQuantity(cart.getItems().get(index), quantity);
            } else {
                throw new OutOfStockException(product, quantity, product.getStock());
            }

            recalculateCart(cart);
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(cartItem ->
                productId.equals(cartItem.getProduct().getId()));

        recalculateCart(cart);
    }

    @Override
    public void clearCart(Cart cart) {
            cart.getItems().clear();
            recalculateCart(cart);
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());

        cart.setTotalCost(cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

    }

    private void increaseCartQuantity(CartItem cartItem, int quantity) {
        setCartQuantity(cartItem, cartItem.getQuantity() + quantity);
    }

    private void setCartQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
    }

    private int getItemIndex(Cart cart, Long productId) {
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
