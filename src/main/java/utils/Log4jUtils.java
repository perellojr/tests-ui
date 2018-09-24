package utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

import cucumber.api.Scenario;

public class Log4jUtils extends seleniumUtils {
	
	public static Logger logger;
	
	@SuppressWarnings("deprecation")
	public static void criaLog(Scenario cenario) {
		String dir = System.getProperty("user.dir");
		PropertyConfigurator.configure(dir + "/resources/log4j.properties");
		String filename = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		filename = filename.replace(".", "_");
		String nomeCenario = cenario.getName();
		nomeCenario = nomeCenario.replace(" ", "_");
		seleniumUtils.criarDiretorio(dir + "\\logs");
		logger = LogManager.getLogger(Log4jUtils.class.getName());
		try {
			Logger.shutdown();
			SimpleLayout layout = new SimpleLayout();
			FileAppender appender = new FileAppender(layout, dir + "\\logs\\" + nomeCenario + "_" + filename + ".log",
					false);
			logger.addAppender(appender);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getLog() {
		return logger;
	}
	
	public static void logMensagem(String tipo_mensagem, String mensagem) {
		switch (tipo_mensagem) {
		case "DEBUG": 
			logger.debug(mensagem);
			break;
		case "INFO": 
			logger.info(mensagem);
			break;
		case "WARN": 
			logger.warn(mensagem);
			break;
		case "ERROR": 
			logger.error(mensagem);
			break;
		case "FATAL": 
			logger.fatal(mensagem);
			break;	
		default:
			logger.warn("Tipo de log: " + tipo_mensagem + " não existe.");
			logger.warn("Mensagem a ser enviada: " + mensagem);
			break;	
		}
	}
}