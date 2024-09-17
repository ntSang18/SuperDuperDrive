package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudstorageApplicationTests {

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
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	public void doCreateNote(String noteTitle, String noteDescription) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-notes > button")));

		WebElement btnOpenCreateNoteModal = driver.findElement(By.cssSelector("#nav-notes > button"));
		btnOpenCreateNoteModal.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement inpTitle = driver.findElement(By.id("note-title"));
		WebElement inpDescription = driver.findElement(By.id("note-description"));
		inpTitle.sendKeys(noteTitle);
		inpDescription.sendKeys(noteDescription);

		WebElement btnSubmitSaveNote = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		btnSubmitSaveNote.click();
	}

	public void doEditNote(String noteTitle, String noteDescription) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > button")));

		WebElement btnOpenEditNoteModal = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > button"));
		btnOpenEditNoteModal.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement inpTitle = driver.findElement(By.id("note-title"));
		WebElement inpDescription = driver.findElement(By.id("note-description"));
		inpTitle.clear();
		inpTitle.sendKeys(noteTitle);
		inpDescription.clear();
		inpDescription.sendKeys(noteDescription);

		WebElement btnSubmitSaveNote = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		btnSubmitSaveNote.click();
	}

	@Test
	public void testCreateNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("create", "note", "createnote", "123");
		doLogIn("createnote", "123");
		doCreateNote("noteTitle", "noteDes");

		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#userTable > tbody > tr > th")));

		WebElement title = driver.findElement(By.cssSelector("#userTable > tbody > tr > th"));
		WebElement description = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(3)"));

		Assertions.assertEquals("noteTitle", title.getText());
		Assertions.assertEquals("noteDes", description.getText());
	}

	@Test
	public void testEditNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("edit", "note", "editnote", "123");
		doLogIn("editnote", "123");
		doCreateNote("createNote", "createNote");
		doEditNote("editNote", "editNote");

		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#userTable > tbody > tr > th")));

		WebElement title = driver.findElement(By.cssSelector("#userTable > tbody > tr > th"));
		WebElement description = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(3)"));

		Assertions.assertEquals("editNote", title.getText());
		Assertions.assertEquals("editNote", description.getText());
	}

	@Test
	public void testRemoveNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("remove", "note", "removenote", "123");
		doLogIn("removenote", "123");
		doCreateNote("createNote", "createNote");

		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > a")));

		WebElement btnRemove = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > a"));
		btnRemove.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		WebElement tabNote1 = driver.findElement(By.id("nav-notes-tab"));
		tabNote1.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("userTable")));

		WebElement table = driver.findElement(By.id("userTable"));

		List<WebElement> rows = table.findElements(By.xpath("//tbody/tr"));

		Assertions.assertEquals(0, rows.size());
	}

	public void doCreateCredential(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialTab.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-credentials > button")));

		WebElement btnOpenCreateCredentialModal = driver.findElement(By.cssSelector("#nav-credentials > button"));
		btnOpenCreateCredentialModal.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement inpUrl = driver.findElement(By.id("credential-url"));
		WebElement inpUsername = driver.findElement(By.id("credential-username"));
		WebElement inpPassword = driver.findElement(By.id("credential-password"));
		inpUrl.sendKeys(url);
		inpUsername.sendKeys(username);
		inpPassword.sendKeys(password);

		WebElement btnSubmitSaveCredential = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		btnSubmitSaveCredential.click();
	}

	public void doEditCredential(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialTab.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-credentials > button")));

		WebElement btnOpenEditCredentialModal = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button"));
		btnOpenEditCredentialModal.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement inpUrl = driver.findElement(By.id("credential-url"));
		WebElement inpUsername = driver.findElement(By.id("credential-username"));
		WebElement inpPassword = driver.findElement(By.id("credential-password"));
		inpUrl.clear();
		inpUrl.sendKeys(url);
		inpUsername.clear();
		inpUsername.sendKeys(username);
		inpPassword.clear();
		inpPassword.sendKeys(password);

		WebElement btnSubmitSaveCredential = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		btnSubmitSaveCredential.click();
	}

	@Test
	public void testCreateCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("create", "credential", "createcredential", "123");
		doLogIn("createcredential", "123");
		doCreateCredential("google.com", "test", "123");

		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#credentialTable > tbody > tr > th")));

		WebElement url = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > th"));
		WebElement username = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)"));
		WebElement password = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(4)"));

		Assertions.assertEquals("google.com", url.getText());
		Assertions.assertEquals("test", username.getText());
		Assertions.assertEquals("123", password.getText());
	}

	@Test
	public void testEditCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("edit", "credential", "editcredential", "123");
		doLogIn("editcredential", "123");
		doCreateCredential("google.com", "test", "123");
		doEditCredential("facebook.com", "test1", "1234");

		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#credentialTable > tbody > tr > th")));

		WebElement url = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > th"));
		WebElement username = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)"));
		WebElement password = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(4)"));

		Assertions.assertEquals("facebook.com", url.getText());
		Assertions.assertEquals("test1", username.getText());
		Assertions.assertEquals("1234", password.getText());
	}

	@Test
	public void testRemoveCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		doMockSignUp("remove", "credential", "removecredential", "123");
		doLogIn("removecredential", "123");
		doCreateCredential("google.com", "test", "123");

		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > a")));

		WebElement btnRemove = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > a"));
		btnRemove.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		WebElement tabCredential1 = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential1.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialTable")));

		WebElement table = driver.findElement(By.id("credentialTable"));

		List<WebElement> rows = table.findElements(By.xpath("//tbody/tr"));

		Assertions.assertEquals(0, rows.size());
	}

}
