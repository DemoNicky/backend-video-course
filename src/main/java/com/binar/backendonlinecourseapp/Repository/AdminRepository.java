package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, String> {
    List<Admin> findByIsActiveTrue();
    List<Admin> findByClassTypeAndIsActiveTrue(ClassType classType);
}