package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
	private SignUpPage signup;
	private LoginPage login;
	private HomePage homePage;
	private NotePage notePage;
	private CredentialPage credentialPage;
	private EncryptionService encryptionService;

	@LocalServerPort
	private int port;

	@Autowired
	private CredentialService credentialService;

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


	@Test
	@Order(1)
	public void testCreatingCredential() {
		encryptionService = new EncryptionService();
		driver.get("http://localhost:"+port+"/home");
		credentialPage = new CredentialPage(driver);
		credentialPage.openModal();
		credentialPage.addCredential("www.google.com","Norbox","12345");
		assertEquals("The credential was successfully added !", credentialPage.getSuccessMessage());
		assertEquals("www.google.com", credentialPage.getCredentialUrlText());
		assertEquals("Norbox", credentialPage.getCredentialUsernameText());
		Credential credential = credentialService.getCredentialsById(1);
		assertEquals("12345", this.encryptionService.decryptValue(credentialPage.getCredentialPasswordText(),credential.getKey()));
	}

	@Test
	public void testEditingCredential() throws InterruptedException {
		testCreatingCredential();
		credentialPage.editCredential("www.facebook.com","Sasuke","Pasta:");
		assertEquals("The credential was successfully edited !", credentialPage.getSuccessMessage());
		assertEquals("www.facebook.com", credentialPage.getCredentialUrlText());
		assertEquals("Sasuke", credentialPage.getCredentialUsernameText());
		Credential credential = credentialService.getCredentialsById(1);
		assertEquals("Pasta:", this.encryptionService.decryptValue(credentialPage.getCredentialPasswordText(),credential.getKey()));
	}

	@Test
	@Order(3)
	public void testDeleteCredential(){
		testCreatingCredential();
		credentialPage.deleteCredential();
		assertEquals("The credential was successfully deleted !", credentialPage.getSuccessMessage());
		assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialUrlText();});
		assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialUsernameText();});
		assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialPasswordText();});
	}

}
