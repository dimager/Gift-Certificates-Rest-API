package com.epam.ems.controller;

import com.epam.ems.entity.User;
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
class UserControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void getUsers() throws Exception {
        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        int userId = 1;
        mvc.perform(get("/users/" + userId))
                .andDo(print())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void createUser() throws Exception {
        String newUser = "{\"username\": \"newUsername\"}";
        MvcResult mvcResult = mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(newUser))
                .andDo(print())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(status().isOk())
                .andReturn();
        User user = mapFromJson(mvcResult.getResponse().getContentAsString(), User.class);
        mvc.perform(get("/users/" + user.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("newUsername"))
                .andExpect(status().isOk());

    }
}