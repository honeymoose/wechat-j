package com.ossez.wechat.open.api.impl;

import com.ossez.wechat.common.bean.oauth2.WxOAuth2AccessToken;
import com.ossez.wechat.common.exception.WxErrorException;
import com.ossez.wechat.common.service.WxOAuth2Service;
import com.ossez.wechat.common.service.WxOAuth2ServiceDecorator;
import com.ossez.wechat.common.util.http.URIUtil;
import com.ossez.wechat.oa.config.WxMpConfigStorage;
import com.ossez.wechat.open.api.WxOpenComponentService;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信 第三方平台对于公众号 oauth2 的实现类
 *
 * @author <a href="https://www.sacoc.cn">广州跨界</a>
 */
public class WxOpenMpOAuth2ServiceImpl extends WxOAuth2ServiceDecorator {

  private final WxOpenComponentService wxOpenComponentService;
  private final WxMpConfigStorage wxMpConfigStorage;


  public WxOpenMpOAuth2ServiceImpl(WxOpenComponentService wxOpenComponentService, WxOAuth2Service wxOAuth2Service, WxMpConfigStorage wxMpConfigStorage) {
    super(wxOAuth2Service);
    this.wxOpenComponentService = wxOpenComponentService;
    this.wxMpConfigStorage = wxMpConfigStorage;
  }

  /**
   * 第三方平台代公众号发起网页授权
   * 文档地址:<a href="https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/2.0/api/Before_Develop/Official_Accounts/official_account_website_authorization.html">第三方平台代公众号发起网页授权</a>
   *
   * @param code 微信授权code
   * @return 微信用户信息
   * @throws WxErrorException 如果微信接口调用失败将抛出此异常
   */
  @Override
  public WxOAuth2AccessToken getAccessToken(String code) throws WxErrorException {
    String url = String.format(
      WxOpenComponentService.OAUTH2_ACCESS_TOKEN_URL,
      wxMpConfigStorage.getAppId(),
      code,
      wxOpenComponentService.getWxOpenConfigStorage().getComponentAppId()
    );
    String responseContent = wxOpenComponentService.get(url);
    return WxOAuth2AccessToken.fromJson(responseContent);
  }

  @Override
  public String buildAuthorizationUrl(String redirectUri, String scope, String state) {
    return String.format(
      WxOpenComponentService.CONNECT_OAUTH2_AUTHORIZE_URL,
      wxMpConfigStorage.getAppId(),
      URIUtil.encodeURIComponent(redirectUri),
      scope,
      StringUtils.trimToEmpty(state),
      wxOpenComponentService.getWxOpenConfigStorage().getComponentAppId()
    );
  }
}
