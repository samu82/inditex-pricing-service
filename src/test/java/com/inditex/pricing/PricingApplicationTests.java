package com.inditex.pricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PricingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static final String URL_API = "/api/v1/prices";
	private static final String BRAND_ID = "1";
	private static final String PRODUCT_ID = "35455";

	@Test
	@DisplayName("Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
	void test1() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "2020-06-14T10:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.productId").value(35455))
				.andExpect(jsonPath("$.brandId").value(1))
				.andExpect(jsonPath("$.priceList").value(1))
				.andExpect(jsonPath("$.price").value(35.50));
	}

	@Test
	@DisplayName("Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
	void test2() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "2020-06-14T16:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.priceList").value(2))
				.andExpect(jsonPath("$.price").value(25.45));
	}

	@Test
	@DisplayName("Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
	void test3() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "2020-06-14T21:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.priceList").value(1))
				.andExpect(jsonPath("$.price").value(35.50));
	}

	@Test
	@DisplayName("Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)")
	void test4() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "2020-06-15T10:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.priceList").value(3))
				.andExpect(jsonPath("$.price").value(30.50));
	}

	@Test
	@DisplayName("Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)")
	void test5() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "2020-06-16T21:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.priceList").value(4))
				.andExpect(jsonPath("$.price").value(38.95));
	}

	@Test
	@DisplayName("Test Extra: Producto inexistente devuelve 404 Not Found")
	void testProductNotFound() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", "99999")
						.param("applicationDate", "2020-06-14T10:00:00")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Not Found"));
	}

	@Test
	@DisplayName("Test Extra: Formato de fecha incorrecto devuelve 400 Bad Request")
	void testBadRequestFormat() throws Exception {
		mockMvc.perform(get(URL_API)
						.param("brandId", BRAND_ID)
						.param("productId", PRODUCT_ID)
						.param("applicationDate", "fecha-incorrecta")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Bad Request"));
	}
}