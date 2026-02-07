package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImageDataTest {

	@Test
	void test() {
		ImageData imageData= new ImageData();
		imageData.setImageId(2);
		imageData.setImageName("Document");
		byte [] data= {1,2,3};
		imageData.setImageInfo(data);
		assertEquals(2, imageData.getImageId());
		assertEquals("Document", imageData.getImageName());
		assertEquals(data, imageData.getImageInfo());
		ImageData data2= ImageData.builder().imageId(5).imageInfo(data).build();
		log.info(data2+"");
	}

}
