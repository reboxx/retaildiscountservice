package com.example.retaildiscountservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.retaildiscountservice.dto.BillRequest;
import com.example.retaildiscountservice.dto.BillResponse;
import com.example.retaildiscountservice.model.Bill;
import com.example.retaildiscountservice.model.Customer;
import com.example.retaildiscountservice.model.Item;
import com.example.retaildiscountservice.model.ItemCategory;
import com.example.retaildiscountservice.model.Role;
import com.example.retaildiscountservice.repository.BillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class RetaildiscountserviceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BillRepository billRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testEmployeeNonGroceryDiscount() throws Exception {

		when(billRepository.save(any(Bill.class))).thenReturn(null);

		BillRequest request = getEmployeeRequest();

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(90.0, response.getPercentageDiscount()); // 30% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(195.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testEmployeeGroceryDiscount() throws Exception {

		BillRequest request = getEmployeeRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);
		request.getItems().get(1).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(0.0, response.getPercentageDiscount()); // 30% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(285.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testEmployeeMixedCategoryDiscount() throws Exception {

		BillRequest request = getEmployeeRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(15.0, response.getPercentageDiscount()); // 30% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(270.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testLoyalCustomerNonGroceryDiscount() throws Exception {

		BillRequest request = getLoyalCustomerRequest();

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(15.0, response.getPercentageDiscount()); // 5% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(270.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testLoyalCustomerGroceryDiscount() throws Exception {

		BillRequest request = getLoyalCustomerRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);
		request.getItems().get(1).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(0.0, response.getPercentageDiscount()); // 0% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(285.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testLoyalCustomerMixedCategoryDiscount() throws Exception {

		BillRequest request = getLoyalCustomerRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(2.5, response.getPercentageDiscount()); // 30% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(282.5, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testAffiliateNonGroceryDiscount() throws Exception {

		BillRequest request = getAffiliateRequest();

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(30.0, response.getPercentageDiscount()); // 5% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(255.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testAffiliateGroceryDiscount() throws Exception {

		BillRequest request = getAffiliateRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);
		request.getItems().get(1).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(0.0, response.getPercentageDiscount()); // 0% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(285.0, response.getNetPayable()); // total - percentage - flat

	}

	@Test
	void testAffiliateMixedCategoryDiscount() throws Exception {

		BillRequest request = getAffiliateRequest();
		request.getItems().get(0).setCategory(ItemCategory.GROCERY);

		String responseString = mockMvc
				.perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

		assertEquals(300.0, response.getTotalAmount()); // 300
		assertEquals(5.0, response.getPercentageDiscount()); // 30% )
		assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
		assertEquals(280.0, response.getNetPayable()); // total - percentage - flat

	}

	private BillRequest getEmployeeRequest() {

		BillRequest request = new BillRequest();
		Customer cust = new Customer(1, "test_name", Role.EMPLOYEE, LocalDate.now());
		List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
				new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
		request.setCustomer(cust);
		request.setItems(items);

		return request;
	}

	private BillRequest getLoyalCustomerRequest() {

		BillRequest request = new BillRequest();
		Customer cust = new Customer(1, "test_name", Role.LOYAL_CUSTOMER, LocalDate.now().minusYears(4));
		List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
				new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
		request.setCustomer(cust);
		request.setItems(items);

		return request;
	}

	private BillRequest getAffiliateRequest() {

		BillRequest request = new BillRequest();
		Customer cust = new Customer(1, "test_name", Role.AFFILIATE, LocalDate.now());
		List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
				new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
		request.setCustomer(cust);
		request.setItems(items);

		return request;
	}

}
