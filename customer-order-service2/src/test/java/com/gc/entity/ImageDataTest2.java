package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ImageDataTest2 {

	@Test
	void test() {
		ImageData data= new ImageData();
		byte [] arr= {1,2,3};
		data.setImageId(5);
		data.setImageInfo(arr);
		data.setImageName("Image");
		assertEquals(5, data.getImageId());
		assertEquals(arr, data.getImageInfo());
		assertEquals("Image", data.getImageName());
		
	}

}
