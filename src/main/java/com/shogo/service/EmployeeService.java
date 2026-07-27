package com.shogo.service;

import com.shogo.entity.Employee;
import com.shogo.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // 全件取得
    public List<Employee> findAll() {
        return repository.findAll();
    }

    // ページネーション
    public Page<Employee> getEmployees(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // 名前検索（ページネーション対応）
    public Page<Employee> search(String keyword, Pageable pageable) {
        return repository.findByNameContaining(keyword, pageable);
    }

    // 部署検索
    public Page<Employee> getByDepartment(String department, Pageable pageable) {
        return repository.findByDepartment(department, pageable);
    }

    // 名前＋部署検索
    public Page<Employee> searchByDepartment(
            String keyword,
            String department,
            Pageable pageable) {

        return repository.findByNameContainingAndDepartment(
                keyword,
                department,
                pageable);
    }

    // 保存
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    // ID検索
    public Employee findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // 詳細取得
    public Employee getEmployeeById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    // 削除
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 社員数
    public long getEmployeeCount() {
        return repository.count();
    }

    // 部署数
    public long getDepartmentCount() {
        return repository.countDepartments();
    }

    // 部署別人数
    public List<Object[]> getDepartmentStatistics() {
        return repository.countByDepartment();
    }
}