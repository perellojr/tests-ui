package pages;

import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.Status;
import elements.GoogleElements;
import setups.TestRule;
import utils.ReportUtils;
import utils.seleniumUtils;

public class GooglePage extends GoogleElements {

	public GooglePage() {
		driver = TestRule.getDriver();
		PageFactory.initElements(TestRule.getDriver(), this);
	}

	public void selecionaSistema() {
		String ambiente = loadFromPropertiesFile("ambientes.properties", "AMBIENTE");
		switch (ambiente) {
		case "DEV":
			String ambienteDev = loadFromPropertiesFile("ambientes.properties", "URL_INICIO_DEV");
			driver.navigate().to(ambienteDev);
			break;
		case "HLG":
			String ambienteHlg = loadFromPropertiesFile("ambientes.properties", "URL_INICIO_HLG");
			driver.navigate().to(ambienteHlg);
			break;
		default:
			break;
		}
		sleeps(2000);
		ReportUtils.logMensagem(Status.INFO, "Realizando Captura de Tela", seleniumUtils.getScreenshotReport());
	}

	public void acessaGoogle() {
		String ambiente = loadFromPropertiesFile("ambientes.properties", "AMBIENTE_GOOGLE");
		driver.navigate().to(ambiente);
		sleeps(2000);
		ReportUtils.logMensagem(Status.INFO, "Realizando Captura de Tela", seleniumUtils.getScreenshotReport());
	}

	public void preencheCampoTextoPesquisa(String textoCampo) {
		CAMPO_INPUT_PESQUISAR.sendKeys(textoCampo);
	}
}
