package com.mercadolibre.grupo1.projetointegrador.controller;

import com.mercadolibre.grupo1.projetointegrador.dtos.ProductDTO;
import com.mercadolibre.grupo1.projetointegrador.dtos.PurchaseOrderStatusDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.PurchaseOrder;
import com.mercadolibre.grupo1.projetointegrador.entities.enums.ProductCategory;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ExceptionCatchIsEmpty;
import com.mercadolibre.grupo1.projetointegrador.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/")
public class PurchaseOrderController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAllProduct() throws ExceptionCatchIsEmpty {
        List<ProductDTO> allProducts = productService.listAllProducts();
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> listProductForCategory( @RequestParam(required = false, name = "status") String productCategory) throws ExceptionCatchIsEmpty {
        List<ProductDTO> productByCategory = productService.listProductByCategory(productCategory);
        return ResponseEntity.ok().body(productByCategory);
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder,
                                                             UriComponentsBuilder uriBuilder) {
        //...

        URI uri =  uriBuilder
                .path("/{idOrder}")
                .buildAndExpand(purchaseOrder.getId())
                .toUri();

        //...
        return null;
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<PurchaseOrder> showProductsOrder(@PathVariable("idOrder") Long idOrder) {

        return null;
    }

    @PutMapping("/orders/{idOrder}")
    public ResponseEntity<PurchaseOrder> modifyOrderStatusByOpenedOrClosed(@PathVariable Long idOrder,
                                                                           @RequestBody PurchaseOrderStatusDTO statusOrder) {

        return null;
    }
}
