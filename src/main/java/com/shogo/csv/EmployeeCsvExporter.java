package com.shogo.csv;

import com.shogo.entity.Employee;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EmployeeCsvExporter {

    private List<Employee> employees;

    public EmployeeCsvExporter(List<Employee> employees) {
        this.employees = employees;
    }

    public void export(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();

        writer.println("ID,名前,メールアドレス,部署");

        for (Employee employee : employees) {
            writer.println(
                    employee.getId() + "," +
                    employee.getName() + "," +
                    employee.getEmail() + "," +
                    employee.getDepartment()
            );
        }

        writer.flush();
        writer.close();
    }
}