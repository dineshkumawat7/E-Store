package com.ecommerce.shop.services.user.cart;

import com.ecommerce.shop.entity.*;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.payload.*;
import com.ecommerce.shop.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartItemsDto addProductToCart(AddProductInCartDto addProductInCartDto) throws Exception {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()) {
            throw new Exception("Cart item already exist.");
        } else {
            @SuppressWarnings("null")
            Optional<Product> optionalProduct = this.productRepository.findById(addProductInCartDto.getProductId());
            @SuppressWarnings("null")
            Optional<User> optionalUser = this.userRepository.findById(addProductInCartDto.getUserId());
            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cartItems = new CartItems();
                Product product = optionalProduct.get();
                cartItems.setProduct(product);
                cartItems.setPrice(product.getPrice());
                cartItems.setQuantity(1);
                cartItems.setTotalPrice(product.getPrice());
                cartItems.setUser(optionalUser.get());
                cartItems.setOrder(activeOrder);
                this.cartItemsRepository.save(cartItems);

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() +
                        cartItems.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());

                activeOrder.getCartItems().add(cartItems);
                this.orderRepository.save(activeOrder);
                return cartItems.getCartDto();
            } else {
                throw new UsernameNotFoundException("User and product not found.");
            }
        }
    }

    @Override
    public CartItemsDto cartItemAlreadyExists(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());
        if (optionalCartItems.isPresent()) {
            return optionalCartItems.get().getCartDto();
        } else {
            return null;
        }
    }

    @Override
    public CartItemsDto getCartIdAndUserId(Long cartId, Long userId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);

        Optional<CartItems> optionalCartItem = this.cartItemsRepository.findByIdAndUserId(cartId, userId);
        if (optionalCartItem.isPresent()) {
            CartItems cartItems = optionalCartItem.get();
            CartItemsDto cartItemsDto = new CartItemsDto();
            cartItemsDto.setId(cartItems.getId());
            cartItemsDto.setProductName(cartItems.getProduct().getName());
            cartItemsDto.setProductImage(cartItems.getProduct().getImageUrl());
            cartItemsDto.setOrderId(cartItems.getOrder().getId());
            cartItemsDto.setProductId(cartItems.getProduct().getId());
            cartItemsDto.setPrice(cartItems.getPrice());
            cartItemsDto.setQuantity(cartItems.getQuantity());
            cartItemsDto.setTotalPrice(cartItems.getTotalPrice());
            cartItemsDto.setUserId(cartItems.getUser().getId());
            return cartItemsDto;
        } else {
            return null;
        }

    }

    @SuppressWarnings("null")
    @Override
    public void removeItemFromCartById(AddProductInCartDto addProductInCartDto) throws Exception {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());
        if (optionalCartItems.isPresent()) {
            Optional<Product> optionalProduct = this.productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = this.userRepository.findById(addProductInCartDto.getUserId());
            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cartItems = optionalCartItems.get();
                activeOrder.setTotalAmount(activeOrder.getTotalAmount() - cartItems.getTotalPrice());
                activeOrder.setAmount(activeOrder.getAmount() - cartItems.getTotalPrice());
                activeOrder.getCartItems().remove(cartItems);
                if (activeOrder.getCoupon() != null) {
                    Integer discountAmount = ((activeOrder.getCoupon().getDiscount() / 100)
                            * activeOrder.getTotalAmount());
                    Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
                    activeOrder.setAmount(netAmount);
                    activeOrder.setDiscount(discountAmount);
                }
                this.cartItemsRepository.deleteById(cartItems.getId());
                this.orderRepository.save(activeOrder);
            }
        } else {
            activeOrder.setTotalAmount(activeOrder.getTotalAmount());
            activeOrder.setAmount(activeOrder.getAmount());
            this.orderRepository.save(activeOrder);
        }
    }

    @Override
    public void removeCartItem(Long cartItemId, Long userId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByIdAndUserId(cartItemId, userId);
        if (optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - cartItems.getTotalPrice());
            activeOrder.setAmount(activeOrder.getAmount() - cartItems.getTotalPrice());
            activeOrder.getCartItems().remove(cartItems);
            if (activeOrder.getCoupon() != null) {
                Integer discountAmount = ((activeOrder.getCoupon().getDiscount() / 100)
                        * activeOrder.getTotalAmount());
                Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            this.cartItemsRepository.deleteById(cartItems.getId());
            this.orderRepository.save(activeOrder);
        } else {
            activeOrder.setTotalAmount(activeOrder.getTotalAmount());
            activeOrder.setAmount(activeOrder.getAmount());
            this.orderRepository.save(activeOrder);
        }
    }

    @Override
    public CartItemsDto updateCartItem(Long cartItemId, Long userId, Integer quantity) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByIdAndUserId(cartItemId, userId);
        if (optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            Integer price = cartItems.getPrice();
            Integer totalPrice = price * quantity;

            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - cartItems.getTotalPrice());
            activeOrder.setAmount(activeOrder.getAmount() - cartItems.getTotalPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + totalPrice);
            activeOrder.setAmount(activeOrder.getAmount() + totalPrice);
            if (activeOrder.getCoupon() != null) {
                Integer discountAmount = ((activeOrder.getCoupon().getDiscount() / 100)
                        * activeOrder.getTotalAmount());
                Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            cartItems.setQuantity(quantity);
            cartItems.setTotalPrice(totalPrice);
            this.cartItemsRepository.save(cartItems);
            this.orderRepository.save(activeOrder);

            return cartItems.getCartDto();
        } else {
            return null;
        }
    }

    @Override
    public List<CartItemsDto> getCartItemsByUserId(Long userId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        List<CartItems> cartItems = activeOrder.getCartItems();
        List<CartItemsDto> cartItemsDtosList = new ArrayList<>();
        for (CartItems cartItem : cartItems) {
            CartItemsDto item = new CartItemsDto();
            item.setId(cartItem.getId());
            item.setProductName(cartItem.getProduct().getName());
            item.setProductImage(cartItem.getProduct().getImageUrl());
            item.setOrderId(cartItem.getOrder().getId());
            item.setProductId(cartItem.getProduct().getId());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalPrice(cartItem.getTotalPrice());
            item.setUserId(cartItem.getUser().getId());
            cartItemsDtosList.add(item);
        }
        return cartItemsDtosList;
    }

    @SuppressWarnings("null")
    @Override
    public void increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<Product> optionalProduct = this.productRepository.findById(addProductInCartDto.getProductId());
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());
        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();
            activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() +
                    product.getPrice());
            cartItems.setQuantity(cartItems.getQuantity() + 1);
            cartItems.setTotalPrice(product.getPrice() * cartItems.getQuantity());
            if (activeOrder.getCoupon() != null) {
                Integer discountAmount = ((activeOrder.getCoupon().getDiscount() / 100)
                        * activeOrder.getTotalAmount());
                Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            this.productRepository.save(product);
            this.cartItemsRepository.save(cartItems);
            this.orderRepository.save(activeOrder);
        }
    }

    @SuppressWarnings("null")
    @Override
    public void decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<Product> optionalProduct = this.productRepository.findById(addProductInCartDto.getProductId());
        Optional<CartItems> optionalCartItems = this.cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());
        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();
            activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() -
                    product.getPrice());
            cartItems.setQuantity(cartItems.getQuantity() - 1);
            cartItems.setTotalPrice(product.getPrice() * cartItems.getQuantity());
            if (activeOrder.getCoupon() != null) {
                Integer discountAmount = ((activeOrder.getCoupon().getDiscount() / 100)
                        * activeOrder.getTotalAmount());
                Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount(netAmount);
                activeOrder.setDiscount(discountAmount);
            }
            this.productRepository.save(product);
            this.cartItemsRepository.save(cartItems);
        } else {
            activeOrder.setAmount(0);
            activeOrder.setTotalAmount(0);
        }

        this.orderRepository.save(activeOrder);
    }

    @Override
    public long getTotalCartItems(Long userId) {
        return this.cartItemsRepository.count();
    }

}
