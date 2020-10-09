package br.com.atech.vo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MetarEstatistica {

	String localidade;
	String data;
	int status;
	GregorianCalendar dataenvio;
	int qtdMsg;
	String idMeteor;

	public String getIdMeteor() {
		return idMeteor;
	}

	public void setIdMeteor(String idMeteor) {
		this.idMeteor = idMeteor;
	}

	public int getQtdMsg() {
		return qtdMsg;
	}

	public void setQtdMsg(int qtdMsg) {
		this.qtdMsg = qtdMsg;
	}

	public MetarEstatistica(String loc, String dat, String stat) {
		this.localidade = loc;
		this.data = dat;
		this.status = obterStatus(stat);
	}

	public MetarEstatistica(String loc, String dat, int qtd) {
		this.localidade = loc;
		this.data = dat;
		this.qtdMsg = qtd;
	}

	public MetarEstatistica(String loc, String dat, String stat, String tipo) {
		this.localidade = loc;
		this.data = dat;
		this.idMeteor = tipo;
		if (tipo.equals("FT") || tipo.equals("FC")) {
			this.status = obterStatusTAF(stat);
		}
	}

	public MetarEstatistica(String loc, String dat, int qtd, String tipo) {
		this.localidade = loc;
		this.data = dat;
		this.idMeteor = tipo;
		this.qtdMsg = qtd;
	}

	private int obterStatusTAF(String stat) {
		if (stat.equals("AUSÊNCIA")) {
			return 0;
		}else if (stat.equals("EXCELENTE")) {
			return 1;
		}else if (stat.equals("ATRASADO_MAIS_1H")) {
			return 2;
		}
		return 0;
	}

	private int obterStatus(String stat) {
		if (stat.equals("AUSÊNCIA")) {
			return 0;
		} else if (stat.equals("MENOR_5")) {
			return 1;
		} else if (stat.equals("ENTRE_5_10")) {
			return 2;
		} else if (stat.equals("ENTRE_10_30")) {
			return 3;
		} else if (stat.equals("MAIOR_30")) {
			return 4;
		}
		return 0;
	}

	public GregorianCalendar getDataenvio() {
		return dataenvio;
	}

	public void setDataenvio(GregorianCalendar dataenvio) {
		this.dataenvio = dataenvio;
	}

	public String getData() {
		return data;
	}

	public String getLocalidade() {
		return localidade;
	}

	public int getStatus() {
		return status;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDiaHoraMetar() {
		GregorianCalendar referencia = getHoraTafReferencia(this.getData());
		String diaHora = getDay(referencia.get(Calendar.DAY_OF_MONTH)) + getDay(referencia.get(Calendar.HOUR_OF_DAY));
		return diaHora; // sdf.format(tomorrow.getTime());
	}

	private GregorianCalendar getHoraTafReferencia(String dataRef) {
		int dia = Integer.parseInt(dataRef.substring(0, 2));
		int mes = Integer.parseInt(dataRef.substring(3, 5));
		int ano = Integer.parseInt(dataRef.substring(6, 10));

		int hora = Integer.parseInt(dataRef.substring(11, 13));
		int min = Integer.parseInt(dataRef.substring(14));

		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		today.set(Calendar.YEAR, ano);
		today.set(Calendar.MONTH, mes - 1);
		today.set(Calendar.DAY_OF_MONTH, dia);
		today.set(Calendar.HOUR_OF_DAY, hora);
		today.set(Calendar.MINUTE, min);

		return today;
	}

	private String getDay(int inDay) {
		String outDay = inDay + "";
		if (inDay < 10) {
			outDay = "0" + inDay;
		}
		return outDay;
	}

	public String toString() {
		return this.localidade + " " + this.data + " " + this.status + " hora envio: "
				+ getDay(this.getDataenvio().get(Calendar.DAY_OF_MONTH))
				+ getDay(this.getDataenvio().get(Calendar.HOUR_OF_DAY))
				+ getDay(this.getDataenvio().get(Calendar.MINUTE));
	}

}
