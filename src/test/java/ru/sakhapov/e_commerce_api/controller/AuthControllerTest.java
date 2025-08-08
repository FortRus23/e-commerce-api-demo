package ru.sakhapov.e_commerce_api.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.sakhapov.e_commerce_api.api.controller.auth.AuthenticationResponse;
import ru.sakhapov.e_commerce_api.api.exception.BadRequestException;
import ru.sakhapov.e_commerce_api.api.exception.UsernameNotFoundException;
import ru.sakhapov.e_commerce_api.store.service.AuthenticationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void register_existingUser_returnsBadRequest() throws Exception {
        Mockito.when(authenticationService.register(Mockito.any()))
                .thenThrow(new BadRequestException("Username already exists"));

        String json = """
        {
            "username": "existingUser",
            "password": "password123",
            "name": "John",
            "surname": "Doe"
        }
        """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(authenticationService.login(Mockito.any()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        String json = """
    {
        "username": "absentUser",
        "password": "password123"
    }
    """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void login_blankFields_returnsBadRequest() throws Exception {
        String json = """
    {
        "username": "",
        "password": ""
    }
    """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.username").value(Matchers.anyOf(
                        Matchers.containsString("empty"),
                        Matchers.containsString("size")
                )))
                .andExpect(jsonPath("$.validationErrors.password").value(Matchers.anyOf(
                        Matchers.containsString("empty"),
                        Matchers.containsString("size")
                )));
    }

    @Test
    public void register_success_returnsOk() throws Exception {
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .build();

        Mockito.when(authenticationService.register(Mockito.any()))
                .thenReturn(mockResponse);

        String json = """
    {
        "username": "newUser",
        "password": "password123",
        "name": "John",
        "surname": "Doe"
    }
    """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("dummyAccessToken"))
                .andExpect(jsonPath("$.refresh_token").value("dummyRefreshToken"));
    }

    @Test
    public void login_success_returnsOk() throws Exception {
        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .build();

        Mockito.when(authenticationService.login(Mockito.any()))
                .thenReturn(mockResponse);

        String json = """
    {
        "username": "existingUser",
        "password": "password123"
    }
    """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("dummyAccessToken"))
                .andExpect(jsonPath("$.refresh_token").value("dummyRefreshToken"));
    }
}

