package utils;

import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import cucumber.api.Scenario;

public class ReportUtils extends seleniumUtils {

	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extentReport;
	public static ExtentTest extentTest;
	public static String diretorioReport;

	public static void criarReport(Scenario cenario) {
		if (extentReport == null) {
			extentReport = new ExtentReports();
			String dir = System.getProperty("user.dir");
			seleniumUtils.criarDiretorio(dir + "\\report");
			limpaDiretorios();
			setDiretorioReport("./report/");
			seleniumUtils.criarDiretorio(diretorioReport);
			htmlReporter = new ExtentHtmlReporter(diretorioReport + "\\report.html");
			extentReport.attachReporter(htmlReporter);
		}
		extentTest = extentReport.createTest(cenario.getName());
	}

	public static void atualizaReport(Scenario cenario) {
		if (cenario.isFailed()) {
			extentTest.log(Status.ERROR, "Erro encontrado durante a execução.");
		} else {
			extentTest.log(Status.PASS, "Cenário executado com sucesso.");
		}
		extentReport.flush();
	}

	public static ExtentTest getExtentTest() {
		return extentTest;
	}

	public static void setDiretorioReport(String diretorio) {
		diretorioReport = diretorio;
	}

	public static String getDiretorioReport() {
		return diretorioReport;
	}

	public static void logMensagem(Status status, String mensagem, String imagem) {
		try {
			System.out.println(mensagem);
			extentTest.log(status, mensagem, MediaEntityBuilder.createScreenCaptureFromPath(imagem).build());
			extentReport.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logMensagem(Status status, String mensagem) {
		System.out.println(mensagem);
		extentTest.log(status, mensagem);
		extentReport.flush();
	}
	
	public static void limpaDiretorios() {
		File report = new File(System.getProperty("user.dir") + "\\report");
		deletarArquivo(report);
		File screenshots = new File(System.getProperty("user.dir") + "\\report\\screenshots");
		deletarArquivo(screenshots);
	}
	
	public static void logMensagemInfoPrint(String mensagem) {
		try {
			System.out.println(mensagem);
			extentTest.log(Status.INFO, mensagem,
					MediaEntityBuilder.createScreenCaptureFromPath(seleniumUtils.getScreenshotReport()).build());
			Log4jUtils.logMensagem("INFO", mensagem);
			extentReport.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMensagemPassouPrint(String mensagem) {
		try {
			System.out.println(mensagem);
			extentTest.log(Status.PASS, mensagem,
					MediaEntityBuilder.createScreenCaptureFromPath(seleniumUtils.getScreenshotReport()).build());
			Log4jUtils.logMensagem("INFO", mensagem);
			extentReport.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMensagemFalhouPrint(String mensagem) {
		try {
			System.out.println(mensagem);
			extentTest.log(Status.FAIL, mensagem,
					MediaEntityBuilder.createScreenCaptureFromPath(seleniumUtils.getScreenshotReport()).build());
			Log4jUtils.logMensagem("INFO", mensagem);
			extentReport.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMensagemErroPrint(String mensagem) {
		try {
			System.out.println(mensagem);
			extentTest.log(Status.ERROR, mensagem,
					MediaEntityBuilder.createScreenCaptureFromPath(seleniumUtils.getScreenshotReport()).build());
			Log4jUtils.logMensagem("INFO", mensagem);
			extentReport.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
