//package top.anets.modules.realTimeSession;
//
///**
// * @author ftm
// * @date 2022/10/26 0026 13:50
// */
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//
//@ServerEndpoint(value="/websocket/{sid}" )
//@Component
//public class WebSocketServer {
//
//    protected  final Logger log = LoggerFactory.getLogger(this.getClass());
//    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
//    private static volatile int onlineCount = 0;
//    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
//    //与某个客户端的连接会话，需要通过它来给客户端发送数据
//    private Session session;
//
//
//    //接收sid
//    private String sid="";
//    /**
//     * 连接建立成功调用的方法*/
//    @OnOpen
//    public void onOpen(Session session,@PathParam("sid") String sid) {
//        this.session = session;
//        this.sid=sid;
//        webSocketMap.put(sid,this );//加入set中
//        addOnlineCount();           //在线数加1
//        log.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
//
//        try {
//            sendMessage("连接成功");
//        } catch (IOException e) {
//            log.error("websocket IO异常");
//        }
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        webSocketMap.remove(this.sid); //从set中删除
//        subOnlineCount();           //在线数减1
//        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息*/
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("收到来自窗口"+sid+"的信息:"+message);
//
//    }
//
//    /**
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误");
//        error.printStackTrace();
//    }
//    /**
//     * 实现服务器主动推送
//     */
//    public void sendMessage(String message) throws IOException {
//        //加同步锁，解决多线程下发送消息异常关闭  ;避免高并发下多处频繁调用sendMessage（）方法发送消息而导致的websocket挂掉
//        synchronized (this.session){
//            this.session.getBasicRemote().sendText(message);
//        }
//
//    }
//
//
//    /**
//     * 群发自定义消息
//     * */
//    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
//        System.out.println("推送消息到窗口"+sid+"，推送内容:"+message);
//
//        WebSocketServer webSocketServer = webSocketMap.get(sid);
//        if(webSocketServer!=null){
//            webSocketServer.sendMessage(message);
//        }
//    }
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        WebSocketServer.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        WebSocketServer.onlineCount--;
//    }
//
//
//
//    public String getSid() {
//        return sid;
//    }
//
//}