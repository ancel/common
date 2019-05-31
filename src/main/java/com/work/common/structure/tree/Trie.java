package com.work.common.structure.tree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Trie {
	private Node root;
	private int size;
	public Trie() {
		root = new Node(' ');
	}

	public Node insert(String word) {
		if (isExist(word) == true)
			return get(word);

		Node current = root;
		for (int i = 0; i < word.length(); i++) {
			Node child = current.subNode(word.charAt(i));
			if (child != null) {
				current = child;
			} else {
				current.childList.add(new Node(word.charAt(i)));
				current = current.subNode(word.charAt(i));
			}
			current.count++;
		}
		current.isEnd = true;
		size++;
		return current;
	}
	
	public Node insert(String word, String data) {
		Node node = insert(word);
		node.datas.add(data);
		return node;
	}
	
	public boolean isExist(String word) {
		Node current = get(word);
		if(null==current){
			return false;
		}
		return true;
	}
	
	public boolean isExist(String word, String data) {
		Node current = get(word);
		if(null==current){
			return false;
		}
		if(!current.datas.contains(data)){
			return false;
		}
		return true;
	}
	
	/**
	 * 怎么判断单词是否存在？
	 * 被判断的单词的字母与根节点下的子节点的字母进行比较，直到匹配到两者最后一个字母相同，并且最后一个节点的isEnd标记为true
	 */
	public Node get(String word){
		Node current = root;
		for (int i = 0; i < word.length(); i++) {
			if (current.subNode(word.charAt(i)) == null) {
				return null;
			} else {
				current = current.subNode(word.charAt(i));
			}
		}
		if(current.isEnd){
			return current;
		}
		return null;
	}

	public void deleteWord(String word) {
		if (isExist(word) == false) {
			return;
		}

		Node current = root;
		for (char c : word.toCharArray()) {
			Node child = current.subNode(c);
			if (child.count == 1) {
				current.childList.remove(child);
				return;
			} else {
				child.count--;
				current = child;
			}
		}
		current.isEnd = false;
		size--;
	}

	public void deleteData(String word, String data){
    	if(isExist(word) == false){
			return;
		}
    	Node current = get(word);
    	current.datas.remove(data);
    }
	
	public int size(){
		return size;
	}

	class Node {
		char content; // 节点包含的内容
		boolean isEnd; // 是否在该节点行成过一个单词
		int count; // 统计该节点的字符被几个单词共享
		Set<String> datas;
		LinkedList<Node> childList; // 子节点的集合

		// 初始化
		public Node(char c) {
			childList = new LinkedList<Node>();
			content = c;
			isEnd = false;
			count = 0;
			datas = new HashSet<String>();
		}

		// 查找子节点中是否有内容为x的子节点，有则返回该子节点，否则返回空
		public Node subNode(char c) {
			if (childList != null) {
				for (Node eachChild : childList) {
					if (eachChild.content == c) {
						return eachChild;
					}
				}
			}
			return null;
		}

	}

	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insert("ball", "333");
		trie.insert("ball", "222");
		trie.insert("balls", "123");
		trie.insert("sense", "436");

		// testing deletion
		System.out.println(trie.isExist("balls"));
		System.out.println(trie.isExist("ba"));
		trie.deleteWord("balls");
		System.out.println(trie.isExist("balls"));
		System.out.println(trie.isExist("ball"));
		System.out.println(trie.isExist("ball", "11"));
		System.out.println(trie.isExist("ball", "333"));
		System.out.println(trie.isExist("ball", "222"));
		trie.deleteData("ball", "333");
		System.out.println(trie.isExist("ball", "333"));
	}
}
