package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTests {
    private SignUpPage signup;
    private LoginPage login;
    private HomePage homePage;
    private WebDriver driver;

    private String username = "AuthUser1";
    private String password = "Nadi";
    private String firstName = "Mimo";
    private String lastName = "Senji";

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }


    @BeforeEach
    public void beforeEach() {

        this.driver = new ChromeDriver();

    }

    @Test
    public void testUnauthorizedUserAccess(){
        //Check that the user can access the signup page
        driver.get("http://localhost:"+port+"/signup");
        assertEquals(driver.getCurrentUrl(), "http://localhost:"+port+"/signup");

        //Check that the user can access the login page
        driver.get("http://localhost:"+port+"/login");
        assertEquals(driver.getCurrentUrl(), "http://localhost:"+port+"/login");

        //Verify that the user cannot access the home page and is redirected to the login page.
        driver.get("http://localhost:"+port+"/home");
        assertEquals(driver.getCurrentUrl(), "http://localhost:"+port+"/login");
    }

    @Test
    public void testSignUpAndLoginWithNewUser(){
        //Sign up
        driver.get("http://localhost:"+port+"/signup");
        signup = new SignUpPage(driver);
        signup.signup(firstName, lastName, username, password);

        //Login
        driver.get("http://localhost:"+port+"/login");
        login = new LoginPage(driver);
        login.login(username, password);
        assertEquals(driver.getCurrentUrl(), "http://localhost:"+port+"/home");


        //Logout and verify that is no longer authorized to access to /home
        homePage = new HomePage(driver);
        homePage.logout();
        driver.get("http://localhost:"+port+"/home");
        assertEquals(driver.getCurrentUrl(), "http://localhost:"+port+"/login");
    }

}
