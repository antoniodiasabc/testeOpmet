package br.com.atech.controler;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ExecutorEstatistico {

	Logger logger = LoggerFactory.getLogger(ExecutorEstatistico.class);

	// @Autowired
	// ExecuteAsyncDEV execucao;

	@Autowired
	ExecuteAsyncQA execucao;

	@Async("threadPoolTaskExecutor")
	public void asyncMethodWithConfiguredExecutor() {
		logger.warn("Execute method with configured executor - " + Thread.currentThread().getName());

//		int x = 0;
//		while (x < 50000) {
//			execucao.testeWebServiceRedemet("[" + x + "]");
//			x++;
//		}

		execucao.testeMensagensIWXXMTAC();
		execucao.searchIWXXMTest();

		// mensagens Recebidas
		// execucao.testeMetarL1();
		// execucao.testeTafL1();
		// execucao.testeSPECIL1();
		// execucao.testeSigmetL1();
		// execucao.testeAirepL1();
		//
		// execucao.testeMetarL2();
		// execucao.testeTafL2();
		// execucao.testeSPECIL2();
		// execucao.testeSigmetL2();
		// execucao.testeAirepL2();
		//
		// execucao.testeMetarL3();
		// execucao.testeTafL3();
		// execucao.testeSPECIL3();
		// execucao.testeSigmetL3();
		// execucao.testeAirepL3();
		//
		// execucao.testeMetarL4();
		// execucao.testeTafL4();
		//
		// execucao.testeMetarL5();
		// execucao.testeTafL5();
		//
		// execucao.testeMetarL6();
		// execucao.testeTafL6();
		// execucao.testeSPECIL6();
		// execucao.testeSigmetL6();
		// execucao.testeAirepL6();
		//
		// try {
		// Thread.sleep(550);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// //mensagens transmitidas
		// execucao.testeMetarTXL1();
		// execucao.testeTafTXL1();
		//
		// execucao.testeMetarTXL2();
		// execucao.testeTafTXL2();
		//
		// execucao.testeMetarTXL3();
		// execucao.testeTafTXL3();
		//
		// execucao.testeMetarTXL4();
		// execucao.testeTafTXL4();
		//
		// execucao.testeMetarTXL5();
		// execucao.testeTafTXL5();
		//
		// execucao.testeMetarTXL6();
		// execucao.testeTafTXL6();
		//
		// execucao.testeMetarTXL7();
		// execucao.testeTafTXL7();
		//
		// execucao.testeMetarTXL8();
		// execucao.testeTafTXL8();
		//
		// execucao.testeSigmetTXL9();
		// execucao.testeAirepTXL9();
	}

	@Bean(name = "threadPoolTaskExecutor")
	public Executor threadPoolTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
