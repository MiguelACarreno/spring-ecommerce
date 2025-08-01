package com.ecommerce.controller;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.*;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {


    private final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String show() {
        return "administrador/productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "administrador/productos/create";
    }


    @PostMapping("/save")
    public String save(Producto producto) {
        logger.info("Guardando producto: {}", producto);
        Usuario u = new Usuario(1,"","","","","","","");
        producto.setUsuario(u);

        productoService.save(producto);
        return "redirect:/productos";
    }


}
