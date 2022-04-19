package com.nminhthang.admin.customer;

import com.nminhthang.admin.AbstractExporter;
import com.nminhthang.common.entity.Customer;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerCSVExporter extends AbstractExporter<Customer> {

    String[] headers = {"Customer ID", "Customer FirstName", "Customer LastName",
                        "Phone Number", "Address Line 1", "Address Line 2",
                        "City", "State", "Created Time", "Enabled",
                        "Postal Code", "Verification Code", "Country Id"};
    String[] fieldMapping = {"id", "firstName", "lastName", "phoneNumber", "addressLine1",
                             "addressLine2", "city", "state", "createTime", "enabled", "postalCode",
                             "verificationCode", "country"};

    @Override
    public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "customer");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Customer customer : listCustomers) {
            try {
                csvWriter.write(customer, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }
}
