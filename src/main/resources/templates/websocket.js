import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

let stompClient = null;
let isConnected = false;
const listeners = new Set(); // 注册的回调列表

// 建立连接，只调用一次
function doConnect() {
  if (isConnected || stompClient) return;

  const socket = new SockJS('/api/ws');
  stompClient = Stomp.over(socket);
  stompClient.debug = () => {}; // 禁用日志

  stompClient.connect({}, () => {
    console.log('✅ WebSocket Connected');
    isConnected = true;

    // 订阅广播
    stompClient.subscribe('/topic/public', (message) => {
      const msg = JSON.parse(message.body);
      // 分发给所有监听器
      listeners.forEach((callback) => {
        try {
          callback(msg);
        } catch (e) {
          console.error('消息处理错误:', e);
        }
      });
    });

    // 连接成功后可选发送一条初始化消息
    sendPublicMessage({ content: '客户端已连接' });
  }, (err) => {
    console.error('❌ WebSocket 连接失败:', err);
    isConnected = false;
  });
}

/**
 * 供页面调用，注册消息处理函数
 * @param {Function} callback 消息处理函数
 * @returns {Function} 取消订阅函数
 */
export function connect(callback) {
  listeners.add(callback);
  doConnect(); // 确保连接

  return () => {
    // 页面卸载时手动取消监听
    listeners.delete(callback);
  };
}

/**
 * 发送广播消息
 */
export function sendPublicMessage(messageObj) {
  if (stompClient && stompClient.connected) {
    const msg = {
      type: 'CHAT',
      businessType: messageObj.businessType || '',
      content: messageObj.content || '',
    };
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(msg));
  } else {
    console.warn('WebSocket 未连接');
  }
}

/**
 * 主动断开（可选）
 */
export function disconnect() {
  if (stompClient) {
    stompClient.disconnect(() => {
      console.log('🔌 WebSocket Disconnected');
      isConnected = false;
      stompClient = null;
    });
  }
}
