package modbusTcp;

public class dllTest {
	
	public static void main(String[] args) {
		
		byte[] b = new byte[]{0x1f, (byte) 0x80};
		byte last2Byte = b[0];	//得到要读取的数量
		int bin2 = last2Byte & 0xFF;	//数量转成十进制
		
		byte last1Byte = b[1];	//得到要读取的数量
		int bin1 = last1Byte & 0xFF;	//数量转成十进制
		
		System.out.println(bin2 + " and " + bin1);
	}
}
