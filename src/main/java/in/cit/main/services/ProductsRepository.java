package in.cit.main.services;

import org.springframework.data.jpa.repository.JpaRepository;

import in.cit.main.model.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

}
