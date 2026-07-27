package com.shogo.excel;

import com.shogo.entity.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class EmployeeExcelExporter {

    private XSSFWorkbook workbook;
    private Sheet sheet;
    private List<Employee> listEmployees;

    public EmployeeExcelExporter(List<Employee> listEmployees) {
        this.listEmployees = listEmployees;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {

        sheet = workbook.createSheet("Employees");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);

        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "名前", style);
        createCell(row, 2, "メールアドレス", style);
        createCell(row, 3, "部署", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {

        sheet.autoSizeColumn(columnCount);

        Cell cell = row.createCell(columnCount);

        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue(String.valueOf(value));
        }

        cell.setCellStyle(style);
    }

    private void writeDataLines() {

        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();

        for (Employee employee : listEmployees) {

            Row row = sheet.createRow(rowCount++);

            createCell(row, 0, employee.getId(), style);
            createCell(row, 1, employee.getName(), style);
            createCell(row, 2, employee.getEmail(), style);
            createCell(row, 3, employee.getDepartment(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {

        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();
        outputStream.close();
    }
}