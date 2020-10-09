package br.atech.teste;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.atech.controler.ClientWebservice1;
import br.com.atech.controler.Conexao;
import br.com.atech.vo.Aerodromo;
import br.com.atech.vo.Observacao;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

public class Minerador {
	public Minerador() {
	}

	public static void main(String[] args) {
		GetMessage msgObservacao = new GetMessage();

		java.util.List<Aerodromo> localidades = msgObservacao.getLocalidades();
		java.util.List<Observacao> message;

//		testarProcessadorIndiv("");
//
//		List<String> messages = new GetAutoMessage().getLogMessageDupe();
//		for (String msg : messages) {
//			testarProcessadorAuto(msg);
//		}
//
//		messages = new GetAutoMessage().getLogMessageMsg();
//		for (String msg : messages) {
//			testarProcessadorAuto(msg);
//		}
//
//		messages = getAutoMeesage();
//		for (String msg : messages) {
//			testarProcessadorAuto(msg);
//		}
//
//		messages = getAutoMeesageTaf();
//		for (String msg : messages) {
//			testarProcessadorAuto(msg);
//		}

		for (Aerodromo aerodromo : localidades) {
			message = msgObservacao.getMessage(aerodromo);
			//testarProcessador(aerodromo, message);
			geraIWXXM(aerodromo, message);
		}

		message = msgObservacao.getMessageFir();
		for (Observacao observacao : message) {
			geraIWXXMFIR(observacao);
		}

		// Aerodromo aerodromo = msgObservacao.getLocalidadeTCA();
		// java.util.List<Observacao> messageTCA =
		// msgObservacao.getMessageTCA(aerodromo, 100);
		// for (Observacao observacao : messageTCA) {
		// new
		// WeatherMessageJMSMessageListener().testConvertToIWXXM(observacao.getMsg(),
		// aerodromo.getNome(),
		// aerodromo.getLatlon().getX() + "", aerodromo.getLatlon().getY() + "");
		// }
		//
		// java.util.List<Observacao> messageAVA =
		// msgObservacao.getMessageAVA(aerodromo, 100);
		// for (Observacao observacao : messageAVA) {
		// new
		// WeatherMessageJMSMessageListener().testConvertToIWXXM(observacao.getMsg(),
		// aerodromo.getNome(),
		// aerodromo.getLatlon().getX() + "", aerodromo.getLatlon().getY() + "");
		// }
	}

	public void testeMsgRedemet() {
		GetMessage msgObservacao = new GetMessage();

		java.util.List<Aerodromo> localidades = msgObservacao.getLocalidades();
		java.util.List<Observacao> message;

		testarProcessadorIndiv("");

		List<String> messages = new GetAutoMessage().getLogMessageDupe();
		for (String msg : messages) {
			testarProcessadorAuto(msg);
		}

		messages = new GetAutoMessage().getLogMessageMsg();
		for (String msg : messages) {
			testarProcessadorAuto(msg);
		}

		messages = getAutoMeesage();
		for (String msg : messages) {
			testarProcessadorAuto(msg);
		}

		messages = getAutoMeesageTaf();
		for (String msg : messages) {
			testarProcessadorAuto(msg);
		}

		for (Aerodromo aerodromo : localidades) {
			message = msgObservacao.getMessage(aerodromo);
			testarProcessador(aerodromo, message);
			geraIWXXM(aerodromo, message);
		}

		message = msgObservacao.getMessageFir();
		for (Observacao observacao : message) {
			geraIWXXMFIR(observacao);
		}

	}

	private static List<String> getAutoMeesage() {
		return new GetAutoMessage().getAutoMessage();
	}

	private static List<String> getAutoMeesageTaf() {
		return new GetAutoMessage().getAutoMessageTaf();
	}

