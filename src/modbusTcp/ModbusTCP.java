package modbusTcp;

import java.util.HashMap;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadExceptionStatusRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.util.queue.ByteQueue;

public class ModbusTCP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		short[] values = {1024,2048,3096};
//		modbusWTCP("localhost", 502, 1, 0, values);
		
		
		
		//ByteQueue a = modbusTCP("192.168.1.118", 502, 0, 10);
//		String fileName = "resource/param.conf";
        HashMap<String, String> param_map = new HashMap<>();
        param_map.put("host", "127.0.0.1");
        param_map.put("port", "9600");
        param_map.put("id", "1");
        param_map.put("fc", "2");
        param_map.put("start", "0");
        param_map.put("length", "10");
        modbusTCP(param_map.get("host"),
				Integer.parseInt(param_map.get("port")),
				Integer.parseInt(param_map.get("id")),
				Integer.parseInt(param_map.get("fc")),
				Integer.parseInt(param_map.get("start")),
				Integer.parseInt(param_map.get("length")));
		
	}
	
	public static void modbusWTCP(String ip, int port, int slaveId, int start, short[] values) {  
	    ModbusFactory modbusFactory = new ModbusFactory();  
	    // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502  
	    IpParameters params = new IpParameters();  
	    params.setHost(ip);  
	    if (502 != port) {  
	        params.setPort(port);  
	    }// 设置端口，默认502  
	    ModbusMaster tcpMaster = null;  
	     // 参数1：IP和端口信息 参数2：保持连接激活  
	     tcpMaster = modbusFactory.createTcpMaster(params, true);  
	     try {  
	         tcpMaster.init();  
	         System.out.println("===============" + 1111111);  
	     } catch (ModbusInitException e) {  
	         // System.out.println("11111111111111=="+"此处出现问题了啊!");  
	         // 如果出现了通信异常信息，则保存到数据库中  
	          //CommunityExceptionRecord cer = new CommunityExceptionRecord();  
	         //cer.setDate(new Date());  
	         //cer.setIp(ip);  
	         // cer.setRemark(bgName+"出现连接异常");  
	         // batteryGroupRecordService.saveCommunityException(cer);  
	     }  
	     try {  
	         WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);  
	         WriteRegistersResponse response = (WriteRegistersResponse) tcpMaster.send(request);  
	         if (response.isException())  
	             System.out.println("Exception response: message=" + response.getExceptionMessage());  
	         else  
	             System.out.println("Success");  
	     } catch (ModbusTransportException e) {  
	         e.printStackTrace();  
	     }  
	 }  
	   
	 public static ByteQueue modbusTCP(String ip, int port, int id, int fc, int start,int readLenth) {  
	     ModbusFactory modbusFactory = new ModbusFactory();  
	     // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502  
	     IpParameters params = new IpParameters();  
	     params.setHost(ip);  
	     if(502!=port){params.setPort(port);}//设置端口，默认502  
	     ModbusMaster tcpMaster = null;  
	         tcpMaster = modbusFactory.createTcpMaster(params, false);  
	         try {  
	             tcpMaster.init();  
	             System.out.println("===============");  
	         } catch (ModbusInitException e) {  
	             return null;  
	         }  
	         ModbusRequest modbusRequest=null;  
	         try {  
	        	 if(fc == 3){
	        		 modbusRequest = new ReadHoldingRegistersRequest(id, start, readLenth);//功能码03  
	        	 }else if(fc == 1){
	        		 modbusRequest = new ReadCoilsRequest(id, start, readLenth);//功能码01
	        	 }else if(fc == 2){
	        		 modbusRequest = new ReadDiscreteInputsRequest(id, start, readLenth);//功能码02
	        	 }else if(fc == 4){
	        		 modbusRequest = new ReadInputRegistersRequest(id, start, readLenth);//功能码04
	        	 }else if(fc == 7){
	        		 modbusRequest = new ReadExceptionStatusRequest(id);//功能码07
	        	 }
	         } catch (ModbusTransportException e) {  
	             e.printStackTrace();  
	         }  
	         ModbusResponse modbusResponse=null;  
	         try {  
	             modbusResponse = tcpMaster.send(modbusRequest);  
	         } catch (ModbusTransportException e) {  
	             e.printStackTrace();  
	         }  
	         
	         ByteQueue byteQueue= new ByteQueue(12);  
	         modbusResponse.write(byteQueue);  
	         System.out.println("功能码:"+modbusRequest.getFunctionCode());  
//	         System.out.println("从站地址:"+modbusRequest.getSlaveId());  
//	         System.out.println("收到的响应信息大小:"+byteQueue.size());  
//	         System.out.println("收到的响应信息值:"+byteQueue); 
	         System.out.println("数据内容长度:"+byteQueue.peek(2)/2); 
	         System.out.print("数据内容:");
	         for (int idx = 2; idx<=byteQueue.size()-3; idx=idx+2){
	        	 System.out.print((byteQueue.peek(idx+1)<<8 & 0xFFFF) 
	        			 +(byteQueue.peek(idx+2) & 0xFF) +", ");
	         }
	         System.out.println();
	         
	         byte[] b = byteQueue.peekAll();
	         printHexString(b);
	         
	         return byteQueue;  
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
}
