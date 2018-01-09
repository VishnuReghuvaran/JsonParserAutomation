package com.att.core.json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {

	Logger LOGGER = LogManager.getLogger(JsonParser.class.getName());

	public static void main(String args[]) {
		JsonParser jsonParser=new JsonParser();
		jsonParser.mapExcelToJava(args[0]);
	}

	/**
	 * This is to map excel file data to java objects
	 */
	private void mapExcelToJava(String path) {
		LOGGER.debug("Inside mapping excle values to Java");
		InputStream excelFileToRead = null;
		int cellCount = 0;
		int rowCount = 0;
		int rowNumber = 0;
		try {
			excelFileToRead = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(excelFileToRead);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();
		Map<String,Account> accountsMap=new HashMap<String,Account>();
		// Iterate the rows
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			// Iterate the cells
			Account accout=new Account();
			while (cells.hasNext()) {
				cell = (HSSFCell) cells.next();
				String key="";
				String value="";
				if (cellCount == 1 &&  cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    key=cell.getStringCellValue();
				}else if(cellCount == 1 &&  cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					String id = String.valueOf(cell.getNumericCellValue());
					key=id.trim().substring(0, id.indexOf("."));					
				}				
				if (cellCount == 2 &&  cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    value=cell.getStringCellValue();
				}
				Gson gson=new Gson();
				accout=gson.fromJson(value, Account.class); 
				accountsMap.put(key, accout);
			}
		}
	}

}
