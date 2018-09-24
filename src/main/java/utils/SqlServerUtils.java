package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import com.aventstack.extentreports.Status;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class SqlServerUtils {

	@SuppressWarnings("static-access")
	public static SQLServerDataSource ds_AgiplanNet() {

		seleniumUtils sel = new seleniumUtils();
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(sel.loadFromPropertiesFile("conexao.properties", "USER_DATABASE_SQLSERVER"));
		ds.setPassword(sel.loadFromPropertiesFile("conexao.properties", "PASSWD_DATABASE_SQLSERVER"));
		ds.setServerName(sel.loadFromPropertiesFile("conexao.properties", "SERVER_NAME_SQLSERVER"));

		return ds;
	}

	@SuppressWarnings("static-access")
	public static SQLServerDataSource ds_Sicred() {

		seleniumUtils sel = new seleniumUtils();

		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(sel.loadFromPropertiesFile("conexao.properties", "SICRED_USER_DATABASE_SQLSERVER"));
		ds.setPassword(sel.loadFromPropertiesFile("conexao.properties", "SICRED_PASSWD_DATABASE_SQLSERVER"));
		ds.setServerName(sel.loadFromPropertiesFile("conexao.properties", "SICRED_SERVER_NAME_SQLSERVER"));
		ds.setPortNumber(
				Integer.parseInt(sel.loadFromPropertiesFile("conexao.properties", "SICRED_SERVER_PORT_SQLSERVER")));
		ds.setDatabaseName(sel.loadFromPropertiesFile("conexao.properties", "SICRED_SERVER_DATA_BASE_NAME"));
		return ds;
	}

	public void insertSQLServerDataBaseAutomacao(SQLServerDataSource ds, String insert) throws SQLException {
		Connection con = ds.getConnection();
		Statement st = con.createStatement();
		try {
			st.executeUpdate(insert);
//			ReportUtils.logMensagem(Status.INFO, "Realizou insert no Banco com sucesso!");
			con.close();
		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "Não foi possivel realizar insert no Banco!" + e.toString());
			Log4jUtils.logMensagem("ERROR", "Não foi possivel realizar insert no Banco!" + e.toString());
			// Assert.assertTrue(false);
		}

	}

	public void executaQuery(SQLServerDataSource ds, String query) throws SQLException {
		Connection con = ds.getConnection();
		Statement st = con.createStatement();
		try {
			st.execute(query);
			con.close();
		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "executaQuery : Não foi possivel realizar insert no Banco! " + e.toString());
			Log4jUtils.logMensagem("ERROR", "executaQuery : Não foi possivel realizar insert no Banco! " + e.toString());
		}
	}

	public ResultSet selectSQL(SQLServerDataSource ds, String select) throws SQLException {
		ResultSet rs = null;
		Connection con = ds.getConnection();
		Statement stmt = con.createStatement();

		try {
			rs = stmt.executeQuery(select);
//			ReportUtils.logMensagem(Status.INFO, "Realizou Consulta no Banco com sucesso!");
		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "Metodo selectSQL : Não foi possivel realizar Consulta no Banco! " + e.toString());
			Log4jUtils.logMensagem("ERROR", "Metodo selectSQL : Não foi possivel realizar Consulta no Banco! " + e.toString());
		}
		return rs;
	}

	public void preparaDiasUteis() throws SQLException {

		ResultSet rs = null;
		String query = "";
		boolean existe = false;

		SQLServerDataSource ds = ds_AgiplanNet();

		Calendar cal = Calendar.getInstance();
		// o mes de janeiro � 0 por isso adicionamos +1
		int mes = cal.get(Calendar.MONTH) + 1;

		query = "use automacao select * from dbo.vencimentosINSS";
		rs = selectSQL(ds, query);

		while (rs.next()) {
			existe = true;
			if (!verificaDataBancoConveniosINSS(rs.getString(1), mes)) {
				query = "use automacao DELETE FROM dbo.vencimentosINSS";
				executaQuery(ds, query);
				existe = false;
			}
			break;
		}
		if (!existe) {
			System.out.println("Consultando informa��es de Vencimentos INSS...");

			query = "use AgiplanNetCn select T01.Data, T02.Nome from FontesPagadoras.DataPagamento T01 join FontesPagadoras.FontePagadora T02 on T01.InformacaoPagamentoCodigo = T02.InformacaoPagamentoCodigo WHERE  T02.FontePagadoraId in ('0001.01','0001.02', '0001.03', '0001.04', '0001.05', '0001.06', '0001.07', '0001.08', '0001.09', '0001.10') and T01.Tipo = 'C' and T01.AnoBase = 2018 and T01.Folha = VALUE1 order by T02.FontePagadoraId";
			query = query.replaceAll("VALUE1", "" + mes + "");
			rs = selectSQL(ds, query);
			insereDiasUteis(ds, rs);
		}
	}

	public void insereDiasUteis(SQLServerDataSource ds, ResultSet rs) throws SQLException {

		String query = "use automacao INSERT INTO dbo.vencimentosINSS (dataPagamento, regraNome) VALUES ";
		StringBuilder select = new StringBuilder();
		select.append(query);
		int i = 1;
		while (rs.next()) {
			if (i == 10) {
				select.append("('" + rs.getString(1) + "'," + "'INSS - 0001." + i + " - " + rs.getString(2) + "'),");
			} else
				select.append("('" + rs.getString(1) + "'," + "'INSS - 0001.0" + i + " - " + rs.getString(2) + "'),");
			i++;
		}

//		ReportUtils.logMensagem(Status.INFO, query);
		// limpa a ultima virgula da string de query
		query = select.toString();
		query = query.substring(0, query.length() - 1);
		executaQuery(ds, query);
//		ReportUtils.logMensagem(Status.INFO, "Insercao de Dias: " + query + "realizada com sucesso!");
	}

	public boolean verificaDataBancoConveniosINSS(String dataBancoDados, int mesAtual) {

		int mesBD = Integer.parseInt(dataBancoDados.substring(6, 7));
//		ReportUtils.logMensagem(Status.INFO, "data Mes de dados = " + mesBD);
//		ReportUtils.logMensagem(Status.INFO, "Mes atual " + mesAtual);

		if (mesBD == mesAtual) {
			return true;
		}
		return false;
	}

	public void insereUsoCpfHistorico(String cpf) throws SQLException {

		SQLServerDataSource ds = ds_AgiplanNet();

		String query = "use automacao insert into cpfUsados values ('" + cpf + "')";
		executaQuery(ds, query);
//		ReportUtils.logMensagem(Status.INFO, "Insercao de CPF: " + cpf + " realizada com sucesso!");
	}

	// public String retornaOrgaosBloqueadosCorrentista() {
	// ResultSet rs = null;
	// String user = seleniumUtils.loadFromPropertiesFile("conexao.properties",
	// "USER_DATABASE_SQLSERVER");
	// String senha = seleniumUtils.loadFromPropertiesFile("conexao.properties",
	// "PASSWD_DATABASE_SQLSERVER");
	// String serverName =
	// seleniumUtils.loadFromPropertiesFile("conexao.properties",
	// "SERVER_NAME_SQLSERVER");
	// int port = Integer
	// .parseInt(seleniumUtils.loadFromPropertiesFile("conexao.properties",
	// "SERVER_PORT_SQLSERVER"));
	// String dataBaseName = "automacao";
	// String query = "use AgiplanNetCn select OrigemOrgaoCodigo from
	// FontesPagadoras.vwEsteira_RegrasPagamento where NomeConcatenado like
	// '%BLOQUEADO%'";
	// StringBuilder select = new StringBuilder();
	//
	// try {
	// rs = selectSQL(user, senha, serverName, port, dataBaseName, query);
	//
	// while (rs.next()) {
	// select.append("'" + rs.getString(1) + "',");
	// }
	// } catch (SQLException e) {
	// ReportUtils.logMensagem(Status.INFO, "Erro em
	// retornaOrgaosBloqueadosCorrentista ao consultar BD!");
	// }
	// query = select.toString();
	// query = query.substring (0, query.length() - 1);
	// ReportUtils.logMensagem(Status.INFO, query);
	// return query;
	// }

	// retorna numero de orgao que atendam ao criterio da String por exemplo
	// Bloqueados, INSS, SIAPE
	
	public String retornaOrgao(String orgao) {
		ResultSet rs = null;
		SQLServerDataSource ds = ds_AgiplanNet();

		String query = "use AgiplanNetCn  select rp.OrigemOrgaoCodigo, rp.*  from FontesPagadoras.vwEsteira_Grupos EG INNER JOIN FontesPagadoras.vwEsteira_FontesPagadoras FP on EG.GrupoCodigo = fp.GrupoCodigo and eg.Nome like '%"
				+ orgao
				+ "%' INNER JOIN fontesPagadoras.vwEsteira_RegrasPagamento RP on RP.FontePagadoraCodigo = fp.FontePagadoraCodigo and fp.FontePagadoraSituacao = 'A'";

//		ReportUtils.logMensagem(Status.INFO, "Query de Retorno de Orgao: " + orgao + " : " + query);

		StringBuilder select = new StringBuilder();
		try {
			rs = selectSQL(ds, query);

			while (rs.next()) {
				select.append("'" + rs.getString(1) + "',");
			}
		} catch (SQLException e) {
//			ReportUtils.logMensagem(Status.INFO, "Erro em retornaOrgaosBloqueadosCorrentista ao consultar BD!");
		}
		query = select.toString();
		query = query.substring(0, query.length() - 1);
//		ReportUtils.logMensagem(Status.INFO, query);
		return query;
	}

	public void insertNoBancoAutomacao(String cpf, String numero_atendimento, String tipo_produto, String piloto,
			String numero_proposta, String numero_ficha, String trabalhado, String politicas_reprovadas,
			String mensagem_policia_reprovada) throws SQLException {
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");

		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp data = new Timestamp(cal.getTimeInMillis());

		SQLServerDataSource ds = ds_AgiplanNet();

		StringBuilder insert = new StringBuilder();

		insert.append("USE AUTOMACAO INSERT INTO propostasEFichasGeradas VALUES (");
		insert.append("'" + cpf + "',");
		insert.append("'" + numero_atendimento + "',");
		insert.append("'" + tipo_produto + "',");
		insert.append("'" + piloto + "',");
		insert.append("'" + numero_proposta + "',");
		insert.append("'" + numero_ficha + "',");
		insert.append("'" + trabalhado + "',");
		insert.append("'" + politicas_reprovadas + "',");
		insert.append("'" + mensagem_policia_reprovada + "',");
		insert.append("'" + data + "'");
		insert.append(")");

//		ReportUtils.logMensagem(Status.INFO, "insert final: " + insert);
		String insertFinal = insert.toString();

		SqlServerUtils sqlUtils = new SqlServerUtils();
		sqlUtils.insertSQLServerDataBaseAutomacao(ds, insertFinal);
	}

	public String retornaOrgaoBloqueados(String orgao) {
		ResultSet rs = null;
		SQLServerDataSource ds = ds_AgiplanNet();

		String query = "use AgiplanNetCn select OrigemOrgaoCodigo from FontesPagadoras.vwEsteira_RegrasPagamento where NomeConcatenado like '%"
				+ orgao + "%'";

//		ReportUtils.logMensagem(Status.INFO, "Query de Retorno de Orgao: " + orgao + " : " + query);

		StringBuilder select = new StringBuilder();
		try {
			rs = selectSQL(ds, query);

			while (rs.next()) {
				select.append("'" + rs.getString(1) + "',");
			}
		} catch (SQLException e) {
//			ReportUtils.logMensagem(Status.INFO, "Erro em retornaOrgaosBloqueadosCorrentista ao consultar BD!");
		}
		query = select.toString();
		query = query.substring(0, query.length() - 1);
//		ReportUtils.logMensagem(Status.INFO, query);
		return query;
	}

	// public String retornaOrgaoSiape() {
	// ResultSet rs = null;
	// SQLServerDataSource ds = ds_AgiplanNet();
	//
	// String query = "use AgiplanNetCn SELECT rp.OrigemOrgaoCodigo, rp.* FROM
	// FontesPagadoras.vwEsteira_RegrasPagamento rp INNER JOIN
	// FontesPagadoras.vwEsteira_FontesPagadoras fp ON rp.FontePagadoraCodigo =
	// fp.FontePagadoraCodigo INNER JOIN FontesPagadoras.vwEsteira_Grupos g ON
	// g.GrupoCodigo = fp.GrupoCodigo WHERE rp.RegraPagamentoSituacao <> 'I' AND
	// g.Nome like 'SIAPE'";
	// //"use AgiplanNetCn select OrigemOrgaoCodigo from
	// FontesPagadoras.vwEsteira_RegrasPagamento where NomeConcatenado like
	// '%"+orgao+"%'";
	// StringBuilder select = new StringBuilder();
	// try {
	// rs = selectSQL(ds, query);
	//
	// while (rs.next()) {
	// select.append("'" + rs.getString(1) + "',");
	// }
	// } catch (SQLException e) {
	// ReportUtils.logMensagem(Status.INFO, "Erro em
	// retornaOrgaosBloqueadosCorrentista ao consultar BD!");
	// }
	// query = select.toString();
	// query = query.substring (0, query.length() - 1);
	// ReportUtils.logMensagem(Status.INFO, " Query retornaOrgaoSiape: " +query);
	// return query;
	// }

	public boolean verificaUsoCpfHistorico(String cpf) throws SQLException {

		SQLServerDataSource ds = ds_AgiplanNet();

		String query = "use automacao select * from dbo.cpfUsados where cpf = '" + cpf + "'";
		ResultSet rs = null;
		rs = selectSQL(ds, query);

		boolean achou = false;
		while (rs.next()) {
			achou = true;
		}
		if (!achou) {
			achou = false;
		}
		return achou;
	}

	public String selectSQLServerSicred(String select) throws SQLException {

		SQLServerDataSource ds = ds_Sicred();

		ResultSet rs = null;
		String cpf = null;

		try {
			rs = selectSQL(ds, select);
			while (rs.next()) {
				cpf = rs.getString(1);
				cpf = cpf.trim();
				cpf = cpf.substring(0, cpf.length());
				if (!verificaUsoCpfHistorico(cpf)) {
//					ReportUtils.logMensagem(Status.INFO, "CPF Verificado: " + cpf);
					break;
				}
				cpf = null;
			}
		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "Não foi possivel realizar Consulta no Banco! " + e.toString());
			Log4jUtils.logMensagem("ERROR", "Não foi possivel realizar Consulta no Banco! " + e.toString());
		}
		return cpf;
	}

	public String selectSQLDiaUtil(String fontePagadora) {
		String data = "";
		ResultSet rs;
		SQLServerDataSource ds = ds_AgiplanNet();

		String query = "use automacao select Convert(varchar(10),CONVERT(date, dataPagamento,102),103) from dbo.vencimentosINSS where regraNome = '"
				+ fontePagadora + "'";

		try {
			rs = selectSQL(ds, query);
			rs.next();
			data = rs.getString(1);

		} catch (SQLException e) {
			ReportUtils.logMensagem(Status.FAIL, "Erro em selectSQLDiaUtil, nao foi possivel executar a query: " + query + e.toString());
			Log4jUtils.logMensagem("ERROR", "Erro em selectSQLDiaUtil, nao foi possivel executar a query: " + query + e.toString());
		}
		return data;
	}

	@SuppressWarnings("static-access")
	public boolean validaProdutoSelecionadoCP(String query, String produto) {
		seleniumUtils utils = new seleniumUtils();
		utils.sleep(3000);
//		ReportUtils.logMensagem(Status.INFO, "validaProdutoSelecionadoCP Query: " + query);

		SQLServerDataSource ds = ds_AgiplanNet();
		ResultSet rs = null;
		boolean achou = false;
		try {
			rs = selectSQL(ds, query);
			while (rs.next()) {
//				ReportUtils.logMensagem(Status.INFO, "Produto selecionado no Atendimento: " + produto);
//				ReportUtils.logMensagem(Status.INFO, "Produto encontrado no sistema: " + rs.getString(3));
				if ((rs.getString(3).toUpperCase()).equals(produto.toUpperCase())) {
					achou = true;
				} else {
					achou = false;
					ReportUtils.logMensagem(Status.FAIL, "Produtos selecionado e esperado diferentes! ");
					Log4jUtils.logMensagem("ERROR", "Produtos selecionado e esperado diferentes! ");
				}
			}
		} catch (SQLException e) {
			ReportUtils.logMensagem(Status.FAIL, "Erro no metodo validaProdutoSelecionadoCP:" + e);
			Log4jUtils.logMensagem("ERROR", "Erro no metodo validaProdutoSelecionadoCP:" + e);
		}
		return achou;
	}

	public String consultaNaBaseMysqlDadosDaProcessadora(String driver_mysql, String connectionString,
			String user_database_mysql, String passwd_database_mysql, String query_Select) {
		int ID_CARTAO_PROCESSADORA = 0;
		int ID_CONTA_PROCESSADORA = 0;
		try {
			// create our mysql database connection
			String myDriver = driver_mysql;
			String myUrl = connectionString;
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, user_database_mysql, passwd_database_mysql);

			String query = query_Select;

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next()) {
				ID_CARTAO_PROCESSADORA = rs.getInt("ID_CARTAO_PROCESSADORA");
				ID_CONTA_PROCESSADORA = rs.getInt("ID_CONTA_PROCESSADORA");
				// ReportUtils.logMensagem(Status.INFO, "ID_CARTAO_PROCESSADORA:
				// "+ID_CARTAO_PROCESSADORA);
				// ReportUtils.logMensagem(Status.INFO, "ID_CONTA_PROCESSADORA: "+
				// ID_CONTA_PROCESSADORA);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		String cartaoProcessadora = Integer.toString(ID_CARTAO_PROCESSADORA);
		String contaProcessadora = Integer.toString(ID_CONTA_PROCESSADORA);
		return cartaoProcessadora + ";" + contaProcessadora;
	}

	public String selectSQLServerCPFS(String select) throws SQLException {

		SQLServerDataSource ds = ds_AgiplanNet();

		ResultSet rs = null;
		String cpf = null;

		try {
			rs = selectSQL(ds, select);
			while (rs.next()) {
				cpf = rs.getString(1);
				cpf = cpf.trim();
				cpf = cpf.substring(0, cpf.length());
				// if(!verificaUsoCpfHistorico(cpf)) {
				// ReportUtils.logMensagem(Status.INFO, "CPF Verificado: "+ cpf);
				// break;
				// }
				// cpf = null;
			}
		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "Não foi possivel realizar Consulta no Banco! " + e.toString());
			Log4jUtils.logMensagem("ERROR", "Não foi possivel realizar Consulta no Banco! " + e.toString());
		}
		return cpf;
	}

	public void preparaBaseP2A() throws SQLException {

//		ReportUtils.logMensagem(Status.INFO, "Preparando as Bases de Dados Utilizadas pela Automacao.");
		SQLServerDataSource ds = SqlServerUtils.ds_AgiplanNet();

		String query = "";

		SqlServerUtils sqlUtils = new SqlServerUtils();
		try {
			query = "if not exists(select * from sys.databases where name = 'P2A_Automacao') BEGIN create database P2A_Automacao END;";
			sqlUtils.executaQuery(ds, query);

			query = " use P2A_Automacao if not exists (select * from sys.tables where name = 'cpfs_automacao') BEGIN create table cpfs_automacao( cpf varchar(12) not null, suite varchar(30) not null, primary key (cpf)) END;";
			sqlUtils.executaQuery(ds, query);

			// POPULANDO A BASE DE CPF UTILIZADO NA AUTOMACAO

			sqlUtils.executaQuery(ds, query);

			// query = "use automacao if not exists (select * from sys.tables where name =
			// 'cpfUsados') BEGIN create table cpfUsados ( cpf varchar(12) not null, primary
			// key (cpf)) end;";
			// sqlUtils.executaQuery(ds, query);
			//
			// query = "use automacao if not exists (select * from sys.tables where name =
			// 'vencimentosINSS') BEGIN create table vencimentosINSS ( dataPagamento DATE
			// not null, regraNome VARCHAR(36) , primary key (dataPagamento)) end;";
			// sqlUtils.executaQuery(ds, query);

			// sqlUtils.preparaDiasUteis();

//			ReportUtils.logMensagem(Status.INFO, "Bases Preparadas com Sucesso.");

		} catch (Exception e) {
			ReportUtils.logMensagem(Status.FAIL, "Erro ao prepara as bases utilizadas pela automação. " + e.toString());
			Log4jUtils.logMensagem("ERROR", "Erro ao prepara as bases utilizadas pela automação. " + e.toString());
		}

	}
}
