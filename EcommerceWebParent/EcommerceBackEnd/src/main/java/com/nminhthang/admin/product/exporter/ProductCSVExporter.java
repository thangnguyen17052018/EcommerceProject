package com.nminhthang.admin.product.exporter;

import com.nminhthang.admin.AbstractExporter;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Product;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductCSVExporter extends AbstractExporter<Product> {

    String[] headers = {"Product ID", "Product Name", "Product Alias",
                        "Short Description", "Full Description", "Created Time",
                        "Updated Time", "Enabled", "In Stock", "Cost",
                        "Price", "Discount Percent", "Length", "Width", "Height", "Weight",
                        "Category", "Brand"};
    String[] fieldMapping = {"id", "name", "alias", "shortDescription", "fullDescription",
                             "createdTime", "updatedTime", "enabled", "inStock", "cost", "price",
                             "discountPercent", "length", "width", "height", "weight", "category", "brand"};

    @Override
    public void export(List<Product> listProducts, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "product");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Product product : listProducts) {
            try {
                csvWriter.write(product, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }
}
