package com.sdc.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Schema {

	private String type;
	private List<Field> fields;
	private String name;
	private boolean optional;
}
