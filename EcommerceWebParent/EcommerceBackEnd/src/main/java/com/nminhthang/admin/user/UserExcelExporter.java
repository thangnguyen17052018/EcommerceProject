package com.nminhthang.admin.user;

import com.nminhthang.common.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    @Override
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx");

        writeHeaderLine();
        writeDataLine(listUsers);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle headerStyle = createCellStyle(true, 16);

        writeRow(headers, row, headerStyle);
    }


    private void writeRow(String[] cells,XSSFRow row, XSSFCellStyle cellStyle) {
        int cellIndex = 0;
        for (String cell : cells){
            createCell(row, cellIndex++, cell, cellStyle);
        }
    }

    private void writeDataLine(List<User> listUsers) {

        XSSFCellStyle normalStyle = createCellStyle(false, 13);

        int rowIndex = 1;
        for (User user : listUsers){
            XSSFRow row = sheet.createRow(rowIndex++);

            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(), normalStyle);
            createCell(row, columnIndex++, user.getEmail(), normalStyle);
            createCell(row, columnIndex++, user.getFirstName(), normalStyle);
            createCell(row, columnIndex++, user.getLastName(), normalStyle);
            createCell(row, columnIndex++, user.getRoles(), normalStyle);
            createCell(row, columnIndex++, user.isEnabled(), normalStyle);
        }
    }

    private XSSFCellStyle createCellStyle(Boolean isBold, int fontHeight) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(cellStyle);
    }
}
