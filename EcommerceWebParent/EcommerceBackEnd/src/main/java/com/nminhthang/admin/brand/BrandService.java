package com.nminhthang.admin.brand;

import com.nminhthang.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE = 5;

    @Autowired
    BrandRepository brandRepository;

    public List<Brand> listAll() {
        Sort sort = Sort.by("name").ascending();

        return (List<Brand>) brandRepository.findAll(sort);
    }

    public Page<Brand> listByPage(int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAllBy(keyword, pageable);
        } else {
            return brandRepository.findAllBy(pageable);
        }

    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new BrandNotFoundException("Could not find any brand with id = " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long brandCountById = brandRepository.countById(id);

        if (brandCountById == null || brandCountById == 0)
            throw new BrandNotFoundException("Could not find any brand with id = " + id);

        brandRepository.deleteById(id);
    }

    public String checkBrandUnique(Integer id, String name) {
        Brand brandByName = brandRepository.findByName(name);

        boolean isCreatingNew = (id == null || id == 0);

        if (isCreatingNew) {
            if (brandByName != null)
                return "Duplicate brand name";
        } else {
            if (brandByName != null && brandByName.getId() != id)
                return "Duplicate brand name";
        }

        return "OK";
    }

}
