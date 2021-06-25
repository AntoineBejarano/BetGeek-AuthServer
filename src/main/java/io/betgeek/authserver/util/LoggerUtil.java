package io.betgeek.authserver.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.stereotype.Component;

import io.betgeek.enums.Bookie;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggerUtil {

	public void info(String idTrace, String msg) {
		log.info("[" + idTrace + "] " + msg);
	}

	public void info(String idTrace, String process, String msg) {
		log.info("[" + idTrace + "] " + process + " " + msg);
	}

	public void info(String idTrace, Bookie betHouse, String msg) {
		log.info("[" + idTrace + "] " + "[" + betHouse.toString() + "] " + msg);
	}
	
	public void error(String idTrace, String msg) {
		log.error("[" + idTrace + "] " + msg);
	}

	public void error(String idTrace, String process, String msg) {
		log.error("[" + idTrace + "] " + process + " " + msg);
	}

	public void error(String idTrace, String process, String msg, Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String sStackTrace = sw.toString();
		log.error("[" + idTrace + "] " + process + " " + msg + " [StackTrace]" + sStackTrace);
	}
	
	public void info(String msg) {
		log.info(msg);
	}
	
	public void error(String msg) {
		log.error(msg);
	}
	
	public void warning(String msg) {
		log.warn(msg);
	}
	
	public void warning(String idTrace, String msg) {
		log.warn("[" + idTrace + "] " + msg);
	}

	public void warning(String idTrace, String process, String msg) {
		log.info("[" + idTrace + "] " + process + " " + msg);
	}

}
