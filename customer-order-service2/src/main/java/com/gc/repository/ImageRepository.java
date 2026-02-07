package com.gc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gc.entity.ImageData;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, Integer> {
	Optional<ImageData> findByImageName(String imageName);
}
