package com.pruebas.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.api.entity.Paciente;
import com.pruebas.api.exception.InvalidRequestException;
import com.pruebas.api.exception.NotFoundException;
import com.pruebas.api.service.PacienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PacienteController.class) //especifica a Spring Boot que se utiliza para probar controladores
public class PacienteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacienteService pacienteService;

    Paciente paciente_001 = new Paciente(1l, "Hugo Arévalo", 21, "hugoarevalogil08@gmail.com");
    Paciente paciente_002 = new Paciente(2l, "Pablo García", 29, "pablog2@gmail.com");
    Paciente paciente_003 = new Paciente(3l, "Mario Castaño", 38, "mariocasta23@gmail.com");

    @Test
    @DisplayName("Listado de pacientes")
    void testListarPacientes() throws Exception {
        List<Paciente> pacientes = new ArrayList<>(Arrays.asList(paciente_001, paciente_002, paciente_003));

        Mockito.when(pacienteService.getAllPacientes())
                .thenReturn(pacientes);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/pacientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].nombre", is("Mario Castaño")));
    }

    @Test
    @DisplayName("Listado de un solo paciente")
    void testListarPacientesPorId() throws Exception {
        Mockito.when(pacienteService.getPacienteById(paciente_001.getPacienteId()))
                .thenReturn(Optional.of(paciente_001));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Hugo Arévalo")));
    }

    @Test
    @DisplayName("Guardar un paciente")
    void testGuardarPaciente() throws Exception {
        Paciente paciente = Paciente.builder()
                .pacienteId(4l)
                .nombre("Juan Gómez")
                .edad(15)
                .correo("juan15@gmail.com")
                .build();

        Mockito.when(pacienteService.createPaciente(paciente)).thenReturn(paciente);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Juan Gómez")));
    }

    @Test
    @DisplayName("Actualizar un paciente con éxito")
    void testActualizarPacienteConExito() throws Exception {
        Paciente pacienteUpdate = Paciente.builder()
                .pacienteId(1l)
                .nombre("Ramiro López")
                .edad(28)
                .correo("ramirolopez5@gmail.com")
                .build();

        Mockito.when(pacienteService.getPacienteById(paciente_001.getPacienteId())).thenReturn(Optional.of(paciente_001));
        Mockito.when(pacienteService.updatePaciente(pacienteUpdate)).thenReturn(pacienteUpdate);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteUpdate));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Ramiro López")));
    }

    @Test
    @DisplayName("Eliminar paciente")
    void testEliminarPacienteConExito() throws Exception {
        Mockito.when(pacienteService.getPacienteById(paciente_002.getPacienteId())).thenReturn(Optional.of(paciente_002));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/pacientes/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
