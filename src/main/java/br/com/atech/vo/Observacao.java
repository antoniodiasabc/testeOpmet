package br.com.atech.vo;

import java.util.Date;

public class Observacao {
	private Date data;
	private String msg;
	private Long idMsg;
	private int alturaNuvem;
	private int temChuva;
	private String tempoPresente;
	private int temCb;

	public Observacao() {
	}

	public String getTempoPresente() {
		return tempoPresente;
	}

	public void setTempoPresente(String tempoPresente) {
		this.tempoPresente = tempoPresente;
	}

	public int getAlturaNuvem() {
		return alturaNuvem;
	}

	public Date getData() {
		return data;
	}

	public Long getIdMsg() {
		return idMsg;
	}

	public String getMsg() {
		return msg;
	}

	public int getTemCb() {
		return temCb;
	}

	public int getTemChuva() {
		return temChuva;
	}

	public void setAlturaNuvem(int alturaNuvem) {
		this.alturaNuvem = alturaNuvem;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setIdMsg(Long idMsg) {
		this.idMsg = idMsg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setTemCb(int temCb) {
		this.temCb = temCb;
	}

	public void setTemChuva(int temChuva) {
		this.temChuva = temChuva;
	}
}
