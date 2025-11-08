package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.Category;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.CategoryRequestDTO;
import in.co.lazylan.bootblog.payload.response.CategoryResponseDTO;
import in.co.lazylan.bootblog.repo.CategoryRepository;
import in.co.lazylan.bootblog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> this.modelMapper.map(category, CategoryResponseDTO.class))
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(int id) throws ResourceNotFoundException {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
        return this.modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO getCategoryByName(String name) throws ResourceNotFoundException {
        Category category = this.categoryRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Name", name));
        return this.modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryDto, User user) throws AccessDeniedException {
        Category category = this.modelMapper.map(categoryDto, Category.class);
        if (!user.isAdmin()) throw new AccessDeniedException("Only Admins Can Create a Category");
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryRequestDTO categoryDto, int id, User user) throws ResourceNotFoundException, AccessDeniedException {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
        if (!user.isAdmin()) throw new AccessDeniedException("Only Admins Can Update a Category");
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category savedCategory = this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    @Override
    public void deleteCategoryById(int id, User user) throws ResourceNotFoundException, AccessDeniedException {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
        if (!user.isAdmin()) throw new AccessDeniedException("Only Admins Can Delete Category");
        this.categoryRepository.delete(category);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("name")) {
            return this.categoryRepository.existsByName((String) value);
        }
        throw new UnsupportedOperationException(fieldName + " field does not exist");
    }

    @Override
    public boolean fieldValueExistsExceptSelf(Object value, String fieldName) throws UnsupportedOperationException {
        return false;
    }

}
