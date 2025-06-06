package in.cit.main.model;



import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.*;

public class ProductDto {

	@NotEmpty(message = "data is required")
	private String name;
	
	@NotEmpty(message = "data is required")
	private String brand;
	
	@NotEmpty(message = "data is required")
	private String category;
	
	@Min(0)
	private Double price;
	
	@Size(min=10,message = "Enter atleast 10 letters")
	@Size(max=2000,message = "cannot exceed 2000 letters")
	private String description;

	private MultipartFile imageFile;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	
	
}
