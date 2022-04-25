package com.epam.ems.controller;

import com.epam.ems.entity.Order;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
@ActiveProfiles("dev")
class OrderControllerTest {
    private JwtTokenProvider jwtTokenProvider;
    private String userToken;
    private String adminToken;

    @Autowired
    private MockMvc mvc;

    @Autowired
    OrderControllerTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        userToken = jwtTokenProvider.createToken("user", "USER", 1L);
        adminToken = jwtTokenProvider.createToken("admin", "ADMIN", 2L);
    }


    @Test
    void getOrders() throws Exception {
        mvc.perform(get("/orders").header(HttpHeaders.AUTHORIZATION, adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*")
                        .isArray()).andReturn();
        mvc.perform(get("/orders").header(HttpHeaders.AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getOrder() throws Exception {
        int orderId = 200;
        int userOrderId = 62;
        mvc.perform(get("/orders/" + orderId).header(HttpHeaders.AUTHORIZATION, adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.purchaseDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty());

        mvc.perform(get("/orders/" + orderId).header(HttpHeaders.AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isForbidden());

        mvc.perform(get("/orders/" + userOrderId).header(HttpHeaders.AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userOrderId))
                .andExpect(jsonPath("$.purchaseDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty());
    }

    @Test
    void getUserOrders() throws Exception {
        mvc.perform(get("/orders").header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void createOrder() throws Exception {
        long userId = 1;
        String order = "{\"orderCertificates\": [{\"certificate\": {\"id\": 200},\"amount\": 2},{\"certificate\": {\"id\": 400},\"amount\": \"3\"}]}";
        MvcResult result = mvc.perform(post("/orders?userId=" + userId).header(HttpHeaders.AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON).content(order))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.purchaseDate").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty())
                .andExpect(jsonPath("$.orderCertificates").isArray())
                .andReturn();
        Order createdOrder = mapFromJson(result.getResponse().getContentAsString(), Order.class);
        mvc.perform(get("/orders/" + createdOrder.getId()).header(HttpHeaders.AUTHORIZATION, userToken)).andExpect(status().isOk());
    }
}