	private static void geraIWXXMFIR(Observacao observacao) {
		try {
			String msg = "";
			int indexOf = observacao.getMsg().indexOf(" - ");
			if (indexOf > 0) {
				msg = observacao.getMsg().substring(indexOf + 2);
			} else {
				msg = observacao.getMsg();
			}

			// new WeatherMessageJMSMessageListener().testConvertToIWXXM(msg, "teste", "0",
			// "0");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void geraIWXXM(Aerodromo aerodromo, java.util.List<Observacao> message) {
		for (Observacao observacao : message) {
			try {
				if (!observacao.getMsg().contains("REDEMET")) {

					String msg = "";
					int indexOf = observacao.getMsg().indexOf(" - ");
					if (indexOf > 0) {
						msg = observacao.getMsg().substring(indexOf + 2);
					} else {
						msg = observacao.getMsg();
					}
					// new WeatherMessageJMSMessageListener().testConvertToIWXXM(msg,
					// aerodromo.getNome(),
					// aerodromo.getLatlon().getX() + "", aerodromo.getLatlon().getY() + "");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void call_msgaus() {

		Connection conn;
		try {
			conn = new Conexao().conectaOPMETi();
			// final Connection c = dataSource.getConnection();
			CallableStatement st = conn.prepareCall("{call PKG_ESTATISTICO.p_gera_dados_msgaus (?,?,?,?,?,?,?,?,?,?)}");
			// st.registerOutParameter(1, java.sql.Types.ARRAY,
			// "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");
			st.registerOutParameter(1, OracleTypes.STRUCT);
			// st.setObject(1, null);

			st.setString(2, "L");
			st.setString(3, "20/08/2019");
			st.setString(4, "26/08/2019");
			st.setString(5, null);
			st.setString(6, null);
			st.setString(7, null);
			st.setString(8, "BZ");
			st.setString(9, null);
			st.setLong(10, 1l);

			boolean execute = st.execute();
			System.out.println("resultado " + execute);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void getReports() {

		// final CallableStatement st = null;
		OraclePreparedStatement ps = null;
		try {
			// final Connection connection = dataSource.getConnection();
			Connection connection = new Conexao().conectaOPMETi();

			// OracleConnection oc = null;
			// if (connection.isWrapperFor(OracleConnection.class)) {
			// oc = connection.unwrap(OracleConnection.class);
			// }
			// ps = (OraclePreparedStatement) connection
			// .prepareStatement("begin
			// PKG_ESTATISTICO.p_gera_dados_msgaus(?,?,?,?,?,?,?,?,?,?); end;");
			// ps = oc.prepareCall("{call PKG_ESTATISTICO.p_gera_dados_msgaus
			// (?,?,?,?,?,?,?,?,?,?)}");

			ps = (OraclePreparedStatement) connection
					.prepareStatement("{call PKG_ESTATISTICO.p_gera_dados_msgaus (?,?,?,?,?,?,?,?,?,?)}");
			// ps.registerOutParameter(1, OracleType.VARRAY,
			// "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");
			// ps.setARRAY(1, getArray(connection));
			ps.registerReturnParameter(1, OracleTypes.ARRAY, "TYPTAB_TB_TEMP_SITGER");
			// ps.registerReturnParameter(2, OracleTypes.ARRAY,
			// "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");

			ps.setString(2, "L");
			ps.setString(3, "20/08/2019");
			ps.setString(4, "26/08/2019");
			ps.setString(5, null);
			ps.setString(6, null);
			ps.setString(7, null);
			ps.setString(8, "BZ");
			ps.setString(9, null);
			ps.setLong(10, 1l);

			final Boolean b = ps.execute();
			ps.close();
			// final ResultSet rs = st.executeQuery("select count(*) from
			// p_insert_dados_sitger");
			// if (rs != null) {
			// while (rs.next()) {
			// final int v = rs.getInt(0);
			// System.out.println(v);
			// }
			// }

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// testProc

	private static void testarProcessador(Aerodromo aerodromo, java.util.List<Observacao> message) {
		for (Observacao observacao : message) {
			try {
				if (!observacao.getMsg().contains("REDEMET")) {

					String msg = "";
					int indexOf = observacao.getMsg().indexOf(" - ");
					if (indexOf > 0) {
						msg = observacao.getMsg().substring(indexOf + 2);
					} else {
						msg = observacao.getMsg();
					}
					if (msg.endsWith("=")) {
						new ClientWebservice1().testValidateHttp(msg);
					} else {
						new ClientWebservice1().testValidateHttp(msg + "=");
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private static void testarProcessadorAuto(String msg) {
		try {
			new ClientWebservice1().testeHttpsv1(msg);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void testarProcessadorIndiv(String msg) {
		try {
			new ClientWebservice1().testeHttpsv1("METAR LFRO 030730Z AUTO 25020KT 3700 BR VV/// 11/11 Q1017=");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
