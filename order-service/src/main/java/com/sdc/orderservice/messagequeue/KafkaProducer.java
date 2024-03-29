package com.sdc.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public OrderDto send(String topic, OrderDto dto) {
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(dto);
		}
		catch (JsonProcessingException ex){
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Kafka Producer send data from the Order microservice : " + dto);
		return dto;
	}
}
