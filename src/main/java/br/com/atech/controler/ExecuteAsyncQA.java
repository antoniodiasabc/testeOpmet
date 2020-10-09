package br.com.atech.controler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import br.atech.teste.GetMessage;
import br.com.atech.data.QaData;
import br.com.atech.vo.Aerodromo;
import br.com.atech.vo.MetarEstatistica;
import br.com.atech.vo.Observacao;

@Configuration
@EnableAsync
public class ExecuteAsyncQA {

	Logger logger = LoggerFactory.getLogger(ExecuteAsyncQA.class);

	@Autowired
	ClientWebservice1 beanRegistration;

	@Autowired
	ClientWebservice2 servicoWeb;

	@Async
	public void asyncMethodWithVoidReturnType() {
		logger.warn("Execute method asynchronously. " + Thread.currentThread().getName());
	}

	ExecuteAsyncQA() {
		System.out.println("construtor de ExecuteAsyncQA " + ExecuteAsyncQA.class.getName());
	}

	@Async
	public void searchIWXXMTest() {
		String result = servicoWeb.sendQAIWXXMSearch("SBSP", "METAR", "2020100115", "2020100215");
		String fileName = saveFile(result);
		servicoWeb.sendQAIWXXMInsertFile2(fileName);
	}

	private String saveFile(String result) {
		String fileName = "/home/opmet/iwxxmout/A_LABR01SBBR211100_C_SBBR_20200921121938.xml";
		saveFile2(result, fileName);
		return fileName;
	}


