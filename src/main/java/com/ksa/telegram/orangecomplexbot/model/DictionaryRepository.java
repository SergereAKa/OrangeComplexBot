package com.ksa.telegram.orangecomplexbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    Optional<Dictionary> findByCode(String code);

    @Transactional
    void deleteByCode(String code);
}
