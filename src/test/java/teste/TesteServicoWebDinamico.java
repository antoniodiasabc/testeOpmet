package teste;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import br.com.atech.Application;
import br.com.atech.controler.ClientWebservice1;
import br.com.atech.controler.GetMessage;
import br.com.atech.vo.Aerodromo;
import br.com.atech.vo.MetarEstatistica;
import br.com.atech.vo.Observacao;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = { Application.class, MockMvc.class })
@EnableConfigurationProperties
@AutoConfigureMockMvc
public class TesteServicoWebDinamico extends TestCase {

	@Autowired
	ClientWebservice1 beanRegistration;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFormula() {
		try {
			beanRegistration
					.testeHttpsv1("TAF SBSP 230906Z 2312/2324 15008KT 9999 SCT007 BKN015 TX19/2317Z TN13/2323Z \r\n"
							+ "PROB30 2312/2315 8000 RA BKN010 \r\n" + "BECMG 2315/2317 BKN025 \r\n"
							+ "BECMG 2320/2322 BKN012 RMK PHM=");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTAF2() {
		try {
			beanRegistration
					.testeHttpsv1("TAF SBSP 230906Z 2312/2324 15008KT 9999 SCT007 BKN015 TX19/2317Z TN13/2323Z \r\n"
							+ "PROB30 2312/2315 8000 RA BKN010 \r\n" + "BECMG 2315/2317 BKN025 \r\n"
							+ "BECMG 2320/2322 BKN012 RMK PHM2=");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void testMetar() {
//		String body = "METAR SBBR " + getHoraReferenciaMetar() + "00Z 19005KT CAVOK 26/19 Q1020=";
//		try {
//			beanRegistration.testeHttpsv1(body);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void insereMetar(MetarEstatistica valor) {
		String body = "METAR " + valor.getLocalidade() + " " + valor.getDiaHoraMetar()
				+ "00Z 19005KT CAVOK 26/19 Q1020=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void testeTAf() {
//		String dataIni = "28/08/2019 00:00";
//		String dataEnd = "30/08/2019 23:59";
//		int total = 28;
//		int totalAusente = 4;
//		int totalNoHorario = 1;
//		int totalAtrasado = total - (totalAusente + totalNoHorario);
//
//		List<String> localidades = new ArrayList<>();
//		localidades.add("SBBR");
//		localidades.add("SBAU");
//
//		testTAFAtrasado(dataIni, dataEnd, totalAtrasado, localidades);
//		System.out.println("inserindo msg no horario");
//		testTAFNoHorario(dataIni, dataEnd, totalNoHorario, localidades);
//	}
	


	@Test
	public void testeMetar() {
		List<MetarEstatistica> metarRX = createMetarRX(); //createMetarRXTeste(); // createMetarRX();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				System.out.println(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Test
	public void testeTafSteps() {
		List<MetarEstatistica> metarRX = createTAFRXTeste();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				System.out.println(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}


	@Test
	public void testeSigmet() {
		List<MetarEstatistica> sigmetWSTeste = createSigmetWSTeste();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total+1);
				total++;
			}
		}
	}
	

	@Test
	public void testeAirep() {
		List<MetarEstatistica> sigmetWSTeste = createAirepTeste();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total+1);
				total++;
			}
		}
	}
	
	@Test
	public void testeAirmet() {
		List<MetarEstatistica> sigmetWSTeste = createSigmetWSTeste();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}



	@Test
	public void testeSPECI() {
		List<MetarEstatistica> sigmetWSTeste = createSPECITeste();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSPECI(metarEstatistica, total+1);
				total++;
			}
		}
	}

	private void insereSPECI(MetarEstatistica valor, int total) {
		String body = "SPECI " + valor.getLocalidade() + " " + getDiaHorafReferenciaMin(valor.getData())
				+ "Z 19005KT CAVOK 26/19 Q1020 RMK " + total + " =";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insereSigmet(MetarEstatistica metarEstatistica, int num) {
		int horaref = Integer.parseInt(metarEstatistica.getData().substring(11, 13));
		System.out.println("hora ref sigmet " + horaref);
		String body = metarEstatistica.getIdMeteor() + "BZ " + metarEstatistica.getLocalidade() + " " + getHoraReferencia()
				+ "\n" + metarEstatistica.getLocalidade() + " SIGMET "+ num + " VALID "
				+ getDiaHorafReferencia(metarEstatistica.getData()) + "00/"
				+ getDiaHoraMenosMais(horaref + 6, metarEstatistica.getDataenvio())
				+ "00 SBCW - SBCW CURITIBA FIR EMBD TS FCST WI S2832 W05606 - S3139 W05213 - S3357 W05157 "
				+ " - S3356 W05325 - S3312 W05332 - S3246 W05303 - S3009 W05741 - S2832 W05606 TOP FL380 MOV E 08KT NC=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void insereAIREP(MetarEstatistica valor, int num) {
		String body = "UABZ01 " + valor.getLocalidade() + " " +  getDiaHorafReferenciaMin(valor.getData()) +
				"\nARP " + getDiaHoraMenosMais(1) + " /// UAE2" + num + " 0607S 01018W " 
				+ getDiaHorafReferenciaMin(valor.getData()) + " F340 MS430 055/046 TB/ S/3/=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void obterDataEnvioWS(MetarEstatistica metarEstatistica) {
		GregorianCalendar horaEnvioMetar = getDiaHoraEnvioTAF(-1, metarEstatistica.getData());
		metarEstatistica.setDataenvio(horaEnvioMetar);
	}

	private void obterDataEnvio(MetarEstatistica metarEstatistica) {
		if (metarEstatistica.getStatus() == 0) { // ausente
			GregorianCalendar horaEnvioMetar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else if (metarEstatistica.getStatus() == 1) { // menor q 5
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioMetar(4, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else if (metarEstatistica.getStatus() == 2) { // entre 5 e 10
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioMetar(8, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else if (metarEstatistica.getStatus() == 3) { // entre 10 e 30
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioMetar(12, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else { // mais de 30
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioMetar(32, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		}
	}

	private void obterDataEnvioTAF(MetarEstatistica metarEstatistica) {
		if (metarEstatistica.getStatus() == 0) { // ausente
			GregorianCalendar horaEnvioMetar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else if (metarEstatistica.getStatus() == 1) { // excelente
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioTAF(-1, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		} else if (metarEstatistica.getStatus() == 2) { // atrasado mais de 1h
			GregorianCalendar horaEnvioMetar = getDiaHoraEnvioTAF(1, metarEstatistica.getData());
			metarEstatistica.setDataenvio(horaEnvioMetar);
		}
	}

	private GregorianCalendar getDiaHoraEnvioTAF(int somaH, String dataRef) {
		GregorianCalendar horaReferencia = getHoraTafReferencia(dataRef);
		GregorianCalendar envio = (GregorianCalendar) horaReferencia.clone();
		envio.add(Calendar.HOUR_OF_DAY, somaH);
		return envio;
	}

	public List<MetarEstatistica> createMetarRX() {
		List<MetarEstatistica> values = new ArrayList<>();		
		
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 00:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 01:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 02:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 03:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 04:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 05:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 06:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 07:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 08:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 09:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 10:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 11:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 12:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 13:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 14:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 15:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 16:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 17:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 18:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 19:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 20:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 21:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 22:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "29/08/2019 23:00","ENTRE_10_30"));

		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 00:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 01:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 02:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 03:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 04:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 05:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 06:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 07:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 08:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 09:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 10:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 11:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 12:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 13:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 14:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 15:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 16:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 17:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 18:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 19:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 20:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 21:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 22:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "30/08/2019 23:00","ENTRE_5_10"));

		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 00:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 01:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 02:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 03:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 04:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 05:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 06:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 07:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 08:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 09:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 10:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 11:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 12:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 13:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 14:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 15:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 16:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 17:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 18:00","MENOR_5"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 19:00","ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 20:00","ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 21:00","MAIOR_30"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 22:00","AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SABE", "31/08/2019 23:00","MENOR_5"));
		return values;
	}

	public List<MetarEstatistica> createMetarRXTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "26/08/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "26/08/2019 19:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "26/08/2019 20:00", "ENTRE_5_10"));

		return values;
	}

	public List<MetarEstatistica> createTAFRXTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "29/08/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "29/08/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "29/08/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		
		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createSigmetWSTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", 3, "WS"));
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", 2, "WC"));
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", 2, "WV"));

		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", 2, "WS"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", 3, "WC"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", 3, "WV"));

		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", 2, "WS"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", 3, "WC"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", 2, "WV"));

		return values;
	}
	

	private List<MetarEstatistica> createAirepTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", 1, "UA"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", 4, "UA"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", 4, "UA"));
		return values;
	}

	private List<MetarEstatistica> createSPECITeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "29/08/2019 00:00", 4, "SP"));
		values.add(new MetarEstatistica("SABE", "30/08/2019 00:00", 2, "SP"));
		values.add(new MetarEstatistica("SABE", "31/08/2019 00:00", 1, "SP"));
		return values;
	}

	private boolean isInTime(MetarEstatistica valor) {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		if (today.after(valor.getDataenvio())) { // esta na hora de enviar
			System.out.println("hora de envio de metar " + valor.getLocalidade() + " data " + valor.getData()
					+ " status: " + valor.getStatus());
			return true;
		}
		return false;
	}

	@Test
	public void testTAFDinamico() {
		GetMessage msgObservacao = new GetMessage();
		java.util.List<Aerodromo> localidades = msgObservacao.getLocalidades();
		java.util.List<Observacao> message;
		for (Aerodromo aerodromo : localidades) {
			message = msgObservacao.getMessage(aerodromo);
			testarProcessador(aerodromo, message);
		}
	}

	/*
	 * fazer a insercao de taf atrasado e logar o que foi feito
	 */
	public void testTAFAtrasado(String dataIni, String dataEnd, int totalAtrasado, List<String> localidades) {
		int i = 0;
		while (i < totalAtrasado) {
			int horaref = getHoraTafReferenciaAtrasado(dataIni);
			GregorianCalendar referencia = getHoraTafReferencia(dataIni);

			// int horaref = getHoraTafReferenciaAtrasado();
			String body = "FTBZ01 SBBR " + getDayToday() + "1503\n" + " TAF SBBR " + getDayToday() + "1601Z "
					+ getDiaHoraMenosMais(horaref, referencia) + "/" + getDiaHoraMenosMais(18, referencia)
					+ " 24005KT 8000 TSRA BKN015 FEW035CB TN01/" + getDiaHoraMenosMais(horaref + 1, referencia)
					+ "Z TX20/" + getDiaHoraMenosMais(horaref + 14, referencia) + "Z " + " BECMG "
					+ getDiaHoraMenosMais(horaref + 2, referencia) + "/" + getDiaHoraMenosMais(horaref + 4, referencia)
					+ " 6000 RA BKN010 " + " BECMG " + getDiaHoraMenosMais(horaref + 6, referencia) + "/"
					+ getDiaHoraMenosMais(horaref + 8, referencia) + " 9999 BKN013 " + " BECMG "
					+ getDiaHoraMenosMais(horaref + 10, referencia) + "/"
					+ getDiaHoraMenosMais(horaref + 12, referencia) + " 15010KT BKN002 RMK PGG=";
			try {
				beanRegistration.testeHttpsv1(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
			dataIni = getNextDataIni(dataIni);
		}
	}

	private String getNextDataIni(String dataRef) {
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

		today.add(Calendar.HOUR_OF_DAY, 6);
		String diaHora = today.get(Calendar.DAY_OF_MONTH) + "/" + getDay(today.get(Calendar.MONTH) + 1) + "/"
				+ today.get(Calendar.YEAR) + " " + getDay(today.get(Calendar.HOUR_OF_DAY)) + ":"
				+ getDay(today.get(Calendar.MINUTE));
		return diaHora; // sdf.format(tomorrow.getTime());
	}

	public void testTAFNoHorario(String dataIni, String dataEnd, int totalNoHorario, List<String> localidades) {
		int horaref = getHoraTafReferencia();
		String body = "FTBZ01 SBBR " + getDayToday() + "1503\nTAF SBBR " + getDayToday() + "1601Z "
				+ getDiaHoraMenosMais(horaref) + "/" + getDiaHoraMenosMais(horaref + 18)
				+ " 24005KT 8000 TSRA BKN015 FEW035CB TN01/" + getDiaHoraMenosMais(horaref + 1) + "Z TX20/"
				+ getDiaHoraMenosMais(horaref + 14) + "Z " + " BECMG " + getDiaHoraMenosMais(horaref + 2) + "/"
				+ getDiaHoraMenosMais(horaref + 4) + " 6000 RA BKN010 " + " BECMG " + getDiaHoraMenosMais(horaref + 6)
				+ "/" + getDiaHoraMenosMais(horaref + 8) + " 9999 BKN013 " + " BECMG "
				+ getDiaHoraMenosMais(horaref + 10) + "/" + getDiaHoraMenosMais(horaref + 12)
				+ " 15010KT BKN002 RMK PGG=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insereTAF(MetarEstatistica valor) {
		int horaref = getHoraTafReferencia();
		String body = "FTBZ01 SBBR " + getDayToday() + "1503\nTAF SBBR " + getDiaHorafReferenciaMin(valor.getData()) + "Z "
				+ getDiaHorafReferencia(valor.getData()) + "/" + getDiaHoraMenosMais(horaref + 18)
				+ " 24005KT 8000 TSRA BKN015 FEW035CB TN01/" + getDiaHoraMenosMais(horaref + 1) + "Z TX20/"
				+ getDiaHoraMenosMais(horaref + 14) + "Z " + " BECMG " + getDiaHoraMenosMais(horaref + 2) + "/"
				+ getDiaHoraMenosMais(horaref + 4) + " 6000 RA BKN010 " + " BECMG " + getDiaHoraMenosMais(horaref + 6)
				+ "/" + getDiaHoraMenosMais(horaref + 8) + " 9999 BKN013 " + " BECMG "
				+ getDiaHoraMenosMais(horaref + 10) + "/" + getDiaHoraMenosMais(horaref + 12)
				+ " 15010KT BKN002 RMK PGG=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	// private int getHoraTafReferenciaAtrasado() {
	// GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
	// List<Integer> valores = new ArrayList<>();
	// valores.add(0);
	// valores.add(6);
	// valores.add(12);
	// valores.add(18);
	// return getMenorMaisProximo(valores, today.get(Calendar.HOUR_OF_DAY));
	// }

	private int getHoraTafReferenciaAtrasado(String dataRef) {
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

		List<Integer> valores = new ArrayList<>();
		valores.add(0);
		valores.add(6);
		valores.add(12);
		valores.add(18);
		return getMenorMaisProximo(valores, today.get(Calendar.HOUR_OF_DAY));
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

	private String getHoraReferenciaMetar() {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		String diaHora = getDay(today.get(Calendar.DAY_OF_MONTH)) + getDay(today.get(Calendar.HOUR_OF_DAY));
		return diaHora;
	}
	
	private String getHoraReferencia() {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		String diaHora = getDay(today.get(Calendar.DAY_OF_MONTH)) + getDay(today.get(Calendar.HOUR_OF_DAY)) + getDay(today.get(Calendar.MINUTE));
		return diaHora;
	}
	

	private String getDiaHorafReferencia(String dataRef) {
		String dia = dataRef.substring(0, 2);		
		String hora = dataRef.substring(11, 13);
		String diaHora = dia + hora;
		return diaHora;
	}
	

	private String getDiaHorafReferenciaMin(String dataRef) {
		String dia = dataRef.substring(0, 2);		
		String hora = dataRef.substring(11, 13);
		String min = dataRef.substring(14);		
		String diaHora = dia + hora + min;
		return diaHora;
	}
	
	private int getHoraAgora() {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		return today.get(Calendar.HOUR_OF_DAY);
	}

	private int getHoraTafReferencia() {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		List<Integer> valores = new ArrayList<>();
		valores.add(0);
		valores.add(6);
		valores.add(12);
		valores.add(18);
		valores.add(24);
		return getMaiorMaisProximo(valores, today.get(Calendar.HOUR_OF_DAY));
	}

	private int getMenorMaisProximo(List<Integer> valores, int ref) {
		int menor = 0;
		int pos = 0;
		for (Integer valor : valores) {
			pos++;
			int diff = valor - ref;
			if (diff < 2 && (diff > menor || pos == 1)) {
				menor = diff;
			}
		}
		return menor;
	}

	private int getMaiorMaisProximo(List<Integer> valores, int ref) {
		int menorMaior = 0;
		int pos = 0;
		for (Integer valor : valores) {
			int diff = valor - ref;
			if (diff > 1 && (diff < menorMaior || pos == 0)) {
				menorMaior = diff;
				pos++;
			}
		}
		return menorMaior;
	}

	private void testarProcessador(Aerodromo aerodromo, java.util.List<Observacao> message) {
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
					if (msg.endsWith("=") == false) {
						msg += "=";
					}

					try {
						beanRegistration.testeHttpsv1(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private String getDayToday() {
		GregorianCalendar tomorrow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(tomorrow.getTime());
	}

	// private String getHoratual() {
	// GregorianCalendar tomorrow = new
	// GregorianCalendar(TimeZone.getTimeZone("UTC"));
	// SimpleDateFormat sdf = new SimpleDateFormat("HH");
	// return sdf.format(tomorrow.getTime());
	// }
	//
	// private String getHoraMenosMais(int total) {
	// GregorianCalendar tomorrow = new
	// GregorianCalendar(TimeZone.getTimeZone("UTC"));
	// tomorrow.add(Calendar.HOUR_OF_DAY, total);
	// SimpleDateFormat sdf = new SimpleDateFormat("HH");
	// return sdf.format(tomorrow.getTime());
	// }

	private String getDiaHoraMenosMais(int total) {
		GregorianCalendar tomorrow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		// System.out.println(" 1o " + tomorrow.get(Calendar.HOUR_OF_DAY));
		tomorrow.add(Calendar.HOUR_OF_DAY, total);
		String diaHora = getDay(tomorrow.get(Calendar.DAY_OF_MONTH)) + getDay(tomorrow.get(Calendar.HOUR_OF_DAY));
		return diaHora; // sdf.format(tomorrow.getTime());
	}

	private String getDiaHoraMenosMais(int total, GregorianCalendar today) {
		GregorianCalendar tomorrow = (GregorianCalendar) today.clone();
		tomorrow.add(Calendar.HOUR_OF_DAY, total);
		String diaHora = getDay(tomorrow.get(Calendar.DAY_OF_MONTH)) + getDay(tomorrow.get(Calendar.HOUR_OF_DAY));
		return diaHora; // sdf.format(tomorrow.getTime());
	}

	private GregorianCalendar getDiaHoraEnvioMetar(int somaMin, String dataRef) {
		GregorianCalendar horaReferencia = getHoraTafReferencia(dataRef);
		GregorianCalendar envio = (GregorianCalendar) horaReferencia.clone();
		envio.add(Calendar.MINUTE, somaMin);
		return envio;
	}

	private String getDay(int inDay) {
		String outDay = inDay + "";
		if (inDay < 10) {
			outDay = "0" + inDay;
		}
		return outDay;
	}

	// private String getDayTomorrow() {
	// GregorianCalendar tomorrow = new
	// GregorianCalendar(TimeZone.getTimeZone("UTC"));
	// tomorrow.add(Calendar.DAY_OF_MONTH, 1);
	// SimpleDateFormat sdf = new SimpleDateFormat("dd");
	// return sdf.format(tomorrow.getTime());
	// }

	// @Test
	// public void testMapDynamicMethod() {
	// Object object = beanRegistration.handleResults2("exercicio11");
	// ObjectPrinter.printClass(object);
	// try {
	// this.mockMvc.perform(get("/testeEW")).andDo(print()).andExpect(status().isOk())
	// .andExpect(content().string("44 anos"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	//
	// @Test
	// public void testAdaptObject() {
	// beanRegistration.handleResults2("exercicio16Adapt1");
	// try {
	// this.mockMvc.perform(get("/endPointAdapt")).andDo(print()).andExpect(status().isOk())
	// .andExpect(content().string("121 dias"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Test
	public void testInsereNovoServicoWeb() {
		try {
			this.mockMvc
					.perform(get("/insere").param("nome", "testeEndPoint30").param("regra", "CalculaAnos")
							.param("data", "15/11/2010").param("index", "30"))
					.andDo(print()).andExpect(status().isOk());

			Thread.sleep(2000);

			this.mockMvc.perform(get("/testeEndPoint30")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string("7 anos"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
