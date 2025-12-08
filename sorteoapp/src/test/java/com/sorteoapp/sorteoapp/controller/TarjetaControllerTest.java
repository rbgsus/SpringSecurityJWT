package com.sorteoapp.sorteoapp.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorteoapp.sorteoapp.dto.CrearTarjetaDto;
import com.sorteoapp.sorteoapp.dto.GetTarjetaDto;
import com.sorteoapp.sorteoapp.dto.TarjetaDtoConverter;
import com.sorteoapp.sorteoapp.model.Categoria;
import com.sorteoapp.sorteoapp.model.Tarjeta;
import com.sorteoapp.sorteoapp.model.UserEntity;
import com.sorteoapp.sorteoapp.service.TarjetaService;

class TarjetaControllerTest {

    @Mock
    private TarjetaService tarjetaService;

    @Mock
    private TarjetaDtoConverter tarjetaDtoConverter;

    @InjectMocks
    private TarjetaController tarjetaController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tarjetaController).build();

        // Mock usuario y SecurityContext
        mockUser = UserEntity.builder().id(1L).username("testuser").build();

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(mockUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testNuevaTarjeta() throws Exception {
        CrearTarjetaDto crearDto = new CrearTarjetaDto();
        crearDto.setNombreTarjeta("Tarjeta 1");
        crearDto.setPrecio(10.0);
        crearDto.setCategoria(Categoria.TECNOLOGÍA); // enum
        crearDto.setImagenesBase64(List.of("base64test"));

        Tarjeta tarjeta = new Tarjeta();
        GetTarjetaDto getDto = GetTarjetaDto.builder().nombreTarjeta("Tarjeta 1").build();

        when(tarjetaDtoConverter.converterCrearTarjetaDtoToTarjeta(crearDto, mockUser)).thenReturn(tarjeta);
        when(tarjetaDtoConverter.converterTarjetaToGetTarjetaDto(tarjeta)).thenReturn(getDto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/cards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreTarjeta").value("Tarjeta 1"));

        verify(tarjetaService).nuevaTarjeta(tarjeta);
    }


    @Test
    void testObtenerTarjetasDelUsuario() throws Exception {
        Tarjeta tarjeta = new Tarjeta();
        GetTarjetaDto dto = GetTarjetaDto.builder().nombreTarjeta("T1").build();

        when(tarjetaService.findByUsuarioId(1L)).thenReturn(List.of(tarjeta));
        when(tarjetaDtoConverter.converterTarjetaToGetTarjetaDto(tarjeta)).thenReturn(dto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/cards/my-cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreTarjeta").value("T1"));
    }

    @Test
    void testObtenerTodasTarjetas2() throws Exception {
        Tarjeta tarjeta = new Tarjeta();
        GetTarjetaDto dto = GetTarjetaDto.builder().nombreTarjeta("T1").build();

        when(tarjetaService.findAll()).thenReturn(List.of(tarjeta));
        when(tarjetaDtoConverter.converterTarjetaToGetTarjetaDto(tarjeta)).thenReturn(dto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/cards/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreTarjeta").value("T1"));
    }

    @Test
    void testObtenerTodasTarjetasConPaginacion() throws Exception {
        Tarjeta tarjeta = new Tarjeta();
        GetTarjetaDto dto = GetTarjetaDto.builder().nombreTarjeta("T1").build();
        Page<Tarjeta> page = new PageImpl<>(List.of(tarjeta), PageRequest.of(0, 12), 1);

        when(tarjetaService.findAll(PageRequest.of(0, 12))).thenReturn(page);
        when(tarjetaDtoConverter.converterTarjetaToGetTarjetaDto(tarjeta)).thenReturn(dto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/cards/")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreTarjeta").value("T1"));
    }
}
