package me.chanjar.weixin.mp.demo;

import com.ossez.wechat.common.api.WxConsts;
import com.ossez.wechat.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoOAuth2Handler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) {
    String href = "<a href=\"" + wxMpService.getOAuth2Service().buildAuthorizationUrl(
      wxMpService.getWxMpConfigStorage().getOauth2redirectUri(),
      WxConsts.OAuth2Scope.SNSAPI_USERINFO, null) + "\">测试oauth2</a>";
    return WxMpXmlOutMessage.TEXT().content(href)
      .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
      .build();
  }
}
