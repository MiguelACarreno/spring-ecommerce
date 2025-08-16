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


    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

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

    @GetMapping("/producto/{id}")
    public String productos(@PathVariable Integer id, Model model) {
       logger.info("Id del Producto enviado como parámetro: {}", id);

        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.getProducto(id);
        producto = (Producto) optionalProducto.get();

        model.addAttribute("producto", producto);

        return "administrador/usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;


        Optional<Producto> optionalProducto = productoService.getProducto(id);
        logger.info("Produto añadido: {}", id);
        logger.info("Cantidad: {}", cantidad);

        producto = (Producto) optionalProducto.get();

        detalleOrden.setCantidad(String.valueOf(cantidad));
        detalleOrden.setPrecio(String.valueOf(producto.getPrecio()));
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        //v validar que el producto no se repita
        Integer idProducto = producto.getId();
       boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

        if (!ingresado) {
            detalles.add(detalleOrden);
        }


        sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "administrador/usuario/carrito";
    }





    //Quitar producto del carrito
    @GetMapping("delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {

        //lista nueva de productos
        List<DetalleOrden> ordenesNueva = new ArrayList<>();

        //recorremos la lista de productos
        for (DetalleOrden detalleOrden : detalles) {
            //Si el id del producto es diferente al id que se pasa por la url, lo agregamos a la nueva lista
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }

        //Ponemos la nueva lista en la lista de productos
        detalles = ordenesNueva;

        double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "administrador/usuario/carrito";
    }


    @GetMapping("/getCart")
    public String getCart(Model model) {
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "administrador/usuario/carrito";
    }
}
