package com.sdc.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.catalogservice.jpa.CatalogEntity;
import com.sdc.catalogservice.service.CatalogService;
import com.sdc.catalogservice.vo.ResponseCatalog;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {

	private final Environment env;
	private final CatalogService catalogService;

//	public CatalogController(Environment env, CatalogService catalogService) {
//		this.env = env;
//		this.catalogService = catalogService;
//	}

	@GetMapping("/health_check")
	public String status() {

		return String.format("It's Working in Catalog Service on PORT %s", env.getProperty("local.server.port")) ;
	}


	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getCatalogs(){
		Iterable<CatalogEntity> CatalogList = catalogService.getAllCatalogs();

		List<ResponseCatalog> result = new ArrayList<ResponseCatalog>();

		for(CatalogEntity Catalog : CatalogList) {

			result.add(new ModelMapper().map(Catalog, ResponseCatalog.class));
		}

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
