package br.atech.teste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import br.com.atech.controler.Conexao;
import br.com.atech.vo.Aerodromo;
import br.com.atech.vo.FIR;
import br.com.atech.vo.Observacao;

public class GetMessage {
	private static final String HTTP_WWW_REDEMET = "http://www.redemet.aer.mil.br/api/consulta_automatica/index.php?";

	public GetMessage() {
	}

	public List<Observacao> getMessage(Aerodromo aerodromo) {
		List<Observacao> msgList = new ArrayList<>();
		for (int pos = 0; pos < 2; pos++) {
			msgList.addAll(getDadosObservacao(pos, aerodromo.getNome()));
		}
		return msgList;
	}

	public List<Observacao> getMessageFir() {
		List<Observacao> msgList = new ArrayList<>();
		for (int pos = 3; pos < 5; pos++) {
			msgList.addAll(getDadosObservacao(pos, null));
		}
		return msgList;
	}

	private List<Observacao> getDadosObservacao(int pos, String nome) {
		String parametros = getParametros(pos, nome);
		List<Observacao> msgList = new ArrayList<>();
		try {
			String urlbase = "http://www.redemet.aer.mil.br/api/consulta_automatica/index.php?" + parametros;

			List<Observacao> lista = downloadFile(urlbase, pos, nome);
			if (lista.size() > 0) {
				msgList.addAll(lista);
			} else {
				msgList.addAll(downloadFile(urlbase, pos, nome));

			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return msgList;
	}

	public List<Observacao> downloadFile(String fileURL, int pos, String nome) throws Exception {
		StringBuilder buffer = new StringBuilder();
		List<Observacao> msgList = new ArrayList<>();
		HttpURLConnection httpConn = null;
		label197: try {
			URL url = new URL(fileURL);
			httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					inputLine = incluiEspacoAntes(inputLine);
					inputLine = incluiEspacoAntes2(inputLine);
					inputLine = inputLine.replaceAll("  ", " ");
					buffer.append(inputLine + "\r\n");
				}
				in.close();
				if (buffer.toString().trim().length() > 0) {
					msgList = getMessagesComCabec(buffer.toString().trim());
					break label197;
				}
			} else {
				System.out.println("No file to download. Server replied HTTP code: " + responseCode);
			}
		} finally {
			httpConn.disconnect();
		}
		return msgList;
	}

