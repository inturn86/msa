package com.sdc.orderservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.sdc.orderservice.dto.OrderDto;
import com.sdc.orderservice.jpa.OrderEntity;
import com.sdc.orderservice.jpa.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepository;

	@Override
	public OrderDto createOrder(OrderDto orderDto) {

		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

		orderRepository.save(orderEntity);

		OrderDto returnOrderDto = mapper.map(orderEntity, OrderDto.class);

		return returnOrderDto;
	}


	@Override
	public OrderDto getOrderByOrderId(String orderId) {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
		OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);
		return orderDto;
	}

	@Override
	public Iterable<OrderEntity> getOrdersByUserId(String userId) {

		return orderRepository.findByUserId(userId);
	}


}
