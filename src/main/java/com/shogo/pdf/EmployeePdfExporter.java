package com.shogo.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.shogo.entity.Employee;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployeePdfExporter {

    private List<Employee> employeeList;

    public EmployeePdfExporter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    private void writeTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(BaseColor.BLACK);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("名前", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("メール", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("部署", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {

        for (Employee employee : employeeList) {

            table.addCell(String.valueOf(employee.getId()));
            table.addCell(employee.getName());
            table.addCell(employee.getEmail());

            if (employee.getDepartment() != null) {
                table.addCell(employee.getDepartment().getName());
            } else {
                table.addCell("");
            }
        }
    }

    public void export(HttpServletResponse response)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);

        Paragraph p = new Paragraph("Employee List", font);

        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100f);

        table.setWidths(new float[]{1.5f, 3.5f, 5.0f, 3.0f});

        table.setSpacingBefore(10);

        writeTableHeader(table);

        writeTableData(table);

        document.add(table);

        document.close();
    }
}