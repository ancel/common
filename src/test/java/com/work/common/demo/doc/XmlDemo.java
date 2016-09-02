package com.work.common.demo.doc;

import java.io.File;
import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlDemo {
	private static void outputXml(File outputDir,String fileName) {
		Document document = DocumentHelper.createDocument();
		Element tableDataElement = document.addElement("tableData");
		Element tableElement = tableDataElement.addElement("table");
		tableElement.addAttribute("tableName", "dict_source");
		try {
			// ** 将document中的内容写入文件中 *//*
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileWriter(new File(outputDir,fileName)),outputFormat);
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		File outputDir = new File("C:\\Users\\Li Yujie\\Desktop");
		String filename = "tt.xml";
		outputXml(outputDir, filename);
	}
}
