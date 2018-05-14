package reporter;

import java.sql.Timestamp;

import org.testng.Reporter;

public class Reporterlog {

	public void info(String msg) {
		Reporter.log(new Timestamp(System.currentTimeMillis()) + "\t[Info]\t" + msg, true);
	}

	public void error(String msg) {
		Reporter.log(new Timestamp(System.currentTimeMillis()) + "\t[Error]\t" + msg, true);
	}

	public void warning(String msg) {
		Reporter.log(new Timestamp(System.currentTimeMillis()) + "\t[Warning]\t" + msg, true);
	}

	public void exceptionlog(String msg) {
		Reporter.log(new Timestamp(System.currentTimeMillis()) + "\t[Exception]\t" + msg, true);
	}

}
