package com.study.modoos.study.repository;

import com.study.modoos.study.entity.PositiveKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositiveKeywordRepository extends JpaRepository<PositiveKeyword, Long> {
}
