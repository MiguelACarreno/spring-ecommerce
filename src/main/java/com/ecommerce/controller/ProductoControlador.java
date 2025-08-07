package com.ecommerce.controller;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.*;

import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {


    private final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.getProducto(id);
        if (optionalProducto.isPresent()) {
            producto = optionalProducto.get();
        }
        logger.info("Editando producto: {}", producto);
        model.addAttribute("producto", producto);
        return "administrador/productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto) {
        logger.info("Actualizando producto: {}", producto);
        productoService.update(producto);
        return "redirect:/productos";
    }


}
