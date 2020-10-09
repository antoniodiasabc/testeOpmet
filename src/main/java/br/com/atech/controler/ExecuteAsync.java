package br.com.atech.controler;

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

import br.com.atech.vo.MetarEstatistica;

@Configuration
@EnableAsync
public class ExecuteAsync {

	Logger logger = LoggerFactory.getLogger(ExecuteAsync.class);

	@Autowired
	ClientWebservice1 beanRegistration;

	@Async
	public void asyncMethodWithVoidReturnType() {
		logger.warn("Execute method asynchronously. " + Thread.currentThread().getName());
	}

	@Async
	public void testeMetarL1() {
		List<MetarEstatistica> metarRX = createMetarRXSABE();
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
		List<MetarEstatistica> metarRX = createMetarTXSBBR();
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
		List<MetarEstatistica> metarRX = createMetarTXSBCT();
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

	private List<MetarEstatistica> createMetarTXSBBR() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SBBR", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 02:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 05:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 06:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 08:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 18:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SBBR", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 19:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SBBR", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 20:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarTXSBCT() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBCT", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SBCT", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SBCT", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarTXSBRF() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBRF", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SBRF", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 09:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 14:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SBRF", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	@Async
	public void testeMetarL2() {
		List<MetarEstatistica> metarRX = createMetarRXSAVC();
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
		List<MetarEstatistica> metarRX = createTAFRXTeste();
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
		List<MetarEstatistica> metarRX = createTAFRXTesteL2();
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
	public void testeSigmet() {
		List<MetarEstatistica> sigmetWSTeste = createSigmetWSTeste();
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
	public void testeAirep() {
		List<MetarEstatistica> sigmetWSTeste = createAirepTeste();
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
	public void testeSPECI() {
		List<MetarEstatistica> sigmetWSTeste = createSPECITeste();
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

	private List<MetarEstatistica> createMetarRXSABE() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 04:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 05:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 09:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 10:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarRXSAVC() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SAVC", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SAVC", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 04:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 05:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 09:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SAVC", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 10:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarRXSVMI() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SVMI", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 14:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SVMI", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 10:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SVMI", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 15:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarRXSUAA() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SUAA", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 01:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 04:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 10:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SUAA", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SUAA", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarRXSUCA() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SUCA", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 01:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SUCA", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 10:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SUCA", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 15:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarRXSCEL() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SCEL", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 01:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SCEL", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 09:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SCEL", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 10:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 11:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 15:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createTAFRXTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createTAFRXTesteL2() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SAVC", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SAVC", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SAVC", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SAVC", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SAVC", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SAVC", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createSigmetWSTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", 3, "WS"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", 2, "WC"));
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", 2, "WV"));

		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", 2, "WS"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", 3, "WC"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", 3, "WV"));

		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", 2, "WS"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", 3, "WC"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", 2, "WV"));

		return values;
	}

	private List<MetarEstatistica> createAirepTeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", 1, "UA"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", 4, "UA"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", 4, "UA"));
		return values;
	}

	private List<MetarEstatistica> createSPECITeste() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SABE", "02/09/2019 00:00", 4, "SP"));
		values.add(new MetarEstatistica("SABE", "03/09/2019 00:00", 2, "SP"));
		values.add(new MetarEstatistica("SABE", "04/09/2019 00:00", 1, "SP"));
		return values;
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
		List<MetarEstatistica> metarRX = createMetarRXSVMI();
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
		List<MetarEstatistica> metarRX = createMetarRXSUAA();
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
		List<MetarEstatistica> metarRX = createMetarRXSUCA();
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
		List<MetarEstatistica> metarRX = createMetarRXSCEL();
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
		List<MetarEstatistica> metarRX = createTAFRXTesteL3();
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
		List<MetarEstatistica> metarRX = createTAFRXTesteL4();
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
		List<MetarEstatistica> metarRX = createTAFRXTesteL5();
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
		List<MetarEstatistica> metarRX = createTAFRXTesteL6();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL1();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL2();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL3();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL4();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL5();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL6();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL7();
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
		List<MetarEstatistica> metarRX = createTafTXTesteL8();
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

	private List<MetarEstatistica> createTAFRXTesteL3() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SVMI", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SVMI", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SVMI", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SVMI", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SVMI", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SVMI", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createTAFRXTesteL4() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SUAA", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUAA", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SUAA", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUAA", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SUAA", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUAA", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createTAFRXTesteL5() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SUCA", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUCA", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SUCA", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUCA", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SUCA", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SUCA", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	private List<MetarEstatistica> createTAFRXTesteL6() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SCEL", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SCEL", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SCEL", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SCEL", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SCEL", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SCEL", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL1() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBBR", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBBR", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBBR", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBBR", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBBR", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBBR", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL2() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBCT", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBCT", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBCT", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBCT", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBCT", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBCT", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL3() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBRF", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBRF", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBRF", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBRF", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBRF", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBRF", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL4() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBMN", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBMN", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBMN", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL5() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBPR", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBPR", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBPR", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBPR", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBPR", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBPR", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBPR", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBPR", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBPR", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBPR", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBPR", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBPR", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL6() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBAU", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAU", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAU", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAU", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBAU", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAU", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBAU", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAU", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBAU", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAU", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBAU", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAU", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL7() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBAC", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAC", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAC", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAC", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBAC", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAC", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBAC", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAC", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBAC", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBAC", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBAC", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBAC", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	public List<MetarEstatistica> createTafTXTesteL8() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new MetarEstatistica("SBTF", "02/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBTF", "02/09/2019 06:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBTF", "02/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBTF", "02/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBTF", "03/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBTF", "03/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBTF", "03/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBTF", "03/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));

		values.add(new MetarEstatistica("SBTF", "04/09/2019 00:00", "EXCELENTE", "FT"));
		values.add(new MetarEstatistica("SBTF", "04/09/2019 06:00", "AUSÊNCIA", "FT"));
		values.add(new MetarEstatistica("SBTF", "04/09/2019 12:00", "ATRASADO_MAIS_1H", "FT"));
		values.add(new MetarEstatistica("SBTF", "04/09/2019 18:00", "ATRASADO_MAIS_1H", "FT"));
		return values;
	}

