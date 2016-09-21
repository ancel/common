package com.work.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfUtil {
	
	/**
	 * 读取pdf文件内容
	 * @param pdfFilePath
	 * @return
	 */
	public static String getTextFromPDF(String pdfFilePath) {
		String result = null;
		FileInputStream is = null;
		PDDocument document = null;
		try {

			is = new FileInputStream(pdfFilePath);
			PDFParser parser = new PDFParser(is);
			parser.parse();
			document = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			result = stripper.getText(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		String pdfFilePath = "C:\\Users\\Li Yujie\\Desktop\\zip_codes_states.pdf";
		String content = PdfUtil.getTextFromPDF(pdfFilePath);
		String[] locationStr = content.split("\\r\\n");
		for (String string : locationStr) {
			if(Regex.check(string.substring(0, 1),Regex.REG_NUMBER)){
				System.out.println(string);
				String[] strs = string.split(" ");
				for (String string2 : strs) {
					System.out.println(string2);
				}
				break;
			}
		}
		System.out.println(locationStr.length);
//		System.out.println(PdfDemo.getTextFromPDF(pdfFilePath));
	}
}
