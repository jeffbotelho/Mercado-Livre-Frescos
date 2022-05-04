package com.mercadolibre.grupo1.projetointegrador.controller;

import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Agent;
import com.mercadolibre.grupo1.projetointegrador.services.AuthService;
import com.mercadolibre.grupo1.projetointegrador.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * @author Nayara Coca
 * criação do controller de warehouse, que localiza produtos por armazém
 */
@RestController
@RequestMapping("/api/v1/fresh-products/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;


    //método para pesquisar o id de produto e retornar a soma dos produtos por warehouse
    @GetMapping
    public ResponseEntity<List<WarehouseProductDTO>> listProductWarehouse(@RequestParam(required = false,
            name = "productId") Long id) {
        List<WarehouseProductDTO> findWarehouseByProducts = warehouseService.findWarehouse(id);
        return ResponseEntity.ok().body(findWarehouseByProducts);
    }

    /**
     * Requisito 06
     * @author Jefferson Botelho
     */

    // EndPoint para criar um novo warehouse, sera retornado no corpo os dados cadastrados mais o ID do wharehouse na URI
    @PostMapping("/new")
    public ResponseEntity<WarehouseDTO> addNewWarehouse(@Valid @RequestBody WarehouseDTO warehouse,
                                                        UriComponentsBuilder uriBuilder) {
        /* sera acionado a funcao para criar um novo Warehouse pelo service com os dados informado pelo dto
           os dados serao salvos em um obj de WarehouseDTO */
        WarehouseDTO warehouseDto = warehouseService.createWarehouse(warehouse);

        // o ID gerado pelo BD ao criar um novo warehouse sera informado na URI
        URI uri = uriBuilder
                .path("/{idWarehouse}/created-warehouse")
                .buildAndExpand(warehouse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(warehouseDto);
    }

    // EndPoint para buscar warehouse, sera retornado uma lista de todos os warehouse cadastrados
    @GetMapping("/list-warehouse")
    public ResponseEntity<List<WarehouseDTO>> findAllWherehouse() {
        List<WarehouseDTO> allWarehouse = warehouseService.listAllWarehouse();
        return ResponseEntity.ok().body(allWarehouse);
    }

}

