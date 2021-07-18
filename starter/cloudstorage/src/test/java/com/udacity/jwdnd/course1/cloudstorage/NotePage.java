package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotePage {

    private JavascriptExecutor jsExec;
    private WebDriver  webDriver;

    @FindBy(id = "nav-notes-button")
    private WebElement noteModalButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputDescription;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(id = "success-alert")
    private WebElement successAlert;

    @FindBy(xpath = "//*[@id='noteTitleText']")
    private WebElement noteTitleText;
    @FindBy(xpath = "//*[@id='noteDescriptionText']")
    private WebElement noteDescriptionText;

    @FindBy(id =  "nav-notes-tab")
    private WebElement noteNavButton;

    @FindBy(id = "edit-note-button")
    private WebElement noteEditButton;
    @FindBy(id = "delete-note")
    private WebElement noteDeleteButton;

    public NotePage(WebDriver driver){
        PageFactory.initElements(driver,this);
        jsExec = (JavascriptExecutor) driver;
        webDriver = driver;
    }

    public void openNoteModal(){
        jsExec.executeScript("arguments[0].click();",noteNavButton);
        jsExec.executeScript("arguments[0].click();",noteModalButton);
    }
    public void addNote(String title, String description){
        jsExec.executeScript("arguments[0].value='" + title + "';", inputNoteTitle);
        jsExec.executeScript("arguments[0].value='" + description + "';", inputDescription);
        noteSubmitButton.submit();
    }

    public void editNote(String title,String description){
        jsExec.executeScript("arguments[0].click();",noteEditButton);
        jsExec.executeScript("arguments[0].value='" + title + "';", inputNoteTitle);
        jsExec.executeScript("arguments[0].value='" + description + "';", inputDescription);
        noteSubmitButton.submit();
    }

    public void deleteNote(){
        jsExec.executeScript("arguments[0].click();",noteDeleteButton);
    }

    public String getSuccessMessage(){
        return successAlert.getText();
    }

    // verify that new note title is created:
    public String getNoteTitleText() {
        return noteTitleText.getAttribute("innerHTML");
    }
    // verify that new note description is created:
    public String getNoteDescriptionText() {
        return noteDescriptionText.getAttribute("innerHTML");
    }
}
