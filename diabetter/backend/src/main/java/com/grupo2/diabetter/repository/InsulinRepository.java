package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsulinRepository extends JpaRepository<Insulin, String> {
}

