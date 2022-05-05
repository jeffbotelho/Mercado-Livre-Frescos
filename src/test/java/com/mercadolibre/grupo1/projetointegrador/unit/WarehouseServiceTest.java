package com.mercadolibre.grupo1.projetointegrador.unit;

import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ListIsEmptyException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.NotFoundException;
import com.mercadolibre.grupo1.projetointegrador.repositories.WarehouseRepository;
import com.mercadolibre.grupo1.projetointegrador.services.WarehouseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Transactional
public class WarehouseServiceTest {
    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    /**
     * @author Jefferson Botelho
     * Testes unitarios do req6. A descricao de cada teste esta explicita no @DisplayName
     */

    @Test
    @DisplayName("Verifica se chamada do metodo esta correta ao criar um warehouse")
    public void createWarehouseValidationsTest() {

        WarehouseDTO saveWarehouse = new WarehouseDTO(1L, "MG", "12345-000");
        Warehouse save = warehouseFake().get(0);

        Mockito.when(warehouseRepository.save(Mockito.any())).thenReturn(save);
        warehouseService.createWarehouse(saveWarehouse);

        Mockito.verify(warehouseRepository).save(Mockito.any(Warehouse.class));
    }

    @Test
    @DisplayName("Verifica se a chamada do metodo esta correta")
    public void listAllWarehouseTest() {
        List<Warehouse> allWarehouses = warehouseFake();
        Mockito.when(warehouseRepository.findAll()).thenReturn(allWarehouses);

        Mockito.verify(warehouseRepository).findAll();
    }

    @Test
    @DisplayName("Verifica retorno de excecao caso a lista de warehouse estiver vazia")
    public void listAllWarehouseExceptionTest() {

        List<Warehouse> allWarehouses = new ArrayList<Warehouse>();
        Mockito.when(warehouseRepository.findAll()).thenReturn(allWarehouses);

        // action
        Exception err = assertThrows(ListIsEmptyException.class, () -> warehouseService.listAllWarehouse());

        // verificacoes
        Assertions.assertEquals(err.getMessage(), "Warehouse nao encontrado");
    }


    /**
     * @author Nayara Coca
     * descrição de cada teste no displayName
     */
    @Test
    @DisplayName("Testa se as quantidades de produtos mostrados no armazém ao pesquisar por ID de produto")
    public void itShouldReturnTheProductsByWarehouse(){
        List<WarehouseProductDTO> warehouse = createWarehouse();
        Mockito.when(warehouseRepository.findProductsInWarehouse(1L)).thenReturn(warehouse);
        List<WarehouseProductDTO> warehouse1 = warehouseService.findWarehouse(1L);
        Assertions.assertEquals(warehouse1, warehouse);
        Assertions.assertEquals(warehouse1.size(),1);
    }
    public List<WarehouseProductDTO> createWarehouse(){
        WarehouseProductDTO createWarehouse = new WarehouseProductDTO();
        createWarehouse.setWarehouseCode(1L);
        createWarehouse.setTotalQuantity(34L);
        WarehouseProductDTO createWarehouse1 = new WarehouseProductDTO();
        createWarehouse.setWarehouseCode(1L);
        createWarehouse.setTotalQuantity(44L);
        WarehouseProductDTO createWarehouse2 = new WarehouseProductDTO();
        createWarehouse.setWarehouseCode(1L);
        createWarehouse.setTotalQuantity(55L);

        return Arrays.asList(createWarehouse);
    }

    @Test
    @DisplayName("Testa se retorna erro ao pesquisar produto que não está localizado")
    public void ItShouldReturnANotFoundException() {
        Mockito.when(warehouseRepository.findProductsInWarehouse(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(
                NotFoundException.class,
                        () -> warehouseService.findWarehouse(1L));
        assertEquals(exception.getMessage(), "PRODUTO NÃO ENCONTRADO");

    }

    /**
     * @author Rogério Lambert
     * Testes unitarios do service de gestão da warehouse
     */
    @DisplayName("Testa se a query certa é chamada quando o método findById é chamado retornando um objeto Warehouse: ")
    public void itShouldCallFindById() {
        //setup do test
        Warehouse warehouse = Warehouse.builder().id(1L).build();
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        //execução
        Warehouse warehouseReturned = warehouseService.findById(1L);

        //verificação
        assertEquals(warehouse.getId(), warehouseReturned.getId());
    }

    @Test
    @DisplayName("Testa se uma exceção correta é lançada quando o armazém não é encontrado: ")
    public void itShouldThrowNotFoundEntity() {
        //setup do test
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        //execução
        Exception e = assertThrows(EntityNotFoundException.class, () -> warehouseService.findById(1L));

        //verificação
        assertEquals(e.getMessage(), "A warehouse com ID 1 não está cadastrada");
    }

    private List<Warehouse> warehouseFake() {
        List<Warehouse> builListdWarehouse = Arrays.asList(
                Warehouse.builder()
                        .id(1L).name("MG").address("39895-000").build(),
                Warehouse.builder()
                        .id(2L).name("SG").address("01319-000").build(),
                Warehouse.builder()
                        .id(3L).name("BA").address("40020-176").build()
        );
        return builListdWarehouse;
    }
}
