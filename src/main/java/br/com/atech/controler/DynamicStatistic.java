package br.com.atech.controler;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.atech.domain.CorrectedMessage;
import br.com.atech.domain.ParametrosRequisicao;
import br.com.atech.domain.RequisicaoResumo;
import br.com.atech.domain.Transport;
import br.com.atech.domain.Wind;
import br.com.atech.domain.WindRose;
import br.com.atech.domain.WindRoseArray;
import br.com.atech.domain.WronMessageReceived;
import br.com.atech.domain.WrongMessage;

@RestController
public class DynamicStatistic {

	// @Autowired
	// DynamicReportsRepository dynamicRep;

	@Autowired
	JpaContext jpaContext;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/metarLate")
	public List getLateByMonthBR(@RequestBody RequisicaoResumo req) {
		String maskData = getMaskTime(req.getUnidadeTempo());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();

		montaData(req, maskData, paramsReq);

		List<String> weatherType = getWheaterTypeLate(req.getTipoMsg());

		Query nativeQuery = montaQueryLate(paramsReq, weatherType, maskData, req.getLocalidade(),
				req.getIdGeografico());
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/resumo", method = RequestMethod.POST)
	public List countByPhenomena(@RequestBody RequisicaoResumo req) {
		String tableName = getTableName(req.getTipoMsg());
		String maskData = getMaskTime(req.getUnidadeTempo());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String dataIni = getDateFormated(req.getTempoInicial(), maskData);
		String dataEnd = getDateFormated(req.getTempoFinal(), maskData);
		paramsReq.setTempoFinal(dataEnd);
		paramsReq.setTempoInicial(dataIni);
		String[] dados = new String[1];
		dados[0] = "%" + req.getFenomeno() + "%";
		Query nativeQuery = montaQueryFenomenaBR(paramsReq, tableName, maskData, dados, req.getLocalidade());
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicmessage", method = RequestMethod.POST)
	public List[] countMessageByPhenomena(@RequestBody RequisicaoResumo req) {
		List[] total;
		List<String> weatherType = getWheaterType(req.getTipoMsg());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;

		if (req.getFenomeno().contains("-")) {
			String[] split = req.getFenomeno().split("-");
			total = new ArrayList[split.length];

			for (int i = 0; i < split.length; i++) {
				String fenomeno = split[i];
				if (fenomeno.contains("COR") || fenomeno.contains("AMD")) {
					posFenomeno = 1;
				}
				Query nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, fenomeno, req.getLocalidade(),
						req.getIdGeografico(), posFenomeno);
				List lista1 = nativeQuery.getResultList();
				mountData(lista1);
				total[i] = lista1;
			}
		} else {
			total = new ArrayList[1];
			if (req.getFenomeno().contains("COR") || req.getFenomeno().contains("AMD")) {
				posFenomeno = 1;
			}
			Query nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, req.getFenomeno(),
					req.getLocalidade(), req.getIdGeografico(), posFenomeno);
			List resultList = nativeQuery.getResultList();
			List mountData = mountData(resultList);
			total[0] = mountData;
		}
		return total;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicmessage1", method = RequestMethod.POST)
	public List countMessageByPhenomena1(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterType(req.getTipoMsg());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;
		if (req.getFenomeno().contains("COR") || req.getFenomeno().contains("AMD")) {
			posFenomeno = 1;
		}
		Query nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, req.getFenomeno(), req.getLocalidade(),
				req.getIdGeografico(), posFenomeno);
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/teste2fenomenos", method = RequestMethod.GET)
	public List[] teste2fenomenos() {
		List<String> weatherType = getWheaterType("METAR");
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime("dia");

		String dataIni = getDatePast(-30, Calendar.DAY_OF_MONTH);
		String dataEnd = getDataAgora();
		paramsReq.setTempoInicial(dataIni);
		paramsReq.setTempoFinal(dataEnd);

		int posFenomeno = 16;

		List[] total = new ArrayList[2];

		Query nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, "CB", "", "BZ", posFenomeno);
		List lista1 = nativeQuery.getResultList();
		mountData(lista1);

		nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, "BR", "", "BZ", posFenomeno);
		List lista2 = nativeQuery.getResultList();
		mountData(lista2);

