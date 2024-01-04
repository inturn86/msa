package com.sdc.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc.orderservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
			new Field("string", true, "user_id"),
			new Field("string", true, "product_id"),
			new Field("int32", true, "qty"),
			new Field("int32", true, "unit_price"),
			new Field("int32", true, "total_price"));

	Schema schema = Schema.builder().type("struct").fields(fields).optional(false).name("orders").build();

	public OrderDto send(String topic, OrderDto dto) {
		Payload paylaod = Payload.builder().order_id(dto.getOrderId())
				.user_id(dto.getUserId())
				.product_id(dto.getProductId())
				.qty(dto.getQty())
				.unit_price(dto.getUnitPrice())
				.total_price(dto.getTotalPrice()).build();

		KafkaOrderDTO kafkaOrderDTO = new KafkaOrderDTO(schema, paylaod);

		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaOrderDTO);
		}
		catch (JsonProcessingException ex){
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Order Producer send data from the Order microservice : " + kafkaOrderDTO);
		return dto;
	}
}
