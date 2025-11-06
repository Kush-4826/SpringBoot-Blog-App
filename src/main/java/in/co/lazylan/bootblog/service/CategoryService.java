package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.CategoryRequestDTO;
import in.co.lazylan.bootblog.payload.response.CategoryResponseDTO;
import in.co.lazylan.bootblog.util.FieldValueExists;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends FieldValueExists {
    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(int id) throws ResourceNotFoundException;

    CategoryResponseDTO getCategoryByName(String name) throws ResourceNotFoundException;

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryDto, User user) throws AccessDeniedException;

    CategoryResponseDTO updateCategory(CategoryRequestDTO categoryDto, int id, User user) throws ResourceNotFoundException, AccessDeniedException;

    void deleteCategoryById(int id, User user) throws ResourceNotFoundException, AccessDeniedException;
}
