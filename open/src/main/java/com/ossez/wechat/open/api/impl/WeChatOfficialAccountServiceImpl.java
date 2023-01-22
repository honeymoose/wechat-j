package com.ossez.wechat.open.api.impl;

import cn.binarywang.wx.miniapp.json.WxMaGsonBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.ossez.wechat.open.bean.mp.FastRegisterResult;
import com.ossez.wechat.open.bean.result.WxAmpLinkResult;
import com.ossez.wechat.open.bean.result.WxOpenResult;
import lombok.SneakyThrows;
import com.ossez.wechat.common.exception.WxErrorException;
import com.ossez.wechat.oa.config.WxMpConfigStorage;
import com.ossez.wechat.open.api.WxOpenComponentService;
import com.ossez.wechat.open.api.WeChatOfficialAccountService;

import java.net.URLEncoder;
import java.util.Objects;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
public class WeChatOfficialAccountServiceImpl extends com.ossez.wechat.oa.api.impl.WeChatOfficialAccountServiceImpl implements WeChatOfficialAccountService {
  private WxOpenComponentService wxOpenComponentService;
  private WxMpConfigStorage wxMpConfigStorage;
  private String appId;

  public WeChatOfficialAccountServiceImpl(WxOpenComponentService wxOpenComponentService, String appId, WxMpConfigStorage wxMpConfigStorage) {
//    wxOpenComponentService.oauth2getAccessToken(appId)
    this.wxOpenComponentService = wxOpenComponentService;
    this.appId = appId;
    this.wxMpConfigStorage = wxMpConfigStorage;
    setOAuth2Service(new WxOpenMpOAuth2ServiceImpl(wxOpenComponentService, getOAuth2Service(), wxMpConfigStorage));
    initHttp();
  }

  @Override
  public WxMpConfigStorage getWxMpConfigStorage() {
    return wxMpConfigStorage;
  }

  @Override
  public String getAccessToken(boolean forceRefresh) throws WxErrorException {
    return wxOpenComponentService.getAuthorizerAccessToken(appId, forceRefresh);
  }

  @SneakyThrows
  @Override
  public String getFastRegisterAuthUrl(String redirectUri, Boolean copyWxVerify) {
    String copyInfo = Objects.equals(copyWxVerify, false) ? "0" : "1";
    String componentAppId = wxOpenComponentService.getWxOpenConfigStorage().getComponentAppId();
    String encoded = URLEncoder.encode(redirectUri, "UTF-8");
    return String.format(URL_FAST_REGISTER_AUTH, appId, componentAppId, copyInfo, encoded);
  }

  @Override
  public FastRegisterResult fastRegister(String ticket) throws WxErrorException {
    String json = post(API_FAST_REGISTER, ImmutableMap.of("ticket", ticket));
    return FastRegisterResult.fromJson(json);
  }


  @Override
  public WxAmpLinkResult getWxAmpLink() throws WxErrorException {
    String response = post(API_WX_AMP_LINK_GET, "{}");
    return WxMaGsonBuilder.create().fromJson(response, WxAmpLinkResult.class);
  }

  @Override
  public WxOpenResult wxAmpLink(String appid, String notifyUsers, String showProfile) throws WxErrorException {
    JsonObject params = new JsonObject();
    params.addProperty("appid", appid);
    params.addProperty("notify_users", notifyUsers);
    params.addProperty("show_profile", showProfile);
    String response = post(API_WX_AMP_LINK_CREATE, params.toString());
    return WxMaGsonBuilder.create().fromJson(response, WxOpenResult.class);
  }

  @Override
  public WxOpenResult wxAmpUnLink(String appid) throws WxErrorException {
    JsonObject params = new JsonObject();
    params.addProperty("appid", appid);
    String response = post(API_WX_AMP_LINK_UN, params.toString());
    return WxMaGsonBuilder.create().fromJson(response, WxOpenResult.class);
  }

}