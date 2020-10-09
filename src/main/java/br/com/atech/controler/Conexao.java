package br.com.atech.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Conexao {
	String[] info = new String[5];
	private static Log log = LogFactory.getLog("iwxxm_converter");
	private static String url = "jdbc:oracle:thin:@172.16.1.50:1521:opmet";
	Connection conexao;

	public Conexao() {
	}

	public Connection conectaOPMETi() throws Exception {
		String usuario = "opmet";
		String senha = "OPMET";
		String driverOPMET = "oracle.jdbc.driver.OracleDriver";

		if ((conexao != null) && (!conexao.isClosed())) {
			return conexao;
		}
		obterPropOpmet(null);

		try {
			Class<?> forName = Class.forName(driverOPMET);
			if ((info[0] != null) && (info[1] != null) && (info[2] != null)) {
				String url_prop = "jdbc:oracle:thin:@";
				url_prop = url_prop + info[2];
				url_prop = url_prop + ":";
				url_prop = url_prop + info[3];
				url_prop = url_prop + "/";
				url_prop = url_prop + info[4];
				log.warn("Conectando no banco como: " + info[0] + "/" + info[1] + "@" + info[2]);
				conexao = DriverManager.getConnection(url_prop, info[0], info[1]);
				return conexao;
			}
			conexao = DriverManager.getConnection(url, usuario, senha);
			return conexao;
		} catch (Exception e) {
			log.warn(e.toString());
			throw e;
		}
	}

	public Connection conectaOPMETProd() throws Exception {
		String usuario = "opmet";
		String senha = "OPMET";
		String driverOPMET = "oracle.jdbc.driver.OracleDriver";

		if ((conexao != null) && (!conexao.isClosed())) {
			return conexao;
		}
		obterPropOpmet("src/main/resources/conexaoprod.properties");

		try {
			Class<?> forName = Class.forName(driverOPMET);
			if ((info[0] != null) && (info[1] != null) && (info[2] != null)) {
				String url_prop = "jdbc:oracle:thin:@(DESCRIPTION =\n" + 
						"    (ADDRESS_LIST =\n" + 
						"      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.103.3.18)(PORT = 1521))\n" + 
						"    )\n" + 
						"    (CONNECT_DATA =\n" + 
					    "      (SERVER = dedicated) \n "+
						"      (SERVICE_NAME = OPMET_DEV)\n" + 
						"    )\n" + 
						"  )";
//				url_prop = url_prop + info[2];
//				url_prop = url_prop + ":";
//				url_prop = url_prop + info[3];
//				url_prop = url_prop + ":";
//				url_prop = url_prop + info[4];
				log.warn("Conectando no banco como: " + info[0] + "/" + info[1] + "@" + info[2]);
				conexao = DriverManager.getConnection(url_prop, info[0], info[1]);
				return conexao;
			}
			conexao = DriverManager.getConnection(url, usuario, senha);
			return conexao;
		} catch (Exception e) {
			log.warn(e.toString());
			throw e;
		}
	}

	private String[] obterPropOpmet(String file) throws Exception {
		InputStream in = null;
		try {
			Properties properties = new Properties();

			if (file != null) {
				try {
					in = new FileInputStream(new File(file).getAbsoluteFile());
				} catch (Exception e) {
					in = getClass().getClassLoader().getResourceAsStream("resources/" + file);
				}

			} else {
				try {
					in = new FileInputStream(new File("src/main/resources/conexao.properties").getAbsoluteFile());
				} catch (Exception e) {
					in = getClass().getClassLoader().getResourceAsStream("resources/conexao.properties");
				}
			}

			properties.load(in);

			info[0] = properties.getProperty("usuario_op", "opmet");
			info[1] = properties.getProperty("senha_op", "OPMET");
			info[2] = properties.getProperty("host_op", "oracle-lh");
			info[3] = properties.getProperty("port_op", "1521");
			info[4] = properties.getProperty("sid_op", "opmet");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return info;
	}
}
