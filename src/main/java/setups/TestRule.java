package setups;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.Status;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.Log4jUtils;
import utils.ReportUtils;
import utils.seleniumUtils;

public class TestRule {

	protected static WebDriver driver;
	public static String nomeCenario;
	
	seleniumUtils seleniumutils = new seleniumUtils();
	
	@SuppressWarnings("static-access")
	@Before
	public void beforeCenario(Scenario cenario) throws SQLException, MalformedURLException {
		ReportUtils.criarReport(cenario);
		Log4jUtils.criaLog(cenario);
		String pathProjeto = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", pathProjeto + "/drivers/chromedriver");
		String URL_HOST_REMOTE = "http://172.19.24.163:4444/wd/hub";
		
		String remoteWebDriver = seleniumutils.loadFromPropertiesFile("config.properties", "REMOTE_WEB_DRIVER");
		String headless = seleniumutils.loadFromPropertiesFile("config.properties", "CHROME_HEADLESS");
		
		if(remoteWebDriver.equalsIgnoreCase("true")) { 
			DesiredCapabilities caps = new DesiredCapabilities();	
			caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
			caps.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			ReportUtils.logMensagem(Status.INFO, "Iniciando execução em Remote Web Driver, server: " + URL_HOST_REMOTE);
		if(headless.equalsIgnoreCase("true")) {
			ChromeOptions options = new ChromeOptions(); 
			options.setAcceptInsecureCerts(true); 
			options.addArguments("headless");
			options.merge(caps); 
			ReportUtils.logMensagem(Status.INFO, "Iniciando ChromeDriver em modo Headless.");
//			RemoteWebDriver driver = new RemoteWebDriver(new URL(hubURL), options);
		}
			driver = new RemoteWebDriver(new URL(URL_HOST_REMOTE), caps);
//			driver.manage().window().maximize();			
			
		}else {
			
//			String pathProjeto = System.getProperty("user.dir");
//			System.setProperty("webdriver.chrome.driver", pathProjeto + "/drivers/chromedriver.exe");
			
			ChromeOptions options = new ChromeOptions();
			
			if(headless.equalsIgnoreCase("true")) {
				  options.addArguments("headless");
				  String pluginToDisable = "Chrome PDF Viewer";
				  options.addArguments("plugins.plugins_enable", pluginToDisable);
			      options.addArguments("window-size=1200x600");//A opção window-size é importante para sites responsivos
			      ReportUtils.logMensagem(Status.INFO, "Iniciando ChromeDriver em modo Headless.");
			      driver = new ChromeDriver(options);
			}else {
				ReportUtils.logMensagem(Status.INFO, "Iniciando ChromeDriver.");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
			}
		}
		
		
		nomeCenario = cenario.getName();
		ReportUtils.logMensagem(Status.INFO, "Executando Cenario de Teste: " + nomeCenario);

		// SqlServerUtils sqlserverutils = new SqlServerUtils();
		// System.out.println("Preparando Base 'Automacao'");
		// sqlserverutils.preparaBaseP2A();
	}
	
	public static WebDriver getDriver() {
		return driver;
	}

	public static String getNomeCensario() {
		return nomeCenario;
	}

	@After
	public void afterCenario(Scenario cenario) {
		ReportUtils.logMensagem(Status.INFO, "Finalizando instancia do Chrome Driver",
				seleniumUtils.getScreenshotReport());
		driver.quit();
		ReportUtils.atualizaReport(cenario);
		seleniumUtils.sleep(2000);
	}
}
