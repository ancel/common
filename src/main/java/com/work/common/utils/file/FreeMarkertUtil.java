package com.work.common.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.work.common.constant.CharSet;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkertUtil {
	/**
	 * 
	 * @param templateDir
	 *            模板文件路径
	 * @param templateName
	 *            模板文件名称
	 * @param outFileName
	 *            输出文件名称
	 * @param templateEncoding
	 *            模板文件的编码方式
	 * @param outFileEncoding
	 *            输出文件的编码方式
	 * @param data
	 *            数据模型根对象
	 */
	public static void analysisTemplate(String templateDir,String templateName,String outFileName,
			String templateEncoding,String outFileEncoding, Map<?, ?> data) {
		try {
			/**
			 * 创建Configuration对象
			 */
			Configuration config = new Configuration(Configuration.VERSION_2_3_22);
			/**
			 * 指定模板路径
			 */
			File file = new File(templateDir);
			/**
			 * 设置要解析的模板所在的目录，并加载模板文件
			 */
			config.setDirectoryForTemplateLoading(file);
			/**
			 * 设置包装器，并将对象包装为数据模型
			 */
			config.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));

			/**
			 * 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			 */
			Template template = config.getTemplate(templateName,
					templateEncoding);
			/**
			 * 合并数据模型与模板
			 */
			FileUtil.createFile(new File(outFileName));
			Writer out = new OutputStreamWriter(new FileOutputStream(outFileName), outFileEncoding);
			template.process(data, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param templateDir
	 * @param templateName
	 * @param outFileName
	 * @param data
	 */
	public static void analysisTemplate(String templateDir,String templateName,String outFileName, Map<?, ?> data){
		analysisTemplate(templateDir,templateName,outFileName,CharSet.UTF_8,CharSet.UTF_8, data); 
	}
	
	
	public static void main(String[] args) {
		
	}
}
