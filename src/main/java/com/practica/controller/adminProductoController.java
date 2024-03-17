/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.controller;



import com.practica.domain.Productos;
import com.practica.service.CategoriasService;
import com.practica.service.firebaseStorageService;
import com.practica.service.productosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author alexa
 */

@Controller
@RequestMapping("/adminProducto")
public class adminProductoController {
  
    @Autowired
    private productosService productosService;
    @Autowired
    private CategoriasService categoriaService;
    
    @GetMapping("/listado")
    private String listado(Model model) {
        var productos = productosService.getProductos();
        var categorias = categoriaService.getCategorias();
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalProductos", productos.size());
        return "adminProducto/listado"; // Quita el slash al inicio
    }
    
    
     @GetMapping("/nuevo")
    public String productoNuevo(Productos producto) {
        return "/adminProducto/modifica";
    }
@Autowired
private firebaseStorageService firebaseStorageService;

@PostMapping("/guardar")
public String productoGuardar(@ModelAttribute("producto") Productos producto,
        @RequestParam("imagenFile") MultipartFile imagenFile) {        
    if (!imagenFile.isEmpty()) {
        // Guardar la imagen en Firebase Storage y establecer la URL en el producto
        producto.setImagen_producto(firebaseStorageService.cargaImagen(imagenFile, "productos", producto.getId_producto()));
    }
    // Guardar el producto (una vez si la imagen no está vacía)
    productosService.save(producto);
    
    return "redirect:/adminProducto/listado";
}

    @GetMapping("/eliminar/{id_producto}")
    public String productoEliminar(Productos producto) {
        productosService.delete(producto);
        return "redirect:/adminProducto/listado";
    }

    @GetMapping("/modifica/{id_producto}")
    public String productoModificar(Productos producto, Model model) {
        producto = productosService.getProducto(producto);
        model.addAttribute("producto", producto);
        return "/adminProducto/modifica";
    }   
}

