/**
 * 
 */
package top.anets.modules.realTimeSession.model;

import lombok.Data;

/**
 * @author Administrator
 *
 */
@Data
public class Message {
	
	public static final String Type_download = "download";
    public static final String Public = "Public";
    public static final String Type_Log_waitInvoice = "Log_waitInvoice";
	public static final String Type_Log_errorInvoice = "Log_errorInvoice";
	public static final String Stop_Log = "Stop_Log";
    public static final String Start_Log ="Start_Log" ;
	public static final String Block_Block ="Block_Block" ;
    public static final String Block_Invoice = "Block_Invoice";
    public static String Type_Log = "log";          //日志
	public static String Type_YuNeng = "invoice";  //发票
	public static String Block_Public = "Block_Public";  //发票
	
	/**
	 * 消息类型
	 */
    private String type;
    /**
     * 消息内容
     */
    private Object data;
	/**
	 * @param type
	 * @param data
	 */
	public Message(String type, Object data) {
		super();
		this.type = type;
		this.data = data;
	}


	public static Message newInstance = null;


	public static synchronized Message getInstance(String type, Object data) {

		if (newInstance == null) {
			newInstance = new Message(type,data);
		} else {
			newInstance.setType(type);
			newInstance.setData(data);
		}

		return newInstance;

	}
    
    
    
    
    
    
}
