package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MySqlUtils {

	private static Statement conectaBdHierarquia() {
		Connection conn = null;
		Statement st = null;

		String myDriver = seleniumUtils.loadFromPropertiesFile("conexao.properties", "DRIVER_MYSQL");
		String myUrl = seleniumUtils.loadFromPropertiesFile("conexao.properties", "STRING_CONNECTION_BASE_HIERARQUIA");
		String myUser = seleniumUtils.loadFromPropertiesFile("conexao.properties", "USER_DATABASE_MYSQL_HIERARQUIA");
		String myPasswd = seleniumUtils.loadFromPropertiesFile("conexao.properties",
				"PASSWD_DATABASE_MYSQL_HIERARQUIA");

		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, myUser, myPasswd);
			st = conn.createStatement();
		} catch (Exception e) {
			System.err.println("Erro ao conectar no banco de dados!");
			e.printStackTrace();
		}

		return st;
	}

	private static Statement conectaBdIndicadorBda() {
		Connection conn = null;
		Statement st = null;

		String myDriver = seleniumUtils.loadFromPropertiesFile("conexao.properties", "DRIVER_MYSQL");
		String myUrl = seleniumUtils.loadFromPropertiesFile("conexao.properties",
				"STRING_CONNECTION_BASE_INDICADORBDA");
		String myUser = seleniumUtils.loadFromPropertiesFile("conexao.properties", "USER_DATABASE_MYSQL_INDICADORBDA");
		String myPasswd = seleniumUtils.loadFromPropertiesFile("conexao.properties",
				"PASSWD_DATABASE_MYSQL_INDICADORBDA");

		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, myUser, myPasswd);
			st = conn.createStatement();
		} catch (Exception e) {
			System.err.println("Erro ao conectar no banco de dados!");
			e.printStackTrace();
		}

		return st;
	}

	public static HashMap<String, Object> consultaLogImportacao(String nomeArquivo) {
		ResultSet rs = null;
		String query = "select * from hierarquia.LogImportacao where NomeArquivo = '" + nomeArquivo
				+ "' order by DataImportacao desc limit 1;";
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdHierarquia();

			rs = st.executeQuery(query);

			while (rs.next()) {
				resultado.put("NomeArquivo", rs.getString("NomeArquivo"));
				resultado.put("RegistrosLidos", rs.getInt("RegistrosLidos"));
				resultado.put("DataImportacao", rs.getDate("DataImportacao"));
				resultado.put("HoraImportacao", rs.getTime("DataImportacao"));
			}

			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public static HashMap<String, Object> consultaIndicadorPorValorReferencia(String valorReferencia) {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i \r\n"
				+ "where i.valor = '" + valorReferencia + "';";
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);

			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
			}

			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public static HashMap<String, Object> consultaIndicadorAgrupadorPorValorReferencia(String valorReferencia) {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo,\r\n" + "a.nome as agrupador_nome\r\n"
				+ "from indicador i inner join indicador_agrupamento a\r\n"
				+ "on i.indicador_agrupamento_codigo = a.indicador_agrupamento_codigo\r\n" + "where i.valor = '"
				+ valorReferencia + "';";
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);

			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				resultado.put("agrupador_nome", rs.getString("agrupador_nome"));
			}

			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public static HashMap<String, Object> consultaIndicadorPorNome(String nomeIndicador) {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i \r\n"
				+ "where i.nome = '" + nomeIndicador + "';";
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);

			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
			}

			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public static HashMap<String, Object> consultaAgrupadorPorNome(String nomeAgrupador) {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo,\r\n" + "a.nome as agrupador_nome\r\n"
				+ "from indicador i inner join indicador_agrupamento a\r\n"
				+ "on i.indicador_agrupamento_codigo = a.indicador_agrupamento_codigo\r\n" + "where i.nome = '"
				+ nomeAgrupador + "';";
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);

			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
			}

			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public static ArrayList<HashMap<String, Object>> consultaIndicadoresExibidosPainel() {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i \r\n"
				+ "where i.painel = 1\r\n" + "and (i.relogio = 0 or i.relogio is null);";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				listaIndicadores.add(resultado);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<HashMap<String, Object>> consultaIndicadoresExibidosRelogios() {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i \r\n"
				+ "where i.relogio = 1\r\n" + "and (i.painel = 0 or i.painel is null)";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				listaIndicadores.add(resultado);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<HashMap<String, Object>> consultaIndicadoresNaoExibidosDashboard() {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i \r\n"
				+ "where (i.painel = 0 or i.painel is null)\r\n" + "and (i.relogio = 0 or i.relogio is null);";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				listaIndicadores.add(resultado);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<HashMap<String, Object>> consultaTodosIndicadores() {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo\r\n" + "from indicador i;";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				listaIndicadores.add(resultado);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<HashMap<String, Object>> consultaIndicadoresComAgrupador() {
		ResultSet rs = null;
		String query = "select i.indicador_codigo as indicador_codigo,\r\n" + "i.nome as indicador_nome, \r\n"
				+ "i.valor as valor_referencia, \r\n" + "i.data_inclusao as data_inclusao, \r\n"
				+ "i.relogio as relogio,\r\n" + "i.painel as painel, \r\n"
				+ "i.indicador_agrupamento_codigo as agrupador_codigo,\r\n" + "a.nome as agrupador_nome\r\n"
				+ "from indicador i inner join indicador_agrupamento a\r\n"
				+ "on i.indicador_agrupamento_codigo = a.indicador_agrupamento_codigo";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_codigo", rs.getInt("indicador_codigo"));
				resultado.put("indicador_nome", rs.getString("indicador_nome"));
				resultado.put("valor_referencia", rs.getString("valor_referencia"));
				resultado.put("data_inclusao", rs.getDate("data_inclusao"));
				resultado.put("hora_inclusao", rs.getTime("data_inclusao"));
				resultado.put("relogio", rs.getBoolean("relogio"));
				resultado.put("painel", rs.getBoolean("painel"));
				resultado.put("agrupador_codigo", rs.getInt("agrupador_codigo"));
				resultado.put("agrupador_nome", rs.getString("agrupador_nome"));
				listaIndicadores.add(resultado);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}
	
	public static ArrayList<HashMap<String, Object>> consultaTodosAgrupadores() {
		ResultSet rs = null;
		String query = "select * from indicador_agrupamento;";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {
			Statement st = conectaBdIndicadorBda();

			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("agrupador_codigo", rs.getInt("indicador_agrupamento_codigo"));
				resultado.put("agrupador_nome", rs.getString("nome"));
				listaIndicadores.add(resultado);
				
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<String> listaTodosAgrupadores() {
		ResultSet rs = null;
		String query = "select indicador_agrupamento_codigo as agrupador_codigo,\r\n" + "nome as agrupador_nome\r\n"
				+ "from indicador_agrupamento;";
		ArrayList<String> listaIndicadores = new ArrayList<>();
		try {

			Statement st = conectaBdIndicadorBda();
			rs = st.executeQuery(query);
			while (rs.next()) {
				listaIndicadores.add(rs.getString("agrupador_nome"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static ArrayList<HashMap<String, Object>> consultaProducaoRelogiosPorProdutor(String codigoProdutor) {
		ResultSet rs = null;
		String query = "select distinct i.nome, p.produto, p.realizado, p.meta, p.meta_dia, p.ritmo, p.percentual_real\r\n"
				+ "from producao_bda p inner join indicador i on p.produto = i.valor\r\n" + "where p.produtor_codigo = "
				+ codigoProdutor + "\r\n" + "and p.produto in (select valor from indicador where relogio = 1)\r\n"
				+ "and p.data = (select data from producao_bda order by data desc limit 1);";
		ArrayList<HashMap<String, Object>> listaIndicadores = new ArrayList<>();
		HashMap<String, Object> resultado = new HashMap<>();
		try {

			Statement st = conectaBdIndicadorBda();
			rs = st.executeQuery(query);
			while (rs.next()) {
				resultado.put("indicador_nome", rs.getString("nome"));
				resultado.put("produto", rs.getString("produto"));
				resultado.put("meta_real", rs.getDouble("realizado"));
				resultado.put("meta", rs.getDouble("meta"));
				resultado.put("meta_dia", rs.getDouble("meta_dia"));
				resultado.put("ritmo", rs.getDouble("ritmo"));
				resultado.put("percentual", (rs.getDouble("percentual_real") * 100));
				listaIndicadores.add(resultado);
				resultado = new HashMap<>();
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}

	public static HashMap<String, Object> consultaProducaoIndicadorPainel(String codigoProdutor, String nomeIndicador, String nomeAgrupador) {
		ResultSet rs = null;
		String query = "select distinct i.nome as indicador_nome, p.produto, a.nome as agrupador_nome, \r\n" + 
				"p.realizado, p.meta, p.percentual_real from producao_bda p \r\n" + 
				"inner join indicador i on p.produto = i.valor \r\n" + 
				"inner join indicador_agrupamento a \r\n" + 
				"on i.indicador_agrupamento_codigo = a.indicador_agrupamento_codigo\r\n" + 
				"where p.produtor_codigo = " + codigoProdutor + "\r\n" + 
				"and p.produto in (select valor from indicador where painel = 1)\r\n" + 
				"and p.data = (select data from producao_bda order by data desc limit 1)\r\n" + 
				"and i.nome = '" + nomeIndicador + "'\r\n" + 
				"and a.nome = '" + nomeAgrupador + "';";
		HashMap<String, Object> indicador = new HashMap<>();
		try {

			Statement st = conectaBdIndicadorBda();
			rs = st.executeQuery(query);
			while (rs.next()) {
				indicador.put("indicador_nome", rs.getString("indicador_nome"));
				indicador.put("produto", rs.getString("produto"));
				indicador.put("agrupador_nome", rs.getString("agrupador_nome"));
				indicador.put("meta_real", rs.getFloat("realizado"));
				indicador.put("meta", rs.getFloat("meta"));
				indicador.put("percentual", (rs.getFloat("percentual_real") * 100));
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return indicador;
	}

	public static ArrayList<String> listaIndicadoresPorAgrupador(String agrupadorNome) {
		ResultSet rs = null;
		String query = "select i.indicador_codigo,\r\n" + " i.nome as indicador_nome,\r\n"
				+ " i.valor as indicador_valor_ref,\r\n" + " i.indicador_agrupamento_codigo as agrupador_codigo,\r\n"
				+ " a.nome as agrupador_nome\r\n" + "from indicador i\r\n" + "inner join indicador_agrupamento a\r\n"
				+ "on i.indicador_agrupamento_codigo = a.indicador_agrupamento_codigo\r\n" 
				+ "where a.nome = '" + agrupadorNome + "';";
		ArrayList<String> listaIndicadores = new ArrayList<>();
		try {

			Statement st = conectaBdIndicadorBda();
			rs = st.executeQuery(query);
			while (rs.next()) {
				listaIndicadores.add(rs.getString("indicador_nome"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao Executar a consulta no Banco!");
			System.err.println(e.getMessage());
		}
		return listaIndicadores;
	}
}
