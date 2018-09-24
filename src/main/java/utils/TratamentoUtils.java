package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TratamentoUtils {

	public static String getString(Object obj) {
		if (!String.valueOf(obj).equals("null")) {
			return String.valueOf(obj);
		} else {
			return null;
		}
	}

	public static int getInteger(Object obj) {
		if (!String.valueOf(obj).equals("null")) {
			return Integer.valueOf(String.valueOf(obj));
		} else {
			return 0;
		}
	}

	public static LocalDate getLocalDate(Object obj) {
		if (!String.valueOf(obj).equals("null")) {
			return LocalDate.parse(String.valueOf(obj));
		} else {
			return null;
		}
	}

	public static LocalTime getLocalTime(Object obj) {
		if (!String.valueOf(obj).equals("null")) {
			return LocalTime.parse(String.valueOf(obj));
		} else {
			return null;
		}
	}

	public static float getFloat(Object obj) {
		if (!String.valueOf(obj).equals("null")) {
			return Float.valueOf(String.valueOf(obj));
		} else {
			return 0;
		}
	}
	
	public static DecimalFormat getFormatadorValoresMonetarios() {
		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		custom.setDecimalSeparator(',');
		custom.setGroupingSeparator('.');
		format.setDecimalFormatSymbols(custom);
		
		return format;
	}
	
	public static DecimalFormat getFormatadorValoresPercentuais() {
		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		custom.setDecimalSeparator('.');
		custom.setGroupingSeparator(',');
		format.setDecimalFormatSymbols(custom);
		
		return format;
	}

	public static Map<String, String> getIndicadorResultadoGeral(HashMap<String, Object> indicadorBD) {
		Map<String, String> indicadorTratado = new HashMap<>();

		DecimalFormat monetario = getFormatadorValoresMonetarios();
		DecimalFormat percentual = getFormatadorValoresPercentuais();

		indicadorTratado.put("indicador_nome", getString(indicadorBD.get("indicador_nome")));
		indicadorTratado.put("produto", getString(indicadorBD.get("produto")));
		indicadorTratado.put("meta_real", monetario.format(indicadorBD.get("meta_real")));
		indicadorTratado.put("meta", monetario.format(indicadorBD.get("meta")));
		indicadorTratado.put("meta_dia", monetario.format(indicadorBD.get("meta_dia")));
		indicadorTratado.put("ritmo", monetario.format(indicadorBD.get("ritmo")));
		indicadorTratado.put("percentual", percentual.format(indicadorBD.get("percentual")));

		return indicadorTratado;
	}
	
	public static Map<String, String> getIndicadorPainel(HashMap<String, Object> indicadorBD) {
		Map<String, String> indicadorTratado = new HashMap<>();

		DecimalFormat monetario = getFormatadorValoresMonetarios();
		DecimalFormat percentual = getFormatadorValoresPercentuais();

		indicadorTratado.put("indicador_nome", getString(indicadorBD.get("indicador_nome")));
		indicadorTratado.put("produto", getString(indicadorBD.get("produto")));
		indicadorTratado.put("agrupador_nome", getString(indicadorBD.get("agrupador_nome")));
		indicadorTratado.put("meta_real", monetario.format(indicadorBD.get("meta_real")));
		indicadorTratado.put("meta", monetario.format(indicadorBD.get("meta")));
		indicadorTratado.put("percentual", percentual.format(indicadorBD.get("percentual")));

		return indicadorTratado;
	}
}
