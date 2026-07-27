package com.shogo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "名前は必須です")
    @Size(max = 50, message = "名前は50文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメールアドレスを入力してください")
    private String email;

    @NotBlank(message = "部署は必須です")
    private String department;

    private String image;

    private LocalDate hireDate;

    public Employee() {
    }

    public Employee(Long id, String name, String email,
                    String department, String image,
                    LocalDate hireDate) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.image = image;
        this.hireDate = hireDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}
