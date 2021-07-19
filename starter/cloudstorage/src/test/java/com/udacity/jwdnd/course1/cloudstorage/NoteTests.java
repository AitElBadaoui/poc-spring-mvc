package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteTests {
    private HomePage homePage;
    private NotePage notePage;
    private static WebDriver driver;
    private SignUpPage signup;
    private LoginPage login;

    private String username = "NoteUser2";
    private String password = "Nadi";
    private String firstName = "Mimo";
    private String lastName = "Senji";

    @LocalServerPort
    private int port;

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
    public void testCreationOfNote(){

        //Create the user that will be used in these tests
        driver.get("http://localhost:"+port+"/signup");
        signup = new SignUpPage(driver);
        signup.signup(firstName, lastName, username, password);

        //Login
        driver.get("http://localhost:"+port+"/login");
        login = new LoginPage(driver);
        login.login(username, password);


        driver.get("http://localhost:"+port+"/home");
        notePage = new NotePage(driver);
        //Open Modal and add Note
        notePage.openNoteModal();
        notePage.addNote("Title","Description");
        //Test success Message
        assertEquals("The note was successfully added !", notePage.getSuccessMessage());

        //Check if table contains new note:
        assertEquals("Title", notePage.getNoteTitleText());
        assertEquals("Description", notePage.getNoteDescriptionText());
    }

    @Test
    @Order(2)
    public void testUpdateNote(){
        driver.get("http://localhost:"+port+"/home");
        notePage = new NotePage(driver);
        homePage = new HomePage(driver);
        //Open Modal and edit note
        notePage.openNoteModal();
        notePage.editNote("Title","Description 2");
        //check success Message
        assertEquals("The note was successfully edited !", homePage.getSuccessMessage());
        //Check if table contains edited note:
        assertEquals("Title", notePage.getNoteTitleText());
        assertEquals("Description 2", notePage.getNoteDescriptionText());
    }

    @Test
    @Order(3)
    public void testDeleteNote(){
        driver.get("http://localhost:"+port+"/home");
        notePage = new NotePage(driver);
        homePage = new HomePage(driver);

        notePage.deleteNote();
        //check success Message
        assertEquals("The note was successfully deleted !", homePage.getSuccessMessage());
        //check that the table no longer contains the note
        assertThrows(NoSuchElementException.class,() ->{notePage.getNoteDescriptionText();});
        assertThrows(NoSuchElementException.class,() ->{notePage.getNoteDescriptionText();});
    }
}
