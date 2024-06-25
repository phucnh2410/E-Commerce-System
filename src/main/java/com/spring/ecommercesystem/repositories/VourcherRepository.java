package com.spring.ecommercesystem.repositories;

import com.spring.ecommercesystem.entities.Vourcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VourcherRepository extends JpaRepository<Vourcher, Long> {
}
