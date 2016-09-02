package com.work.common.demo.poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxDemo {

	public static void main(String[] args) throws IOException {
		XlsxDemo xlsxMain = new XlsxDemo();

		xlsxMain.readXlsx();
		//xlsxMain.writeXls();
	}

	public void readXlsx() throws IOException {
		String fileName = "d:\\data_temp\\内容识别详细数据表20141118.xlsx";
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(
				fileName));

		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}

				// 循环列Cell
				for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					
					if (xssfCell == null) {
						continue;
					}
//					xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
//					System.out.print("   " + String.valueOf(xssfCell.getStringCellValue()));
					System.out.print("   " + getValue(xssfCell));
				}
				System.out.println();
			}
		}
	}
	
	public void writeXls() throws IOException {
		SXSSFWorkbook wb = new SXSSFWorkbook(100);
		Sheet sh = wb.createSheet();
		for (int rownum = 0; rownum < 1000; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < 10; cellnum++) {
				Cell cell = row.createCell(cellnum);
				String address = new CellReference(cell).formatAsString();
				cell.setCellValue(address);
			}

		}

		FileOutputStream out = new FileOutputStream("d:\\workbook.xlsx");
		wb.write(out);
		out.close();

		// dispose of temporary files backing this workbook on disk
		wb.dispose();
	}
	
	public void update(){
		String fileName = "D:\\data_temp\\奔腾.东芝.飞科.好记星.华光.卡西欧.神舟.爱普.尼康.新科.漫步者.先锋.樱桃.小鸭.金士顿.爱国者.魅族.昂达.灿坤.3M.博朗.xlsx";
		InputStream inp;
        try {
            inp = new FileInputStream(fileName);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i < 3203; i++) {
            	Row row = sheet.getRow(i);
            	if(row==null){
            		break;
            	}
            	//将ingdex为2的数据复制到index12
                Cell cell = getCell(row,2);
                String str = cell.getStringCellValue();
                cell = getCell(row,12);
				cell.setCellType(Cell.CELL_TYPE_STRING);
            	cell.setCellValue(str);
//                if (i==40) {
//					break;
//				}
			}

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	private Cell getCell(Row row,int cellNum){
		Cell cell = row.getCell(cellNum);
		if(cell==null){
			cell = row.createCell(cellNum);
		}
		return cell;
	}

	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfCell) {
		if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfCell.getNumericCellValue());
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}

}
