![image](https://github.com/meteorOSS/revoke-listener/assets/61687266/6505b2e6-a608-4280-872c-5cfae15afe75)

基于 [WeChatBc](https://github.com/meteorOSS/WeChatBc) 实现

## 功能

监听消息撤回 (图片，文字，视频消息)

### 配置文件

``` yaml
message:
  TextMsg: '{nick_name} 撤回了消息: {content}'
  VideoMsg: '{nick_name} 撤回了一条视频消息: '
  ImageMsg: '{nick_name} 撤回了一条图片消息: '
```

## 使用方法
放入wechatbc的plugins文件夹，随后重启服务