	@Async
	public void testeMetarTXL3() {
		List<MetarEstatistica> metarRX = createMetarTXSBRF();
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
		List<MetarEstatistica> metarRX = createMetarTXSBMN();
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
		List<MetarEstatistica> metarRX = createMetarTXSBPR();
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
		List<MetarEstatistica> metarRX = createMetarTXSBAU();
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
		List<MetarEstatistica> metarRX = createMetarTXSBAC();
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
		List<MetarEstatistica> metarRX = createMetarTXSBTF();
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

	private List<MetarEstatistica> createMetarTXSBTF() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 01:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 06:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 14:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 16:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 21:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 02:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 07:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 14:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 15:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 17:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 22:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 03:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 08:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 16:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 18:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBTF", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarTXSBAC() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 01:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 06:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 14:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 16:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 21:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 02:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 07:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 08:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 09:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 17:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 22:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 03:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 08:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 18:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAC", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarTXSBMN() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new MetarEstatistica("SBMN", "02/09/2019 00:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 06:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 09:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 11:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 14:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 16:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 21:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new MetarEstatistica("SBMN", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 02:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 07:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 09:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 12:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 14:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 17:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 22:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new MetarEstatistica("SBMN", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 03:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 08:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 11:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 13:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 15:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 18:00", "MENOR_5"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new MetarEstatistica("SBMN", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}

	private List<MetarEstatistica> createMetarTXSBPR() {
		List<MetarEstatistica> values = new ArrayList<>();

		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 01:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 06:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 07:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 08:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 09:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 14:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 16:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 19:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 21:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 02:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 07:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 10:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 15:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 17:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 20:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 22:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 03:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 06:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 08:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 15:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 18:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 21:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBPR", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}
	

	private List<MetarEstatistica> createMetarTXSBAU() {
		List<MetarEstatistica> values = new ArrayList<>();
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 00:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 01:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 02:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 03:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 04:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 06:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 07:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 08:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 09:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 10:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 12:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 13:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 14:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 15:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 16:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 17:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 18:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 19:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 20:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 21:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 22:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "02/09/2019 23:00", "ENTRE_10_30"));

		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 00:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 01:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 02:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 03:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 04:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 05:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 06:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 07:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 08:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 09:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 10:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 11:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 12:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 13:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 14:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 15:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 16:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 17:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 18:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 19:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 20:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 21:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 22:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "03/09/2019 23:00", "ENTRE_5_10"));

		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 00:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 01:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 02:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 03:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 04:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 05:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 06:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 07:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 08:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 09:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 10:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 11:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 12:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 13:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 14:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 15:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 16:00", "MAIOR_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 17:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 18:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 19:00", "ENTRE_5_10"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 20:00", "ENTRE_10_30"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 21:00", "MENOR_5"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 22:00", "AUSÊNCIA"));
		values.add(new  MetarEstatistica ( "SBAU", "04/09/2019 23:00", "MENOR_5"));

		return values;
	}
}
