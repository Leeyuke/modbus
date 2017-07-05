package modbusTcp;

public class crc16_modbusRTU {

	public static void setCrc16(byte[] array, int length) {
		long wcrc=0XFFFF;
		int temp;
		for(int i=0 ; i<length ; i++) {
			temp=array[i] & 0X00FF;
			wcrc ^= temp;
			for(int j=0; j<8; j++) {
				System.out.print(wcrc & 0X0001);
			}
		}
	}
	
	public static String getCrc16(byte[] arr_buff) {
		int len = arr_buff.length;
		//预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
		int crc = 0xFFFF;
		int i, j; 
		for (i = 0; i < len; i++) {
			//把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
			crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
			for (j = 0; j < 8; j++) {
				//把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
				if ((crc & 0x0001) > 0) {
					//如果移出位为 1, CRC寄存器与多项式A001进行异或
					crc = crc >> 1;
					crc = crc ^ 0xA001;
				} else
					//如果移出位为 0,再次右移一位
					crc = crc >> 1;
			}
		}
		byte[] b = hexStringToBytes(Integer.toHexString(crc));
		for(byte bb : b) {
			System.out.print(bb);
		}
		System.out.println();
		return Integer.toHexString(crc);
	}
	
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}  
	
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}  
}
