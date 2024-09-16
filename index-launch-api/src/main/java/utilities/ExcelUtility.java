package utilities;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ExcelUtility {


	static final String testCaseFile = System.getProperty("user.dir") + "/src/main/resources/testdata/testData.xlsx";
	//    static String testDataFile = System.getProperty("user.dir") + "/testData.xlsx";
	static final String testDataFile = System.getProperty("user.dir") + "/src/main/resources/testdata/TestData_Template.xlsx";
//		static final String testDataFile = System.getProperty("user.dir") + "/TestData_Template.xlsx";
	public static final Map<String, HashMap<String, Object>> testdata = new HashMap<>();
	//    public static HashMap<Object, Object> values = new  HashMap<Object, Object>();
	public static Map<String, ArrayList<String>> colValues = new HashMap<String, ArrayList<String>>();
	public static ArrayList<String> valuelist = new ArrayList<String>();



	public static void generateTestData(String sheetName) {
		try {
			File f;
			if (sheetName.contains("data")) {
				f = new File(testDataFile);
			} else {
				f = new File(testCaseFile);
			}
			FileInputStream fs = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheet(sheetName);
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
			XSSFRow row = sheet.getRow(0);
			int totalcol = row.getLastCellNum();
			int totalrow = sheet.getLastRowNum();
//            System.out.println(totalcol);
//            System.out.println(totalrow);
			for (int i = 1; i <= totalrow; i++) {
//                System.out.println("Row" + row.getCell(i).toString());
				HashMap<String, Object> values = new HashMap<>();
				for (int j = 0; j < totalcol; j++) {
//                    System.out.println("Col " +sheet.getRow(i).getCell(j).toString());

					if (j == 0) {
						testdata.put(sheet.getRow(i).getCell(0).toString(), values);
					} else {
						XSSFCell cell = sheet.getRow(i).getCell(j);
						Object val = null;
						if (cell.getCellType() == CellType.FORMULA) {
							switch (evaluator.evaluateFormulaCell(cell)) {
							case BOOLEAN:
								// System.out.println("bollean"+cell.getBooleanCellValue());
								val = cell.getBooleanCellValue();
								break;
							case NUMERIC:
								// System.out.println("numeric"+ cell.getNumericCellValue());
								val = cell.getNumericCellValue();
								break;
							case STRING:
								// System.out.println("String"+ cell.getStringCellValue());
								val = cell.getStringCellValue();
								break;
							default:
								break;
							}
							values.put(row.getCell(j).toString(), val);
						}
						else
						{
							values.put(row.getCell(j).toString(), cell);
						}
					}
				}
				valuelist.add(sheet.getRow(i).getCell(0).toString());
			}
			colValues.put(sheet.getRow(0).getCell(0).toString(), valuelist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Please provide the valid input file");
		}
	}

	public static void writeDataToSheet(String sheetName,HashMap<String,ArrayList<String>> cdrArrangementIds) throws IOException {
		int cdrArrangementIdColumnNumber = 0;
		int customerNumberColumnNumber = 0;
		int newAccountColumnNumber=0;
		int consentIdColumnNumber=0;
		try {
			File f = new File(testDataFile);
			FileInputStream fs = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheet(sheetName);
			XSSFRow row = sheet.getRow(0);
			int totalrow = sheet.getLastRowNum();
			int totalcol = row.getLastCellNum();
			for(int j=0;j<totalcol;j++)
			{
				if(sheet.getRow(0).getCell(j).toString().equals("CDRArrangementId"))
				{
					cdrArrangementIdColumnNumber=j;
				}
				if(sheet.getRow(0).getCell(j).toString().equals("CustomerNumber"))
				{
					customerNumberColumnNumber=j;
				}
				if(sheet.getRow(0).getCell(j).toString().equals("NewAccountNumber"))
				{
					newAccountColumnNumber=j;
				}
				if(sheet.getRow(0).getCell(j).toString().equals("ConsentId"))
				{
					consentIdColumnNumber=j;
				}
			}

			for (Map.Entry names : cdrArrangementIds.entrySet()) {
				for (int i = 1; i <= totalrow; i++) {
					if(sheetName.equals("API-Persona data")) {
						if (sheet.getRow(i).getCell(0).toString().equals(names.getKey()) && (sheet.getRow(i).getCell(cdrArrangementIdColumnNumber) + "").toString().equals("") && (sheet.getRow(i).getCell(customerNumberColumnNumber) + "").toString().equals("")) {
							setText(sheetName, i, cdrArrangementIdColumnNumber, cdrArrangementIds.get(names.getKey()).get(0));
							setText(sheetName, i, customerNumberColumnNumber, cdrArrangementIds.get(names.getKey()).get(1));
						//	setText(sheetName, i, newAccountColumnNumber, cdrArrangementIds.get(names.getKey()).get(2));
							break;
						}
					}
					if(sheetName.equals("ConsentTest data")){
						if (sheet.getRow(i).getCell(0).toString().equals(names.getKey()) && (sheet.getRow(i).getCell(consentIdColumnNumber) + "").toString().equals("") ) {
							setText(sheetName, i, consentIdColumnNumber, cdrArrangementIds.get(names.getKey()).get(0));
							break;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  static void setText(String sheetName, int rowNum,int col, String value) throws IOException {
		FileInputStream fio = new FileInputStream(testDataFile);
		XSSFWorkbook wb = new XSSFWorkbook(fio);
		XSSFSheet sheet = wb.getSheet(sheetName);
		XSSFRow row;
		if(rowNum>=0 && col>=0){
			try{
				row = sheet.getRow(rowNum);
				XSSFCellStyle cell1 = row.getCell(col).getCellStyle();
				XSSFCell cell = row.createCell(col);
				cell.setCellStyle(cell1);
				cell.setCellValue(value);
			}catch(Exception e){
				System.out.println("Row Creation Required..");
				row = sheet.createRow(rowNum);
				XSSFCellStyle cell1 = row.getCell(col).getCellStyle();
				XSSFCell cell = row.createCell(col);
				cell.setCellStyle(cell1);
				cell.setCellValue(value);
			}
		}else{
			System.out.println("Plz Enter a positive Row and Column");
		}
		fio.close();
		FileOutputStream  fileOut = new FileOutputStream(testDataFile);
		//write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
	}
}