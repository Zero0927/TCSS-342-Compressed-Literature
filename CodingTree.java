//Siyuan Zhou
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CodingTree implements Comparable<CodingTree> {

	private HuffmanTreeNode node;
	private CodingTree leftChild;
	private CodingTree rightChild;
	
	//a map of characters in the message to binary codes (Strings of ‘1’s and ‘0’s) created by tree.
	public Map<Character, String> codes;
	
	//the message encoded using the Huffman codes.
	public String bits="";
	
	/**a constructor that takes the text of a message to be compressed. 
	 * The constructor is responsible for calling all private methods 
	 * that carry out the Huffman coding algorithm
	 * */
	public  CodingTree(String message){
		int[]freq=computeFrequency(message);
		buildTree(freq);
		codes=new HashMap<Character, String>();
		buildCodesMap("", codes);
		StringBuilder sb=new StringBuilder();
		char[]chars=message.toCharArray();
		for(char c:chars)
			sb.append(codes.get(c));
		bits=sb.toString();
	}
	private CodingTree(HuffmanTreeNode node) {
		this.node=node;
		leftChild=rightChild=null;
	}
	private CodingTree(CodingTree left,CodingTree right) {
		this.node=new HuffmanTreeNode(left.node, right.node);
		leftChild=left;
		rightChild=right;
	}
	//compute frequency of each byte from 0 to 255
	private int[]computeFrequency(String message){
		int[]freq=new int[256];//the value of a unsigned byte is from 0 to 255
		Arrays.fill(freq, 0);
		//count the frequency for each byte
		for(int k=0;k<message.length();++k){
			char c=message.charAt(k);
			++freq[c&0xff];//'b&0xff' make the value a positive integer
		}
		return freq;
	}
	//use a min-heap to build huffman tree
	private void buildTree(int[]freq){
		MyPriorityQueue<CodingTree>heap=new MyPriorityQueue<CodingTree>(freq.length);
		//build a original min heap
		for(int k=0;k<freq.length;++k){
			if(freq[k]==0)//non-existing Char
				continue;
			System.out.printf("char=%c,freq=%d\n",k,freq[k]);
			CodingTree.HuffmanTreeNode node=new CodingTree.HuffmanTreeNode(k, freq[k]);
			heap.insert(new CodingTree(node));
		}
		//build tree
		while(heap.getSize()>1){
			CodingTree min=heap.popMin();//pop the min tree
			CodingTree min2=heap.popMin();//pop second min tree
			//make a new tree with two trees
			heap.insert(new CodingTree(min, min2));
		}
		CodingTree tree= heap.popMin();
		this.node=tree.node;
		this.leftChild=tree.leftChild;
		this.rightChild=tree.rightChild;
		//tree.display();//for debug
	}
	//
	private void buildCodesMap(String prefix,Map<Character, String> codes){
		//leaf node. the key is valid and should be encoded
		if(node.isValid()){
			//store the key and its code, length of bit of the code
			codes.put((char)node.getKey(),prefix);
			System.out.printf("char=%c, codes=%s\n",(char)node.getKey(),prefix);
		}else{
			//code for left child tree
			leftChild.buildCodesMap(prefix+"0",codes);
			// code for right child tree
			rightChild.buildCodesMap(prefix+"1",codes);
		}
	}
	//­ this method will	take the output of Huffman’s encoding and produce the original text.
	private static String decode(String bits,Map<Character,String>codes){
		StringBuilder sb=new StringBuilder();
		Map<String,Character>decodes=new HashMap<String, Character>();
		for(char c:codes.keySet())
			decodes.put(codes.get(codes), c);
		String tmp="";
		for(int k=0;k<bits.length();++k){
			tmp+=bits.charAt(k);
			//find a sequence of bits that can be decoded to a character
			if(decodes.containsKey(tmp)){
				sb.append(decodes.get(tmp));
				tmp="";
			}
		}
		return sb.toString();
	}

	@Override
	public int compareTo(CodingTree o) {
		return node.getFrequency()-o.node.getFrequency();
	}
	
	public void display() {
		display(0);
		System.out.println();
	}
	private void display(int depth){
		char[]space=new char[12*(depth+1)];
		Arrays.fill(space, ' ');
		System.out.print(" "+node);
		if(leftChild!=null)
			leftChild.display(depth+1);
		if(rightChild!=null){
			System.out.println();
			System.out.print(new String(space));
			rightChild.display(depth+1);
		}
		//System.out.println();
	}
	
	static class HuffmanTreeNode {
		public static final int INVALID_KEY=-1;
		private int key;
		private int frequency;
		
		public HuffmanTreeNode(int key, int frequency) {
			super();
			this.key = key;
			this.frequency = frequency;
		}
		//join two nodes and new node's frequency is the sum of two nodes'frequency
		public HuffmanTreeNode(HuffmanTreeNode a, HuffmanTreeNode b) {
			super();
			this.key = INVALID_KEY;
			this.frequency = a.frequency+b.frequency;
		}
		public boolean isValid(){
			return key!=INVALID_KEY;
		}
		public int getKey() {
			return key;
		}

		public int getFrequency() {
			return frequency;
		}

		@Override
		public String toString() {
			return isValid()?String.format("[0x%02x,%4d]", key,frequency):
				String.format("[0x-1,%4d]", frequency);
		}
	}}
