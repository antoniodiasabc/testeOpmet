package br.com.atech.controler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;


public class AbsentLateReportRepository {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


	public static void main(String args) {
		new AbsentLateReportRepository().getReports();
	}


    public void getReports() {

        LOGGER.info("iniciando a inclusão da entidade: {}");
        final CallableStatement st = null;
        OraclePreparedStatement ps = null;
        try {
           // final Connection connection = dataSource.getConnection();
            Connection connection = new Conexao().conectaOPMETi();

            OracleConnection oc = null;
            if (connection.isWrapperFor(OracleConnection.class)) {
                oc = connection.unwrap(OracleConnection.class);
            }
            ps = (OraclePreparedStatement) oc
                .prepareStatement("begin PKG_ESTATISTICO.p_gera_dados_msgaus(?,?,?,?,?,?,?,?,?,?); end;");
            // ps = oc.prepareCall("{call PKG_ESTATISTICO.p_gera_dados_msgaus (?,?,?,?,?,?,?,?,?,?)}");

            // final OraclePreparedStatement ps = (OraclePreparedStatement) c
            // .prepareStatement("{call PKG_ESTATISTICO.p_gera_dados_msgaus (?,?,?,?,?,?,?,?,?,?)}");
            // ps.registerOutParameter(1, OracleType.VARRAY, "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");
            ps.setARRAY(1, getArray(oc));
            ps.registerReturnParameter(1, Types.ARRAY, "PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER");
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
            // final ResultSet rs = st.executeQuery("select count(*) from p_insert_dados_sitger");
            // if (rs != null) {
            // while (rs.next()) {
            // final int v = rs.getInt(0);
            // System.out.println(v);
            // }
            // }

        } catch (final Exception e) {
            LOGGER.error("Erro ao executar a function PKG_ESTATISTICO.p_gera_dados_msgaus");
            LOGGER.error("Erro apresentado: {} {}", e.getCause(), e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (final SQLException e) {
                    LOGGER.error("Erro ao fechar a conexão com a function PKG_ESTATISTICO.p_gera_dados_msgaus");
                    LOGGER.error("Erro apresentado: {} {}", e.getCause(), e.getMessage());
                }
            }
        }
    }

    private ARRAY getArray(final Connection conn) throws SQLException {
        final StructDescriptor itemDescriptor = StructDescriptor
            .createDescriptor("PKG_ESTATISTICO.TYPE_REC_TB_TEMP_SITGER", conn);

        final Object[] itemAtributes = new Object[] {new String("METAR"), new String("PBTT"), new String("2019-08-01"),
                new String("ATRASADO")};
        final STRUCT itemObject1 = new STRUCT(itemDescriptor, conn, itemAtributes);

        final Object[] itemAtributes2 = new Object[] {new String("METAR"), new String("PBTT"), new String("2019-08-01"),
                new String("ATRASADO")};
        final STRUCT itemObject2 = new STRUCT(itemDescriptor, conn, itemAtributes2);

        final STRUCT[] idsArray = {itemObject1, itemObject2};

        final ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("PKG_ESTATISTICO.TYPTAB_TB_TEMP_SITGER",
            conn);

        final ARRAY array_to_pass = new ARRAY(descriptor, conn, idsArray);

        return array_to_pass;
    }

}
