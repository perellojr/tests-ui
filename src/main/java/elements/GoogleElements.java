package elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import utils.seleniumUtils;

public class GoogleElements extends seleniumUtils {

	public static WebDriver driver;

	@FindBy(css = "[id='lst-ib']")
	public WebElement CAMPO_INPUT_PESQUISAR;
}
