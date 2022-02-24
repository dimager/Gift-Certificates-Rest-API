package com.epam.ems.controller;

import com.epam.ems.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.ems.controller.mapper.JsonMapper.mapFromJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("controller_test")
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;


    @Test
    void getOrders() throws Exception {
        mvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*")
                        .isArray()).andReturn();
    }

    @Test
    void getOrder() throws Exception {
        int orderId = 200;
        mvc.perform(get("/orders/" + orderId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.purchaseDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty());
    }

    @Test
    void getUserOrders() throws Exception {
        int userid = 1;
        mvc.perform(get("/orders?userId=" + userid))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void createOrder() throws Exception {
        long userId = 100;
        String order = "{\"orderCertificates\": [{\"certificate\": {\"id\": 200},\"amount\": 2},{\"certificate\": {\"id\": 400},\"amount\": \"3\"}]}";
        MvcResult result = mvc.perform(post("/orders?userId=" + userId).contentType(MediaType.APPLICATION_JSON).content(order))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.purchaseDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty())
                .andExpect(jsonPath("$.orderCertificates").isArray())
                .andReturn();
        Order createdOrder = mapFromJson(result.getResponse().getContentAsString(), Order.class);
        mvc.perform(get("/orders/" + createdOrder.getId())).andExpect(status().isOk());
    }
}