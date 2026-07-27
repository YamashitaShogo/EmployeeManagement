package com.shogo.controller;

import com.itextpdf.text.DocumentException;
import com.shogo.csv.EmployeeCsvExporter;
import com.shogo.entity.Employee;
import com.shogo.excel.EmployeeExcelExporter;
import com.shogo.pdf.EmployeePdfExporter;
import com.shogo.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    // 一覧
    @GetMapping("/")
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        Sort.Direction direction =
                dir.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(page, 10, Sort.by(direction, sort));

        Page<Employee> employeePage;

        if (keyword != null && !keyword.isBlank()
                && department != null && !department.isBlank()) {

            employeePage =
                    service.searchByDepartment(keyword, department, pageable);

        } else if (keyword != null && !keyword.isBlank()) {

            employeePage =
                    service.search(keyword, pageable);

        } else if (department != null && !department.isBlank()) {

            employeePage =
                    service.getByDepartment(department, pageable);

        } else {

            employeePage =
                    service.getEmployees(pageable);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("employees", employeePage.getContent());

        model.addAttribute("keyword", keyword);
        model.addAttribute("department", department);

        model.addAttribute("employeeCount", service.getEmployeeCount());
        model.addAttribute("departmentCount", service.getDepartmentCount());
        model.addAttribute("departmentStats", service.getDepartmentStatistics());

        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "index";
    }

    // 新規登録画面
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "form";
    }

    // 保存
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute Employee employee,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (result.hasErrors()) {
            return "form";
        }

        if (!imageFile.isEmpty()) {

            String fileName =
                    UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            imageFile.transferTo(uploadPath.resolve(fileName));

            employee.setImage(fileName);
        }

        service.save(employee);

        return "redirect:/";
    }

    // 編集
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.findById(id));
        return "form";
    }

    // 削除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }

    // 詳細
    @GetMapping("/employee/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.getEmployeeById(id));
        return "detail";
    }

    // Excel出力
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String currentDateTime =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                        .format(new Date());

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=employees_" + currentDateTime + ".xlsx");

        new EmployeeExcelExporter(service.findAll()).export(response);
    }

    // CSV出力
    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=employees.csv");

        new EmployeeCsvExporter(service.findAll()).export(response);
    }

    // PDF出力
    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response)
            throws IOException, DocumentException {

        response.setContentType("application/pdf");

        String currentDateTime =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                        .format(new Date());

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=employees_" + currentDateTime + ".pdf");

        new EmployeePdfExporter(service.findAll()).export(response);
    }
}