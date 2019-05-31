package com.work.common.test;

import java.io.FileNotFoundException;

import javax.script.ScriptException;

import org.junit.Test;

import com.work.common.utils.js.JsUtil;

public class JSTest {
	@Test
	public void testFunc(){
		String jsPath = "file/aes.js";
		try {
			JsUtil ju = new JsUtil(jsPath, "file/mode-ecb.js", "file/jiemi.js");
			String key = "wangku.99114.com";
			String word = "J0wIeC91iUCck6chDAeL/g==";
			Object result = ju.invokeFunction("Decrypt", word, key);
			System.out.println(result.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
