package com.gc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.gc.dto.ApiResponse;
import com.gc.entity.FileData;
import com.gc.entity.ImageData;
import com.gc.repository.FileRepository;
import com.gc.repository.ImageRepository;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
	private final static String filepath = "C:\\Users\\JEEVAN_OUNCE\\FileManagar\\";

	@Mock
	private ImageRepository imageRepository;
	@Mock
	private FileRepository fileRepository;
	@InjectMocks
	private ImageService imageService;
	@Mock
	private MultipartFile file;
	@Mock
	private ImageData imageData;
	@Mock
	private FileData fileData;

	@Test
	@DisplayName("Testing Saving Image into the Database - ImageService")
	void testImageSaveInBytes() throws IOException {
		ImageData data = ImageData.builder().imageName(file.getName()).imageInfo(file.getBytes()).build();
		when(imageRepository.save(any(ImageData.class))).thenReturn(data);
		String result = imageService.save(file).getMessage();
		assertEquals("Image Saved Successfully" + data.getImageName(), result);
	}

	@Test
	@DisplayName("Failed Saving Image into the Database -ImageService")
	void testImageSaveInBytesFailed() throws IOException {
		ImageData data = ImageData.builder().imageName(file.getName()).imageInfo(file.getBytes()).build();
		when(imageRepository.save(data)).thenReturn(null);
		String result = imageService.save(file).getMessage();
		assertEquals("Failed to save Image", result);
	}

	@Test
	@DisplayName("Success Retreiving Image from Database - ImageService")
	void testGetImage() {
		when(imageRepository.findByImageName("image")).thenReturn(Optional.of(imageData));
		byte[] image = imageService.getImage("image");
		assertEquals(imageData.getImageInfo(), image);
	}

	@Test
	@DisplayName("Error Retreiving Image from Database - ImageService")
	void testGetImageError() {
		when(imageRepository.findByImageName("image")).thenReturn(Optional.empty());
		byte[] image = imageService.getImage("image");
		assertEquals(null, image);
	}

	@Test
	@DisplayName("Success Saving File in Local - ImageService")
	void testSaveFileInLocal() throws IllegalStateException, IOException {
		FileData data = FileData.builder().fileName(file.getOriginalFilename())
				.filePath(filepath + file.getOriginalFilename()).build();
		when(fileRepository.save(any(FileData.class))).thenReturn(data);
		String result = imageService.saveFileInLocal(file).getMessage();
		assertEquals("Image Saved Successfully" + fileData.getFileName(), result);
	}

	@Test
	@DisplayName("Exception while Saving File in Local - ImageService")
	void testSaveFileInLocalFailed() throws IOException {
		FileData data = FileData.builder().fileName(file.getOriginalFilename())
				.filePath(filepath + file.getOriginalFilename()).build();
		when(fileRepository.save(data)).thenReturn(null);
		String result = imageService.saveFileInLocal(file).getMessage();
		assertEquals("Failed to save Image", result);
	}

	@Test
	@DisplayName("Success Deleting Image")
	void testDeleteImage() {
		when(imageRepository.findByImageName(anyString())).thenReturn(Optional.of(imageData));
		ApiResponse rs = imageService.deleteImage(anyString());
		assertEquals(true, rs.isSuccess());
		assertEquals("Image Deleted Successfully", rs.getMessage());
		assertEquals(HttpStatus.OK.value(), rs.getStatusCode());
	}

	@Test
	@DisplayName("Image Not Found Exception while Deleting Image")
	void testDeleteImageNotFoundException() {
		when(imageRepository.findByImageName(anyString())).thenReturn(Optional.empty());
		ApiResponse rs = imageService.deleteImage(anyString());
		assertEquals(false, rs.isSuccess());
		assertEquals("Image Not Found", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
	@Test
	@DisplayName("Exception while Deleting Image")
	void testDeleteImageException() {
		when(imageRepository.findByImageName(anyString())).thenReturn(null);
		ApiResponse rs = imageService.deleteImage(anyString());
		assertEquals(false, rs.isSuccess());
		assertEquals("Exception While Deleting Image", rs.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST.value(), rs.getStatusCode());
	}
}
