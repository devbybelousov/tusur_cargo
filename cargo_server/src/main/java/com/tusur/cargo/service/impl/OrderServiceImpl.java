package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.PhotoRepository;
import com.tusur.cargo.repository.SizeRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.OrderService;
import java.util.List;
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
    User user = userRepository.findByEmail(email).orElse(null);
    if (user == null) {
      return 2;
    }

    order.setUser(user);
    orderRepository.save(order);

    return 1;
  }

  @Override
  public List<Order> getAllOrder() {
    return orderRepository.findAll();
  }

  @Override
  public Order getOrder(Long id) {
    return orderRepository.findById(id).orElse(new Order());
  }

  @Override
  public short editOrder(OrderRequest orderRequest, Long id) {
    Order oldOrder = orderRepository.findById(id).orElse(null);
    if (oldOrder == null) {
      return 9;
    }

    oldOrder.setType(orderRequest.getType());
    oldOrder.setTitle(orderRequest.getTitle());
    oldOrder.setDescription(orderRequest.getDescription());
    oldOrder.setAddressSender(orderRequest.getAddressSender());
    oldOrder.setAddressRecipient(orderRequest.getAddressRecipient());
    oldOrder.setPrice(orderRequest.getPrice());
    oldOrder.setDepartDate(orderRequest.getDepartDate());
    oldOrder.setArrivalDate(orderRequest.getArrivalDate());
    oldOrder.setSize(orderRequest.getSize());
    oldOrder.setPhotos(orderRequest.getImagesId().stream()
        .map(photoRepository::findByPhotoId)
        .collect(Collectors.toList()));
    return 1;
  }

  @Override
  public short deleteOrder(Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order == null) {
      return 9;
    }
    orderRepository.delete(order);
    return 1;
  }

  @Override
  public short updateStatus(Long id, String status) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order == null) {
      return 9;
    }
    order.setStatus(status);
    orderRepository.save(order);
    return 1;
  }
}
