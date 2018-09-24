package steps;

import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.Status;

import cucumber.api.DataTable;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Quando;
import pages.GooglePage;
import utils.ReportUtils;

public class GoogleSteps {

	GooglePage GooglePage = new GooglePage();

	@Dado("que acesse o site do google")
	public void acessaSiteGoogle() {
		ReportUtils.logMensagem(Status.INFO, "Dado que acesse o site do google");
		GooglePage.acessaGoogle();
	}

	@Quando("valido todos os elementos da pagina inicial do google")
	public void validaElementosPaginaInicialGoogle() {
		ReportUtils.logMensagem(Status.INFO, "Quando valido todos os elementos da pagina inicial do google");
	}

	@E("preencho o campo de pesquisa com dados validos")
	public void preencheCampoPesquisaGoogle(DataTable credenciais) {
		ReportUtils.logMensagem(Status.INFO, "preencho o campo de pesquisa com dados validos");
		List<Map<String,String>> data = credenciais.asMaps(String.class,String.class);
		GooglePage.preencheCampoTextoPesquisa(data.get(0).get("cep"));
	}

}
