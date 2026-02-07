package com.gc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FileDataTest {

	@Test
	void testGettersAndSetters() {
		FileData fileData= new  FileData();
		fileData.setFileId(2);
		fileData.setFileName("Document");
		fileData.setFilePath("/documents/files");
		assertEquals(2, fileData.getFileId());
		assertEquals("Document", fileData.getFileName());
		assertEquals("/documents/files", fileData.getFilePath());
		FileData data2= FileData.builder().fileId(5).fileName("Document2").filePath("/document").build();
		log.info(data2+"");
	}

}
