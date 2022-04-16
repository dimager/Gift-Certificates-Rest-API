package com.epam.ems.controller;

import com.epam.ems.entity.Tag;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class TagControllerTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private String userToken;
    private String adminToken;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        userToken = jwtTokenProvider.createToken("user", "USER", 1L);
        adminToken = jwtTokenProvider.createToken("admin", "ADMIN", 2L);
    }

    @Test
    void getAllTags() throws Exception {
        mvc.perform(get("/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*")
                        .isArray()).andReturn();
    }

    @Test
    void getTag() throws Exception {
        int tagId = 1;
        String tagName = "video1";
        mvc.perform(get("/tags/" + tagId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tagName))
                .andReturn();
    }

    @Test
    void getAbsentTag() throws Exception {
        int tagId = 1000000;
        mvc.perform(get("/tags/" + tagId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void addTag() throws Exception {
        String tag = "{\"name\":\"newTag\"}";

        mvc.perform(post("/tags").header(HttpHeaders.AUTHORIZATION, userToken).contentType(MediaType.APPLICATION_JSON).content(tag))
                .andDo(print())
                .andExpect(status().isForbidden());

        MvcResult mvcResult = mvc.perform(post("/tags").header(HttpHeaders.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON).content(tag))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newTag")).andReturn();

        Tag tagFromDB = mapFromJson(mvcResult.getResponse().getContentAsString(), Tag.class);

        mvc.perform(get("/tags/" + tagFromDB.getId()).header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(jsonPath("$.name").value("newTag"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTag() throws Exception {
        String tag = "{\"name\":\"updatedName\"}";
        String newTagName = "updatedName";
        mvc.perform(put("/tags/100").header(HttpHeaders.AUTHORIZATION, userToken).contentType(MediaType.APPLICATION_JSON).content(tag))
                .andExpect(status().isForbidden());
        mvc.perform(put("/tags/100").header(HttpHeaders.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON).content(tag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value(newTagName));
    }

    @Test
    void mostPopularTag() throws Exception {
        mvc.perform(get("/tags/mostpopular")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void deleteTag() throws Exception {
        mvc.perform(delete("/tags/1").header(HttpHeaders.AUTHORIZATION, userToken)).andExpect(status().isForbidden());
        mvc.perform(delete("/tags/1").header(HttpHeaders.AUTHORIZATION, adminToken)).andExpect(status().isOk());
        mvc.perform(get("/tags/1").header(HttpHeaders.AUTHORIZATION, adminToken)).andExpect(status().isNotFound());
    }
}
