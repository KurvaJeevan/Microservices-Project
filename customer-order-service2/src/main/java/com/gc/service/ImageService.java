package com.gc.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gc.dto.ApiResponse;
import com.gc.entity.FileData;
import com.gc.entity.ImageData;
import com.gc.repository.FileRepository;
import com.gc.repository.ImageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

	private ImageRepository imageRepository;
	private FileRepository fileRepository;
	private String filePath ;

	public ImageService(ImageRepository imageRepository, FileRepository fileRepository) {
		super();
		this.imageRepository = imageRepository;
		this.fileRepository = fileRepository;
		this.filePath="C:\\Users\\JEEVAN_OUNCE\\FileManagar\\";
	}

	public ApiResponse save(MultipartFile file) throws IOException {
		try {
			log.info("Image Saving Initiated");
			ImageData data = ImageData.builder().imageName(file.getName()).imageInfo(file.getBytes()).build();
			ImageData data2 = imageRepository.save(data);
			log.info("Image Saved Successfully");
			return new ApiResponse(true, "Image Saved Successfully" + data2.getImageName(), data2,
					HttpStatus.OK.value(), null);
		} catch (Exception e) {
			log.error("Failed to save Image");
			return new ApiResponse(false, "Failed to save Image", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

	public byte[] getImage(String imageName) {
		log.info("Getting Image Initiated");
		Optional<ImageData> byImageName = imageRepository.findByImageName(imageName);
		if (byImageName.isEmpty()) {
			log.warn("Image Not Found");
			return null;
		}
		log.info("Image Found ");
		return byImageName.get().getImageInfo();
	}

	public ApiResponse saveFileInLocal(MultipartFile file) throws IllegalStateException, IOException {
		try {
			log.info("Saving Image Initiated");
			FileData data = FileData.builder().fileName(file.getOriginalFilename())
					.filePath(filePath + file.getOriginalFilename()).build();
			file.transferTo(new File(filePath + file.getOriginalFilename()));
			FileData data2 = fileRepository.save(data);
			log.info("Image Saved Successfully");
			return new ApiResponse(true, "Image Saved Successfully" + data2.getFileName(), data2,
					HttpStatus.OK.value(), null);
		} catch (Exception e) {
			log.error("Failed to save Image");
			return new ApiResponse(false, "Failed to save Image", e.getMessage(),
					HttpStatus.BAD_REQUEST.value(), Collections.singletonList(e.getMessage()));
		}
	}

	public ApiResponse deleteImage(String imageName) {
		try {
			log.info("DeleteImage Initiated");
			Optional<ImageData> fetchedImage = imageRepository.findByImageName(imageName);
			if(fetchedImage.isEmpty()) {
				log.warn("Image Not Found");
				return new ApiResponse(false, "Image Not Found", null, HttpStatus.BAD_REQUEST.value(),
						Collections.emptyList());
			}
			imageRepository.delete(fetchedImage.get());
			log.info("Image Deleted Successfully");
			return new ApiResponse(true, "Image Deleted Successfully", null, HttpStatus.OK.value(),
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Exception Occured"+e.getMessage());
			return new ApiResponse(false, "Exception While Deleting Image", e.getMessage(), HttpStatus.BAD_REQUEST.value(),
					Collections.singletonList(e.getMessage()));
		}
	}


}
