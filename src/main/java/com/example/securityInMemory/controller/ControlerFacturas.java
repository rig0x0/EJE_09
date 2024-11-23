package com.example.securityInMemory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class ControlerFacturas {

    @GetMapping("/listar")
    public String listar() {
        return "Lista de facturas - Sin seguridad";
    }

    @GetMapping("/actualizar")
    public String actualizar() {
        return "factura actualizada - con seguridad";
    }

    @GetMapping("/eliminar")
    public String eliminar() {
        return "factura eliminada - con seguridad";
    }

    @GetMapping("/crear")
    public String crear() {
        return "factura creada - con seguridad";
    }
}
