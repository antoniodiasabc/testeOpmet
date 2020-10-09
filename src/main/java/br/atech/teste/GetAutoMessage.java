package br.atech.teste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.atech.controler.Conexao;

public class GetAutoMessage {

	public List<String> getAutoMessage() {
		Connection conectaOPMETi = null;
		List<String> listaLocalidades = new ArrayList<>();
		try {
			conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from vw_info_hist_sa_sp where desc_mens like '%AUTO%' "
					+ "  and desc_mens not like '%SM %' and desc_mens not like '% CLR %' "
					+ " and id_geografico <> 'BZ' "
					+ "and to_char(DT_VALIDADE_INICIAL, 'DDHH24') > ? and to_char(DT_VALIDADE_INICIAL, 'DDHH24') < ? "
					+ "and rownum < 500 ";

			GregorianCalendar greg = new GregorianCalendar();
			greg.add(Calendar.HOUR_OF_DAY, -10);
			String dataAgora = new SimpleDateFormat("ddHH").format(new Date());
			String data10Hatras = new SimpleDateFormat("ddHH").format(greg.getTime());

			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setString(1, data10Hatras);
			prep.setString(2, dataAgora);

			ResultSet result = prep.executeQuery();
			while (result.next()) {
				listaLocalidades.add(result.getString(1));
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

	public List<String> getAutoMessageTaf() {
		List<String> listaLocalidades = new ArrayList<>();
		Connection conectaOPMETi = null;
		try {
			conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from tb_msg_taf_historico "
					+ " where desc_mens not like '%SM %' and  desc_mens not like '% CNL%' "
					+ "and to_char(DT_VALIDADE_INICIAL, 'DDHH24') > ? and to_char(DT_VALIDADE_INICIAL, 'DDHH24') < ? "
					+ "and rownum < 500 ";

			GregorianCalendar greg = new GregorianCalendar();
			greg.add(Calendar.HOUR_OF_DAY, 24);
			String dataAgora = new SimpleDateFormat("ddHH").format(new Date());
			String data36Futuro = new SimpleDateFormat("ddHH").format(greg.getTime());

			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setString(1, dataAgora);
			prep.setString(2, data36Futuro);

			ResultSet result = prep.executeQuery();
			while (result.next()) {
				listaLocalidades.add(result.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	

	public List<String> getLogMessageMsg() {
		Connection conectaOPMETi = null;
		List<String> listaLocalidades = new ArrayList<>();
		try {
			conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from tb_log_msg "
					+ " where (desc_mens like '%METAR%' or desc_mens like '%TAF%' or desc_mens like '%SPECI%')\r\n" + 
					" and desc_mens like ? and rownum < 500 ";

			String dataAgora = new SimpleDateFormat("ddHH").format(new Date());
			
			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setString(1, "% " + dataAgora + "%" );

			ResultSet result = prep.executeQuery();
			while (result.next()) {
				listaLocalidades.add(result.getString(1));
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


	public List<String> getLogMessageDupe() {
		Connection conectaOPMETi = null;
		List<String> listaLocalidades = new ArrayList<>();
		try {
			conectaOPMETi = new Conexao().conectaOPMETProd();
			String sql = "select desc_mens from tb_log_msg where (desc_mens like '% R%' or desc_mens like '%DUPE%' or desc_mens like '%PART%') "
					+ " and TO_char(dt_hr_erro, 'MMDD') = ? "
					+ " and rownum < 500 ";			

			GregorianCalendar greg = new GregorianCalendar();
			String dataHoraHoje = new SimpleDateFormat("MMdd").format(greg.getTime());

			PreparedStatement prep = conectaOPMETi.prepareStatement(sql);
			prep.setString(1, dataHoraHoje);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				listaLocalidades.add(result.getString(1));
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


}
