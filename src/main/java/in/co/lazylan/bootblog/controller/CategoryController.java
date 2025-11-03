package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.CategoryRequestDTO;
import in.co.lazylan.bootblog.payload.response.CategoryResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDTO>> index() {
        List<CategoryResponseDTO> categoryDtoList = this.categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> show(@PathVariable String id) throws ResourceNotFoundException {
        CategoryResponseDTO categoryById = this.categoryService.getCategoryById(id);
        return ResponseEntity.ok().body(categoryById);
    }

    @PostMapping("")
    public ResponseEntity<CategoryResponseDTO> store(@Valid @RequestBody CategoryRequestDTO categoryDto) {
        CategoryResponseDTO category = this.categoryService.createCategory(categoryDto);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable String id, @Valid @RequestBody CategoryRequestDTO categoryDto) throws ResourceNotFoundException {
        CategoryResponseDTO updatedCategory = this.categoryService.updateCategory(categoryDto, id);
        return ResponseEntity.ok().body(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable String id) throws ResourceNotFoundException {
        this.categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(
                new SuccessResponse("Category has been deleted successfully"),
                HttpStatus.OK
        );
    }
}
