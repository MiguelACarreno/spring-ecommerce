package com.ecommerce.controller;

import com.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.*;

@Controller
@RequestMapping("/")
public class HomeController {


    private final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String home(Model model) {

        model.addAttribute("productos", productoService.findAll());
        return "administrador/usuario/home";
    }

    @GetMapping("/productohome/{id}")
    public String productos(@PathVariable Integer id) {
       logger.info("Id del Producto enviado como parámetro: {}", id);
        return "administrador/usuario/productohome";
    }
}
