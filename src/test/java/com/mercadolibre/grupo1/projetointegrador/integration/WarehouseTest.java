package com.mercadolibre.grupo1.projetointegrador.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Agent;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para Warehouse
 * descrição de cada teste no displayName
 * @author Nayara Coca
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class WarehouseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/fresh-products";

    @Test
    @WithMockUser(username = "agent1", roles = {"AGENT"})
    @DisplayName("Testa se retorna a quantidade total de produtos por armazém")
    public void itShouldReturnTheTotalQuantityOfProductOnTheWarehouses() throws Exception {
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/warehouse?productId=1"))
                        .andExpect(status().isOk())
                        .andExpect((ResultMatcher) jsonPath("$.length()", Matchers.is(2)))
                        .andReturn();

        List<WarehouseProductDTO> finalResult =
                Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(),
                        WarehouseProductDTO[].class));
        assertEquals(2, finalResult.size());
        assertEquals(30, finalResult.get(0).getTotalQuantity());
    }

    @Test
    @WithMockUser(username = "agent1", roles = {"AGENT"})
    @DisplayName("Testa se retorna mensagem de erro quando o produto não existe em nenhum armazém")
    public void itShouldReturnAnException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/warehouse?productId=15").contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("PRODUTO NÃO ENCONTRADO"));
    }

    /**
     * @author Jefferson Botelho
     * Req06 Testes te integracao
     */

    // Verifica se e criado um novo warehouse
    @Test
    @WithMockUser(username = "agent1", roles = {"AGENT"})
    public void createWarehouse() throws Exception {

        WarehouseDTO warehouse = warehouseFake();
        String s = objectMapper.writeValueAsString(warehouse);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/warehouse/new").contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("MG")))
                .andExpect(jsonPath("$.adress", Matchers.is("39895-000"))).andReturn();


    }

    // verifica se e retornado os warehouses cadastrados
    @Test
    @WithMockUser(username = "agent1", roles = {"AGENT"})
    public void listAllWarehousesTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/warehouse/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].name", Matchers.is("SP")))
                .andExpect(jsonPath("$.[1].adress", Matchers.is("22222-000")));
    }

    private WarehouseDTO warehouseFake() {
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setName("MG");
        warehouseDTO.setAdress("39895-000");
        return warehouseDTO;
    }
}
