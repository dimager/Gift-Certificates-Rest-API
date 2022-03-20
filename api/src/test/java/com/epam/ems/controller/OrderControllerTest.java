package com.epam.ems.controller;

import com.epam.ems.entity.Order;
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
@ActiveProfiles("controller_test")
class OrderControllerTest {
    private String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpc3MiOiJhdXRoMCIsImlkIjoxLCJleHAiOjE2NDgzNTE5NjJ9." +
            "tVnU0fa44BXBAOwHNskx3WvL3Rg7twLetLo46aoJi-U";

    private String adminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlzcyI6ImF1dGgwIiwiaWQiOjIsImV4cCI6MTY0ODM1MTYyNH0" +
            ".mO0vZDehfguYesegEFwDcNVi19kHIrnzFn30tDvRI34";


    @Autowired
    private MockMvc mvc;

    @Test
    void getOrders() throws Exception {
        mvc.perform(get("/orders").header(HttpHeaders.AUTHORIZATION, adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*")
                        .isArray()).andReturn();
        mvc.perform(get("/orders").header(HttpHeaders.AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isForbidden());
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
        int userid = 1;
        int incorrectUserId = 2;
        mvc.perform(get("/orders?userId=" + userid).header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(status().isOk());
        mvc.perform(get("/orders?userId=" + incorrectUserId).header(HttpHeaders.AUTHORIZATION, userToken))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(status().isForbidden());
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