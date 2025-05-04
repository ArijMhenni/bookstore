package com.example.bookstore.service;

import com.example.bookstore.dto.CartItemDto;

import java.util.List;

public interface CartService {
    void addToCart(Long bookId);
    void removeFromCart(Long bookId);
    List<CartItemDto> getCartItems();
    double getTotal();
}
