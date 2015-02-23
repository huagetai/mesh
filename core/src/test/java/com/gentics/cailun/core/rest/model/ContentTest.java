package com.gentics.cailun.core.rest.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gentics.cailun.core.repository.TagRepository;
import com.gentics.cailun.core.rest.service.ContentService;
import com.gentics.cailun.test.AbstractDBTest;

public class ContentTest extends AbstractDBTest {

	@Autowired
	ContentService contentService;

	@Autowired
	TagRepository folderRepository;

	/**
	 * Test linking two contents
	 */
	@Test
	public void testPageLinks() {
		Content content = new Content();
		contentService.setContent(content, english, "english content");
		contentService.setFilename(content, english, "english.html");
		contentService.save(content);

		Content content2 = new Content();
		contentService.setContent(content2, english, "english2 content");
		contentService.setFilename(content2, english, "english2.html");
		contentService.save(content2);
		contentService.createLink(content, content2);
		
		//TODO verify that link relation has been created
		//TODO render content and resolve links
	}

}
