package com.nminhthang.admin.category;

import com.nminhthang.common.entity.Category;
import com.nminhthang.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.IntStream;

@Service
public class CategoryService {

    public static final int CATEGORIES_PER_PAGE = 5;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> listAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Category> listAllHierarchical() {
        List<Category> rootCategories = categoryRepository.listRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            if (rootCategory.getParent() == null) {
                hierarchicalCategories.add(Category.copyFull(rootCategory));

                Set<Category> children = rootCategory.getChildren();

                children.forEach(subCategory -> {
                    hierarchicalCategories.add(Category.copyFullWithFullName(subCategory, "--" + subCategory.getName()));

                    listSubCategory(hierarchicalCategories, subCategory, 1);
                });

            }
        }

        return hierarchicalCategories;
    }

    private void listSubCategory(List<Category> hierarchicalCategories, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            IntStream.range(0, newSubLevel)
                    .forEach(i -> subCategory.setName("--" + subCategory.getName()));

            hierarchicalCategories.add(Category.copyFull(subCategory));

            listSubCategory(hierarchicalCategories, subCategory, newSubLevel);
        }

    }

    public List<Category> listAllCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {

                categoriesUsedInForm.add(Category.builder()
                        .name(category.getName())
                        .id(category.getId())
                        .build());

                Set<Category> children = category.getChildren();

                children.forEach(subCategory -> {
                    categoriesUsedInForm.add(Category.builder()
                            .name("--" + subCategory.getName())
                            .id(subCategory.getId())
                            .build());
                    listChildren(categoriesUsedInForm, subCategory, 1);
                });
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            IntStream.range(0, newSubLevel)
                    .forEach(i -> subCategory.setName("--" + subCategory.getName()));

            categoriesUsedInForm.add(Category.builder()
                    .name(subCategory.getName())
                    .id(subCategory.getId())
                    .build());

            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }

    }

    public Page<Category> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, CATEGORIES_PER_PAGE, sort);
        Page<Category> categoryPage;
        List<Category> contentPage;
        if (keyword != null) {
            categoryPage = categoryRepository.findAll(keyword, pageable);
            contentPage = pageHierarchicalCategories(categoryPage);
        } else {
            categoryPage = categoryRepository.findRootCategories(pageable);
            contentPage = pageHierarchicalCategories(categoryPage);
        }

        Page<Category> resultPage = new PageImpl<>(contentPage, pageable, categoryPage.getTotalElements());

        return resultPage;
    }

    private List<Category> pageHierarchicalCategories(Page<Category> rootCategories) {
        List<Category> rootCategoriesContent = rootCategories.getContent();
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategoriesContent) {
            if (rootCategory.getParent() == null) {
                hierarchicalCategories.add(Category.copyFull(rootCategory));

                Set<Category> children = rootCategory.getChildren();

                children.forEach(subCategory -> {
                    hierarchicalCategories.add(Category.copyFullWithFullName(subCategory, "--" + subCategory.getName()));

                    listSubCategory(hierarchicalCategories, subCategory, 1);
                });

            }
        }

        return hierarchicalCategories;
    }


    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public Category findCategoryByAlias(String alias) throws CategoryNotFoundException {
        try {
            return categoryRepository.findCategoryByAlias(alias);
        } catch (NoSuchElementException exception) {
            throw new CategoryNotFoundException("Could not find any category with alias: " + alias);
        }
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long categoryCountById = categoryRepository.countById(id);

        if (categoryCountById == null || categoryCountById == 0)
            throw new CategoryNotFoundException("Could not find any category with ID " + id);

        categoryRepository.deleteById(id);
    }

    @Transactional
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public String isCategoryUnique(Integer id, String name, String alias){
        Category categoryByName =  categoryRepository.findCategoryByName(name);
        Category categoryByAlias = categoryRepository.findCategoryByAlias(alias);

        boolean isCreatingNew = (id == null || id == 0);

        if (isCreatingNew){
            if (categoryByName != null) return "Duplicate category name";
            if (categoryByAlias != null) return "Duplicate category alias";
        }
//        else {
//            if (userByEmail.getId() != id){
//                return false;
//            }
//        }

        return "OK";
    }
}
