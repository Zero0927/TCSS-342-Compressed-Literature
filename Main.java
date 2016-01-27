//Siyuan Zhou
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 */

/**huffman coding.Encode
 * @author ×××
 *
 */
public class Main {

	private static final int[]MASKS={
		0,1,3,7,0x0f,0x1f,0x3f,0x7f,
		0xff,0x1ff,0x3ff,0x7ff,0x0fff,0x1fff,0x3fff,0x7fff,
		0xffff,0x1ffff,0x3ffff,0x7ffff,0x0fffff,0x1fffff,0x3fffff,0x7fffff,
		0xffffff,0x1ffffff,0x3ffffff,0x7ffffff,0x0fffffff,0x1fffffff,0x3fffffff,0x7fffffff,
		0xffffffff
	};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file="WarAndPeace.txt";
		String content=readTextContent(file);
		long startTime=System.currentTimeMillis();
		//Pass the String into the CodingTree in order to initiate Huffman’s encoding procedure and generate a map of codes.
		CodingTree tree=new CodingTree(content);
		try {
			//Output the codes to a text file.
			PrintStream save=new PrintStream("codes.txt");
			save.print(tree.codes);
			save.close();
			//Output the compressed message to a binary file.
			save=new PrintStream("compressed.txt");
			int saveLen=(tree.bits.length()+7)>>3;
			byte[]saveBit=new byte[saveLen];
			for(int k=0;k<saveLen;++k){
				byte b=0;
				for(int j=0;j<8;++j)
					if(8*k+j<tree.bits.length()&&tree.bits.charAt(8*k+j)=='1')
						b|=1<<j;
				saveBit[k]=b;
			}
			save.write(saveBit);
			save.close();
			//Display statistics. 
			//output the original size (in bits)
			//the compressed size (in bits)
			//the compression ratio (as a percentage) 
			// the elapsed time for compression.
			long endTime=System.currentTimeMillis();
			int originalSize=content.length();
			int compressedSize=saveLen;
			double ratio=100.0*compressedSize/originalSize;
			long time=endTime-startTime;
			System.out.printf("original    size :%dbyte\n",originalSize);
			System.out.printf("compressed  size :%dbyte\n",compressedSize);
			System.out.printf("compression ratio:%.2f%%\n",ratio);
			System.out.printf("elapsed     time :%dms\n",time);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Read the contents of a text file into a String
	private static String readTextContent(String file){
		try {
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			StringBuilder sb=new StringBuilder();
			char[]buf=new char[4096];
			int read;
			while((read=br.read(buf))>0){
				sb.append(buf, 0, read);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
