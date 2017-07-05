package modbusTcp;

/**
 * 版权所有:精诚瑞宝计算机系统有限公司
 * @描述: 十六位的类 
 * @作者: yuke
 * @时间: 2017年7月5日 上午10:56:22   
 * @版本:	1.0
 */
public class byte_16 {

	byte low_byte8;	//低八位
	byte high_byte8;	//高八位
	
	public byte getLow_byte8() {
		return low_byte8;
	}
	public void setLow_byte8(byte low_byte8) {
		this.low_byte8 = low_byte8;
	}
	public byte getHigh_byte8() {
		return high_byte8;
	}
	public void setHigh_byte8(byte high_byte8) {
		this.high_byte8 = high_byte8;
	}
}
