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
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialTests {
    private HomePage homePage;
    private CredentialPage credentialPage;
    private static WebDriver driver;
    private SignUpPage signup;
    private LoginPage login;

    private String username = "CredentialUser";
    private String password = "Nadi";
    private String firstName = "Mimo";
    private String lastName = "Senji";

    @LocalServerPort
    private int port;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeAll
    static void beforeAll(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

    }
    @BeforeEach
    public void beforeEach() {
    }

    @Test
    @Order(1)
    public void testCreatingCredential() throws InterruptedException {
        //Create the user that will be used in these tests
        driver.get("http://localhost:"+port+"/signup");
        signup = new SignUpPage(driver);
        signup.signup(firstName, lastName, username, password);

        //Login
        driver.get("http://localhost:"+port+"/login");
        login = new LoginPage(driver);
        login.login(username, password);

        driver.get("http://localhost:"+port+"/home");
        credentialPage = new CredentialPage(driver);
        credentialPage.openModal();
        credentialPage.addCredential("www.google.com","Norbox","12345");
        assertEquals("The credential was successfully added !", credentialPage.getSuccessMessage());
        assertEquals("www.google.com", credentialPage.getCredentialUrlText());
        assertEquals("Norbox", credentialPage.getCredentialUsernameText());
        Thread.sleep(3000);
        Credential credential = credentialService.getLastElement();
        System.out.println(credentialPage.getCredentialPasswordText());
        System.out.println(credential);
        assertEquals("12345", this.encryptionService.decryptValue(credentialPage.getCredentialPasswordText(),credential.getKey()));
    }

    @Test
    @Order(2)
    public void testEditingCredential() throws InterruptedException {
        driver.get("http://localhost:"+port+"/home");
        credentialPage = new CredentialPage(driver);
        credentialPage.editCredential("www.facebook.com","Sasuke","Pasta:");
        assertEquals("The credential was successfully edited !", credentialPage.getSuccessMessage());
        assertEquals("www.facebook.com", credentialPage.getCredentialUrlText());
        assertEquals("Sasuke", credentialPage.getCredentialUsernameText());
        Credential credential = credentialService.getLastElement();
        assertEquals("Pasta:", this.encryptionService.decryptValue(credentialPage.getCredentialPasswordText(),credential.getKey()));
    }

    @Test
    @Order(3)
    public void testDeleteCredential(){
        driver.get("http://localhost:"+port+"/home");
        credentialPage = new CredentialPage(driver);
        credentialPage.deleteCredential();
        assertEquals("The credential was successfully deleted !", credentialPage.getSuccessMessage());
        assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialUrlText();});
        assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialUsernameText();});
        assertThrows(NoSuchElementException.class,() ->{credentialPage.getCredentialPasswordText();});
    }

}