	private String incluiEspacoAntes2(String inputLine) {
		String result = inputLine;
		try {
			Process p = null;

			StringBuffer cmdOut = new StringBuffer();
			String lineOut = null;
			int numberOfOutline = 0;
			try {
				File file = new File("/home/opmet/proc/mensagens.txt");
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				BufferedWriter bf = new BufferedWriter(new FileWriter(file));
				bf.write(inputLine);
				bf.flush();
				bf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			String barraOS = getOS();
			String cmd = "awk -f /home/opmet/proc/awk/adicionaSeparador.awk /home/opmet/proc/mensagens.txt";
			if (barraOS.equals("\\")) {
				cmd = "C:\\Users\\adias\\Documents\\gawk-3.1.6-1-bin\\bin\\awk -f \\home\\opmet\\proc\\adicionaSeparador.awk \\home\\opmet\\proc\\mensagens.txt";
			}

			try {
				p = Runtime.getRuntime().exec(cmd);
				if (p.waitFor() == 0) {
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((lineOut = input.readLine()) != null) {
						if (numberOfOutline > 0) {
							cmdOut.append("\n");
						}
						cmdOut.append(lineOut);
						numberOfOutline++;
					}
					result = cmdOut.toString();
					input.close();
				} else {
					System.out.println("erro ao executar comando");
					BufferedReader errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					while ((lineOut = errorStream.readLine()) != null) {
						System.out.println(lineOut);
					}
					errorStream.close();
				}
			} catch (IOException e) {
				result = String.format("Falha ao executar comando Erro: %s", new Object[] { e.toString() });
			} finally {
				p.destroy();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getOS() {
		boolean isWindows = false;

		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if (OS.indexOf("win") >= 0) {
			isWindows = true;
		}
		return isWindows ? "\\" : "/";
	}

	private String incluiEspacoAntes(String inputLine) {
		StringBuffer resp = new StringBuffer(inputLine);
		int pos = 0;

		// pos = resp.indexOf("TX");
		// if (pos > 0) {
		// resp.insert(pos, '\n');
		// }

		return resp.toString();
	}

	private List<Observacao> getMessages(String data) throws Exception {
		List<Observacao> msgList = new ArrayList<>();
		String[] parts = data.split("=");
		for (int i = 0; i < parts.length; i++) {
			Observacao msgMetar = new Observacao();
			int indexOf = parts[i].indexOf(" - ");
			if (indexOf > 0) {
				msgMetar.setMsg(parts[i].substring(indexOf + 2).trim());
				msgMetar.setData(getDate(parts[i].substring(0, indexOf).trim()));
			}
			String[] newParts = parts[i].split(" -");
			if (newParts.length == 2) {
				msgMetar.setMsg(newParts[1].trim());
				msgMetar.setData(getDate(newParts[0].trim()));
			} else {
				try {
					String dataStr = newParts[0].trim();
					if (StringUtils.isBlank(dataStr)) {
						dataStr = parts[i].substring(0, 12).trim();
					}
					msgMetar.setData(getDate(dataStr));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (newParts.length == 3) {
					msgMetar.setMsg(newParts[1].trim() + " " + newParts[2].trim());
				} else
					msgMetar.setMsg(parts[i]);
			}
			msgList.add(msgMetar);
		}
		return msgList;
	}

	private List<Observacao> getMessagesComCabec(String data) throws Exception {
		List<Observacao> msgList = new ArrayList<>();
		String[] parts = data.split("=");
		for (int i = 0; i < parts.length; i++) {
			Observacao msgMetar = new Observacao();
			StringBuffer msgBuff = new StringBuffer(parts[i]);
			msgBuff.append("=");
			msgMetar.setMsg(msgBuff.toString());
			msgList.add(msgMetar);
		}
		return msgList;
	}

	private Date getDate(String trim) throws Exception {
		return new SimpleDateFormat("yyyyMMddHH").parse(trim);
	}

	private String getDataInicial() {
		GregorianCalendar fiveBefore = new GregorianCalendar();
		fiveBefore.add(11, -5);
		return new SimpleDateFormat("yyyyMMddHH").format(fiveBefore.getTime());
	}

	private String getDataFinal() {
		GregorianCalendar greg = new GregorianCalendar();
		greg.add(10, 3);
		return new SimpleDateFormat("yyyyMMddHH").format(greg.getTime());
	}

	private String getDataInicial1() {
		GregorianCalendar fiveBefore = new GregorianCalendar();
		fiveBefore.add(11, -69);
		return new SimpleDateFormat("yyyyMMddHH").format(fiveBefore.getTime());
	}

	private String getDataFinal1() {
		GregorianCalendar greg = new GregorianCalendar();
		greg.add(10, -45);
		return new SimpleDateFormat("yyyyMMddHH").format(greg.getTime());
	}

	public String getParametros(int posicao, String nome) {
		String localidades = "";
		String tipoMsg = "";

		switch (posicao) {
		case 0:
			localidades = "local=" + nome;
			tipoMsg = "&msg=metar";
			break;
		case 1:
			localidades = "local=" + nome;
			tipoMsg = "&msg=taf,aviso_aerodromo";
			break;
		case 4:
			localidades = "local=" + getFirsAsString();
			tipoMsg = "&msg=sigmet";
			break;
		case 2:
			localidades = "local=" + getFirsAsString();
			tipoMsg = "&msg=airep";
			break;
		case 3:
			localidades = "local=" + getFirsAsString();
			tipoMsg = "&msg=airmet,gamet";
			break;
		}

		String dataInicial = "&data_ini=" + getDataInicial();
		String dataFinal = "&data_fim=" + getDataFinal();
		return localidades + tipoMsg + dataInicial + dataFinal + "&data_hora=nao&cabecalho=sim";
	}

	public List<Aerodromo> getLocalidades() {
		return montaLocalidades();
	}

	public void testeRedemet() {
		Connection conectaOPMETi;
		try {
			conectaOPMETi = new Conexao().conectaOPMETi();
			List<Redemet2OPMET> listaDoRedemet = getMsgRedemet();

			try {
				gravarRegistrosNoOPMET(conectaOPMETi, listaDoRedemet);
				if (conectaOPMETi != null) {
					try {
						conectaOPMETi.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public List<Aerodromo> montaLocalidades() {
		List<Aerodromo> listaLocalidades = new ArrayList<>();
		Connection conectaOPMETi = null;
		try {
			conectaOPMETi = new Conexao().conectaOPMETi();// aOPMETProd();
			String sql = "select cd_localidade, latitude, longitude,  altitude " + "     from TB_LOCALIDADE"
					+ "   where CD_LOCALIDADE like ?  order by 1 ";

			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setString(1, "SB%");
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				Aerodromo aer1 = criarAerodromo(result.getDouble(2), result.getDouble(3), result.getString(1),
						result.getInt(4));
				listaLocalidades.add(aer1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conectaOPMETi != null) {
				try {
					conectaOPMETi.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return listaLocalidades;
	}

	private List<Redemet2OPMET> getMsgRedemet() {
		List<Redemet2OPMET> listaDoRedemet = new ArrayList<>();
		Redemet2OPMET t2 = new Redemet2OPMET();
		t2.setDataRecebimento(new Date());
		t2.setDescMens("METAR teste9999999999999999999999999999999999999999999999999999999");
		t2.setDtEnvioOpmet(new Date());
		t2.setIdMens((long) ((Math.random() * 1000000) + 1));
		t2.setIdRedemet("999");
		t2.setRementente("SBBRWXXX");
		listaDoRedemet.add(t2);
		return listaDoRedemet;
	}

	private void gravarRegistrosNoOPMET(Connection connOPMET, List<Redemet2OPMET> listaDoRedemet) throws SQLException {
		java.sql.CallableStatement objStmt = connOPMET
				.prepareCall("{ ? = call PKG_OPMET.f_ins_tb_comunicacao_redemet (?, ?, ?, ?, ?, ?, ?, ?, ?)}");
		try {
			List<Long> listaIds = new ArrayList<Long>();

			for (Redemet2OPMET registro : listaDoRedemet) {
				objStmt.registerOutParameter(1, java.sql.Types.INTEGER);
				objStmt.setLong(2, registro.getIdMens());
				objStmt.setString(3, registro.getRementente());
				objStmt.setTimestamp(4, new java.sql.Timestamp(registro.getDataRecebimento().getTime()));
				objStmt.setInt(5, 1);
				objStmt.setString(6, registro.getDescMens());
				objStmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
				objStmt.setString(8, "0");
				objStmt.setString(9, "G");
				objStmt.setString(10, registro.getIdRedemet());

				boolean status = objStmt.execute();
				int retorno = objStmt.getInt(1);
				System.out.println("Retorno da package redemet:" + String.valueOf(retorno));

				listaIds.add(registro.getIdMens());
			}
			objStmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (objStmt != null) {
					objStmt.close();
				}
			} catch (Exception e) {
				System.out.println("Erro ao fechar conexao OPMET " + e);
				e.printStackTrace();
			}
		}
	}

	public List<FIR> getFirs() {
		List<FIR> listaFirs = new ArrayList();
		FIR fir1 = criarFIR("SBBS");
		FIR fir2 = criarFIR("SBCW");
		FIR fir3 = criarFIR("SBAO");
		FIR fir4 = criarFIR("SBAZ");
		FIR fir5 = criarFIR("SBRF");

		listaFirs.add(fir1);
		listaFirs.add(fir2);
		listaFirs.add(fir3);
		listaFirs.add(fir4);
		listaFirs.add(fir5);

		return listaFirs;
	}

	private FIR criarFIR(String nome) {
		FIR fir1 = new FIR();
		fir1.setNome(nome);
		return fir1;
	}

	public String getListAsString() {
		StringBuffer buffer = new StringBuffer();
		List<Aerodromo> localidades = getLocalidades();
		for (Aerodromo aerodromo : localidades) {
			buffer.append(aerodromo.getNome() + ",");
		}
		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}

	public String getFirsAsString() {
		StringBuffer buffer = new StringBuffer();
		List<FIR> firs = getFirs();
		for (FIR fir : firs) {
			buffer.append(fir.getNome() + ",");
		}
		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}

	public Aerodromo criarAerodromo(double lat, double lon, String nome, double nivel) {
		Aerodromo aer = new Aerodromo();
		Point pnt = new GeometryFactory().createPoint(new Coordinate(lat, lon, nivel));
		aer.setNome(nome);
		aer.setLatlon(pnt);
		aer.setNivel(Double.valueOf(nivel));
		return aer;
	}

	public Aerodromo getLocalidadeTCA() {
		double x = -23.62611111111111D;
		double y = -46.656388888888884D;
		double z = 802.0D;
		return criarAerodromo(x, y, "SBSP", z);
	}

	public List<Observacao> getMessageTCA(Aerodromo aerodromo, int qtd) {
		List<Observacao> msgList = new ArrayList();
		try {
			Connection conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from tb_msg_ciclone_historico where rownum < ? ";
			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setInt(1, qtd);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				Observacao obsTCA = new Observacao();
				String msg = result.getString(1);
				obsTCA.setMsg(msg);
				msgList.add(obsTCA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgList;
	}

	public List<Observacao> getMessageAVA(Aerodromo aerodromo, int qtd) {
		List<Observacao> msgList = new ArrayList();
		try {
			Connection conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from tb_msg_cinzas_historico where rownum < ? ";
			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setInt(1, qtd);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				Observacao obsTCA = new Observacao();
				String msg = result.getString(1);
				obsTCA.setMsg(msg);
				msgList.add(obsTCA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgList;
	}
}
