package com.shogo.repository;

import com.shogo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 名前検索
    Page<Employee> findByNameContaining(String keyword, Pageable pageable);

    // 部署検索
    Page<Employee> findByDepartment(String department, Pageable pageable);

    // 名前＋部署検索
    Page<Employee> findByNameContainingAndDepartment(
            String keyword,
            String department,
            Pageable pageable);

    long count();

    @Query("SELECT COUNT(DISTINCT e.department) FROM Employee e")
    long countDepartments();

    @Query("""
        SELECT e.department, COUNT(e)
        FROM Employee e
        GROUP BY e.department
    """)
    List<Object[]> countByDepartment();
}