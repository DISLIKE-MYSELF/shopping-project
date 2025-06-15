package com.example.backend.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.CreatePaymentRequest;
import com.example.backend.dto.request.UpdatePaymentRequest;
import com.example.backend.dto.response.PaymentResponse;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.PaymentMapper;
import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import com.example.backend.model.PaymentStatus;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final PaymentMapper paymentMapper;

  @Override
  @Transactional
  public List<PaymentResponse> getPaymentsByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    // 获取用户的所有订单
    List<Order> orders = orderRepository.findByUserId(user.getId());

    if (orders.isEmpty()) {
      return Collections.emptyList();
    }

    List<Long> orderIds = orders.stream().map(Order::getId).toList();
    List<Payment> payments = paymentRepository.findByOrder_IdIn(orderIds);

    return payments.stream().map(paymentMapper::toPaymentResponse).collect(Collectors.toList());
  }

  @Override
  public PaymentResponse getPaymentByOrderId(String username, Long orderId) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("无权操作");
    }

    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Payment", "OrderId=" + orderId));

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  public PaymentResponse getPaymentById(String username, Long id) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment", id));

    if (!payment.getOrder().getUser().equals(user)) {
      throw new UnauthorizedException("无权操作");
    }

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  @Transactional
  public PaymentResponse createPayment(String username, CreatePaymentRequest request) {
    if (!userRepository.existsByUsername(username)) {
      throw new EntityNotFoundException("User", username);
    }

    Order order = orderRepository.findById(request.orderId())
        .orElseThrow(() -> new EntityNotFoundException("Order", request.orderId()));

    if (!order.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    Payment payment = new Payment();
    payment.setOrder(order);
    payment.setAmount(request.amount());
    payment.setPaymentMethod(request.paymentMethod());
    payment.setStatus(PaymentStatus.fromString(request.status()));

    return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
  }

  @Override
  @Transactional
  public PaymentResponse updatePaymentStatus(String username, Long id,
      UpdatePaymentRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment", id));

    if (!payment.getOrder().getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("无权操作");
    }

    PaymentStatus status = PaymentStatus.fromString(request.status());

    if (status == null) {
      throw new BusinessException("未知订单状态：" + request.status());
    }

    payment.setStatus(status);

    return paymentMapper.toPaymentResponse(paymentRepository.saveAndFlush(payment));
  }

  @Override
  @Transactional
  public void deletePaymentById(String username, Long id) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment", id));

    if (!payment.getOrder().getUser().equals(user)) {
      throw new UnauthorizedException("无权操作");
    }

    paymentRepository.deleteById(id);;
  }
}
