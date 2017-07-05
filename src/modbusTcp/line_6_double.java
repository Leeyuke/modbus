package modbusTcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import com.opencsv.CSVReader;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述: 6线双铺 
 * @作者: yuke
 * @时间: 2017年7月5日 上午9:59:28   
 * @版本: 1.0
 */
public class line_6_double {

//	final static byte[] SENDINFO = {0x00, 0x07, 0x00, 0x00, 0x00, 0x06, 0x01, 0x02, 0x14, (byte) 0xf1, 0x0f, (byte) 0xc0};
	final static byte[] SENDINFO = {0x00, 0x07, 0x00, 0x00, 0x00, 0x06, 0x01, 0x02, 0x00, (byte) 0x00, 0x00, (byte) 0x0a};
	final static int START_ADDR = 15361;
	final static int RESULT_START = 9;
	
	static int Length;
	
	
	static {
		Length = 9;
//		byte lastByte = SENDINFO[SENDINFO.length - 1];	//得到要读取的数量
//		int bin = lastByte & 0xFF;	//数量转成十进制
//		Length += bin%8 == 0 ? bin/8 : bin/8+1;	//得到长度来存放结果
		Length += 1008;	//长度
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket("192.1.1.4",9600);
		InputStream is=socket.getInputStream();
		
		//发送指令
		OutputStream os=socket.getOutputStream(); 
		os.write(SENDINFO);
		os.flush(); 
		
		//得到返回结果
		byte[] bs = new byte[Length];    
		is.read(bs);
		
		File file = new File("F:\\write.csv");  
		InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"Utf-8");
		CSVReader csvReader = new CSVReader(isr); 
		List<String[]> list = csvReader.readAll();
		for(String[] line : list) {
			String type = line[2];	//得到该行的类型
			int addr = Integer.parseInt(line[3]);	//得到寄存器的地址
			int byte_addr = addr - START_ADDR + RESULT_START;

			//对于数据类型为bit的情况
			if(type.equals("bit")) {
				String hex = printHexString(bs[byte_addr + 1]) +  printHexString(bs[byte_addr]);
				String binary = hexString2binaryString(hex);
				char[] chars = invert(binary.toCharArray());
				int pass_num = Integer.parseInt(line[1].split("\\.")[1]);
				String res = chars[pass_num] + "";
				System.out.println(line[0] + " and " + res);
			}
			//对于数据类型为16位无符号二进制的情况
			else if(type.equals("16_unbinary")) {
				String hex = printHexString(bs[byte_addr + 1]) +  printHexString(bs[byte_addr]);
				String binary = hexString2binaryString(hex);
				char[] chars = invert(binary.toCharArray());
				String res = new String(chars);
				System.out.println(line[0] + " and " + res);
			}
			//对于数据类型为32位无符号二进制的情况
			else if(type.equals("32_unbinary")) {
				String hex = printHexString(bs[byte_addr + 3]) + printHexString(bs[byte_addr + 2]) + 
							printHexString(bs[byte_addr + 1]) +  printHexString(bs[byte_addr]);
				String binary = hexString2binaryString(hex);
				char[] chars = invert(binary.toCharArray());
				String res = new String(chars);
				System.out.println(line[0] + " and " + res);
			}
			//对于数据类型为32位浮点数的情况
			else if(type.equals("32_float")) {
				String hex = printHexString(bs[byte_addr + 3]) + printHexString(bs[byte_addr + 2]) + 
						printHexString(bs[byte_addr + 1]) +  printHexString(bs[byte_addr]);
				String binary = hexString2binaryString(hex);
				char[] chars = invert(binary.toCharArray());
				String res = new String(chars);
				System.out.println(line[0] + " and " + toFloatString(res));
			}
		}
		
	}
	
	
	/**
	 * 将byte字节转换为16进制
	 * @param b
	 * @return 十六进制数
	 */
	public static String printHexString( byte b) {  
		String hex = Integer.toHexString(b & 0xFF); 
		if (hex.length() == 1) { 
			hex = '0' + hex; 
		} 
		return hex;
	}
	
	/**
	 * 十六进制转换为二进制
	 * @param hexString
	 * @return 二进制结果
	 */
	public static String hexString2binaryString(String hexString) {  
		if(hexString == null || hexString.length() % 2 != 0)  
			return null;  	
        String bString = "", tmp;  
        for (int i = 0; i < hexString.length(); i++) {  
        	tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));  
        	bString += tmp.substring(tmp.length() - 4);  
        }  
        return bString;  
	}  
	
	/**
	 * 将二进制颠倒
	 * @param chars
	 * @return
	 */
	public static char[] invert(char[] chars) {
		int length = chars.length;
		char[] newChars = new char[length];
		for(int i=0; i<length; i++) {
			newChars[length - 1 - i] = chars[i];
		}
		return newChars;
	}
	
	/**
	 * 二进制数转换为32位浮点数
	 * @param binaryString
	 * @return 浮点数
	 */
	public static double toFloatString(String binaryString) {
		
		double result;
		
		String sign = binaryString.substring(0, 1);	//得到数符
		
		String exponent = binaryString.substring( 1, 9 );	//得到指数位
		int expint = Integer.parseInt(exponent, 2);		//指数转换为十进制
		int mobit = expint - 127;	//得到实际的阶码
		Double d = Math.pow(2,mobit);	//以2为底求值
		
		String last = binaryString.substring(9);	//得到尾数
		double lastRes = 0D;	//存放尾数的结果
		for(int i=0; i<last.length(); i++) {
			char b = last.charAt(i);
			if(b == '1') {
				lastRes += Math.pow(2, -(i + 1));	//尾数的计算
			}
		}
		result = d * (sign.equals("1") ? -1 : 1) * (1 + lastRes);
		
		return result;
		
	}
}
 