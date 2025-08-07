package com.ecommerce.controller;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import com.ecommerce.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {


    private final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

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
    public String save(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
        logger.info("Guardando producto: {}", producto);
        Usuario u = new Usuario(1,"","","","","","","");
        producto.setUsuario(u);

        //imagen
        if (producto.getId() == null) { // cuando es nuevo un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }else { // cuando se edita un producto
            if (file.isEmpty()) { // editando sin cambiar la imagen
                Producto p = new Producto();
                p = productoService.getProducto(producto.getId()).get();
                producto.setImagen(p.getImagen());
            }else { // editando y cambiando la imagen
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }
        }

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
