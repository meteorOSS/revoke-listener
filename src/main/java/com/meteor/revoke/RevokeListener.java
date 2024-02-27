package com.meteor.revoke;
import com.meteor.wechatbc.config.ConfigurationSection;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.event.EventHandler;
import com.meteor.wechatbc.impl.HttpAPI;
import com.meteor.wechatbc.impl.event.Listener;
import com.meteor.wechatbc.impl.event.sub.ReceiveMessageEvent;
import com.meteor.wechatbc.impl.model.MsgType;
import com.meteor.wechatbc.impl.model.message.ImageMessage;
import com.meteor.wechatbc.impl.model.message.RevokeMessage;
import com.meteor.wechatbc.impl.model.message.TextMessage;
import com.meteor.wechatbc.impl.model.message.VideoMessage;
import com.meteor.wechatbc.impl.plugin.BasePlugin;

import java.io.File;

public class RevokeListener extends BasePlugin
implements Listener {
    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getLogger().info("插件已载入...");
        getWeChatClient().getEventManager().registerPluginListener(this,this);
    }

    private HttpAPI httpAPI(){
        return getWeChatClient().getWeChatCore().getHttpAPI();
    }

    private String getTip(MsgType msgType,String nickName,String content){
        ConfigurationSection message = getConfig().getConfigurationSection("message");
        return message.getString(msgType.name())
                .replace("{nick_name}",nickName)
                .replace("{content}",content==null?"":content);
    }

    @EventHandler
    public void receiveMessage(ReceiveMessageEvent receiveMessageEvent){
        Message message = receiveMessageEvent.getMessage();
        if(message.getMsgType() == MsgType.RevokeMsg){
            message = ((RevokeMessage)message).getOldMessage();
            Contact contact = getWeChatClient().getContactManager().getContactCache().getIfPresent(message.getSenderUserName());
            if(message.getMsgType() == MsgType.TextMsg){
                TextMessage textMessage = (TextMessage) message;
                String tip = getTip(MsgType.TextMsg, contact.getNickName(), textMessage.getContent());
                httpAPI().sendMessage(message.getFromUserName(),tip);
            }else if(message.getMsgType() == MsgType.VideoMsg){
                VideoMessage videoMessage = (VideoMessage) message;
                String tip = getTip(MsgType.VideoMsg, contact.getNickName(), null);
                httpAPI().sendMessage(message.getFromUserName(),tip);
                File file = videoMessage.saveFile(new File(getDataFolder(), "video.mp4"));
                httpAPI().sendVideo(message.getFromUserName(),file);
            }else if(message.getMsgType() == MsgType.ImageMsg){
                ImageMessage imageMessage = (ImageMessage) message;
                String tip = getTip(MsgType.ImageMsg, contact.getNickName(), null);
                httpAPI().sendMessage(message.getFromUserName(),tip);
                File file = imageMessage.saveImage(new File(getDataFolder(), "pic.png"),"png");
                httpAPI().sendImage(message.getFromUserName(),file);
            }
        }
    }


}
