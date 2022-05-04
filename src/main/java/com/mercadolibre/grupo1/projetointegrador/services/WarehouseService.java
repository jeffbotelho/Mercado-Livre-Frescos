package com.mercadolibre.grupo1.projetointegrador.services;

import com.mercadolibre.grupo1.projetointegrador.dtos.PurchaseOrderDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.WarehouseProductDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Customer;
import com.mercadolibre.grupo1.projetointegrador.entities.PurchaseOrder;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ListIsEmptyException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.NotFoundException;
import com.mercadolibre.grupo1.projetointegrador.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
     * a funcao ira retornar uma lista com todos os armazens cadastrados
     */

    public WarehouseDTO createWarehouse() {
    }

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
