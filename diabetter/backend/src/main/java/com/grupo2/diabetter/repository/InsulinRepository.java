package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InsulinRepository extends JpaRepository<Insulin, UUID> {
}