		total[0] = lista1;
		total[1] = lista2;
		return total;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/qtdmessage", method = RequestMethod.POST)
	public List countMessage(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterTypeExpanded(req.getTipoMsg());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		Query nativeQuery = montaQueryQtd(paramsReq, req.getTipoMsg(), weatherType, maskData, req.getLocalidade(),
				req.getIdGeografico());
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/anuario", method = RequestMethod.POST)
	public List countMessageYear(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterTypeExpanded(req.getTipoMsg());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		Query nativeQuery = montaQueryQtdYear(paramsReq, req.getTipoMsg(), weatherType, maskData, req.getLocalidade(),
				req.getIdGeografico());
		List resultList = nativeQuery.getResultList();
		return resultList;
	}

	private Query montaQueryQtdYear(ParametrosRequisicao paramsReq, String tipoMsg, List<String> weatherType,
			String maskData, String localidade, String idGeografico) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		if (weatherType.contains("FT")) {
			nativeQueryString.append("select w.id_localidade_confeccao, count(*) from tb_msg_taf_historico w ");
		}

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.id_localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.id_fir ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.id_localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ? ");

		if (weatherType.contains("FT")) {
			nativeQueryString.append(" group by  w.id_localidade_confeccao order by 1,2 ");
		}

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/temperature", method = RequestMethod.POST)
	public List temperatura(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterType(req.getTipoMsg());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;

		Query nativeQuery = montaQueryTemperatura(paramsReq, req.getTipoMsg(), weatherType, maskData, req.getFenomeno(),
				req.getLocalidade(), req.getIdGeografico(), posFenomeno);
		List resultList = nativeQuery.getResultList();
		List mountData = mountData(resultList);
		return mountTemperature(mountData, "", "", "");
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/variaveisMetar", method = RequestMethod.POST)
	public Collection variaveisMetar(@RequestBody RequisicaoResumo req) {
		Collection result = new ArrayList<>();

		List<String> weatherType = getWheaterType("METAR");
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;

		if (req.getVariable().equalsIgnoreCase("temperature")) {
			Query nativeQuery = montaQueryTemperatura(paramsReq, req.getTipoMsg(), weatherType, maskData,
					req.getFenomeno(), req.getLocalidade(), req.getIdGeografico(), posFenomeno);
			List resultList = nativeQuery.getResultList();
			List mountData = mountData(resultList);
			result = mountTemperature(mountData, req.getComparator(), req.getFilter1(), req.getFilter2());
		} else if (req.getVariable().equalsIgnoreCase("wind_barb")) {
			Query nativeQuery = montaQueryWindRose(paramsReq, req.getTipoMsg(), weatherType, maskData,
					req.getFenomeno(), req.getLocalidade(), req.getIdGeografico());
			List resultList = nativeQuery.getResultList();
			List mountData = mountData(resultList);
			result = mountWindBarb(mountData, req.getComparator(), req.getFilter1(), req.getFilter2());
		} else if (req.getVariable().equalsIgnoreCase("dewpoint")) {
			Query nativeQuery = montaQueryDewPoint(paramsReq, req.getTipoMsg(), weatherType, maskData,
					req.getFenomeno(), req.getLocalidade(), req.getIdGeografico(), posFenomeno);
			List resultList = nativeQuery.getResultList();
			List mountData = mountData(resultList);
			result = mountTemperature(mountData, req.getComparator(), req.getFilter1(), req.getFilter2());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/windRose", method = RequestMethod.POST)
	public Set<WindRoseArray> windRose(@RequestBody RequisicaoResumo req) {

		List<String> weatherType = getWheaterType("METAR");
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		Query nativeQuery = montaQueryWindRose(paramsReq, req.getTipoMsg(), weatherType, maskData, req.getFenomeno(),
				req.getLocalidade(), req.getIdGeografico());
		List resultList = nativeQuery.getResultList();
		Set<WindRoseArray> mountWindRose = mountWindRose(resultList, req.getComparator(), req.getFilter1(),
				req.getFilter2());

		return mountWindRose;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/windBarb", method = RequestMethod.POST)
	public List windBarb(@RequestBody RequisicaoResumo req) {
		List result = new ArrayList<>();

		List<String> weatherType = getWheaterType("METAR");
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String maskData = getMaskTime(req.getUnidadeTempo());

		montaData(req, maskData, paramsReq);

		Query nativeQuery = montaQueryWindRose(paramsReq, req.getTipoMsg(), weatherType, maskData, req.getFenomeno(),
				req.getLocalidade(), req.getIdGeografico());
		List resultList = nativeQuery.getResultList();
		List mountData = mountData(resultList);
		result = mountWindBarb(mountData, req.getComparator(), req.getFilter1(), req.getFilter2());

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List mountWindBarb(List inList, String comparator, String filter1, String filter2) {
		List result = new ArrayList<>();
		for (int i = 0; i < inList.size(); i++) {
			Object[] element = (Object[]) inList.get(i);
			Object[] element2 = new Object[3];
			String elem1 = (String) element[1];
			String elem2 = (String) element[2];
			String elem3 = (String) element[3];
			Long date = (Long) element[0];

			try {
				if (elem1.contains("G")) { // rajada
					System.out.println("rajada " + elem1);
					String elem5 = (String) element[4];
					String[] split = elem5.split("G");
					String direction = split[0].substring(0, 3);
					String intensity = split[0].substring(3, 5);
					Double intensityMs = Double.parseDouble(intensity) / 1.944d;
					String gust = split[1];
					mountWindData(element2, direction, intensityMs, gust, date, comparator, filter1, filter2);
				} else {
					String gust = "-";
					String wIntensity = elem3.trim();
					if (wIntensity.contains("//")) {
						Double intensityMs = -1d;
						String wDirection = "-1";
						mountWindData(element2, wDirection, intensityMs, gust, date, comparator, filter1, filter2);
					} else {
						Double intensityMs = Double.parseDouble(wIntensity) / 1.944d;
						mountWindData(element2, elem2.trim(), intensityMs, gust, date, comparator, filter1, filter2);
					}
				}
			} catch (Exception e) {
				System.out.println("Erro: " + e + " - " + elem1 + " - " + date);
			}
			if (element2[0] != null) {
				result.add(element2);
			}
		}
		return result;
	}

	private Set<WindRoseArray> calculatePercentWind(Map<String, WindRose> mapRose, double total) {
		Set<WindRoseArray> result = new LinkedHashSet<>();
		Set<String> keySet = mapRose.keySet();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		double totalPercent = 0d;

		for (String key : keySet) {
			WindRose windRose = mapRose.get(key);
			WindRoseArray wra = new WindRoseArray();
			wra.setName(mountName(windRose.getName()));
			wra.set_colorIndex(windRose.get_colorIndex());
			wra.setQtdVRB(windRose.getQtdVRB());
			wra.setQtdBar(windRose.getQtdBarra());
			Set<String> keySet2 = windRose.getData().keySet();
			int pos = 0;
			Object[][] windA = new Object[16][2];
			for (String key2 : keySet2) {
				Wind wind = windRose.getData().get(key2);
				double percent = (wind.getQtd() / total) * 100d;
				wind.setPercentual(percent);
				totalPercent += percent;
				// WindArray wa = new WindArray();
				Object[] values = new Object[2];
				values[0] = wind.getDirecao();
				values[1] = Double.parseDouble(nf.format(percent).replace(",", "."));
				// wa.setValues(values);
				windA[pos] = values; // wind;// values[0];
				// windA[pos] = wa;
				pos++;
			}
			wra.setData(windA);
			result.add(wra);
			System.out.println("percent: " + totalPercent);
		}
		return result;
	}

	private String mountName(String name) {
		if (name.equals("1")) {
			return "menor que 0.5 m/s";
		} else if (name.equals("2")) {
			return "0.5 - 2";
		} else if (name.equals("3")) {
			return "2 - 4";
		} else if (name.equals("4")) {
			return "4 - 6";
		} else if (name.equals("5")) {
			return "6 - 8";
		} else if (name.equals("6")) {
			return "8 - 10";
		} else if (name.equals("7")) {
			return "maior que 10 m/s";
		}
		return "";
	}

	@SuppressWarnings({ "rawtypes" })
	private Set<WindRoseArray> mountWindRose(List inList, String comparator, String filter1, String filter2) {

		Map<String, WindRose> mapRose = new TreeMap<>();
		Map<String, Wind> winds = new TreeMap<>();
		int total = inList.size();

		for (int i = 0; i < 7; i++) {
			WindRose windRose = new WindRose();
			String name = getNameRose(i);
			windRose.setName(name);
			windRose.set_colorIndex(i + 1 + "");
			winds = mountWindDirections();
			windRose.setData(winds);
			mapRose.put(i + 1 + "", windRose);
		}

		for (int i = 0; i < inList.size(); i++) {
			Object[] element = (Object[]) inList.get(i);

			String elem1 = (String) element[1];
			String elem2 = (String) element[2];
			String elem3 = (String) element[3];

			int status = 0;
			try {
				if (elem1.contains("G")) { // rajada
					System.out.println("rajada " + elem1);
					String elem5 = (String) element[4];
					String[] split = elem5.split("G");
					String direction = split[0].substring(0, 3);
					String intensity = split[0].substring(3, 5);
					Double intensityMs = Double.parseDouble(intensity) / 1.944d;
					String gust = split[1];
					status = mountWindRoseData(mapRose, direction, intensityMs, gust);
				} else {
					String gust = "-";
					String wIntensity = elem3.trim();
					if (wIntensity.contains("//")) {
						Double intensityMs = -1d;
						String wDirection = "///";
						System.out.println("vento com ///");
						status = mountWindRoseData(mapRose, wDirection, intensityMs, gust);
					} else {
						Double intensityMs = Double.parseDouble(wIntensity) / 1.944d;
						status = mountWindRoseData(mapRose, elem2.trim(), intensityMs, gust);
					}
				}
				if (status > 0) {
					total--;
					System.out.println("subtrai total: " + total);
				}
			} catch (Exception e) {
				System.out.println("Erro: " + e + " - " + elem1 + " - " + elem3 + " - " + element[0]);
			}
		}
		System.out.println("total: " + inList.size());
		return calculatePercentWind(mapRose, total);
	}

	private Map<String, Wind> mountWindDirections() {
		Map<String, Wind> winds = new LinkedHashMap<String, Wind>();
		Wind wind = new Wind();
		wind.setDirecao("N");
		wind.setQtd(0);
		winds.put("N", wind);

		wind = new Wind();
		wind.setDirecao("NNE");
		wind.setQtd(0);
		winds.put("NNE", wind);

		wind = new Wind();
		wind.setDirecao("NE");
		wind.setQtd(0);
		winds.put("NE", wind);

		wind = new Wind();
		wind.setDirecao("ENE");
		wind.setQtd(0);
		winds.put("ENE", wind);

		wind = new Wind();
		wind.setDirecao("E");
		wind.setQtd(0);
		winds.put("E", wind);

		wind = new Wind();
		wind.setDirecao("ESE");
		wind.setQtd(0);
		winds.put("ESE", wind);

		wind = new Wind();
		wind.setDirecao("SE");
		wind.setQtd(0);
		winds.put("SE", wind);

		wind = new Wind();
		wind.setDirecao("SSE");
		wind.setQtd(0);
		winds.put("SSE", wind);

		wind = new Wind();
		wind.setDirecao("S");
		wind.setQtd(0);
		winds.put("S", wind);

		wind = new Wind();
		wind.setDirecao("SSW");
		wind.setQtd(0);
		winds.put("SSW", wind);

		wind = new Wind();
		wind.setDirecao("SW");
		wind.setQtd(0);
		winds.put("SW", wind);

		wind = new Wind();
		wind.setDirecao("WSW");
		wind.setQtd(0);
		winds.put("WSW", wind);

		wind = new Wind();
		wind.setDirecao("W");
		wind.setQtd(0);
		winds.put("W", wind);

		wind = new Wind();
		wind.setDirecao("WNW");
		wind.setQtd(0);
		winds.put("WNW", wind);

		wind = new Wind();
		wind.setDirecao("NW");
		wind.setQtd(0);
		winds.put("NW", wind);

		wind = new Wind();
		wind.setDirecao("NNW");
		wind.setQtd(0);
		winds.put("NNW", wind);

		return winds;
	}

	private String getNameRose(int i) {
		return i + 1 + "";
	}

	private int mountWindRoseData(Map<String, WindRose> windRoses, String direction, Double intensityMs, String gust)
			throws NumberFormatException {
		double dir = 0;
		WindRose windRose = null;
		Wind wind = null;

		int status = 0;

		if (intensityMs < 0.5) {
			windRose = windRoses.get("1");
		} else if (intensityMs < 2) {
			windRose = windRoses.get("2");
		} else if (intensityMs < 4) {
			windRose = windRoses.get("3");
		} else if (intensityMs < 6) {
			windRose = windRoses.get("4");
		} else if (intensityMs < 8) {
			windRose = windRoses.get("5");
		} else if (intensityMs < 10) {
			windRose = windRoses.get("6");
		} else if (intensityMs >= 10) {
			windRose = windRoses.get("7");
		}
		if (direction.startsWith("VRB")) {
			System.out.println("vento variavel");
			windRose.setQtdVRB(windRose.getQtdVRB() + 1);
			status = 1;
		} else if (direction.startsWith("///")) {
			System.out.println("vento com ///");
			windRose.setQtdBarra(windRose.getQtdBarra() + 1);
			status = 2;
		} else {
			dir = Integer.parseInt(direction);
			if (dir < 22.5 || dir >= 360) {
				wind = windRose.getData().get("N");
			} else if (dir < 45) {
				wind = windRose.getData().get("NNE");
			} else if (dir < 67.5) {
				wind = windRose.getData().get("NE");
			} else if (dir < 90) {
				wind = windRose.getData().get("ENE");
			} else if (dir < 112.5) {
				wind = windRose.getData().get("E");
			} else if (dir < 135) {
				wind = windRose.getData().get("ESE");
			} else if (dir < 157.5) {
				wind = windRose.getData().get("SE");
			} else if (dir < 180) {
				wind = windRose.getData().get("SSE");
			} else if (dir < 202.5) {
				wind = windRose.getData().get("S");
			} else if (dir < 225) {
				wind = windRose.getData().get("SSW");
			} else if (dir < 247.5) {
				wind = windRose.getData().get("SW");
			} else if (dir < 270) {
				wind = windRose.getData().get("WSW");
			} else if (dir < 292.5) {
				wind = windRose.getData().get("W");
			} else if (dir < 315) {
				wind = windRose.getData().get("WNW");
			} else if (dir < 337.5) {
				wind = windRose.getData().get("NW");
			} else if (dir < 360) {
				wind = windRose.getData().get("NNW");
			}
			// System.out.println("somou wind em " + wind.getDirecao());
			wind.setQtd(wind.getQtd() + 1);
		}
		return status;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List mountTemperature(List inList, String comparator, String filter1, String filter2) {
		List result = new ArrayList<>();
		for (int i = 0; i < inList.size(); i++) {
			Object[] element = (Object[]) inList.get(i);
			Object[] element2 = new Object[2];
			String elem1 = (String) element[1];
			String elem2 = (String) element[2];
			String elem3 = (String) element[3];

			try {
				if (elem2.endsWith("/") || elem2.contains(" ")) {
					mountTempData(element2, elem1.trim(), element[0], comparator, filter1, filter2);
				} else {
					mountTempData(element2, elem2.trim(), element[0], comparator, filter1, filter2);
				}
			} catch (Exception e) {
				try {
					mountTempData(element2, elem3.trim(), element[0], comparator, filter1, filter2);
				} catch (NumberFormatException n) {
					System.out.println("Erro: " + elem3 + " - " + element[0]);
				}
			}
			if (element2[0] != null) {
				result.add(element2);
			}
		}
		return result;
	}

	private void mountTempData(Object[] element2, String elem1, Object data, String comparator, String filter1,
			String filter2) throws NumberFormatException {
		int v1 = 0;
		if (elem1.startsWith("M")) {
			v1 = Integer.parseInt(elem1.substring(1)) * -1;
		} else {
			v1 = Integer.parseInt(elem1);
		}
		if (v1 < 45) {// clause guard
			if (StringUtils.isNotBlank(filter1)) {
				if (comparator.equals("gt")) {
					int f1 = Integer.parseInt(filter1);
					if (v1 >= f1) {
						element2[1] = v1;
						element2[0] = data;
					}
				} else if (comparator.equals("lt")) {
					int f1 = Integer.parseInt(filter1);
					if (v1 <= f1) {
						element2[1] = v1;
						element2[0] = data;
					}
				} else if (comparator.equals("between")) {
					int f1 = Integer.parseInt(filter1);
					int f2 = Integer.parseInt(filter2);

					if (v1 >= f1 && v1 <= f2) {
						element2[1] = v1;
						element2[0] = data;
					}
				} else { // sem filtro
					element2[1] = v1;
					element2[0] = data;
				}
			} else {
				element2[1] = v1;
				element2[0] = data;
			}
		} else {
			System.out.println("alto " + v1 + " - " + data);
			element2[1] = 1;
			element2[0] = data;
		}
	}

	private void mountWindData(Object[] element2, String direction, Double intensityMs, String gust, Object data,
			String comparator, String filter1, String filter2) throws NumberFormatException {
		int v1 = 0;
		if (direction.startsWith("VRB")) {
			v1 = -1;
		} else {
			v1 = Integer.parseInt(direction);
		}
		if (v1 <= 360) {// clause guard
			if (StringUtils.isNotBlank(filter1)) {
				if (comparator.equals("gt")) {
					int f1 = Integer.parseInt(filter1);
					if (v1 >= f1) {
						element2[2] = v1;
						element2[1] = intensityMs;
						element2[0] = data;
					}
				} else if (comparator.equals("lt")) {
					int f1 = Integer.parseInt(filter1);
					if (v1 <= f1) {
						element2[2] = v1;
						element2[1] = intensityMs;
						element2[0] = data;
					}
				} else if (comparator.equals("between")) {
					int f1 = Integer.parseInt(filter1);
					int f2 = Integer.parseInt(filter2);

					if (v1 >= f1 && v1 <= f2) {
						element2[2] = v1;
						element2[1] = intensityMs;
						element2[0] = data;
					}
				}
			} else {
				element2[2] = v1;
				element2[1] = intensityMs;
				element2[0] = data;
			}
		} else {
			System.out.println("alto " + v1 + " - " + data);
			element2[1] = -1;
			element2[2] = -1;
			element2[0] = data;
		}
	}

	private Query montaQueryTemperatura(ParametrosRequisicao paramsReq, String tipoMsg, List<String> weatherType,
			String maskData, String fenomeno, String localidade, String idGeografico, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select TO_char(dt_validade_inicial, " + maskData
				+ "), trim(substr(desc_mens , instr(desc_mens, ' Q', 1 ) -7, 3 )), "
				+ " trim(substr(desc_mens , instr(desc_mens, ' Q', 1 ) -6, 3 )), "
				+ " trim(substr(desc_mens ,  instr (desc_mens, '/') -3, 3 )) " + " from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryDewPoint(ParametrosRequisicao paramsReq, String tipoMsg, List<String> weatherType,
			String maskData, String fenomeno, String localidade, String idGeografico, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select TO_char(dt_validade_inicial, " + maskData + "), "
				+ " trim(substr(desc_mens , instr(desc_mens, ' Q', 1 ) -1, 3 )), "
				+ " trim(substr(desc_mens , instr(desc_mens, ' Q', 1 ) -2, 3 )), "
				+ " trim(substr(desc_mens ,  instr (desc_mens, '/') +2, 3 )) " + " from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryWindRose(ParametrosRequisicao paramsReq, String tipoMsg, List<String> weatherType,
			String maskData, String fenomeno, String localidade, String idGeografico) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append("select TO_char(dt_validade_inicial, " + maskData + "), "
				+ " trim(substr(desc_mens , instr(desc_mens, 'KT', 1 ) -6, 9 )), "
				+ " trim(substr(desc_mens , instr(desc_mens, 'KT', 1 ) -6, 4 )), "
				+ " trim(substr(desc_mens ,  instr (desc_mens, 'KT') -2, 2 )) , "
				+ " trim(substr(desc_mens , instr(desc_mens, 'KT', 1 ) -8, 10 )) from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private void mountData(String periodo, ParametrosRequisicao paramsReq) {
		String[] strings = periodo.split("_");
		if (strings.length == 2) {
			if (strings[1].equals("1")) {
				if (strings[0].equals("hora")) { // ultimas 24 horas
					String dataIni = getDatePast(-24, Calendar.HOUR_OF_DAY);
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				} else if (strings[0].equals("dia")) { // ultimos 30 dias
					String dataIni = getDatePast(-30, Calendar.DAY_OF_MONTH);
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				} else if (strings[0].equals("ano") && strings[1].equals("1")) { // neste ano
					String dataIni = getDateYear();
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				}
			} else {
				if (strings[0].equals("mes")) { // ultimos 12 meses
					int mes = Integer.parseInt(strings[1]);
					mes = mes * -1;
					String dataIni = getDatePast(mes, Calendar.MONTH);
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				} else if (strings[0].equals("ano")) { // ultimos 5 anos
					int ano = Integer.parseInt(strings[1]);
					ano = ano * -1;
					String dataIni = getDatePast(ano, Calendar.YEAR);
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				} else if (strings[0].equals("dia")) { // ultimos 5 anos
					int dia = Integer.parseInt(strings[1]);
					dia = dia * -1;
					String dataIni = getDatePast(dia, Calendar.DAY_OF_MONTH);
					String dataEnd = getDataAgora();
					paramsReq.setTempoInicial(dataIni);
					paramsReq.setTempoFinal(dataEnd);
				}
			}
		} else { // anuario
			String dataIni = periodo;
			String dataEnd = periodo;
			paramsReq.setTempoInicial(dataIni);
			paramsReq.setTempoFinal(dataEnd);
		}
	}

	private String getDateYear() {
		GregorianCalendar now = new GregorianCalendar();
		now.set(Calendar.MONTH, 0);
		now.set(Calendar.DAY_OF_MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return new SimpleDateFormat("yyyyMMddHHmm").format(now.getTime());
	}

	private String getDataAgora() {
		GregorianCalendar now = new GregorianCalendar();
		return new SimpleDateFormat("yyyyMMddHHmm").format(now.getTime());
	}

	private String getDatePast(int qtd, int unidade) {
		GregorianCalendar now = new GregorianCalendar();
		now.add(unidade, qtd);
		return new SimpleDateFormat("yyyyMMddHHmm").format(now.getTime());
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicdetail", method = RequestMethod.GET)
	public List countByPhenomenaDetail(String fenomeno, String tipoMsg, String unidadeTempo, String drilldown,
			String localidade, String idGeografico, String nivel, String report) {
		List<String> weatherType = getWheaterType(tipoMsg);

		String maskData = getMaskTimeDrill(unidadeTempo);

		String[] dataIni;
		if (nivel.equals("3")) {
			dataIni = getDateFormatedDrill3rd(drilldown, unidadeTempo);
			maskData = getMaskTimeDrill3rd(unidadeTempo);
		} else if (nivel.equals("4")) {
			dataIni = getDateFormatedDrill4rd(drilldown, unidadeTempo);
			maskData = getMaskTimeDrill4rd(unidadeTempo);
		} else {
			dataIni = getDateFormatedDrill(drilldown, unidadeTempo);
		}
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		paramsReq.setTempoFinal(dataIni[1]);
		paramsReq.setTempoInicial(dataIni[0]);

		Query nativeQuery = null;
		if (report.startsWith("dynamic")) {
			int posFenomeno = 16;
			if (fenomeno.contains("COR") || fenomeno.contains("AMD")) {
				posFenomeno = 1;
			}
			nativeQuery = montaQueryFenomena(paramsReq, weatherType, maskData, fenomeno, localidade, idGeografico,
					posFenomeno);
		} else if (report.startsWith("metarLate")) {
			weatherType = getWheaterTypeLate(tipoMsg);
			nativeQuery = montaQueryLate(paramsReq, weatherType, maskData, localidade, idGeografico);
		} else if (report.startsWith("qtdmessage")) {
			nativeQuery = montaQueryQtd(paramsReq, tipoMsg, weatherType, maskData, localidade, idGeografico);
		}

		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicmessagebytype", method = RequestMethod.POST)
	public List countMessageByPhenomenaByType(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterType(req.getTipoMsg());
		String maskData = getMaskTime(req.getUnidadeTempo());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;
		if (req.getFenomeno().contains("COR") || req.getFenomeno().contains("AMD")) {
			posFenomeno = 1;
		}
		Query nativeQuery = montaQueryFenomenaByType(paramsReq, req.getTipoMsg(), weatherType, maskData,
				req.getFenomeno(), req.getLocalidade(), req.getIdGeografico(), posFenomeno);
		List resultList = nativeQuery.getResultList();
		mountData(resultList);
		return resultList;
	}

	private List<String> getWheaterType(String tipoMsg) {
		List<String> weatherTypes = new ArrayList<>();
		if (tipoMsg.equals("METAR")) {
			weatherTypes.add("SA");
			weatherTypes.add("SP");
		} else if (tipoMsg.equals("TAF")) {
			weatherTypes.add("FT");
			weatherTypes.add("FC");
		} else if (tipoMsg.equals("SIGMET")) {
			weatherTypes.add("WS");
			weatherTypes.add("WC");
			weatherTypes.add("WV");
		} else if (tipoMsg.equals("AIRMET")) {
			weatherTypes.add("WA");
		} else if (tipoMsg.equals("GAMET")) {
			weatherTypes.add("FA");
		} else {
			weatherTypes.add("SA");
			weatherTypes.add("SP");
		}
		// TODO completar lista
		return weatherTypes;
	}

	private List<String> getWheaterTypeUnique(String tipoMsg) {
		List<String> weatherTypes = new ArrayList<>();
		if (tipoMsg.equals("METAR")) {
			weatherTypes.add("SA");
		} else if (tipoMsg.equals("SPECI")) {
			weatherTypes.add("SP");
		}
		return weatherTypes;
	}

	private List<String> getWheaterTypeExpanded(String tipoMsg) {
		List<String> weatherTypes = new ArrayList<>();
		if (tipoMsg.equals("METAR")) {
			weatherTypes.add("SA");
		} else if (tipoMsg.equals("SPECI")) {
			weatherTypes.add("SP");
		} else if (tipoMsg.equals("TAF")) {
			weatherTypes.add("FT");
			weatherTypes.add("FC");
		} else if (tipoMsg.equals("SIGMET")) {
			weatherTypes.add("WS");
			weatherTypes.add("WC");
			weatherTypes.add("WV");
		} else if (tipoMsg.equals("AIRMET")) {
			weatherTypes.add("WA");
		}
		return weatherTypes;
	}

	private List<String> getWheaterTypeLate(String tipoMsg) {
		List<String> weatherTypes = new ArrayList<>();
		if (tipoMsg.equals("METAR")) {
			weatherTypes.add("SA");
		} else if (tipoMsg.equals("TAF")) {
			weatherTypes.add("FT");
			weatherTypes.add("FC");
		} else if (tipoMsg.equals("GAMET")) {
			weatherTypes.add("FA");
		}
		return weatherTypes;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/resumoAll", method = RequestMethod.GET)
	public List countByPhenomenaAll(String fenomeno, String tipoMsg, String unidadeTempo, String strIni, String strEnd,
			String localidade) {
		String tableName = getTableName(tipoMsg);
		String maskData = "'YYYYMMDDHH24'";// getMaskTime(req.getUnidadeTempo());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String dataIni = getDateFormatedAll(strIni);
		String dataEnd = getDateFormatedAll(strEnd);
		paramsReq.setTempoFinal(dataEnd);
		paramsReq.setTempoInicial(dataIni);
		String[] dados = new String[1];
		dados[0] = "%" + fenomeno + "%";
		Query nativeQuery = montaQueryFenomenaBR(paramsReq, tableName, maskData, dados, localidade);
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/detalhe", method = RequestMethod.GET)
	public List countByPhenomenaDetalhe(String fenomeno, String tipoMsg, String unidadeTempo, String drilldown,
			String localidade) {
		String tableName = getTableName(tipoMsg);
		String maskData = getMaskTimeDrill(unidadeTempo);
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		String[] dataIni = getDateFormatedDrill(drilldown, unidadeTempo);
		paramsReq.setTempoFinal(dataIni[1]);
		paramsReq.setTempoInicial(dataIni[0]);
		String[] dados = new String[1];
		dados[0] = "%" + fenomeno + "%";
		Query nativeQuery = montaQueryFenomenaBR(paramsReq, tableName, maskData, dados, localidade);
		List resultList = nativeQuery.getResultList();
		return mountData(resultList);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicdetaillocal", method = RequestMethod.GET)
	public List countLocalByPhenomenaDetail(String fenomeno, String tipoMsg, String unidadeTempo, String drilldown,
			String localidade, String idGeografico, String nivel, String report, String periodo, String dataIni,
			String dataEnd) {
		List<String> weatherType = getWheaterType(tipoMsg);

		String maskData = "'YYYYMMDDHH24MI'";
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();

		if (report.startsWith("top")) {
			if (StringUtils.isNotBlank(dataIni) && periodo.equals("data")) {
				maskData = "'YYYYMMDD'";
				paramsReq.setTempoFinal(getDateFormated(dataEnd, maskData));
				paramsReq.setTempoInicial(getDateFormated(dataIni, maskData));
			} else {
				mountData(periodo, paramsReq);
			}
		} else {
			String[] date = getDateFormatedDrill5rd(drilldown, unidadeTempo);
			paramsReq.setTempoFinal(date[1]);
			paramsReq.setTempoInicial(date[0]);
		}

		int posFenomeno = 16;
		if (fenomeno.contains("COR") || fenomeno.contains("AMD")) {
			posFenomeno = 1;
		}

		Query nativeQuery = null;
		if (report.startsWith("dynamic") || report.startsWith("top")) {
			nativeQuery = montaQueryLocalidadeMsg(paramsReq, weatherType, maskData, fenomeno, idGeografico, localidade,
					posFenomeno);
		} else if (report.startsWith("metarLate")) {
			weatherType = getWheaterTypeLate(tipoMsg);
			nativeQuery = montaQueryLateDetail(paramsReq, weatherType, maskData, idGeografico, localidade);
		} else if (report.startsWith("temper") || report.startsWith("variaveisM")) {
			nativeQuery = montaQueryLocalidadeMsg(paramsReq, weatherType, maskData, idGeografico, localidade,
					posFenomeno);
		} else if (report.startsWith("qtdmessage")) {
			nativeQuery = montaQueryLocalidadeQtdMsg(paramsReq, weatherType, maskData, idGeografico, localidade);
		}
		List resultList = nativeQuery.getResultList();
		return resultList;
	}

	private Query montaQueryLocalidadeQtdMsg(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String idGeografico, String localidade) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select localidade || ' - ' || desc_mens  from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1 ");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryLocalidadeMsg(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String idGeografico, String localidade, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select localidade || ' - ' || desc_mens  from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1 ");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dynamicdetaillocalbytype", method = RequestMethod.GET)
	public List countLocalByPhenomenaDetailByType(String fenomeno, String tipoMsg, String unidadeTempo,
			String drilldown, String localidade, String idGeografico, String nivel, String report) {
		List<String> weatherType = getWheaterTypeUnique(tipoMsg);

		String[] dataIni = getDateFormatedDrill5rd(drilldown, unidadeTempo);
		String maskData = "'YYYYMMDDHH24MI'";

		ParametrosRequisicao paramsReq = new ParametrosRequisicao();
		paramsReq.setTempoFinal(dataIni[1]);
		paramsReq.setTempoInicial(dataIni[0]);

		int posFenomeno = 16;
		if (fenomeno.contains("COR") || fenomeno.contains("AMD")) {
			posFenomeno = 1;
		}

		Query nativeQuery = null;
		if (report.startsWith("dynamic")) {
			nativeQuery = montaQueryLocalidadeMsg(paramsReq, weatherType, maskData, fenomeno, idGeografico, localidade,
					posFenomeno);
		} else if (report.startsWith("metarLate")) {
			weatherType = getWheaterTypeLate(tipoMsg);
			nativeQuery = montaQueryLateDetail(paramsReq, weatherType, maskData, idGeografico, localidade);
		}
		List resultList = nativeQuery.getResultList();
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/top10", method = RequestMethod.POST)
	public List top10(@RequestBody RequisicaoResumo req) {
		List<String> weatherType = getWheaterType(req.getTipoMsg());
		String maskData = getMaskTime(req.getUnidadeTempo());
		ParametrosRequisicao paramsReq = new ParametrosRequisicao();

		montaData(req, maskData, paramsReq);

		int posFenomeno = 16;
		if (req.getFenomeno().contains("COR") || req.getFenomeno().contains("AMD")) {
			posFenomeno = 1;
		}
		Query nativeQuery = montaQueryTop10(paramsReq.getTempoInicial(), paramsReq.getTempoFinal(), req.getTipoMsg(),
				weatherType, maskData, req.getFenomeno(), req.getIdGeografico(), posFenomeno, req.getQtd());
		List resultList = nativeQuery.getResultList();
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/testeR1", method = RequestMethod.GET)
	public List testeReport() {

		String sql = "SELECT \r\n" + "A.ID_LOG\r\n" + ",A.ID_MENS_ANTERIOR \r\n" + ",DECODE(b.ID_REGIAO,NULL,\r\n"
				+ "             opmet_brasilia.pkg_util.f_rec_subordinacao\r\n"
				+ "                                               (P_ID_GEOGRAFICO => b.ID_GEOGRAFICO,P_ID_SUBORDINA => 'S',P_ID_IF_BRASIL => 'N'),\r\n"
				+ "             opmet_brasilia.pkg_util.f_rec_subordinacao\r\n"
				+ "                                               (P_ID_REGIAO     => b.ID_REGIAO,    P_ID_SUBORDINA => 'S',P_ID_IF_BRASIL => 'S')) SUBORDINACAO\r\n"
				+ ",A.ID_REMETENTE -- opmet_brasilia.TB_LOG_MSG A\r\n" + ", TO_CHAR(A.DT_HR_ERRO,'DD/MM/RRRR HH24:MI') "
				+ ", dbms_lob.substr( AA.DESC_MENS,4000,1) DESC_MENS_ORI-- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",DECODE(AA.ID_ESTADO,'A','INCORRETA'\r\n" + "                    ,'B','ABORTADA PROCESSADOR'\r\n"
				+ "                    ,'C','CORRETA'\r\n" + "                    ,'N','ABORTADA SUPERVISOR'\r\n"
				+ "                    ,'R','INCORRETA'\r\n" + "                    ,'S','INCORRETA'\r\n"
				+ "                    ,NULL)  STATUS_ORI -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",DECODE(AA.TP_PRIORIDADE,'G','GG'\r\n" + "                        ,'F','FF'\r\n"
				+ "                        ,'S','SS'\r\n"
				+ "                        , NULL)  PRIORIDADE_ORI -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",TO_CHAR(AA.DT_CORRECAO,'DD/MM/RRRR HH24:MI') DT_CORRECAO_ORI -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",AA.DESC_ERRO DESC_ERRO_ORI -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",AA.ID_CABECALHO -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",dbms_lob.substr( A.DESC_MENS, 4000,1) DESC_MENS_CORR\r\n"
				+ ",A.ID_REMETENTE ID_REMETENTE_CORR -- opmet_brasilia.TB_LOG_MSG A  \r\n"
				+ ",TO_CHAR(A.DT_HR_ERRO,'DD/MM/RRRR HH24:MI') DT_HR_ERRO_CORR -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ ",DECODE(A.ID_ESTADO,'A','INCORRETA'\r\n" + "                   ,'B','ABORTADA PROCESSADOR'\r\n"
				+ "                   ,'C','CORRETA'\r\n" + "                   ,'N','ABORTADA SUPERVISOR'\r\n"
				+ "                   ,'R','INCORRETA'\r\n" + "                   ,'S','INCORRETA'\r\n"
				+ "                   ,NULL)  STATUS_CORR -- opmet_brasilia.TB_LOG_MSG A\r\n"
				+ "FROM opmet_brasilia.TB_LOG_MSG A, opmet_brasilia.TB_LOG_MSG AA, opmet_brasilia.TB_LOG_MSG_ERRO C, opmet_brasilia.TB_ASSOCIACAO_REMETENTE B\r\n"
				+ "where AA.ID_LOG(+) = A.ID_MENS_ANTERIOR\r\n" + "AND A.ID_MENS_ANTERIOR = C.ID_MENS \r\n"
				+ "AND a.id_mens_anterior <> 0\r\n" + "AND B.ID_REMETENTE=A.ID_REMETENTE\r\n"
				+ "AND TRUNC(a.DT_HR_ERRO,'DD')  >= TRUNC(TO_DATE('20/04/2019','DD/MM/RRRR'),'DD')\r\n"
				+ "AND   TRUNC(a.DT_HR_ERRO,'DD')  <= TRUNC(TO_DATE('23/04/2019',  'DD/MM/RRRR'),'DD')";

		// StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(sql);

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		// int parameterNumber = 1;

		// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
		// idGeografico);
		//
		// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%"
		// + fenomeno + "%");
		// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
		// fenomeno);
		//
		// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
		// dataIni);
		// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
		// dataEnd);

		List resultList = nativeQuery.getResultList();
		return montaResult(resultList);
	}

	@SuppressWarnings("rawtypes")
	private List montaResult(List inList) {
		List<WrongMessage> result = new ArrayList<>();
		List<WronMessageReceived> resultRec = new ArrayList<>();

		List<CorrectedMessage> corr = new ArrayList<>();

		WrongMessage wmsg = new WrongMessage();
		wmsg.setData_final("final");
		wmsg.setData_inicial("inicial");
		wmsg.setFiltro_geral("filtro geral");
		wmsg.setFiltro_regiao("");
		wmsg.setIdioma("PT");
		wmsg.setCaminho_armazenar("/home/opmet/reports/");
		wmsg.setFormato_relatorio("PDF");

		for (int i = 0; i < inList.size(); i++) {
			Object[] element = (Object[]) inList.get(i);

			// novo no pai
			WronMessageReceived wMsgRec = new WronMessageReceived();
			// wMsgRec.setCorrecoes(corr);
			wMsgRec.setDesc_erro_ori((String) element[9]);
			wMsgRec.setDesc_mens((String) element[5]);
			wMsgRec.setDt_correcao_ori((String) element[8]);
			wMsgRec.setDt_hr_erro((String) element[4]);
			wMsgRec.setId_remetente((String) element[3]);
			wMsgRec.setPrioridade_ori((String) element[7]);
			wMsgRec.setStatus_ori((String) element[6]);
			wmsg.setSubordinacao((String) element[2]);
			Object[] filho = temFilho(element, inList);
			if (filho != null) {
				// novo filho
				wMsgRec = new WronMessageReceived();
				wMsgRec.setDesc_erro_ori((String) element[9]);
				wMsgRec.setDesc_mens((String) element[5]);
				wMsgRec.setDt_correcao_ori((String) element[8]);
				wMsgRec.setDt_hr_erro((String) element[4]);
				wMsgRec.setId_remetente((String) element[3]);
				wMsgRec.setPrioridade_ori((String) element[7]);
				wMsgRec.setStatus_ori((String) element[6]);
				wmsg.setSubordinacao((String) element[2]);

				corr = new ArrayList<>();
				CorrectedMessage corrected = new CorrectedMessage();
				corrected.setDesc_erro_corr((String) filho[9]);
				// corrected.setDt_correcao_corr(dt_correcao_corr);
				corrected.setDt_hr_erro_corr((String) element[13]);
				corrected.setId_remetente((String) element[12]);
				corrected.setMsg_corr((String) element[11]);
				// corrected.setPrioridade_corr(prioridade_corr);
				corrected.setStatus_corr((String) element[14]);
				corr.add(corrected);

				corrected = new CorrectedMessage();
				corrected.setDesc_erro_corr(null);
				// corrected.setDt_correcao_corr(dt_correcao_corr);
				corrected.setDt_hr_erro_corr((String) filho[13]);
				corrected.setId_remetente((String) filho[12]);
				corrected.setMsg_corr((String) filho[11]);
				// corrected.setPrioridade_corr(prioridade_corr);
				corrected.setStatus_corr((String) filho[14]);
				corr.add(corrected);

				wMsgRec.setCorrecoes(corr);
				i++;

			} else {
				corr = new ArrayList<>();
				CorrectedMessage corrected = new CorrectedMessage();
				// corrected.setDesc_erro_corr(desc_erro_corr);
				// corrected.setDt_correcao_corr(dt_correcao_corr);
				corrected.setDt_hr_erro_corr((String) element[13]);
				corrected.setId_remetente((String) element[12]);
				corrected.setMsg_corr((String) element[11]);
				// corrected.setPrioridade_corr(prioridade_corr);
				corrected.setStatus_corr((String) element[14]);
				corr.add(corrected);
				wMsgRec.setCorrecoes(corr);
			}

			resultRec.add(wMsgRec);
		}
		wmsg.setMensagens(resultRec);
		result.add(wmsg);

		return result;
	}

	@SuppressWarnings("rawtypes")
	private Object[] temFilho(Object[] element, List inList) {
		for (int i = 0; i < inList.size(); i++) {
			Object[] elem = (Object[]) inList.get(i);
			if (element[0].equals(elem[1])) {
				System.out.println("iguais " + elem[1]);
				return elem;
			}
		}
		return null;
	}

	private void montaData(RequisicaoResumo req, String maskData, ParametrosRequisicao paramsReq) {
		if (req.getPeriodo().equals("data")) {
			String dataIni = getDateFormated(req.getTempoInicial(), maskData);
			String dataEnd = getDateFormated(req.getTempoFinal(), maskData);
			paramsReq.setTempoFinal(dataEnd);
			paramsReq.setTempoInicial(dataIni);
		} else {
			mountData(req.getPeriodo(), paramsReq);
		}
	}

	private Query montaQueryTop10(String dataIni, String dataEnd, String tipoMsg, List<String> weatherType,
			String maskData, String fenomeno, String idGeografico, int posFenomeno, String qtd) {

		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" SELECT * FROM( select localidade, count(*) from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and desc_mens like ?  and INSTR (desc_mens, ? ) > " + posFenomeno);

		nativeQueryString
				.append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and  TO_char(dt_validade_inicial, "
						+ maskData + ") <= ? " + " group by localidade order by 2 desc)  WHERE ROWNUM <= ? ");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%" + fenomeno + "%");
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, fenomeno);

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dataIni);
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dataEnd);
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, qtd);

		return nativeQuery;
	}

	private String[] getDateFormatedDrill5rd(String drilldown, String unidadeTempo) {
		String[] dates = new String[2];
		String[] split = drilldown.split("-");
		String dataIni = split[3] + getMonth(split[2]) + getDay(split[1]) + getDay(split[0]) + "00";
		String dataEnd = split[3] + getMonth(split[2]) + getDay(split[1]) + getDay(split[0]) + "59";
		dates[0] = dataIni;
		dates[1] = dataEnd;
		return dates;
	}

	private String[] getDateFormatedDrill(String drilldown, String unidadeTempo) {
		String[] dates = new String[2];
		if (unidadeTempo.equals("mes")) {
			String[] split = drilldown.split("-");
			String dataIni = split[2] + getMonth(split[1]) + "01";
			String dataEnd = split[2] + getMonth(split[1]) + "32";
			dates[0] = dataIni;
			dates[1] = dataEnd;
		} else if (unidadeTempo.equals("ano")) {
			String[] split = drilldown.split("-");
			String dataIni = split[1] + getMonth(split[0]) + "00";
			String dataEnd = split[1] + getMonth(split[0]) + "13";
			dates[0] = dataIni;
			dates[1] = dataEnd;
		} else if (unidadeTempo.equals("dia")) {
			String[] split = drilldown.split("-");
			String dataIni = split[3] + getMonth(split[2]) + getDay(split[1]) + "00";
			String dataEnd = split[3] + getMonth(split[2]) + getDay(split[1]) + "24";
			dates[0] = dataIni;
			dates[1] = dataEnd;
		}
		return dates;
	}

	private String[] getDateFormatedDrill3rd(String drilldown, String unidadeTempo) {
		String[] dates = new String[2];
		if (unidadeTempo.equals("ano")) {
			String[] split = drilldown.split("-");
			String dataIni = split[2] + getMonth(split[1]) + "01";
			String dataEnd = split[2] + getMonth(split[1]) + "32";
			dates[0] = dataIni;
			dates[1] = dataEnd;
		} else if (unidadeTempo.equals("mes")) {
			String[] split = drilldown.split("-");
			String dataIni = split[3] + getMonth(split[2]) + getDay(split[1]) + "00";
			String dataEnd = split[3] + getMonth(split[2]) + getDay(split[1]) + "24";
			dates[0] = dataIni;
			dates[1] = dataEnd;
		}
		return dates;
	}

	private String[] getDateFormatedDrill4rd(String drilldown, String unidadeTempo) {
		String[] dates = new String[2];
		String[] split = drilldown.split("-");
		// String dataIni = split[2] + getMonth(split[1]) + getDay(split[0]) + "00";
		// String dataEnd = split[2] + getMonth(split[1]) + getDay(split[0]) + "24";
		String dataIni = split[3] + getMonth(split[2]) + getDay(split[1]) + "00";
		String dataEnd = split[3] + getMonth(split[2]) + getDay(split[1]) + "24";

		dates[0] = dataIni;
		dates[1] = dataEnd;
		return dates;
	}

	private String getDay(String inDay) {
		String outDay = inDay;
		int int1 = Integer.parseInt(inDay);
		if (int1 < 10) {
			outDay = "0" + inDay;
		}
		return outDay;
	}

	private String getMonth(String month) {
		if (month.equals("janeiro")) {
			return "01";
		} else if (month.equals("fevereiro")) {
			return "02";
		} else if (month.equals("março")) {
			return "03";
		} else if (month.equals("abril")) {
			return "04";
		} else if (month.equals("maio")) {
			return "05";
		} else if (month.equals("junho")) {
			return "06";
		} else if (month.equals("julho")) {
			return "07";
		} else if (month.equals("agosto")) {
			return "08";
		} else if (month.equals("setembro")) {
			return "09";
		} else if (month.equals("outubro")) {
			return "10";
		} else if (month.equals("novembro")) {
			return "11";
		} else if (month.equals("dezembro")) {
			return "12";
		}
		return "";
	}

	private String getDateFormated(String tempoFinal, String maskData) {
		String date = "";
		if (maskData.length() == 6) { // year
			date = tempoFinal.substring(0, 4);
		} else if (maskData.length() == 8) { // month
			date = tempoFinal.substring(0, 4);
			date += tempoFinal.substring(5, 7);
		} else if (maskData.length() == 10) { // day
			date = tempoFinal.substring(0, 4);
			date += tempoFinal.substring(5, 7);
			date += tempoFinal.substring(8, 10);
		} else if (maskData.length() == 14) { // hour
			date = tempoFinal.substring(0, 4);
			date += tempoFinal.substring(5, 7);
			date += tempoFinal.substring(8, 10);
			date += tempoFinal.substring(11, 13);
		}
		return date;
	}

	private String getDateFormatedAll(String tempoFinal) {
		return tempoFinal.substring(0, 4);
	}

	private String getMaskTime(String unidadeTempo) {
		String mask = "";
		if (unidadeTempo.equals("ano")) {
			mask = "'YYYY'";
		} else if (unidadeTempo.equals("mes")) {
			mask = "'YYYYMM'";
		} else if (unidadeTempo.equals("dia")) {
			mask = "'YYYYMMDD'";
		} else if (unidadeTempo.equals("hora")) {
			mask = "'YYYYMMDDHH24'";
		}
		return mask;
	}

	private String getMaskTimeDrill(String unidadeTempo) {
		String mask = "";
		if (unidadeTempo.equals("ano")) {
			mask = "'YYYYMM'";
		} else if (unidadeTempo.equals("mes")) {
			mask = "'YYYYMMDD'";
		} else if (unidadeTempo.equals("dia")) {
			mask = "'YYYYMMDDHH24'";
		}
		return mask;
	}

	private String getMaskTimeDrill3rd(String unidadeTempo) {
		String mask = "";
		if (unidadeTempo.equals("ano")) {
			mask = "'YYYYMMDD'";
		} else if (unidadeTempo.equals("mes")) {
			mask = "'YYYYMMDDHH24'";
		}
		return mask;
	}

	private String getMaskTimeDrill4rd(String unidadeTempo) {
		String mask = "";
		mask = "'YYYYMMDDHH24'";
		return mask;
	}

	private String getTableName(String tipoMsg) {
		String tableName = "";
		if (tipoMsg.equals("METAR")) {
			tableName = "VW_INFO_HIST_METAR_SPECI";
		} else if (tipoMsg.equals("TAF")) {
			tableName = "VW_INFO_HIST_TAF";
		} else if (tipoMsg.equals("SIGMET")) {
			tableName = "VW_INFO_HIST_SIGMET";
		} else if (tipoMsg.equals("AIRMET")) {
			tableName = "VW_INFO_HIST_AIRMET";
		} else if (tipoMsg.equals("GAMET")) {
			tableName = "VW_INFO_HIST_GAMET";
		}
		return tableName;
	}

	@SuppressWarnings({ "rawtypes" })
	private List mountData(List inList) {
		for (int i = 0; i < inList.size(); i++) {
			Object[] element = (Object[]) inList.get(i);
			element[0] = conv2Long(element[0]);
		}
		return inList;
	}

	private Query montaQueryLate(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String localidade, String idGeografico) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(
				" select TO_char(dt_validade_inicial, " + maskData + "), count(*) from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		nativeQueryString.append(
				" AND TO_char(dt_recebimento, 'YYYYMMDDHH24MISS') - TO_char(dt_validade_inicial, 'YYYYMMDDHH24MISS') > 0500 "
						+ " and  TO_char(dt_recebimento, 'YYYYMMDDHH24MISS') - TO_char(dt_validade_inicial, 'YYYYMMDDHH24MISS') < 1500 ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and desc_mens not like '% COR %'");

		nativeQueryString
				.append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and  TO_char(dt_validade_inicial, "
						+ maskData + ") <= ? " + " group by TO_char(dt_validade_inicial, " + maskData + ") order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryLateDetail(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String idGeografico, String localidade) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(
				" select localidade || '-' || desc_mens  || ' Recebido as ' || TO_char(dt_recebimento, 'DD/MM/YYYY HH24:MI:SS') "
						+ " from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(
				" AND TO_char(dt_recebimento, 'YYYYMMDDHH24MISS') - TO_char(dt_validade_inicial, 'YYYYMMDDHH24MISS') > 0500 "
						+ " and  TO_char(dt_recebimento, 'YYYYMMDDHH24MISS') - TO_char(dt_validade_inicial, 'YYYYMMDDHH24MISS') < 1500 ");

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");
		nativeQueryString.append(" and desc_mens not like '% COR %'");

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  " + " order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryFenomenaBR(ParametrosRequisicao paramsReq, String tableName, String maskData,
			String[] dados, String localidade) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString
				.append(" select TO_char(dt_validade_inicial, " + maskData + "), count(*) " + " from " + tableName);

		if (StringUtils.isNotBlank(localidade)) {
			if (tableName.contains("SIGMET") || tableName.contains("AIRMET")) {
				nativeQueryString.append(" where id_fir = ? ");
			} else {
				nativeQueryString.append(" where id_localidade = ? ");
			}
		} else {
			if (tableName.contains("SIGMET") || tableName.contains("AIRMET")) {
				nativeQueryString.append(" where id_fir like 'SB%' ");
			} else {
				nativeQueryString.append(" where id_localidade like 'SB%' ");
			}
		}

		if (dados.length > 0) {
			nativeQueryString.append(" and desc_mens like ? ");
		}
		nativeQueryString
				.append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and  TO_char(dt_validade_inicial, "
						+ maskData + ") <= ? " + " group by TO_char(dt_validade_inicial, " + maskData + ") order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		for (int j = 0; j < dados.length; j++) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dados[j]);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryFenomena(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String dados, String localidade, String idGeografico, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(
				" select TO_char(dt_validade_inicial, " + maskData + "), count(*) from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and desc_mens like ?  and INSTR (desc_mens, ? ) > " + posFenomeno);

		nativeQueryString
				.append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and  TO_char(dt_validade_inicial, "
						+ maskData + ") <= ? " + " group by TO_char(dt_validade_inicial, " + maskData + ") order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%" + dados + "%");
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dados);

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private Query montaQueryQtd(ParametrosRequisicao paramsReq, String tableName, List<String> weatherType,
			String maskData, String localidade, String idGeografico) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(
				" select TO_char(dt_validade_inicial, " + maskData + "), count(*) from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString
				.append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and  TO_char(dt_validade_inicial, "
						+ maskData + ") <= ? " + " group by TO_char(dt_validade_inicial, " + maskData + ") order by 1");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private boolean isFir(List<String> weatherType) {
		if (weatherType.contains("WS") || weatherType.contains("FA") || weatherType.contains("WA")) {
			return true;
		}
		return false;
	}

	private boolean isLocalidade(List<String> weatherType) {
		if (weatherType.contains("SA") || weatherType.contains("SP") || weatherType.contains("FT")) {
			return true;
		}
		return false;
	}

	private Query montaQueryFenomenaByType(ParametrosRequisicao paramsReq, String tableName, List<String> weatherType,
			String maskData, String dados, String localidade, String idGeografico, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select TO_char(dt_validade_inicial, " + maskData
				+ "), count(*) , id_meteor from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and desc_mens like ?  and INSTR (desc_mens, ? ) > " + posFenomeno);

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ? "
				+ " group by TO_char(dt_validade_inicial, " + maskData + ") , id_meteor order by 1, 3");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%" + dados + "%");
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dados);

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	// private Query montaQueryLocalidade(ParametrosRequisicao paramsReq,
	// List<String> weatherType, String maskData,
	// String dados, String idGeografico, String localidade, int posFenomeno) {
	// StringBuilder paramsString = new StringBuilder();
	// StringBuilder nativeQueryString = new StringBuilder();
	//
	// nativeQueryString.append(" select localidade, count(*) from
	// VW_MSG_GERAL_HISTORICO w ");
	//
	// if (isLocalidade(weatherType)) {
	// nativeQueryString.append(", tb_localidade l ");
	// nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
	// } else if (isFir(weatherType)) {
	// nativeQueryString.append(", tb_fir l ");
	// nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
	// }
	//
	// nativeQueryString.append(" and l.id_geografico = ? ");
	//
	// if (StringUtils.isNotBlank(localidade)) {
	// nativeQueryString.append(" and l.localidade = ? ");
	// }
	//
	// nativeQueryString.append(" and id_meteor in ('");
	// for (int i = 0; i < weatherType.size(); i++) {
	// nativeQueryString.append(weatherType.get(i));
	// if (weatherType.size() > 1 && i < weatherType.size() - 1) {
	// nativeQueryString.append("','");
	// }
	// }
	// nativeQueryString.append("') ");
	//
	// nativeQueryString.append(" and desc_mens like ? and INSTR (desc_mens, ? ) > "
	// + posFenomeno);
	//
	// nativeQueryString
	// .append(" and TO_char(dt_validade_inicial, " + maskData + ") >= ? and
	// TO_char(dt_validade_inicial, "
	// + maskData + ") <= ? " + " group by localidade order by 2 desc");
	//
	// Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
	// .createNativeQuery(nativeQueryString.toString());
	//
	// int parameterNumber = 1;
	//
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
	// idGeografico);
	//
	// if (StringUtils.isNotBlank(localidade)) {
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
	// localidade);
	// }
	//
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%"
	// + dados + "%");
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
	// dados);
	//
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
	// paramsReq.getTempoInicial());
	// setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++,
	// paramsReq.getTempoFinal());
	//
	// return nativeQuery;
	// }
	//

	private Query montaQueryLocalidadeMsg(ParametrosRequisicao paramsReq, List<String> weatherType, String maskData,
			String dados, String idGeografico, String localidade, int posFenomeno) {
		StringBuilder paramsString = new StringBuilder();
		StringBuilder nativeQueryString = new StringBuilder();

		nativeQueryString.append(" select localidade || ' - ' || desc_mens  from VW_MSG_GERAL_HISTORICO w ");

		if (isLocalidade(weatherType)) {
			nativeQueryString.append(", tb_localidade l ");
			nativeQueryString.append(" WHERE l.cd_localidade = w.localidade ");
		} else if (isFir(weatherType)) {
			nativeQueryString.append(", tb_fir l ");
			nativeQueryString.append(" WHERE l.cd_fir = w.localidade ");
		}

		nativeQueryString.append(" and l.id_geografico = ? ");

		if (StringUtils.isNotBlank(localidade)) {
			nativeQueryString.append(" and w.localidade = ? ");
		}

		nativeQueryString.append(" and id_meteor in ('");
		for (int i = 0; i < weatherType.size(); i++) {
			nativeQueryString.append(weatherType.get(i));
			if (weatherType.size() > 1 && i < weatherType.size() - 1) {
				nativeQueryString.append("','");
			}
		}
		nativeQueryString.append("') ");

		nativeQueryString.append(" and desc_mens like ?  and INSTR (desc_mens, ? ) > " + posFenomeno);

		nativeQueryString.append(" and TO_char(dt_validade_inicial, " + maskData
				+ ") >= ? and  TO_char(dt_validade_inicial, " + maskData + ") <= ?  order by 1 ");

		Query nativeQuery = jpaContext.getEntityManagerByManagedType(Transport.class)
				.createNativeQuery(nativeQueryString.toString());

		int parameterNumber = 1;

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, idGeografico);

		if (StringUtils.isNotBlank(localidade)) {
			setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, localidade);
		}

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, "%" + dados + "%");
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, dados);

		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoInicial());
		setQueryAndStringParameters(nativeQuery, paramsString, parameterNumber++, paramsReq.getTempoFinal());

		return nativeQuery;
	}

	private void setQueryAndStringParameters(Query q, StringBuilder s, int cardinal, String value) {
		q.setParameter(cardinal, value);
		s.append("'" + value + "' ");
	}

	private long conv2Long(Object data) {
		String dataBase = (String) data;
		GregorianCalendar now = new GregorianCalendar();
		int year = Integer.parseInt(dataBase.substring(0, 4));
		now.set(Calendar.YEAR, year);

		int month = 1;
		if (dataBase.length() > 5) {
			month = Integer.parseInt(dataBase.substring(4, 6));
		}

		now.set(Calendar.MONTH, month - 1);

		int day = 1;
		if (dataBase.length() > 6) {
			day = Integer.parseInt(dataBase.substring(6, 8));
		}

		int hour = 0;
		if (dataBase.length() > 8) {
			hour = Integer.parseInt(dataBase.substring(8));
		}

		now.set(Calendar.DAY_OF_MONTH, day);
		now.set(Calendar.HOUR_OF_DAY, hour);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		long timeInMillis = now.getTimeInMillis();
		return timeInMillis;
	}
}
