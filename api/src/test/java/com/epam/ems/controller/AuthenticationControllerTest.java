package com.epam.ems.controller;

import com.epam.ems.entity.User;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
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
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AuthenticationControllerTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    private JwtTokenProvider jwtTokenProvider;
    private  String userToken;
    private String adminToken;
    @BeforeEach
    void setUp() {
        userToken = jwtTokenProvider.createToken("user","USER", 1L);
        adminToken = jwtTokenProvider.createToken("admin","ADMIN", 2L);
    }

    @Test
    void createUser() throws Exception {
        String newUser = "{\"username\": \"newUsername\",\"password\": \"Pa$$w0rd\"}";
        MvcResult mvcResult = mvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON).content(newUser))
                .andDo(print())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(status().isOk())
                .andReturn();
        User user = mapFromJson(mvcResult.getResponse().getContentAsString(), User.class);
        mvc.perform(get("/users/" + user.getId()).header(HttpHeaders.AUTHORIZATION, adminToken))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("newUsername"))
                .andExpect(status().isOk());
    }


    @Test
    void login() throws Exception {
        String user = "{\n" +
                "    \"username\": \"user\",\n" +
                "    \"password\": \"PASS\"\n" +
                "}";
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(status().isOk());
        String incorrectPasswordUser = "{\"username\": \"user\",\"password\": \"PASS1\"}";
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(incorrectPasswordUser))
                .andExpect(status().isBadRequest());
    }

}