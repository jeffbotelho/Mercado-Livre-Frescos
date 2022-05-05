package com.mercadolibre.grupo1.projetointegrador.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Nayara Coca
 * DTO de controle de infos do warehouse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL) // com esta anotacao nao sera retornado o ID caso for null
    private Long id;
    @NotNull(message = "O nome do armazém não pode estar vazio")
    private String name;
    @NotNull(message = "O endereço do armazém não pode estar vazio")
    private String adress;

    public static WarehouseDTO fromWarehouse(Warehouse fromWarehouse){
        return new WarehouseDTO(
                fromWarehouse.getId(),
                fromWarehouse.getName(),
                fromWarehouse.getAddress()
        );
    }
}
