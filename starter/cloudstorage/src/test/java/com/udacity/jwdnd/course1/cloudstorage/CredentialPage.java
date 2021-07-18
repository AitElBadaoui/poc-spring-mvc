package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    private JavascriptExecutor jsExec;
    private WebDriver  webDriver;

    @FindBy(id = "model-credential-button")
    private WebElement credentialModalButton;

    @FindBy(id = "credential-url")
    private WebElement inputUrl;

    @FindBy(id = "credential-username")
    private WebElement inputUsername;

    @FindBy(id = "credential-password")
    private WebElement inputPassword;

    @FindBy(id = "credentialSubmit")
    private WebElement credentialSubmitButton;

    @FindBy(id = "success-alert")
    private WebElement successAlert;

    @FindBy(id = "credentialUrlText")
    private WebElement credentialUrlText;

    @FindBy(id = "credentialUsernameText")
    private WebElement credentialUsernameText;

    @FindBy(id = "credentialPasswordText")
    private WebElement credentialPasswordText;

    @FindBy(id =  "nav-credentials-tab")
    private WebElement credentialNavButton;

    @FindBy(id = "edit-credential-button")
    private WebElement credentialEditButton;

    @FindBy(id = "delete-credential")
    private WebElement credentialDeleteButton;

    public CredentialPage(WebDriver driver){
        PageFactory.initElements(driver,this);
        jsExec = (JavascriptExecutor) driver;
        webDriver = driver;
    }

    public void openModal(){
        jsExec.executeScript("arguments[0].click();",credentialNavButton);
        jsExec.executeScript("arguments[0].click();",credentialModalButton);
    }

    public void addCredential(String url, String username, String password){
        jsExec.executeScript("arguments[0].value='" + url + "';", inputUrl);
        jsExec.executeScript("arguments[0].value='" + username + "';", inputUsername);
        jsExec.executeScript("arguments[0].value='" + password + "';", inputPassword);
        credentialSubmitButton.submit();
    }

    public void editCredential(String url, String username, String password) throws InterruptedException {
        jsExec.executeScript("arguments[0].click();",credentialNavButton);
        jsExec.executeScript("arguments[0].click();",credentialEditButton);
        jsExec.executeScript("arguments[0].value='" + url + "';", inputUrl);
        jsExec.executeScript("arguments[0].value='" + username + "';", inputUsername);
        jsExec.executeScript("arguments[0].value='" + password + "';", inputPassword);
        credentialSubmitButton.submit();
    }

    public void deleteCredential(){
        jsExec.executeScript("arguments[0].click();",credentialDeleteButton);
    }

    public String getSuccessMessage(){
        return successAlert.getText();
    }

    // verify that new note title is created:
    public String getCredentialUrlText() {
        return credentialUrlText.getAttribute("innerHTML");
    }

    // verify that new note description is created:
    public String getCredentialUsernameText() {
        return credentialUsernameText.getAttribute("innerHTML");
    }

    public String getCredentialPasswordText() {
        return credentialPasswordText.getAttribute("innerHTML");
    }

}
