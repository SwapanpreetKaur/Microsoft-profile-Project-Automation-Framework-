package reporter;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;

import com.appdirect.googleAccess.GoogleDriveAccess;
import com.appdirect.googleAccess.GoogleDriveTest;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import au.com.bytecode.opencsv.CSVReader;

public class PdfReporter {
	private Properties properties;
	private Reporterlog log;
	Document document;
	CsvReporter csvReporter;

	public PdfReporter() {
		csvReporter = new CsvReporter();
		log = new Reporterlog();
		properties = csvReporter.getProperty("resources/res-utils.properties");
	}

	public GoogleDriveAccess csvToPdf(String inputCSVFile, String finalPDFFile) {
		CSVReader csvReader;
		try {
			csvReader = new CSVReader(new FileReader(inputCSVFile));
			String[] nextLine;
			PdfPTable my_first_table = new PdfPTable(12);
			document = new Document();
			PdfWriter writer;
			writer = PdfWriter.getInstance(document, new FileOutputStream(finalPDFFile));
			Rectangle rectangle = new Rectangle(40, 30, 550, 800);
			writer.setBoxSize(properties.getProperty("boxName"), rectangle);
			document.open();
			footer(writer, document);
			document.add(new Paragraph("Addons " + finalPDFFile.replace(".pdf", "")));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			my_first_table.setTotalWidth(new float[] { 130, 600, 1000, 300, 190, 200, 190, 200, 190, 200, 190, 200 });
			PdfPCell table_cell;
			while ((nextLine = csvReader.readNext()) != null) {
				for (int i = 0; i < nextLine.length; i++) {
					table_cell = new PdfPCell(new Phrase(nextLine[i], FontFactory.getFont(FontFactory.HELVETICA, 8)));
					my_first_table.addCell(table_cell);
				}
			}
			document.add(my_first_table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GoogleDriveAccess(finalPDFFile);
	}

	public void footer(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase(properties.getProperty("pdfFooter")), rect.getLeft(), rect.getBottom(), 0);
	}

	public CsvReporter pdfReport() {
		try {
			csvToPdf(properties.getProperty("samePriceCSV"), properties.getProperty("samePricePDF"));//.insertFile();
			log.info("PDF (" + properties.getProperty("samePricePDF") + ") with same prices Created");
			csvToPdf(properties.getProperty("differentPriceCSV"), properties.getProperty("differentPricePDF"));//.insertFile();
			log.info("PDF (" + properties.getProperty("differentPricePDF") + ") with different prices Created");
			new GoogleDriveTest().insertToDrive();
		} catch (Exception e) {
			log.exceptionlog("PDF or Upload was Failed" + e.getLocalizedMessage());
		}
		return new CsvReporter();
	}
}