	public static void saveFile2(final String conteudo, String path) {
		FileWriter fw = null;
		try {
			final File file = new File(path);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			fw = new FileWriter(file);
			fw.write(conteudo);
		} catch (final Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@Async
	public void testeMensagensIWXXMTAC() {
		insereMensagemPorWebService("METAR SBSP 221200Z 16012KT 9000 -DZ BKN007 OVC008 14/13 Q1025=");

		GetMessage msgObservacao = new GetMessage();

		java.util.List<Aerodromo> localidades = msgObservacao.getLocalidades();
		java.util.List<Observacao> message;

		message = msgObservacao.getMessageFir();
		for (Observacao observacao : message) {
			sendMessageIWXXM(observacao);
		}

		for (Aerodromo aerodromo : localidades) {
			message = msgObservacao.getMessage(aerodromo);
			for (Observacao observacao : message) {
				sendMessageIWXXM(observacao);
			}
		}
		System.out.println("ja inseriu ");
		System.exit(0);
	}

	@Async
	public void testeMensagensRedemet() {

		GetMessage msgObservacao = new GetMessage();

		java.util.List<Aerodromo> localidades = msgObservacao.getLocalidades();
		java.util.List<Observacao> message;

		message = msgObservacao.getMessageFir();
		for (Observacao observacao : message) {
			sendMessageRedemet(observacao);
		}

		for (Aerodromo aerodromo : localidades) {
			message = msgObservacao.getMessage(aerodromo);
			for (Observacao observacao : message) {
				sendMessageRedemet(observacao);
			}
		}
	}

	@Async
	public void testeWebServiceRedemet(String msg) {
		testaRequisicaoRedemetTAC(msg);
	}

	private void testaRequisicaoRedemetTAC(String msg) {
		beanRegistration.testeWebserviceRedemet2(msg);
		// beanRegistration.testeWebserviceRedemet(msg);
	}

	private void sendMessageRedemet(Observacao observacao) {
		try {
			if (!observacao.getMsg().contains("REDEMET")) {
				String msg = observacao.getMsg();
				if (msg.startsWith("SQL")) {

				} else {
					if (msg.endsWith("=")) {
						insereMensagem(msg);
					} else {
						insereMensagem(msg + "=");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	private void sendMessageIWXXM(Observacao observacao) {
		try {
			if (!observacao.getMsg().contains("REDEMET")) {
				String msg = observacao.getMsg();
				if (msg.startsWith("SQL")) {

				} else {
					if (msg.endsWith("=")) {
						insereMensagemPorWebService(msg);
					} else {
						insereMensagemPorWebService(msg + "=");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Async
	public void testeMetarL1() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSABE();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL1() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBBR();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL2() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBCT();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarL2() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSAVC();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafL1() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTeste();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafL2() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTesteL2();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeSigmetL1() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSigmetWSTesteL1();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSigmetL2() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSigmetWSTesteL2();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSigmetL3() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSigmetWSTesteL3();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSigmetL6() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSigmetWSTesteL6();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeAirepL1() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createAirepTesteL1();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeAirepL2() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createAirepTesteL2();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeAirepL3() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createAirepTesteL3();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeAirepL6() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createAirepTesteL6();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSPECIL1() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSPECITesteL1();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSPECI(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	private void insereTAF(MetarEstatistica valor) {
		int horaref = getHoraTafReferencia();
		String body = "FTBZ01 SBBR " + getDayToday() + "1503\nTAF " + valor.getLocalidade() + " "
				+ getDiaHorafReferenciaMin(valor.getData()) + "Z " + getDiaHorafReferencia(valor.getData()) + "/"
				+ getDiaHoraMenosMais(horaref + 18) + " 24005KT 8000 TSRA BKN015 FEW035CB TN01/"
				+ getDiaHoraMenosMais(horaref + 1) + "Z TX20/" + getDiaHoraMenosMais(horaref + 14) + "Z " + " BECMG "
				+ getDiaHoraMenosMais(horaref + 2) + "/" + getDiaHoraMenosMais(horaref + 4) + " 6000 RA BKN010 "
				+ " BECMG " + getDiaHoraMenosMais(horaref + 6) + "/" + getDiaHoraMenosMais(horaref + 8)
				+ " 9999 BKN013 " + " BECMG " + getDiaHoraMenosMais(horaref + 10) + "/"
				+ getDiaHoraMenosMais(horaref + 12) + " 15010KT BKN002 RMK PGG=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
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
		logger.warn("hora ref sigmet " + horaref);
		String body = metarEstatistica.getIdMeteor() + "BZ " + metarEstatistica.getLocalidade() + " "
				+ getHoraReferencia() + "\n" + metarEstatistica.getLocalidade() + " SIGMET " + num + " VALID "
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
		String body = "UABZ01 " + valor.getLocalidade() + " " + getDiaHorafReferenciaMin(valor.getData()) + "\nARP "
				+ getDiaHoraMenosMais(1) + " /// UAE2" + num + " 0607S 01018W "
				+ getDiaHorafReferenciaMin(valor.getData()) + " F340 MS430 055/046 TB/ S/3/=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void obterDataEnvioWS(MetarEstatistica metarEstatistica) {
		GregorianCalendar horaEnvioMetar = getDiaHoraEnvioTAF(0, metarEstatistica.getData());
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

	private GregorianCalendar getDiaHoraEnvioMetar(int somaMin, String dataRef) {
		GregorianCalendar horaReferencia = getHoraTafReferencia(dataRef);
		GregorianCalendar envio = (GregorianCalendar) horaReferencia.clone();
		envio.add(Calendar.MINUTE, somaMin);
		return envio;
	}

	private GregorianCalendar getDiaHoraEnvioTAF(int somaH, String dataRef) {
		GregorianCalendar horaReferencia = getHoraTafReferencia(dataRef);
		GregorianCalendar envio = (GregorianCalendar) horaReferencia.clone();
		envio.add(Calendar.HOUR_OF_DAY, somaH);
		envio.add(Calendar.MINUTE, somaH);
		return envio;
	}

	private boolean isInTime(MetarEstatistica valor) {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		if (today.after(valor.getDataenvio())) { // esta na hora de enviar
			logger.warn("hora de envio de msg " + valor.getLocalidade() + " data " + valor.getData() + " status: "
					+ valor.getStatus());
			return true;
		}
		return false;
	}

	private void insereMetar(MetarEstatistica valor) {
		String body = "METAR " + valor.getLocalidade() + " " + valor.getDiaHoraMetar()
				+ "00Z 19005KT CAVOK 26/19 Q1020=";
		try {
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insereMensagem(String body) {
		try {
			System.out.println("-- antes [" + body + "]");
			beanRegistration.testeHttpsv1(body);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void insereMensagemPorWebService(String body) {
		try {
			servicoWeb.sendQAIWXXMInsert(body);
			System.out.println("[" + body + "]");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
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

	private String getHoraReferencia() {
		GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		String diaHora = getDay(today.get(Calendar.DAY_OF_MONTH)) + getDay(today.get(Calendar.HOUR_OF_DAY))
				+ getDay(today.get(Calendar.MINUTE));
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

	private String getDayToday() {
		GregorianCalendar tomorrow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(tomorrow.getTime());
	}

	private String getDiaHoraMenosMais(int total) {
		GregorianCalendar tomorrow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		// logger.warn(" 1o " + tomorrow.get(Calendar.HOUR_OF_DAY));
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

	private String getDay(int inDay) {
		String outDay = inDay + "";
		if (inDay < 10) {
			outDay = "0" + inDay;
		}
		return outDay;
	}

	@Async
	public void testeMetarL3() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSVMI();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}

	}

	@Async
	public void testeMetarL4() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSUAA();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}

	}

	@Async
	public void testeMetarL5() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSUCA();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}

	}

	@Async
	public void testeMetarL6() {
		List<MetarEstatistica> metarRX = QaData.createMetarRXSCEL();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}

	}

	@Async
	public void testeTafL3() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTesteL3();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafL4() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTesteL4();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafL5() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTesteL5();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafL6() {
		List<MetarEstatistica> metarRX = QaData.createTAFRXTesteL6();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL1() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL1();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL2() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL2();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL3() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL3();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL4() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL4();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL5() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL5();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL6() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL6();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL7() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL7();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeTafTXL8() {
		List<MetarEstatistica> metarRX = QaData.createTafTXTesteL8();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvioTAF(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());

			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereTAF(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL3() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBRF();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL4() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBMN();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL5() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBPR();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL6() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBAU();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL7() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBAC();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeMetarTXL8() {
		List<MetarEstatistica> metarRX = QaData.createMetarTXSBTF();
		for (MetarEstatistica metarEstatistica : metarRX) {
			obterDataEnvio(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			if (metarEstatistica.getStatus() != 0) { // se status eh 0 = ausente - nao enviar
				insereMetar(metarEstatistica);
			} else {
				logger.warn(" ausente nao enviar " + metarEstatistica.toString());
			}
		}
	}

	@Async
	public void testeSPECIL2() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSPECITesteL2();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSPECI(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSPECIL3() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSPECITesteL3();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSPECI(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSPECIL6() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSPECITesteL6();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSPECI(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeSigmetTXL9() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createSigmetWSTesteL9();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereSigmet(metarEstatistica, total + 1);
				total++;
			}
		}
	}

	@Async
	public void testeAirepTXL9() {
		List<MetarEstatistica> sigmetWSTeste = QaData.createAirepTesteL9();
		for (MetarEstatistica metarEstatistica : sigmetWSTeste) {
			obterDataEnvioWS(metarEstatistica);
			while (isInTime(metarEstatistica) == false) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.warn(" esta na hora de enviar msg " + metarEstatistica.toString());
			int total = 0;
			while (total < metarEstatistica.getQtdMsg()) {
				insereAIREP(metarEstatistica, total + 1);
				total++;
			}
		}
	}

}
