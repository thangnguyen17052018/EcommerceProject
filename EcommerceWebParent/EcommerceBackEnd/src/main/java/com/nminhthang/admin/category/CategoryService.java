package com.nminhthang.admin.category;

import com.nminhthang.common.entity.Category;
import com.nminhthang.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 3;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.listRootCategories(Sort.by("name").ascending());
        return listHierarchicalCategories(rootCategories, "asc");
    }

    public List<Category> listByPage(CategoryPageInfo categoryPageInfo ,int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> rootCategoriesPage;
        if (keyword != null) {
            rootCategoriesPage = categoryRepository.findCategoriesWithKeyword(keyword ,pageable);
        } else {
            rootCategoriesPage = categoryRepository.findRootCategories(pageable);
        }

        categoryPageInfo.setTotalElements(rootCategoriesPage.getTotalElements());
        categoryPageInfo.setTotalPages(rootCategoriesPage.getTotalPages());

        List<Category> rootCategories = rootCategoriesPage.getContent();

        if (keyword == null) {
            return listHierarchicalCategories(rootCategories, sortDir);
        } else {
            for (Category category : rootCategories) {
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return rootCategories;
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            if (rootCategory.getParent() == null) {
                hierarchicalCategories.add(Category.copyFull(rootCategory));

                Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

                children.forEach(subCategory -> {
                    hierarchicalCategories.add(Category.copyFullWithFullName(subCategory, "--" + subCategory.getName()));

                    listSubCategory(hierarchicalCategories, subCategory, 1, sortDir);
                });

            }
        }

        return hierarchicalCategories;
    }

    private void listSubCategory(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

        for (Category subCategory : children) {
            IntStream.range(0, newSubLevel)
                    .forEach(i -> subCategory.setName("--" + subCategory.getName()));

            hierarchicalCategories.add(Category.copyFull(subCategory));

            listSubCategory(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }

    }

    public List<Category> listAllCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.listRootCategories(Sort.by("name").ascending());

        for (Category category : categories) {
            if (category.getParent() == null) {

                categoriesUsedInForm.add(Category.builder()
                        .name(category.getName())
                        .id(category.getId())
                        .build());

                Set<Category> children = sortSubCategories(category.getChildren());

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
        Set<Category> children = sortSubCategories(parent.getChildren());

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

    public Category save(Category category) {
        Category parent = category.getParent();
        if (parent != null) {
            String allParentIDs = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIDs += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIDs);
        }
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

    public String isCategoryUnique(Integer id, String name, String alias) {
        Category categoryByName = categoryRepository.findCategoryByName(name);
        Category categoryByAlias = categoryRepository.findCategoryByAlias(alias);

        boolean isCreatingNew = (id == null || id == 0);

        if (isCreatingNew) {
            if (categoryByName != null)
                return "Duplicate category name";
            else if (categoryByAlias != null)
                return "Duplicate category alias";
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "Duplicate category name";
            }
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "Duplicate category alias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>((cat1, cat2) -> {
            if (sortDir.equals("asc"))
                return cat1.getName().compareTo(cat2.getName());
            else
                return cat2.getName().compareTo(cat1.getName());
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }

}
