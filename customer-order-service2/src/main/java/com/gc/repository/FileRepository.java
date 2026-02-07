package com.gc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gc.entity.FileData;

@Repository
public interface FileRepository extends JpaRepository<FileData, Integer> {
	Optional<FileData> findByFileName(String filename);
}
