package com.nminhthang.admin.brand.exporter;

import com.nminhthang.admin.AbstractExporter;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BrandCSVExporter extends AbstractExporter<Brand> {

    String[] headers = {"Brand ID", "Brand Name", "Categories"};
    String[] fieldMapping = {"id", "name", "categories"};

    @Override
    public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "brand");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Brand brand : listBrands) {
            try {
                csvWriter.write(brand, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }
}
