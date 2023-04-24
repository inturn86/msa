package com.sdc.orderservice;

import com.sdc.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
public class OrderServiceTest {

	@MockBean
	private OrderServiceImpl orderRepository;

	@Test
	public void test(){

	}
}
