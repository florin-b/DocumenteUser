package documente.impdocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import documente.beans.SinteticDocument;
import documente.model.OperatiiDocumente;

public class ImportDocs {

	public void importFromExcel() throws IOException {
		
		List<SinteticDocument> listSintetice = new ArrayList<>();

		//File myFile = new File("d://temp/Doc_calitate/docs_03-09.xlsx");
		
		File myFile = new File("d://temp/Doc_calitate/docs_11.xlsx");
		FileInputStream fis = new FileInputStream(myFile);

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();

		// Traversing over each row of XLSX file
		int nrCol = 0;
		String tipDoc = "";
		String sintetic = "";
		List<String> listTips = new ArrayList<>();
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (!sintetic.isEmpty() && !tipDoc.isEmpty()) {
				
				SinteticDocument sinteticDoc = new SinteticDocument();
				sinteticDoc.setCodSintetic(sintetic);
				sinteticDoc.setTipuriDocumente(listTips);
				listSintetice.add(sinteticDoc);
				
				
				
			}

			nrCol = 0;
			tipDoc = "";
			sintetic = "";
			listTips = new ArrayList<>();

			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {

				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:

					if (cell.getStringCellValue().startsWith("B"))
						sintetic = cell.getStringCellValue();
					else 
						if (cell.getStringCellValue().equals("X")) {
						
						
						listTips.add(String.valueOf((nrCol - 2)));
						
						if (tipDoc.isEmpty())
							tipDoc = (nrCol - 1) + "";
						else
							tipDoc += "," + (nrCol - 1) + "";
					}
					break;
				case Cell.CELL_TYPE_NUMERIC:
					sintetic = String.valueOf((int) cell.getNumericCellValue());
					break;

				default:

				}


				nrCol++;
			}
			
		}

		myWorkBook.close();
		
		System.out.println(listSintetice);
		
		insertTipDocument(listSintetice);

	}
	
	
	
	private void insertTipDocument(List<SinteticDocument> listSintetice){
		
		OperatiiDocumente opDoc = new OperatiiDocumente();
		
		for (SinteticDocument sintDoc : listSintetice){
			
			
			if (!sintDoc.getCodSintetic().startsWith("4")) {
				
				for (String  tipDoc : sintDoc.getTipuriDocumente())
					opDoc.adaugaTipDocSintetic(sintDoc.getCodSintetic(), tipDoc);
				
			}
				
			
		}
		
	}

	private boolean isNumber(String letter) {

		try {

			Integer.parseInt(letter);

		} catch (Exception ex) {
			return false;
		}

		return true;
	}

}
