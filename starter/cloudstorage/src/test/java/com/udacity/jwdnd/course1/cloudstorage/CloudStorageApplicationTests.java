package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpOutputMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
	private SignUpPage signup;
	private LoginPage login;
	private HomePage homePage;
	private NotePage notePage;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {

		this.driver = new ChromeDriver();
		driver.get("http://localhost:"+port+"/signup");
		signup = new SignUpPage(driver);
		signup.signup("az", "az", "az", "az");

		driver.get("http://localhost:"+port+"/login");
		login = new LoginPage(driver);
		login.login("az", "az");
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}


	@Test
	@Order(1)
	public void testCreationOfNote() throws InterruptedException {
		driver.get("http://localhost:"+port+"/home");
		notePage = new NotePage(driver);
		notePage.openNoteModal();
		notePage.addNote("Title","Description");
		assertEquals("The note was successfully added !", notePage.getSuccessMessage());
		assertEquals("Title", notePage.getNoteTitleText());
		assertEquals("Description", notePage.getNoteDescriptionText());
	}
	@Test
	@Order(2)
	public void testEditingNote() throws InterruptedException {
		testCreationOfNote();
		driver.get("http://localhost:"+port+"/home");
		notePage = new NotePage(driver);
		homePage = new HomePage(driver);
		notePage.openNoteModal();
		notePage.editNote("Title","Description 2");
		assertEquals("The note was successfully edited !", homePage.getSuccessMessage());
		assertEquals("Title", notePage.getNoteTitleText());
		assertEquals("Description 2", notePage.getNoteDescriptionText());
	}
	@Test
	@Order(3)
	public void testDeleteNote() throws InterruptedException {
		driver.get("http://localhost:"+port+"/home");
		notePage = new NotePage(driver);
		homePage = new HomePage(driver);

		notePage.openNoteModal();
		notePage.addNote("Title","Description");

		notePage.deleteNote();
		assertEquals("The note was successfully deleted !", homePage.getSuccessMessage());
		assertThrows(NoSuchElementException.class,() ->{notePage.getNoteDescriptionText();});
		assertThrows(NoSuchElementException.class,() ->{notePage.getNoteDescriptionText();});
	}
	/*@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}*/

}
