package com.mercadolibre.grupo1.projetointegrador.services;

import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ListIsEmptyException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.NotFoundException;
import com.mercadolibre.grupo1.projetointegrador.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nayara Coca
 * responsável por mandar mensagem de erro se produto não for encontrado
 */

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;


    public List<WarehouseProductDTO> findWarehouse(Long productsId) {
        List<WarehouseProductDTO> stockByWarehouseProduct = warehouseRepository.findProductsInWarehouse(productsId);
        if(stockByWarehouseProduct.isEmpty()){
            throw new NotFoundException("PRODUTO NÃO ENCONTRADO");
        }
        return stockByWarehouseProduct;

    }

    /**
     * @author Jefferson Botelho
     * Criar um novo Warehouse e Listar todos os Warehouses
     */
    @Transactional  // so sera persistido no BD se a funcao for executada com sucesso
    public WarehouseDTO createWarehouse(WarehouseDTO warehouse) {

        // o warehouse ira pegar todos os dados informados pelo dto
        Warehouse responseWarehouse = Warehouse.builder()
                .name(warehouse.getName())
                .address(warehouse.getAdress())
                .build();

        /* o warehouse ira salvar os dados informados pelo dto atraves do repository
           e ira armazenar as informacoes em um obj */
        Warehouse createdWarehouse = warehouseRepository.save(responseWarehouse);
        // o ID do Warehouse sera informado ao dto que sera utilizado pelo controller que esta chamando este service
        warehouse.setId(createdWarehouse.getId());

        return warehouse;
    }


    // a funcao ira retornar uma lista com todos os armazens cadastrados
    public List<WarehouseDTO> listAllWarehouse() {

        List<WarehouseDTO> allWarehouse =  warehouseRepository.findAll().stream()
                .map(warehouse -> new WarehouseDTO(warehouse.getId(), warehouse.getName(), warehouse.getAddress())).collect(Collectors.toList());
        if(allWarehouse.isEmpty()){
            throw new ListIsEmptyException("Warehouse nao encontrado");
        }
        return allWarehouse;
    }

    /**
     * @author Rogério Lambert
     * metodo busca armazém por id, e lança exceção caso não encontre
     */

    public Warehouse findById(long warehouseId) {
        String errorMessage = "A warehouse com ID " + warehouseId + " não está cadastrada";
        return warehouseRepository
                .findById(warehouseId)
                .orElseThrow(() ->
                        new EntityNotFoundException(errorMessage));
    }

}
