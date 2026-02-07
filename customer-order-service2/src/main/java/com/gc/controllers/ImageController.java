package com.gc.controllers;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gc.dto.ApiResponse;
import com.gc.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {

	private ImageService imageService;

	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@PostMapping
	public ApiResponse saveImage(@RequestParam("image") MultipartFile file) throws IOException {
		log.trace("saveImage called in ImageController");
		return imageService.save(file);
	}

	@GetMapping("/{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
		log.trace("getImage Called in ImageController");
		byte[] image = imageService.getImage(imageName);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
	@PostMapping("/file")
	public ApiResponse saveFile(@RequestParam("file") MultipartFile file) throws IOException {
		log.trace("saveFile Called in ImageController");
		return imageService.saveFileInLocal(file);
	}
	
	@DeleteMapping("/{imageName}")
	public ApiResponse deleteImage(@PathVariable String imageName) {
		log.trace("deleteImage Called in ImageController");
		return imageService.deleteImage(imageName);
	}
}
