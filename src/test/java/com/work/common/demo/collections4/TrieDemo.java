package com.work.common.demo.collections4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.io.IOUtils;

public class TrieDemo {
	public static void main(String[] args) {
		Trie<String, List<String>> trie = new PatriciaTrie<List<String>>();
//		String telPath = "C:\\Users\\Li Yujie\\Desktop\\lifestyle\\lifestyle.tel";
		String telPath = args[0];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(telPath));
			String line;
			while((line=br.readLine())!=null){
				line = line.trim();
				String[] ss = line.split("\t");
				String tel = ss[0];
				String shopid = ss[1];
						
//				trie.put(line, 1);
				if(!trie.containsKey(tel)){
					List<String> list = new ArrayList<String>();
					trie.put(tel, list);
				}
				if(!trie.get(tel).contains(shopid)){
					trie.get(tel).add(shopid);
				}
			}
			System.out.println(trie.containsKey("95587028"));
			System.out.println(trie.containsKey("95588888"));
			System.out.println(trie.containsKey("18980221686"));
			System.out.println(trie.containsKey("18920228888"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		try {
			Thread.sleep(1000*60*60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
