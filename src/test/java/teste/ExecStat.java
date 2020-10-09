package teste;

import java.sql.CallableStatement;
import java.sql.Connection;

import br.com.atech.controler.Conexao;

public class ExecStat {

	public static void main(String args) {
		new ExecStat().call_msgaus();
	}

	public void call_msgaus() {

		Connection conn;
		try {
			conn = new Conexao().conectaOPMETi();
			// final Connection c = dataSource.getConnection();
			CallableStatement st = conn.prepareCall("{call PKG_ESTATISTICO.p_gera_dados_msgaus (?,?,?,?,?,?,?,?,?,?)}");
			st.setArray(1, null);
			st.registerOutParameter(1, java.sql.Types.ARRAY, "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");
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
		}
	}
}
