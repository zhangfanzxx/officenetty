package com.paic.vodo.node.netty.socket;

import com.paic.vodo.node.netty.service.OfficeService;
import com.paic.vodo.node.netty.bean.SaveMind;
import com.paic.vodo.node.netty.config.Constant;
import com.paic.vodo.node.netty.utlis.JsonUtil;
import com.paic.vodo.node.netty.utlis.RedisUtil;
import com.paic.vodo.node.netty.utlis.SpringUtil;
import com.paic.vodo.node.office.bean.vo.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @BelongsProject: paoffice
 * @BelongsPackage: com.paic.paoffice.socket
 * @Author: zff
 * @CreateTime: 2018-10-22 09:21
 * @Description: ${Description}
 */

@Component
@Scope("prototype")
public class NettyWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocket.class);
    private String id;

    private ChannelHandlerContext ctx;

    private OfficeService officeService;
    private String cookie;


    private OfficeService getOfficeService() {
        if (officeService == null) {
            officeService = SpringUtil.getBean(OfficeService.class);
        }
        return officeService;
    }
    @Resource(name="myredis2")
    private RedisTemplate<String,Object > redisTemplate;

    private RedisSessionDAO redisSessionDAO;

    private RedisSessionDAO getRedisSessionDAO() {
        if (redisSessionDAO == null) {
            redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);
        }
        return this.redisSessionDAO;
    }

    private User staff;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<NettyWebSocket> webSocketSet = new CopyOnWriteArraySet<NettyWebSocket>();
    private static ConcurrentHashMap<String, CopyOnWriteArraySet<NettyWebSocket>> webSocketConcurrentHashMap = new ConcurrentHashMap<>();
    private StringBuilder stringBuilder = new StringBuilder();

    private static String COOKIES = "cookies=";
    private static String JAVA_SESSION_ID = "JSESSIONID";


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String message = textWebSocketFrame.text();
        if ("ping".equals(message)) {
            sendMessage("pong");
            return;
        }
        if (message.endsWith("next")) {
            stringBuilder.append(message.substring(0, message.length() - 4));
            return;
        } else {
            stringBuilder.append(message);
            message = stringBuilder.toString();
            stringBuilder = new StringBuilder();
        }
        if (message == null) {
            return;
        }
        logger.info("来自客户端{}的消息:" + message, id);
        if (message.startsWith(COOKIES)) {
            message = message.substring(COOKIES.length());
            Object sss = redisTemplate.opsForValue() .get((getRedisSessionDAO().getKeyPrefix() + message));
            System.out.println(sss);
            Session s = (Session) SerializeUtils.deserialize((byte[]) sss);
            Object attribute = s.getAttribute(Constant.SESSION_LOGIN_KEY_NAME);
            if (attribute != null) {
                if (attribute instanceof User) {
                    staff = (User) attribute;
                    this.cookie=message;
                }
                sendMessage("HelloMind");
            }
            return;
        }
        if (staff == null) {
            return;
        }
        if (message.startsWith("chart=")) {
            String id = message.substring(6);
            CopyOnWriteArraySet<NettyWebSocket> webSockets = webSocketConcurrentHashMap.get(message.substring(3));
            if (webSockets == null) {
                webSockets = new CopyOnWriteArraySet<>();
                webSocketConcurrentHashMap.put(id, webSockets);
            }
            this.setId(id);
            String ss = RedisUtil.getChart(id);
            if (ss != null) {
                this.sendMessage("result=" + ss);
            } else {
                String s = getOfficeService().getOfficeByid(id,this.cookie);
                if (s!=null){
                    this.sendMessage("result=" + s);
                }
                RedisUtil.setChart(id, s);
            }
            if (webSockets.size() == 0) {
                webSockets.add(this);
            } else {
                handlerRemoved(this.ctx);
            }
        } else if (message.startsWith("id=")) {
            String id = message.substring(3);
            CopyOnWriteArraySet<NettyWebSocket> webSockets = webSocketConcurrentHashMap.get(message.substring(3));
            if (webSockets == null) {
                webSockets = new CopyOnWriteArraySet<>();
                webSocketConcurrentHashMap.put(message.substring(3), webSockets);
            }
            webSockets.add(this);
            this.setId(id);
            String ss = RedisUtil.getMind(id);
            if (ss != null) {
                this.sendMessage("result=" + ss);
            } else {
                String s = getOfficeService().getOfficeByid(id,this.cookie);
                if (s!=null){
                    this.sendMessage("result=" + s);
                }
                RedisUtil.setMind(id, s);
            }
        } else if (message.startsWith("saves=")) {
            String saveVal = message.substring(6);
            SaveMind mind = JsonUtil.jsonToPojo(saveVal, SaveMind.class);
            String s = RedisUtil.getMind(id);
            if (s != null && s.equalsIgnoreCase(mind.getVal())) {
                System.out.println("然而并没有改变");
            } else {
                RedisUtil.setMind(id, mind.getVal());
                CopyOnWriteArraySet<NettyWebSocket> webSockets = webSocketConcurrentHashMap.get(getId());
                for (NettyWebSocket webSocket : webSockets) {
                    if (!webSocket.equals(this)) {
                        webSocket.sendMessage("results=" + JsonUtil.objectToJson(mind));
                    }
                }
            }
        } else if (message.startsWith("save=")) {
            String s = RedisUtil.getMind(id);
            String saveVal = message.substring(5);
            System.out.println(saveVal);
            if (s.equalsIgnoreCase(saveVal)) {
                System.out.println("然而并没有改变");
            } else {
                RedisUtil.setMind(id, saveVal);
                CopyOnWriteArraySet<NettyWebSocket> webSockets = webSocketConcurrentHashMap.get(getId());
                for (NettyWebSocket webSocket : webSockets) {
                    if (!webSocket.equals(this)) {
                        webSocket.sendMessage("result=" + saveVal);
                    }
                }
            }
        } else if (message.startsWith("saveoffice")) {
            try {
                savefile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        sendMessage("HelloMind");
    }
    private static int i=0;

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        i++;
        System.out.println("close:"+i);
        CopyOnWriteArraySet<NettyWebSocket> webSockets = webSocketConcurrentHashMap.get(this.getId());
        if (webSockets != null) {
            webSockets.remove(this);
            if (webSockets.size() == 0) {
                closeSaveFile();
            }
        }
        this.ctx.channel().close();
        id = null;
        officeService = null;
        redisSessionDAO = null;
        staff=null;
        stringBuilder=null;
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }

    public void sendMessage(String message) {
        if (message.startsWith("result=")){
            RedisUtil.set(Constant.LOCK_FILENAME+":"+getId(),"usering");
        }
        this.ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * 将文件保存一个版本
     */
    private void savefile() throws Exception {
      getOfficeService().saveOffice(getId(),staff.getId()+"",this.cookie);
    }
    private void closeSaveFile() throws Exception {
        getOfficeService().saveOffice(getId(),staff.getId()+"",this.cookie);
        RedisUtil.delete(RedisUtil.MIND_KEY+getId());
        redisTemplate.delete(Constant.LOCK_FILENAME+":"+getId());
        System.out.println(Constant.LOCK_FILENAME+":"+getId());
    }
}
