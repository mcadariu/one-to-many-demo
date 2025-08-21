package com.parent_child.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parent_child.demo.dtos.AuthorWithBooksDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class DemoApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testNPlusOneEndpoint() throws Exception {
        while(true) { }
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

//        long startTime = System.currentTimeMillis();
//        mockMvc.perform(get("/api/authors/n-plus-one"))
//                .andExpect(status().isOk())
//                .andReturn();
//        long endTime = System.currentTimeMillis();
//
//        System.out.println(endTime - startTime);
    }

    @Test
    void testJoinFetchEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/api/authors/join-fetch"))
                .andExpect(status().isOk())
                .andReturn();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);
    }

    @Test
    void testJsonbAggEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/api/authors/jsonb-agg"))
                .andExpect(status().isOk())
                .andReturn();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);
    }

    @Test
    void testAllEndpointsReturnSameData() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Call all three endpoints
        String nPlusOneResponse = mockMvc.perform(get("/api/authors/n-plus-one"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String joinFetchResponse = mockMvc.perform(get("/api/authors/join-fetch"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String jsonbAggResponse = mockMvc.perform(get("/api/authors/jsonb-agg"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse all responses
        List<AuthorWithBooksDto> nPlusOneAuthors = objectMapper.readValue(nPlusOneResponse,
                new TypeReference<>() {});
        List<AuthorWithBooksDto> joinFetchAuthors = objectMapper.readValue(joinFetchResponse,
                new TypeReference<>() {});
        List<AuthorWithBooksDto> jsonbAggAuthors = objectMapper.readValue(jsonbAggResponse,
                new TypeReference<>() {});

        // Verify all return same number of authors
        assertEquals(1000, nPlusOneAuthors.size());
        assertEquals(1000, joinFetchAuthors.size());
        assertEquals(1000, jsonbAggAuthors.size());
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.show_sql", () -> true);
    }
}
