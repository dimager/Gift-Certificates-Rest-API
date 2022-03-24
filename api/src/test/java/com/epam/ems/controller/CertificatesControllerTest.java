package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
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

import java.math.BigDecimal;

import static com.epam.ems.controller.mapper.JsonMapper.mapFromJson;
import static com.epam.ems.controller.mapper.JsonMapper.mapToJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("controller_test")
class CertificatesControllerTest {
    @Autowired
    private MockMvc mvc;

    private String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpc3MiOiJhdXRoMCIsImlkIjoxLCJleHAiOjE2NDgzNTE5NjJ9." +
            "tVnU0fa44BXBAOwHNskx3WvL3Rg7twLetLo46aoJi-U";

    private String adminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlzcyI6ImF1dGgwIiwiaWQiOjIsImV4cCI6MTY0ODM1MTYyNH0" +
            ".mO0vZDehfguYesegEFwDcNVi19kHIrnzFn30tDvRI34";
    @Test
    void getCertificates() throws Exception {
        mvc.perform(get("/certificates"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*").isArray());
    }

    @Test
    void getCertificate() throws Exception {
        int certificateId = 200;
        mvc.perform(get("/certificates/" + certificateId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificateId))
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.duration").isNotEmpty())
                .andExpect(jsonPath("$.createdDateTime").isNotEmpty())
                .andExpect(jsonPath("$.lastUpdatedDateTime").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void deleteCertificate() throws Exception {
        int certificateId = 220;
        mvc.perform(delete("/certificates/" + certificateId).header(HttpHeaders.AUTHORIZATION, adminToken)).andExpect(status().isOk());
        mvc.perform(delete("/certificates/" + certificateId).header(HttpHeaders.AUTHORIZATION, userToken)).andExpect(status().isForbidden());
        mvc.perform(get("/certificates/" + certificateId).header(HttpHeaders.AUTHORIZATION, adminToken)).andExpect(status().isNotFound());
    }

    @Test
    void deleteMissingCertificate() throws Exception {
        int certificateId = 11220;
        mvc.perform(delete("/certificates/" + certificateId).header(HttpHeaders.AUTHORIZATION, adminToken)).andExpect(status().isNotFound());
        mvc.perform(delete("/certificates/" + certificateId).header(HttpHeaders.AUTHORIZATION, userToken)).andExpect(status().isForbidden());
    }

    @Test
    void addCertificate() throws Exception {
        String newCert = "{\"name\": \"new_cert1\",\"description\": \"new_desc\",\"price\": 83.0,\"duration\": " +
                "4,\"tags\": [{\"name\": \"auto\" },{\"name\": \"food\"},{\"name\": \"newtag101\"}]}\n";
        MvcResult result = mvc.perform(post("/certificates").header(HttpHeaders.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON).content(newCert))
                .andExpect(status().isOk())
                .andReturn();
        Certificate certificate = mapFromJson(result.getResponse().getContentAsString(), Certificate.class);
        mvc.perform(get("/certificates/" + certificate.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificate.getId()))
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.duration").isNotEmpty())
                .andExpect(jsonPath("$.createdDateTime").isNotEmpty())
                .andExpect(jsonPath("$.lastUpdatedDateTime").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void updateCertificate() throws Exception {
        int certificateId = 240;
        MvcResult mvcResult = mvc.perform(get("/certificates/" + certificateId))
                .andExpect(status().isOk()).andReturn();
        Certificate certificate = mapFromJson(mvcResult.getResponse().getContentAsString(), Certificate.class);
        certificate.setName("newName");
        certificate.setDescription("newDescription");
        certificate.setPrice(new BigDecimal("10.53"));
        mvc.perform(put("/certificates/" + certificate.getId()).header(HttpHeaders.AUTHORIZATION, adminToken)
                        .contentType(MediaType.APPLICATION_JSON).content(mapToJson(certificate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(certificate.getName()))
                .andExpect(jsonPath("$.description").value(certificate.getDescription()))
                .andExpect(jsonPath("$.price").value(certificate.getPrice().toString()));

        mvc.perform(put("/certificates/" + certificate.getId()).header(HttpHeaders.AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON).content(mapToJson(certificate)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateCertificateDuration() throws Exception {
        short newDuration = 20;
        String newCertJson = "{\"name\": \"new_cert1\",\"description\": \"new_desc\",\"price\": 83.0,\"duration\": " +
                "4,\"tags\": [{\"name\": \"auto\" },{\"name\": \"food\"},{\"name\": \"newtag101\"}]}\n";
        MvcResult result = mvc.perform(post("/certificates").header(HttpHeaders.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON).content(newCertJson))
                .andExpect(status().isOk()).andReturn();
        Certificate certificate = mapFromJson(result.getResponse().getContentAsString(), Certificate.class);
        certificate.setDuration(newDuration);
        mvc.perform(patch("/certificates/" + certificate.getId()).header(HttpHeaders.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON).content(mapToJson(certificate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration").value(String.valueOf(newDuration)));
        mvc.perform(patch("/certificates/" + certificate.getId()).header(HttpHeaders.AUTHORIZATION, userToken).contentType(MediaType.APPLICATION_JSON).content(mapToJson(certificate)))
                .andExpect(status().isForbidden());
    }
}
