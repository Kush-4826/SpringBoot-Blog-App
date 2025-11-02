package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(String id) throws ResourceNotFoundException;

    CategoryDto getCategoryByName(String name) throws ResourceNotFoundException;

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, String id) throws ResourceNotFoundException;

    void deleteCategoryById(String id) throws ResourceNotFoundException;
}
