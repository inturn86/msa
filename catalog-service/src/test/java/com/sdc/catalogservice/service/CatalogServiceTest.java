package com.sdc.catalogservice.service;

import com.sdc.catalogservice.jpa.CatalogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
public class CatalogServiceTest {

	@MockBean
	private CatalogServiceImpl catalogRepository;


//	public CatalogServiceTest(CatalogRepository catalogRepository) {
//		this.catalogRepository = catalogRepository;
//	}

	@Test
	@DisplayName("카탈로그 등록")
	public void test(){

	}

}
