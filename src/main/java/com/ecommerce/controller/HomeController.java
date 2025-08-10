package com.ecommerce.controller;

import com.ecommerce.model.DetalleOrden;
import com.ecommerce.model.Orden;
import com.ecommerce.model.Producto;
import com.ecommerce.service.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import org.slf4j.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {


    private final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoService productoService;

    //Para alamacenar los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList<>();

    //Para almacenar datos de la orden
    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model) {

        model.addAttribute("productos", productoService.findAll());
        return "administrador/usuario/home";
    }

    @GetMapping("/productohome/{id}")
    public String productos(@PathVariable Integer id, Model model) {
       logger.info("Id del Producto enviado como parámetro: {}", id);

        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.getProducto(id);
        producto = (Producto) optionalProducto.get();

        model.addAttribute("producto", producto);

        return "administrador/usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumatoria = 0;


        Optional<Producto> optionalProducto = productoService.getProducto(id);
        logger.info("Produto añadido: {}", id);
        logger.info("Cantidad: {}", cantidad);
        return "administrador/usuario/carrito";
    }
}
