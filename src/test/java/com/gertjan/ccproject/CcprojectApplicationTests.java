package com.gertjan.ccproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gertjan.ccproject.domain.DataObject;
import com.gertjan.ccproject.repo.DataObjectRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CcprojectApplicationTests {

    // Yes these are *technically* integration tests.
    // But since our controller & repository barely do any work, this is fine..


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DataObjectRepository repository;


    @BeforeEach
    void init() {
        // Clean slate for each test.
        repository.deleteAll();
    }

    @Test
    void firstGetShouldBeEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addThenGetShouldGiveNewObject() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        DataObject object = new DataObject();
        object.setName("Name1");
        object.setDescription("This is an optional description");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/data/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(object)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].description").value("This is an optional description"));
    }

    @Test
    void addObjectShouldAllowGetOnId() throws Exception {

        DataObject object = new DataObject();
        object.setName("Name1");
        object.setDescription("This is an optional description");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/data/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(object)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        String id = JsonPath.read(content, "$[0].id");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(String.format("/data/%s", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("description").value("This is an optional description"));
    }

    @Test
    void addAndUpdateObjectShouldWork() throws Exception {

        DataObject object = new DataObject();
        object.setName("Name1");
        object.setDescription("This is an optional description");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/data/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(object)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        String id = JsonPath.read(content, "$[0].id");

        DataObject updateObject = new DataObject();

        updateObject.setName("Another name");
        updateObject.setDescription("Description 2: electric boogaloo");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(String.format("/data/%s", id))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updateObject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("description").value("Description 2: electric boogaloo"));
    }

    @Test
    void addAndDeleteObjectShouldWork() throws Exception {

        DataObject object = new DataObject();
        object.setName("Name1");
        object.setDescription("This is an optional description");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/data/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(object)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        String id = JsonPath.read(content, "$[0].id");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(String.format("/data/%s", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("description").value("This is an optional description"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(String.format("/data/%s", id)))
                .andExpect(status().isNoContent());

    }


    @Test
    void getFoobarIdShouldFail() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data/nonexistingId"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/data/5b9d149a-4593-4dee-834b-fbdbbbf360ed"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No entity with given ID found"));
        // This is technically a BAD way to do this in a real life application, as is technically allows ID enumeration
        // but for that reason we use UUID's, so it is fine...
    }
}
