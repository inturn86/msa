package com.sdc.orderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sdc.orderservice.messagequeue.KafkaProducer;
import com.sdc.orderservice.messagequeue.OrderProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.cli.ConnectDistributed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.orderservice.dto.OrderDto;
import com.sdc.orderservice.jpa.OrderEntity;
import com.sdc.orderservice.service.OrderService;
import com.sdc.orderservice.vo.RequestOrder;
import com.sdc.orderservice.vo.ResponseOrder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

	private final Environment env;
	private final OrderService orderService;

	private final KafkaProducer kafkaProducer;

	private final OrderProducer orderProducer;

	@GetMapping("/health_check")
	public String status() {
		return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port")) ;
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {

		log.info("Before Added orders data");
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		//jpa
		OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
		orderDto.setUserId(userId);

//		OrderDto createOrder = orderService.createOrder(orderDto);
//		ResponseOrder res = mapper.map(createOrder, ResponseOrder.class);

		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

		kafkaProducer.send("example-catalog-topic", orderDto);
		orderProducer.send("orders", orderDto);

		ResponseOrder res = mapper.map(orderDto, ResponseOrder.class);

		log.info("After Added orders data");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> createOrder(@PathVariable("userId") String userId) throws Exception{

		log.info("Before retrieve orders data");
		Iterable<OrderEntity> OrderList = orderService.getOrdersByUserId(userId);

		List<ResponseOrder> result = new ArrayList<ResponseOrder>();

		for(OrderEntity Order : OrderList) {
			result.add(new ModelMapper().map(Order, ResponseOrder.class));
		}

		try{
			Thread.sleep(1000);
			throw new Exception("장애");
		}
		catch (InterruptedException e) {
			log.error(e.getMessage());
		}

		log.info("After retrieve orders data");

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
