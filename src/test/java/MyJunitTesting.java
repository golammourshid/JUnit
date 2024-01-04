import org.apache.commons.io.FileUtils;
import java.lang.Thread;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunitTesting {
    WebDriver driver;

    @BeforeAll
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @DisplayName("Visit the website and match the title")
    @Test
    public void getTitle() {
        driver.get("https://demoqa.com/");
        String title__actual = driver.getTitle();
        String title_expected = "DEMOQA";
        System.out.println(title__actual);
        Assertions.assertTrue(title__actual.contains(title_expected));
//        Assertions.assertEquals(title_expected, title__actual);
    }

    @DisplayName("Check if the banner-image exists")
    @Test
    public void checkIfImageExists() {
        driver.get("https://demoqa.com/");
//        boolean isExists = driver.findElement(By.cssSelector("[class='banner-image']")).isDisplayed();
        boolean isExists = driver.findElement(By.className("banner-image")).isDisplayed();
        Assertions.assertTrue(isExists);
    }

    @DisplayName("User should submit the form properly")
    @Test
    public void submitForm() {
        driver.get("https://demoqa.com/text-box");
        List<WebElement> txtBoxElement = driver.findElements(By.className("form-control"));
        txtBoxElement.get(0).sendKeys("Golam Mourshid");
        txtBoxElement.get(1).sendKeys("golammourshid100@gmail.com");
        txtBoxElement.get(2).sendKeys("Dhaka");
        txtBoxElement.get(3).sendKeys("Kushtia");
        driver.findElement(By.id("submit")).click();

        Utils.doScroll(driver);
    }

    @DisplayName("Multiple button clicking")
    @Test
    public void clickOnMultipleButton() {
        driver.get("https://demoqa.com/buttons");

        Actions action = new Actions(driver);
        List<WebElement> buttonList = driver.findElements(By.cssSelector("button"));

        action.doubleClick(buttonList.get(1)).perform();
        String doubleClickMessage = driver.findElement(By.id("doubleClickMessage")).getText();
        Assertions.assertTrue(doubleClickMessage.contains("You have done a double click"));

        action.contextClick(buttonList.get(2)).perform();
        String rightClickMessage = driver.findElement(By.id("rightClickMessage")).getText();
        Assertions.assertTrue(rightClickMessage.contains("You have done a right click"));

        action.click(buttonList.get(3)).perform();
//        buttonList.get(3).click();
        String dynamicClickMessage = driver.findElement(By.id("dynamicClickMessage")).getText();
        Assertions.assertTrue(dynamicClickMessage.contains("You have done a dynamic click"));
    }

    @DisplayName("Handling Alerts")
    @Test
    public void handleAlerts() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        driver.switchTo().alert().accept();
//        driver.switchTo().alert().dismiss();
        driver.findElement(By.id("promtButton")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.switchTo().alert().sendKeys("Antor");
//        Thread.sleep(3000);
        driver.switchTo().alert().accept();
//        Thread.sleep(5000);

        String promptResult = driver.findElement(By.id("promptResult")).getText();
        Assertions.assertTrue(promptResult.contains("Antor"));


    }

    @DisplayName("Selecting date")
    @Test
    public void selectDate() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
//        Thread.sleep(2000);
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.CONTROL + "A" + Keys.BACK_SPACE);
//        Thread.sleep(2000);
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("11/11/2022", Keys.ENTER);
    }

    @DisplayName("Getting current date and paste in the editable field")
    @Test
    public void gettingCurrentDateAndPasteInTheEditableField() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.CONTROL + "A" + Keys.BACK_SPACE);
//        Create object of SimpleDateFormat class and decide the format
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

//        get current date and time
        Date date = new Date();
//        Formatting the date
        String currentDate = dateFormat.format(date);
        System.out.println(currentDate);
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(currentDate, Keys.ENTER);
    }

    @DisplayName("Select date from dropdown")
    @Test
    public void selectDateFromDropdown() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        Utils.waitForThePresenceOfTheElement(driver, By.className("react-datepicker__month-select"));
        Select month = new Select(driver.findElement(By.className("react-datepicker__month-select")));
        month.selectByValue("2");
        Select year = new Select(driver.findElement(By.className("react-datepicker__year-select")));
        year.selectByValue("1904");
        driver.findElement(By.xpath("//div[@aria-label='Choose Wednesday, March 23rd, 1904']")).click();
    }

    @DisplayName("Select other dropdown")
    @Test
    public void selectDropdown() {
        driver.get("https://demoqa.com/select-menu");
//        driver.findElement(By.id("datePickerMonthYearInput")).click();
//        Utils.waitForThePresenceOfTheElement(driver, By.className("react-datepicker__month-select"));
        Select color = new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("2");
        Select cars = new Select(driver.findElement(By.id("cars")));

//        Checking if can select multiple value
        if (cars.isMultiple()) {
            cars.selectByValue("volvo");
            cars.selectByValue("saab");
        }
    }

    @DisplayName("Hovering mouse")
    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("https://tinywow.com/");
        WebElement menuWrite = driver.findElement(By.xpath("//a[@href='https://tinywow.com/tools/write'][normalize-space()='Write']"));
        Actions action = new Actions(driver);
        action.moveToElement(menuWrite).perform();
        Thread.sleep(5000);
    }

    @DisplayName("Keyboard events")
    @Test
    public void keyboardEvents() throws InterruptedException, IOException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        Thread.sleep(5000);
        action.sendKeys("Selenium Webdriver").keyDown(Keys.SHIFT).doubleClick().contextClick().perform();
        Thread.sleep(5000);

        Utils.takeScreenShot(driver);
    }

    @DisplayName("Checking modal dialog")
    @Test
    public void modalDialog() {
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String text = driver.findElement(By.className("modal-body")).getText();
        System.out.println(text);
        driver.findElement(By.id("closeSmallModal")).click();
    }

    @DisplayName("Uploading file")
    @Test
    public void uploadImage() {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("C:\\Users\\USER\\Downloads\\Upload.jpg");
        String text = driver.findElement(By.id("uploadedFilePath")).getText();
        System.out.println(text);
        Assertions.assertTrue(text.contains("Upload.jpg"));
    }

    @DisplayName("Download image")
    @Test
    public void downloadFile() {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("downloadButton")).click();
    }

    

    @AfterAll
    public void closeDriver() {
        //Close the entire browser
//        driver.quit();

//        Close only that specific tab
//        driver.close();
    }
}
