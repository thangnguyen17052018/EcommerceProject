package com.nminhthang.admin;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractExporter<T> {

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String module) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy");
        String timestamp = dateFormatter.format(new Date());
        String fileName = module + "_" + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    public abstract void export(List<T> listItem, HttpServletResponse response) throws IOException;

}
