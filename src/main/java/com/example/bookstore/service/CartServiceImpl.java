package com.example.bookstore.service;

import com.example.bookstore.dto.CartItemDto;
import com.example.bookstore.models.Book;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "cart";

    private final BookService bookService;
    private final HttpSession session;

    public CartServiceImpl(BookService bookService, HttpSession session) {
        this.bookService = bookService;
        this.session = session;
    }

    @Override
    public void addToCart(Long bookId) {
        Map<Long, CartItemDto> cart = getCartMap();
        Book book = bookService.getBookById(bookId);
        cart.putIfAbsent(bookId, new CartItemDto());

        CartItemDto item = cart.get(bookId);
        if (item.getQuantity() == 0) {
            item.setBookId(book.getId());
            item.setTitle(book.getTitle());
            item.setPrice(book.getPrice());
        }
        item.setQuantity(item.getQuantity() + 1);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeFromCart(Long bookId) {
        Map<Long, CartItemDto> cart = getCartMap();
        cart.remove(bookId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public List<CartItemDto> getCartItems() {
        return new ArrayList<>(getCartMap().values());
    }

    @Override
    public double getTotal() {
        return getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItemDto> getCartMap() {
        Object cart = session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new HashMap<Long, CartItemDto>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return (Map<Long, CartItemDto>) cart;
    }
}
