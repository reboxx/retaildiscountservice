package com.example.retaildiscountservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

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
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
class RetaildiscountserviceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillRepository billRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String ERROR_MSG = "Customer is blacklisted and not eligible for billing.";

    /*
     * test employee with non grocery items
     */
    @Test
    void testEmployeeNonGroceryDiscount() throws Exception {

        when(billRepository.save(any(Bill.class))).thenReturn(null);

        BillRequest request = getEmployeeRequest();

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(90.0, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(200.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test emplouee with only grocery items
     */
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

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(0.0, response.getPercentageDiscount());
        assertEquals(15.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test employee with grocery and non-grocery items
     */
    @Test
    void testEmployeeMixedCategoryDiscount() throws Exception {

        BillRequest request = getEmployeeRequest();
        request.getItems().get(0).setCategory(ItemCategory.GROCERY);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(15.0, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(275.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test employee who is blacklisted
     */
    @Test
    void testEmployeeBlackListed() throws Exception {

        BillRequest request = getEmployeeRequest();
        request.getCustomer().setBlacklisted(true);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

        assertEquals(responseString, ERROR_MSG);

    }

    /*
     * test loyal customer >2 years with only non-grocery items
     */
    @Test
    void testLoyalCustomerNonGroceryDiscount() throws Exception {

        BillRequest request = getLoyalCustomerRequest();

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(15.0, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(275.0, response.getTotalAmountAfterDiscount());
    }

    /*
     * test loyal customer >2 years with only grocery items
     */
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

        assertEquals(300.0, response.getTotalAmountBeforeDiscount()); // 300
        assertEquals(0.0, response.getPercentageDiscount()); // 0% )
        assertEquals(15.0, response.getFlatDiscount()); // floor(1050/100)*5
        assertEquals(285.0, response.getTotalAmountAfterDiscount()); // total - percentage - flat

    }

    /*
     * test loyal customer >2 years with only grocery and non-grocery items
     */
    @Test
    void testLoyalCustomerMixedCategoryDiscount() throws Exception {

        BillRequest request = getLoyalCustomerRequest();
        request.getItems().get(0).setCategory(ItemCategory.GROCERY);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(2.5, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(287.5, response.getTotalAmountAfterDiscount());

    }

    /*
     * test loyal customer <2 years with only non-grocery items
     */
    @Test
    void testLoyalCustomerNonGroceryDiscountLessThan2Years() throws Exception {

        BillRequest request = getLoyalCustomerRequest();
        request.getCustomer().setJoinDate(LocalDate.now());

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(0.0, response.getPercentageDiscount());
        assertEquals(15.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());
    }

    /*
     * test loyal customer <2 years with only grocery items
     */
    @Test
    void testLoyalCustomerGroceryDiscountLessThan2Years() throws Exception {

        BillRequest request = getLoyalCustomerRequest();
        request.getCustomer().setJoinDate(LocalDate.now());

        request.getItems().get(0).setCategory(ItemCategory.GROCERY);
        request.getItems().get(1).setCategory(ItemCategory.GROCERY);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(0.0, response.getPercentageDiscount());
        assertEquals(15.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test loyal customer <2 years with grocery and non-grocery items
     */
    @Test
    void testLoyalCustomerMixedCategoryDiscountLessThan2Years() throws Exception {

        BillRequest request = getLoyalCustomerRequest();
        request.getCustomer().setJoinDate(LocalDate.now());

        request.getItems().get(0).setCategory(ItemCategory.GROCERY);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(0.0, response.getPercentageDiscount());
        assertEquals(15.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test loyal customer who is blacklisted
     */
    @Test
    void testLoyalCustomerBlackListed() throws Exception {

        BillRequest request = getLoyalCustomerRequest();
        request.getCustomer().setBlacklisted(true);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

        assertEquals(ERROR_MSG, responseString);

    }

    /*
     * test affiliate customer with only non-grocery items
     */
    @Test
    void testAffiliateNonGroceryDiscount() throws Exception {

        BillRequest request = getAffiliateRequest();

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(30.0, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(260.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test affiliate customer with only grocery items
     */
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

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(0.0, response.getPercentageDiscount());
        assertEquals(15.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test affiliate customer with grocery and non-grocery items
     */
    @Test
    void testAffiliateMixedCategoryDiscount() throws Exception {

        BillRequest request = getAffiliateRequest();
        request.getItems().get(0).setCategory(ItemCategory.GROCERY);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BillResponse response = objectMapper.readValue(responseString, BillResponse.class);

        assertEquals(300.0, response.getTotalAmountBeforeDiscount());
        assertEquals(5.0, response.getPercentageDiscount());
        assertEquals(10.0, response.getFlatDiscount());
        assertEquals(285.0, response.getTotalAmountAfterDiscount());

    }

    /*
     * test affiliate customer who is blacklisted
     */
    @Test
    void testAffiliateDiscountBlackListed() throws Exception {

        BillRequest request = getAffiliateRequest();
        request.getCustomer().setBlacklisted(true);

        String responseString = mockMvc
                .perform(post("/calculate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

        assertEquals(ERROR_MSG, responseString);

    }

    private BillRequest getEmployeeRequest() {

        BillRequest request = new BillRequest();
        Customer cust = new Customer(1, "test_name", Role.EMPLOYEE, LocalDate.now(), false);
        List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
                new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
        request.setCustomer(cust);
        request.setItems(items);

        return request;
    }

    private BillRequest getLoyalCustomerRequest() {

        BillRequest request = new BillRequest();
        Customer cust = new Customer(1, "test_name", Role.LOYAL_CUSTOMER, LocalDate.now().minusYears(4), false);
        List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
                new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
        request.setCustomer(cust);
        request.setItems(items);

        return request;
    }

    private BillRequest getAffiliateRequest() {

        BillRequest request = new BillRequest();
        Customer cust = new Customer(1, "test_name", Role.AFFILIATE, LocalDate.now(), false);
        List<Item> items = List.of(new Item(1, "test_name", ItemCategory.NON_GROCERY, 250),
                new Item(2, "test_name", ItemCategory.NON_GROCERY, 50));
        request.setCustomer(cust);
        request.setItems(items);

        return request;
    }

}
