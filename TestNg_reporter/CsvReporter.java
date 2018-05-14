package reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.appdirect.googleAccess.EditionPriceDomain;
import com.appdirect.pages.ProfilePages;

public class CsvReporter {
    private String CommaSeparator = ",";
    private String NewLineSeparator = "\n";
    private String Space = "";
    Reporterlog log;
    private String FILE_HEADER = "SrNo,OfferId,EditionName,Type,USD,,EUR,,AUD,,GBP,";
    private String SUB_FILE_HEADER = ",,,,OLD,NEW,OLD,NEW,OLD,NEW,OLD,NEW";
    private Properties properties;
    private FileWriter fileWriter;
    public CsvReporter() {
        properties = getProperty("resources/res-utils.properties");
        this.log = new Reporterlog();
    }
    public CsvReporter csvSamePrice() {
        int count = 0;
        try {
            fileWriter = new FileWriter(properties.getProperty("samePriceCSV"));
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NewLineSeparator);
            fileWriter.append(SUB_FILE_HEADER.toString());
            fileWriter.append(NewLineSeparator);
            for (EditionPriceDomain editions : ProfilePages.samePricesLists) {
                fileWriter.append(String.valueOf(++count));
                fileWriter.append(CommaSeparator);
                fileWriter.append(String.valueOf(editions.getOfferid()));
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getEditionName().replaceAll(CommaSeparator, Space));
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getSkutype());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldusdPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewusdPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldeurPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNeweurPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldgbpPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewgbpPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldaudPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewaudPrice());
                fileWriter.append(NewLineSeparator);
            }
            log.info("CSV_file (" + properties.getProperty("samePriceCSV") + ") was created successfully !!!");
        } catch (Exception e) {
            log.error("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                log.error("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
        return new CsvReporter();
    }
    public CsvReporter csvDifferentPrice() {
        int count = 0;
        try {
            fileWriter = new FileWriter(properties.getProperty("differentPriceCSV"));
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NewLineSeparator);
            fileWriter.append(SUB_FILE_HEADER.toString());
            fileWriter.append(NewLineSeparator);
            for (EditionPriceDomain editions : ProfilePages.differentPricesLists) {
                fileWriter.append(String.valueOf(++count));
                fileWriter.append(CommaSeparator);
                fileWriter.append(String.valueOf(editions.getOfferid()));
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getEditionName().replaceAll(CommaSeparator, Space));
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getSkutype());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldusdPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewusdPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldeurPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNeweurPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldgbpPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewgbpPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getOldaudPrice());
                fileWriter.append(CommaSeparator);
                fileWriter.append(editions.getNewaudPrice());
                fileWriter.append(NewLineSeparator);
            }
            log.info("CSV_file (" + properties.getProperty("differentPriceCSV") + ") was created successfully !!!");
        } catch (Exception e) {
            log.error("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                log.error("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
        return new CsvReporter();
    }
    
    public Properties getProperty(String resource) {
        try {
            File file = new File(resource);
            FileInputStream fileInput = new FileInputStream(file);
            properties = new Properties();
            properties.load(fileInput);
        } catch (Exception e) {
            log.exceptionlog("Property file load exception");
        }
        return properties;
    }
}