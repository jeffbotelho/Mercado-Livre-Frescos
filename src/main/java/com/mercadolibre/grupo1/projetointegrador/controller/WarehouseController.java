package com.mercadolibre.grupo1.projetointegrador.controller;

import com.mercadolibre.grupo1.projetointegrador.dtos.ProductDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.PurchaseOrderDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.enums.ProductCategory;
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

    @PostMapping("/new-warehouse")
    public ResponseEntity<WarehouseDTO> addNewWarehouse(@Valid @RequestBody WarehouseDTO warehouse,
                                                        UriComponentsBuilder uriBuilder) {
        // pega o usuário que esta logado
//        Customer customer = authService.getPrincipalAs(Customer.class);

//        PurchaseOrder purchaseOrderDTO = purchaseOrderService.createPurchaseOrder(purchaseOrder, customer);

        WarehouseDTO warehouseDto = warehouseService.createWarehouse();

        URI uri = uriBuilder
                .path("/{idwarehouse}")
                .buildAndExpand(warehouseDto.getId())
                .toUri();


        return ResponseEntity.created(uri).body(null);
    }

    // EndPoint para buscar warehouse, sera retornado uma lista de todos os warehouse cadastrados
    @GetMapping("/list-warehouse")
    public ResponseEntity<List<WarehouseDTO>> findAllWherehouse() {
        List<WarehouseDTO> allWarehouse = warehouseService.listAllWarehouse();
        return ResponseEntity.ok().body(allWarehouse);
    }

}

