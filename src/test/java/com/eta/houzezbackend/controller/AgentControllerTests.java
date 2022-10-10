package com.eta.houzezbackend.controller;

import com.eta.houzezbackend.dto.AgentSignUpDto;
import com.eta.houzezbackend.repository.AgentRepository;
import com.eta.houzezbackend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AgentControllerTests extends ApplicationIntTest{
    @Autowired
    private AgentController agentController;

    @Autowired
    private JwtService jwtService;

    private Long mockUserId;

    private String mockUserEmail;

    private String mockJwt;

    private String mockFakeJwt;

    @Autowired
    private AgentRepository agentRepository;

    @BeforeEach
    void signUp() {
        agentRepository.deleteAll();
        agentRepository.flush();
        mockUserId = agentController.signUp(AgentSignUpDto.builder().email("test3@gmail.com")
                .password("123qqqqq.").build()).getId();
        mockUserEmail = agentController.signUp(AgentSignUpDto.builder().email("agent002@gmail.com")
                .password("agent002@gmail.comA.").build()).getEmail();
        String mockUserName = agentController.getAgent(mockUserId).getName();
        mockJwt = jwtService.createJWT(String.valueOf(mockUserId),mockUserName,80000);
        mockFakeJwt = jwtService.createJWT(String.valueOf(mockUserId),mockUserName,-80000);

    }

    @Test
    void shouldReturn201AndAgentDtoWhenAgentIsCreated() throws Exception {
        AgentSignUpDto agentSignUpDto = AgentSignUpDto.builder().email("test4@gmail.com").password("123qqqqq.").build();

        mockMvc.perform(post("/agents")
                        .content(objectMapper.writeValueAsString(agentSignUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test4@gmail.com"));
    }

    @Test
    void shouldReturn200AndAgentDtoWhenGetAgentDto() throws Exception {
        mockMvc.perform(get("/agents/" + mockUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserId));
    }

    @Test
    void shouldReturn200WhenFindByEmail() throws Exception {
        mockMvc.perform(head("/agents?email=" + mockUserEmail))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenCannotFindByEmail() throws Exception {
        mockMvc.perform(head("/agents?email=t" + mockUserEmail))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200AndAgentWhenSetAgentToActive() throws Exception{
        mockMvc.perform(patch("/agents/decode/" + mockJwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activated").value(true));
    }

    @Test
    void shouldReturn401WhenSetAgentToActive() throws Exception{
        mockMvc.perform(patch("/agents/decode/" + mockFakeJwt))
                .andExpect(status().isUnauthorized());

    }
}