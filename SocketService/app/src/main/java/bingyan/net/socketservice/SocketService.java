package bingyan.net.socketservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.nio.charset.Charset;

/**
 * Created by Demon on 2015/9/22.
 * 使用 Apache Mina2.0框架搭建
 * 所有代码都在Service线程执行
 */
public class SocketService extends IntentService {

    /**
     * 提供登录接口
     */
    public interface LoginCallBack{
        public void loginCallBack(int responseCode);
    }

    /**
     * 提供匹配接口
     */
    public interface MatchCallBack{
        public void matchCallBack(int responseCode);
    }

    /**
     * 提供聊天回调接口
     */
    public interface ChatCallBack{
        public void chatCallBack(String s);
    }

    /**
     * 回调接口实例
     */
    private LoginCallBack loginCallBack;
    private MatchCallBack matchCallBack;
    private ChatCallBack chatCallBack;

    /**
     * 请求状态码
     */
    private static final int LOGIN = 0;
    private static final int MATCH = 2;
    private static final int CHAT = 1;

    public SocketService() {
        super("MyIntentService");
    }

    /**
     * 设置登录回调接口
     * @param loginCallBack 登录回调接口实例
     */
    public void setLoginCallBack(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    /**
     * 设置匹配回调接口
     * @param matchCallBack 匹配接口回调
     */
    public void setMatchCallBack(MatchCallBack matchCallBack) {
        this.matchCallBack = matchCallBack;
    }

    /**
     * 设置聊天回调接口
     * @param chatCallBack 聊天回调接口
     */
    public void setChatCallBack(ChatCallBack chatCallBack) {
        this.chatCallBack = chatCallBack;
    }

    public class MyBinder extends Binder {

        public SocketService getService() {
            return SocketService.this;
        }
    }

    private MyBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_LOGIN:
                    handleActionLogin();//登录后服务一直阻塞至此，直到断开连接
                    break;
            }
        }
    }

    private static final String ACTION_LOGIN = "bingyan.net.flowertalk.action.LOGIN";

    /**
     * 开始登录
     * @param context 上下文
     */
    public static void startActionLogin(Context context) {
        Intent intent = new Intent(context, SocketService.class);
        intent.setAction(ACTION_LOGIN);
        context.startService(intent);
    }

    private void handleActionLogin() {
        startLogin();
    }

    /**
     * 控制读写
     */
    public static IoSession session;

    /**
     * 链接超时时间,单位ms,默认300000
     */
    private long conTimeout = 300000;

    /**
     * 设置链接超时时间
     * @param timeout 超时时间单位ms
     */
    public void setConTimeoutMillis(long timeout) {
        this.conTimeout = timeout;
    }

    /**
     * 编码方式,默认utf-8
     */
    private String codeFormat = "utf-8";

    /**
     * 设置编码方式
     * @param codeFormat 编码方式
     */
    public void setCodeFormat(String codeFormat) {
        this.codeFormat = codeFormat;
    }

    /**
     * 编码结束标志符,默认Unix
     */
    private String encodingDelimiter = LineDelimiter.UNIX.getValue();

    /**
     * 设置编码结束符
     * @param ecCodingDelimiter 编码结束符,可自己制定
     */
    public void setEncodingDelimiter(String ecCodingDelimiter) {
        this.encodingDelimiter = ecCodingDelimiter;
    }

    /**
     * 解码结束标志符,默认Unix
     */
    private String deCodingDelimiter = LineDelimiter.UNIX.getValue();

    public void setDeCodingDelimiter(String deCodingDelimiter) {
        this.deCodingDelimiter = deCodingDelimiter;
    }

    /**
     * 双方设备空闲临界时间,默认10s
     */
    private int bothIdleTime = 10;

    /**
     * 设置双方设备均空闲临界时间,单位S
     * @param s 临界时间
     */
    public void setBothIdleTime(int s) {
        this.bothIdleTime = s;
    }

    /**
     * 建立Socket连接首先进行登录
     */
    private void startLogin() {
        IoConnector connector = new NioSocketConnector();  // 创建连接
        // 设置链接超时时间
        connector.setConnectTimeoutMillis(conTimeout);
        // 添加过滤器
        connector.getFilterChain().addLast(   //添加消息过滤器
                "codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset
                        .forName(codeFormat), encodingDelimiter,
                        deCodingDelimiter)));
        //设置空闲时间
        connector.getSessionConfig().setBothIdleTime(10);

    }

    /**
     * 自定义业务处理逻辑处理器类
     */
    class MyClientHandler extends IoHandlerAdapter{

        /**
         * 收到数据触发
         * @param session
         * @param message
         * @throws Exception
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }
    }


}
