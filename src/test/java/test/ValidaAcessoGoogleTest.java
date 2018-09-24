package test;

import org.junit.runner.RunWith;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/java/features/ValidaIntegracaoDadosPessoais.feature",
glue = { "" }, monochrome = true, dryRun = false)
@Test(priority = 11)
public class ValidaAcessoGoogleTest extends AbstractTestNGCucumberTests{

}