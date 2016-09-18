package com.work.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * csv文件读取，使用superCSV
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午2:59:39 
 * 
 */  
public class CsvParser implements Iterator<List<String>>{
    
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);
            
    private CsvListReader reader = null;
    private List<String> row = null;
    
    public CsvParser(String csvFile, String encoding) {
        super();
        try {
            reader = new CsvListReader(new InputStreamReader(new FileInputStream(csvFile), encoding), CsvPreference.EXCEL_PREFERENCE);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public CsvParser(){
    	
    }
    
    public boolean hasNext(){
        try {
            if(reader.getLineNumber() == 0){//
                row = reader.read();
            }
            row = reader.read();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return row != null;
    }
    
    public List<String> next(){
        return row;
    }
    
    public void remove(){
        throw new UnsupportedOperationException("本CSV解析器是只读的."); 
    }
    
    public void close(){
        if(reader != null){
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 当前行号,从1开始
     * @return int
     */
    public int getLineNumber(){
        return reader.getLineNumber() - 1;
    }
    
    
    
    public static void main(String[] args) {
    	String file = "D:"+File.separator+"jh.csv";
        CsvParser p = new CsvParser(file, "UTF-8");
        while(p.hasNext()){
            List<String> row = p.next();
            if("建设银行（建国支行）".equals(row.get(0))){
            	System.out.println(row.get(0));
            	System.out.println(row.get(1));
            	System.out.println(row.get(2));
            	System.out.println(row.get(3));
            	System.out.println(row.get(4));
            	System.out.println(row.get(5));
            	System.out.println(row.get(6));
            	System.out.println(row.get(7));
            	System.out.println(row.get(8));
            	System.out.println(row.get(9));
            	System.out.println(row.get(10));
            	break;
            }
        }
        p.close();
    }

}