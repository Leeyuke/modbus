package modbusTcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class socketSend {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1",9600);

		InputStream is=socket.getInputStream(); 
		
		
//		OutputStream os=socket.getOutputStream(); 
//		byte[] sendInfo = new byte[] { 0x22, (byte) 0xa3, 0x00, 0x00, 0x00, 0x06, 0x00, 0x03, 0x00, 0x1f, (byte) 0xff, 0x00 };  
//		os.write(sendInfo);
//		os.flush(); 
//		byte[] bs = new byte[20];    
//		is.read(bs);
//		printHexString(bs);
//		System.out.println();

		OutputStream os=socket.getOutputStream(); 
//		byte[] sendInfo = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, 0x00, 0x00, 0x00, 0x0a };  
//		byte[] sendInfo = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x00, 0x00, (byte) 0xFF };  
//		byte[] sendInfo = new byte[] {0x01, 0x03, 0x00, 0x00, 0x00, 0x0a, (byte) 0xc5, (byte) 0xcd};
		byte[] sendInfo = new byte[] {0x00, 0x07, 0x00, 0x00, 0x00, 0x06, 0x01, 0x02, 0x00, 0x00, 0x00, 0x0a};
		os.write(sendInfo);
		os.flush(); 
		
		int length = 9;
		byte lastByte = sendInfo[sendInfo.length - 1];	//得到要读取的数量
		int bin = lastByte & 0xFF;	//数量转成十进制
		length += bin%8 == 0 ? bin/8 : bin/8+1;	//得到长度来存放结果
		
		byte[] bs = new byte[length];    
		is.read(bs);
		for(int i=0; i<bs.length; i++) {
			printHexString(bs[i]);
		}
//		printHexString(bs);
		System.out.println();
		
//		byte[] array = new byte[] {0x01, 0x03, 0x00, 0x00, 0x00, 0x0a};
//		System.out.println(crc16_modbusRTU.getCrc16(array));
		
		//写
//		byte[] sendInfo = new byte[] {0x01, 0x06, 0x00, 0x00, 0x00, (byte) 0xff, (byte) 0xc9, (byte) 0x8a};
//		os.write(sendInfo);
//		os.flush(); 
//		byte[] bs = new byte[32];    
//		is.read(bs);
//		printHexString(bs);
//		System.out.println();
		
//		byte[] data = new byte[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x01, 0x01, 0x00, 0x14, 0x00, 0x13};  
//		os=socket.getOutputStream(); 
//		os.write(data);
//		os.flush(); 
//		is=socket.getInputStream(); 
//		bs = new byte[20];    
//		is.read(bs);
//		printHexString(bs);
//		System.out.println();
		
//		data = new byte[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x01, 0x00, 0x03};  
//		os=socket.getOutputStream(); 
//		os.write(data);
//		os.flush(); 
//		is=socket.getInputStream(); 
//		bs = new byte[20];    
//		is.read(bs);
//		printHexString(bs);
//		System.out.println();
		
		os.close();
		socket.close();
	}
	
	public static void printHexString( byte[] b) {  
		System.out.println(b.length);
		for (int i = 0; i < b.length; i++) { 
			String hex = Integer.toHexString(b[i] & 0xFF); 
			if (hex.length() == 1) { 
				hex = '0' + hex; 
			} 
			System.out.print(hex.toUpperCase() ); 
		} 
	}
	
	public static void printHexString( byte b) {  
		String hex = Integer.toHexString(b & 0xFF); 
		if (hex.length() == 1) { 
			hex = '0' + hex; 
		} 
		System.out.print(hex.toUpperCase() ); 
	}
}
