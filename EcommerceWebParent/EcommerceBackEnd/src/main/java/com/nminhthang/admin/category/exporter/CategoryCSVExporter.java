package com.nminhthang.admin.category.exporter;

import com.nminhthang.admin.AbstractExporter;
import com.nminhthang.common.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCSVExporter extends AbstractExporter<Category> {

    String[] headers = {"Category ID", "Category Name", "Alias", "Enabled", "Parent"};
    String[] fieldMapping = {"id", "name", "alias", "enabled", "parent"};

    @Override
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "category");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Category category : listCategories) {
            try {
                csvWriter.write(category, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }

}
