package com.work.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private Workbook workBook;
	
	public ExcelUtil(){
		
	}
	
	public void init(String fileName) throws Exception {
		
		//文件验证		
		if(null == fileName || "".equals(fileName.trim())){
			throw new Exception("文件名不正确");
		}
		
		File file = new File(fileName);
		if(!file.exists() || !file.isFile()){
			throw new Exception("文件不存在");
		}
		
		InputStream fis = new FileInputStream(file);
		if (!fis.markSupported()) {
			fis = new PushbackInputStream(fis, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(fis)) {
			workBook = new HSSFWorkbook(fis);
		}
		if (POIXMLDocument.hasOOXMLHeader(fis)) {
			workBook = new XSSFWorkbook(OPCPackage.open(fis));
		}
		
		if(workBook == null){
			throw new IllegalArgumentException("你的excel版本目前poi解析不了");
		}
	}
	
	public List<String[][]> getData(int ignoreRows) throws Exception {
		if(null == workBook){
			throw new Exception("没有获取到数据文件对象");
		}
		
		List<String[][]> list = new ArrayList<String[][]>();
		
		//遍历工作列表sheet  
		for(int sheetIndex = 0; sheetIndex < workBook.getNumberOfSheets(); sheetIndex++){
			Sheet sheet = workBook.getSheetAt(sheetIndex);
			int rowSize = 0;
			List<String[]> result = new ArrayList<String[]>();
			
			//遍历行数
			for(int rowIndex = (ignoreRows > 0 ? ignoreRows : 0); rowIndex <= sheet.getLastRowNum(); rowIndex++){
				Row row = sheet.getRow(rowIndex);
				if(null == row){
					continue;
				}
				
				//最大列数
				int tempRowSize = row.getLastCellNum() + 1;
				if(tempRowSize > rowSize){
					rowSize = tempRowSize;
				}
				
				String[] values = new String[rowSize];
				Arrays.fill(values, "");//将""赋值给values的每个元素
				boolean hasValue = false;
				//遍历一行的列数
				for(short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++){
					String value = "";
					Cell cell = row.getCell(columnIndex);
					if(cell != null){
						switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if(DateUtil.isCellDateFormatted(cell)){
									Date date = cell.getDateCellValue();
									if(null != date){
										value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
									}else{
										value = "";
									}
								}else {
									value = new DecimalFormat("0").format(cell.getNumericCellValue());
								}
								break;
							case Cell.CELL_TYPE_FORMULA:
								//导入时如果为公式生成的数据则无值
								if(!"".equals(cell.getStringCellValue())){
									value = cell.getStringCellValue();
								}else {
									value = cell.getNumericCellValue() + "";
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								break;
							case Cell.CELL_TYPE_ERROR:
								value = "";
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								value = cell.getBooleanCellValue() ? "Y" : "N";
								break;
							default:
								value = "";
								break;
						}
						if(columnIndex == 0 && "".equals(value.trim())){
							break;
						}
						values[columnIndex] = rightTrim(value);
						hasValue = true;
					}
				}
				
				if(hasValue){
					result.add(values);
				}
			}
			
			String[][] tempArray = new String[result.size()][rowSize];
			for(int i = 0; i < tempArray.length; i++){
				tempArray[i] = result.get(i);
			}
			list.add(tempArray);
		}
		
		return list;
	}
	
	/**
     * 去掉字符串右边的空格
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
     private String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }

	
	public static void main(String[] args) {
		ExcelUtil eu = new ExcelUtil();
		try {
			eu.init("/Users/King/penggq/workspace/test//1.xlsx");
			List<String[][]> list = eu.getData(0);
			for(String[][] s : list){
				for(int i =0; i < s.length; i++){
					for(int j =0; j < s[i].length; j++){
						if(j > 0){
							System.out.print("    ");
						}
						System.out.print(s[i][j]);
					}
					System.out.println("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
