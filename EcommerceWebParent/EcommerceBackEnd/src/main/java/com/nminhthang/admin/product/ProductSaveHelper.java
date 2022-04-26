package com.nminhthang.admin.product;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.common.entity.product.Product;
import com.nminhthang.common.entity.product.ProductImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductSaveHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

    public static void deleteExtraImagesWeredRemovedFrom(Product product) {
        String extraImageDir = "../product-images/" + product.getId() + "/extras";
        Path extraImageDirPath  = Paths.get(extraImageDir);

        try {
            Files.list(extraImageDirPath).forEach(file -> {
                String fileName = file.toFile().getName();
                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + fileName);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + fileName);
                    }
                }
            });
        } catch (IOException ex) {
            LOGGER.error("Could not list directory: " + extraImageDirPath);
        }
    }

    public static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
        if (imageIds == null || imageIds.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIds.length; count++) {
            Integer id = Integer.parseInt(imageIds[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    public static void setProductDetails(String[] detailIds, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIds[count]);

            if (!name.isEmpty() && !value.isEmpty()) {
                if (id != 0) {
                    product.addDetail(id, name, value);
                } else {
                    product.addDetail(name, value);
                }
            }

        }
    }

    public static void saveUploadedImages(MultipartFile mainImage, MultipartFile[] extraImages, Product savedProduct) throws IOException {
        if (!mainImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImage.getOriginalFilename()));
            String uploadDir = "../" + FileUploadUtil.PRODUCT_DIR_NAME + savedProduct.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImage);
        }

        if (extraImages.length > 0) {
            String uploadDir = "../" + FileUploadUtil.PRODUCT_DIR_NAME + savedProduct.getId() + "/extras";

            for (MultipartFile extraImage : extraImages) {
                if (extraImage.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, fileName, extraImage);
            }
        }
    }


    public static void setNewExtraImages(MultipartFile[] extraImageMultiparts, Product product) {
        if (extraImageMultiparts.length > 0) {
            for (MultipartFile extraImageMultipart : extraImageMultiparts) {
                if (!extraImageMultipart.isEmpty()) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImageMultipart.getOriginalFilename()));

                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    public static void setMainImage(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

}
