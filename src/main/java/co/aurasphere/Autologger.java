package co.aurasphere;

import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Application for automatically logging into websites and sending screenshots
 * through Telegram.
 * 
 * @author Donato Rimenti
 */
public class Autologger {

	/**
	 * URL of the login page.
	 */
	private static String targetUrl = System.getenv("target_url");

	/**
	 * Email for the login.
	 */
	private static String email = System.getenv("email");

	/**
	 * Password for the login.
	 */
	private static String password = System.getenv("password");

	/**
	 * Token for the screenshots Telegram bot.
	 */
	private static String botToken = System.getenv("bot_token");

	/**
	 * ID of the chat where to send screenshots.
	 */
	private static String chatId = System.getenv("chat_id");

	/**
	 * Telegram bot for sending screenthots.
	 */
	private static TelegramBot bot = new TelegramBot(botToken);

	/**
	 * Logs into a website and sends a screenshot through Telegram.
	 * 
	 * @param args
	 *                 null
	 * @throws InterruptedException
	 *                                  if anything wrong occurs
	 */
	public static void main(String[] args) throws InterruptedException {

		// Fast-fail if the required environment variables are not set.
		Objects.requireNonNull(botToken);
		Objects.requireNonNull(targetUrl);
		Objects.requireNonNull(email);
		Objects.requireNonNull(password);
		Objects.requireNonNull(chatId);

		// Configures the ChromeDriver object by downloading it and setting the required
		// environment variable.
		WebDriverManager.chromedriver().setup();

		// Required options to run on UNIX. Disables the developer tools and the
		// security.
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
		ChromeDriver driver = new ChromeDriver(chromeOptions);

		// Goes to the page and inputs the credentials. Between each step there's a
		// delay in order to wait for new HTML elements to be displayed.
		driver.get(targetUrl);
		Thread.sleep(20_000);
		WebElement emailElement = driver.findElement(By.id("email"));
		emailElement.sendKeys(email);
		emailElement.submit();
		Thread.sleep(20_000);
		WebElement passwordElement = driver.findElement(By.id("password"));
		passwordElement.sendKeys(password);
		passwordElement.submit();
		Thread.sleep(100_000);

		// Sends a screenshot through Telegram and closes the window.
		sendScreenshot(driver);
		driver.quit();
	}

	/**
	 * Sends a screenshot through Telegram.
	 * 
	 * @param driver
	 *                   the ChromeDriver, used to take the screenshot
	 */
	private static void sendScreenshot(ChromeDriver driver) {
		byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
		bot.execute(new SendPhoto(chatId, screenshot));
	}

}