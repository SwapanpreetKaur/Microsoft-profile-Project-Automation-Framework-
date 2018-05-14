package reporter;

import com.appdirect.googleAccess.Editions;
import org.testng.Reporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GoogleSheetToCsv {
	private String CommaSeparator = ",";
	private String NewLineSeparator = "\n";
	private String Space = "";
	private String FILE_HEADER = "SrNo,Offer Display Name,Offer ID,Secondary License Type,USD,EUR,AUD,GBP,App Id";

	public GoogleSheetToCsv csvgenerate(HashMap<String, Editions> skuSet)

	{
		Iterator<Entry<String, Editions>> iterator = skuSet.entrySet().iterator();
		FileWriter fileWriter = null;
		int count = 0;

		try {

			fileWriter = new FileWriter("SheetToCsv.csv");
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NewLineSeparator);

			while (iterator.hasNext()) {
				Entry<String, Editions> pair = iterator.next();
				Editions ed = pair.getValue();
				if (!ed.getappId().equals("NA")) {

					fileWriter.append(String.valueOf(++count));
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getEditionName().replaceAll(CommaSeparator, Space));
					fileWriter.append(CommaSeparator);
					fileWriter.append(String.valueOf(ed.getOfferId()));
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getSkuType());
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getusdPrice());
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.geteurPrice());
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getaudPrice());
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getgbpPrice());
					fileWriter.append(CommaSeparator);
					fileWriter.append(ed.getappId());
					fileWriter.append(NewLineSeparator);
				}
			}

			Reporter.log("CSV file was created successfully !!!", true);
		} catch (Exception e) {
			Reporter.log("Error in CsvFileWriter !!!", true);
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				Reporter.log("Error while flushing/closing fileWriter !!!", true);
				e.printStackTrace();
			}
		}
		return new GoogleSheetToCsv();
	}
}
