package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.OrderPagingResponse;
import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.dto.PagingHeaders;
import com.tusur.cargo.enumiration.OrderStatus;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.PhotoRepository;
import com.tusur.cargo.repository.SizeRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.OrderService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final PhotoRepository photoRepository;
  private final SizeRepository sizeRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
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

    order.setStatus(OrderStatus.CHECKED.toString());
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
  public List<Order> getAllOrderByType(String type) {
    return orderRepository.findAllByTypeOrderByCreated(type);
  }

  @Override
  public OrderPagingResponse getAllOrder(Specification<Order> spec, HttpHeaders headers,
      Sort sort) {
    if (isRequestPaged(headers)) {
      return getAllOrder(spec, buildPageRequest(headers, sort));
    } else {
      List<Order> entities = getAllOrder(spec, sort);
      return new OrderPagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
    }
  }

  @Override
  public OrderPagingResponse getAllOrder(Specification<Order> spec, Pageable pageable) {
    Page<Order> page = orderRepository.findAll(spec, pageable);
    List<Order> content = page.getContent();
    return new OrderPagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), content);
  }

  @Override
  public List<Order> getAllOrder(Specification<Order> spec, Sort sort) {
    return orderRepository.findAll(spec, sort);
  }

  private boolean isRequestPaged(HttpHeaders headers){
    return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) && headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
  }

  private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
    int page = Integer.parseInt(
        Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
    int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
    return PageRequest.of(page, size, sort);
  }

  @Override
  public List<Order> getAllOrderByStatusChecked() {
    return orderRepository.findAllByStatusOrderByCreated(OrderStatus.CHECKED.toString());
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

    oldOrder.setStatus(OrderStatus.CHECKED.toString());
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
  @Transactional
  public short deleteOrder(Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order == null) {
      return 9;
    }
    orderRepository.delete(order);
    return 1;
  }

  @Override
  @Transactional
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
