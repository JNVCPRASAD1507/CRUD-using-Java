package in.cit.main.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import in.cit.main.model.ProductDto;
import in.cit.main.model.Products;
import in.cit.main.services.ProductsRepository;



@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    // Show all products
    @GetMapping
    public String listProducts(Model model) {
        List<Products> products = repo.findAll();
        model.addAttribute("products", products);
        return "products/index"; 
    }

    @GetMapping("/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/createProduct";
    }


    // To handle form submission
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("productDto") ProductDto dto) {
    	 Products product = new Products();
    	    product.setName(dto.getName());
    	    product.setBrand(dto.getBrand());
    	    product.setCategory(dto.getCategory());
    	    product.setPrice(dto.getPrice());
    	    product.setDescription(dto.getDescription());

    	    // 2. Handle image upload
    	    MultipartFile imageFile = dto.getImageFile();
    	    if (imageFile != null && !imageFile.isEmpty()) {
    	        String filename = imageFile.getOriginalFilename();
    	        try {
    	            // Save file to local folder: /src/main/resources/static/images/
    	            Path path = Paths.get("public/images/" + filename);
    	            Files.write(path, imageFile.getBytes());
    	            product.setImageFile(filename);
    	        } catch (IOException e) {
    	            e.printStackTrace(); // Handle errors better in production
    	        }
    	    }

    	    // 3. Set creation date
    	    product.setCreatedAt(java.sql.Date.valueOf(LocalDate.now()));
    	    
    	    System.out.println("Saving Product: " + dto.getName());

    	    // 4. Save to database
    	    repo.save(product);
        return "redirect:/products";
    }
    
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Integer id, Model model) {
        Products product = repo.findById(id)
                               .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        model.addAttribute("productDto", dto);
        model.addAttribute("productId", id); // so we know what to update later
        return "products/editProduct"; // create this Thymeleaf file
    }
    
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer id) {
        // Check if product exists before deleting
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Invalid product Id: " + id);
        }

        repo.deleteById(id);
        return "redirect:/products";
    }

    
    @PostMapping("/update")
    public String updateProduct(@RequestParam("id") Integer id,
                                @ModelAttribute("productDto") ProductDto dto) {

        Products product = repo.findById(id)
                               .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        MultipartFile imageFile = dto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = imageFile.getOriginalFilename();
            try {
                Path path = Paths.get("public/images/" + filename);
                Files.write(path, imageFile.getBytes());
                product.setImageFile(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        repo.save(product);
        return "redirect:/products";
    }

    


}

