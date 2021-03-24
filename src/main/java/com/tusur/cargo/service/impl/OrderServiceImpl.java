package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.PhotoRepository;
import com.tusur.cargo.repository.SizeRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.OrderService;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final PhotoRepository photoRepository;
  private final SizeRepository sizeRepository;
  private final UserRepository userRepository;

  @Override
  public short createOrder(OrderRequest orderRequest) {
    Order order = new Order(
        orderRequest.getType(),
        orderRequest.getTitle(),
        orderRequest.getDescription(),
        orderRequest.getAddressSender(),
        orderRequest.getAddressRecipient(),
        orderRequest.getPrice(),
        orderRequest.getDepartDate(),
        orderRequest.getArrivalDate(),
        sizeRepository.save(orderRequest.getSize()),
        orderRequest.getImagesId().stream()
            .map(photoRepository::findByPhotoId)
            .collect(Collectors.toList()));

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByEmail(email).orElseThrow(() -> new SpringCargoException("User not found with email -" + email));
    order.setUser(user);
    orderRepository.save(order);

    return 1;
  }
}
