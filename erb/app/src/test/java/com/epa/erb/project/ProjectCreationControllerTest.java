package com.epa.erb.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.epa.erb.App;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectCreationControllerTest {
	private ProjectCreationController projectCreationController;
	private App app;

	@BeforeEach
	public void setUp() throws Exception {
		app = new App();
		projectCreationController = new ProjectCreationController(app);
	}

	@Test
	public void testInvalidProjectNameNull() {
		assertFalse(projectCreationController.isValidNewProjectName(null));
	}

	@Test
	public void testInvalidProjectNameEmpty() {
		assertFalse(projectCreationController.isValidNewProjectName(""));
	}
	
	@Test
	public void testValidNewProjectName() {
		assertTrue(projectCreationController.
			isValidNewProjectName("Name"));
	}
	
	@Test
	public void testCleanStringForWindows() {
		assertEquals("_Name_Dog_123", projectCreationController.
			cleanStringForWindows("+Name=Dog$123"));
	}
}
