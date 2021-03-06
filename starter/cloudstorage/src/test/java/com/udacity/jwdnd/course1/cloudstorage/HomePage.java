package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {


    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "success-alert")
    private WebElement successAlert;


    public HomePage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }
    public void logout(){
        logoutButton.submit();
    }

    public String getSuccessMessage(){
        return successAlert.getText();
    }
}
