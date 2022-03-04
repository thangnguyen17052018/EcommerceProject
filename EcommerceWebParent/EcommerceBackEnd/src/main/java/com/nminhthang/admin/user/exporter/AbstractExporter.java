package com.nminhthang.admin.user.exporter;

import com.nminhthang.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractExporter {

    String[] headers = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
    String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "users_" + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    public abstract void export(List<User> listUsers, HttpServletResponse response) throws IOException;

}
