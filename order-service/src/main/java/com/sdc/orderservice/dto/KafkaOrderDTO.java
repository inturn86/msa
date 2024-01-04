package com.sdc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaOrderDTO implements Serializable {

	private Schema schema;
	private Payload payload;
}
