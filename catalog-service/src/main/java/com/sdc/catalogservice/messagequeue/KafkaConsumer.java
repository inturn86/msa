package com.sdc.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc.catalogservice.jpa.CatalogEntity;
import com.sdc.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final CatalogRepository catalogRepository;

	@KafkaListener(topics = "example-catalog-topic")
	public void updateQty(String kafkaMessage) {
		log.info("Kafka Message: -> " + kafkaMessage);

		Map<Object, Object> map = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
			});
		}
		catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		CatalogEntity entity = catalogRepository.findByProductId((String)map.get("productId"));
		if(ObjectUtils.isEmpty(entity)) {
			entity.setStock(entity.getStock() - (Integer) map.get("qty"));
			catalogRepository.save(entity);
		}
	}
}
