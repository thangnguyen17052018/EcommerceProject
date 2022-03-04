package com.nminhthang.admin.user.exporter;

import com.nminhthang.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCSVExporter extends AbstractExporter{

    @Override
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (User user : listUsers) {
            try {
                csvWriter.write(user, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }

}
