package com.work.common.utils.struct.tree;

/**
 * 字典树 又称单词查找树，Trie树，是一种树形结构，是一种哈希树的变种。典型应用是用于统计，排序和保存大量的字符串（但不仅限于字符串），
 * 所以经常被搜索引擎系统用于文本词频统计。 它的优点是：利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。
 * 
 * @author：wanghaibo
 * @creattime：2017年1月12日 下午5:34:35
 * 
 */
public class Trie {
	private int SIZE = 26;
	private TrieNode root;// 字典树的根

	// 初始化字典树
	Trie() {
		root = new TrieNode();
	}

	// 字典树节点
	private class TrieNode {
		private int num;// 有多少单词通过这个节点,即由根至该节点组成的字符串模式出现的次数
		private TrieNode[] son;// 所有的儿子节点
		private boolean isEnd;// 是不是最后一个节点
		private char val;// 节点的值

		TrieNode() {
			num = 1;
			son = new TrieNode[SIZE];
			isEnd = false;
		}
	}

	// 建立字典树
	// 在字典树中插入一个单词
	public void insert(String str) {
		if (str == null || str.length() == 0) {
			return;
		}
		TrieNode node = root;
		char[] letters = str.toCharArray();
		for (int i = 0, len = str.length(); i < len; i++) {
			int pos = letters[i] - 'a';
			if (node.son[pos] == null) {
				node.son[pos] = new TrieNode();
				node.son[pos].val = letters[i];
			} else {
				node.son[pos].num++;
			}
			node = node.son[pos];
		}
		node.isEnd = true;
	}

	// 计算单词前缀的数量
	public int countPrefix(String prefix) {
		if (prefix == null || prefix.length() == 0) {
			return -1;
		}
		TrieNode node = root;
		char[] letters = prefix.toCharArray();
		for (int i = 0, len = prefix.length(); i < len; i++) {
			int pos = letters[i] - 'a';
			if (node.son[pos] == null) {
				return 0;
			} else {
				node = node.son[pos];
			}
		}
		return node.num;
	}

	// 打印指定前缀的单词
	public String hasPrefix(String prefix) {
		if (prefix == null || prefix.length() == 0) {
			return null;
		}
		TrieNode node = root;
		char[] letters = prefix.toCharArray();
		for (int i = 0, len = prefix.length(); i < len; i++) {
			int pos = letters[i] - 'a';
			if (node.son[pos] == null) {
				return null;
			} else {
				node = node.son[pos];
			}
		}
		preTraverse(node, prefix);
		return null;
	}

	// 遍历经过此节点的单词.
	public void preTraverse(TrieNode node, String prefix) {
		if (!node.isEnd) {
			for (TrieNode child : node.son) {
				if (child != null) {
					preTraverse(child, prefix + child.val);
				}
			}
			return;
		}
		System.out.println(prefix);
	}

	// 在字典树中查找一个完全匹配的单词.
	public boolean has(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		TrieNode node = root;
		char[] letters = str.toCharArray();
		for (int i = 0, len = str.length(); i < len; i++) {
			int pos = letters[i] - 'a';
			if (node.son[pos] != null) {
				node = node.son[pos];
			} else {
				return false;
			}
		}
		return node.isEnd;
	}

	// 前序遍历字典树.
	public void preTraverse(TrieNode node) {
		if (node != null) {
			System.out.print(node.val + "-");
			for (TrieNode child : node.son) {
				preTraverse(child);
			}
		}
	}

	public TrieNode getRoot() {
		return this.root;
	}

	public static void main(String[] args) {
		Trie tree = new Trie();
		String[] strs = { "banana", "band", "bee", "absolute", "acm", };
		String[] prefix = { "ba", "b", "band", "abc", };
		for (String str : strs) {
			tree.insert(str);
		}
		System.out.println(tree.has("abc"));
		tree.preTraverse(tree.getRoot());
		System.out.println();
		System.out.println(tree.has("acm"));
		System.out.println(tree.has("acb"));
		// tree.printAllWords();
		for (String pre : prefix) {
			int num = tree.countPrefix(pre);
			System.out.println(pre + "" + num);
		}
	}
}