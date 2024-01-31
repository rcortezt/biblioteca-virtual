package com.unir.library.controller;

import com.unir.library.model.pojo.Image;
import com.unir.library.service.ImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre productos alojados en una base de datos en memoria.")
public class ImagesController {

    private final ImagesService service;

    @GetMapping("/images")
    @Operation(
            operationId = "Obtener productos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los productos almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class)))
    public ResponseEntity<List<Image>> getImages(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "path", description = "Path del libro")
            String path)

    {

        log.info("headers: {}", headers);
        List<Image> products = service.getImages(path);

        return ResponseEntity.ok(Objects.requireNonNullElse(products, Collections.emptyList()));
    }


    @GetMapping("/image/{imageId}")
    @Operation(
            operationId = "Obtener un producto",
            description = "Operacion de lectura",
            summary = "Se devuelve un producto a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema()),
            description = "No se ha encontrado el producto con el identificador indicado.")
    public ResponseEntity<Image> getImage(@PathVariable String imageId) {

        log.info("Request received for product {}", imageId);
        Image product = service.getImage(imageId);

        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            log.info("No se ha encontrado la imagen");
            return ResponseEntity.notFound().build();
        }

    }
}