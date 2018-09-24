package utils;

import java.util.List;
import java.util.Map;

import cucumber.api.DataTable;

public class CucumberUtils {

	public static String getFromDataTable(DataTable table, String coluna) {
		List<Map<String,String>> data = table.asMaps(String.class,String.class);
		return data.get(0).get(coluna);
	}
	
	public static String getFromDataTable(DataTable table, String coluna, int index) {
		List<Map<String,String>> data = table.asMaps(String.class,String.class);
		return data.get(index).get(coluna);
	}
}
