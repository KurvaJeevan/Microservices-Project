package com.gc.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.gc.dto.ApiResponse;
import com.gc.entity.ImageData;
import com.gc.service.ImageService;

@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ImageService imageService;
	@Mock
	private MultipartFile multipartFile;
	@Mock
	private ImageData data;

	@Test
	@DisplayName("Testing Saving of Image in DB")
	void testSaveImage() throws Exception {
		MockMultipartFile file = new MockMultipartFile("image", multipartFile.getName(), multipartFile.getContentType(),
				multipartFile.getBytes());
		ApiResponse rs = new ApiResponse(true, "Image Saved Successfully", "DummyFile", HttpStatus.OK.value(), null);
		when(imageService.save(file)).thenReturn(rs);
		mockMvc.perform(multipart("/image").file(file)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Testing Fetching of Image from DB")
	void testGetImage() throws Exception {
		when(imageService.getImage(anyString())).thenReturn(new byte[] { 1, 2, 3 });
		mockMvc.perform(get("/image/{imageName}", "Dummy")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Testing to save Image/ File Locally")
	void testSaveFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", multipartFile.getName(), multipartFile.getContentType(),
				multipartFile.getBytes());
		ApiResponse rs = new ApiResponse(true, "Image Saved Successfully", "DUmmy", HttpStatus.OK.value(), null);
		when(imageService.saveFileInLocal(file)).thenReturn(rs);
		mockMvc.perform(multipart("/image/file").file(file)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deleting Image from Controller")
	void testDeleteImage() throws Exception {
		ApiResponse rs = new ApiResponse(true, "Image Deleted Successfully", null, HttpStatus.OK.value(),
				Collections.emptyList());
		when(imageService.deleteImage(anyString())).thenReturn(rs);
		mockMvc.perform(delete("/image/{imageName}", "Hello")).andDo(print()).andExpect(status().is(200))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
	}
}
