package com.daxingframe.frame.network.volley;

import android.content.Intent;

/**
 * 继承自 VolleyError，代表请求认证失败错误，如 RespondeCode 的 401 和 403。
 */
@SuppressWarnings("serial")
public class AuthFailureError extends VolleyError {
    /** An intent that can be used to resolve this exception. (Brings up the password dialog.) */
    private Intent mResolutionIntent;

    public AuthFailureError() { }

    public AuthFailureError(Intent intent) {
        mResolutionIntent = intent;
    }

    public AuthFailureError(NetworkResponse response) {
        super(response);
    }

    public AuthFailureError(String message) {
        super(message);
    }

    public AuthFailureError(String message, Exception reason) {
        super(message, reason);
    }

    public Intent getResolutionIntent() {
        return mResolutionIntent;
    }

    @Override
    public String getMessage() {
        if (mResolutionIntent != null) {
            /**
             * credentials: 凭证
             */
            return "User needs to (re)enter credentials.";
        }
        return super.getMessage();
    }

    /**
     * HTTP 错误 401
     401.1 未授权：登录失败
     此错误表明传输给服务器的证书与登录服务器所需的证书不匹配。
     请与 Web 服务器的管理员联系，以确认您是否具有访问所请求资源的权限。
     401.2 未授权：服务器的配置导致登录失败
     此错误表明传输给服务器的证书与登录服务器所需的证书不匹配。此错误通常由未发送正确的 WWW 验证表头字段所致。
     请与 Web 服务器的管理员联系，以确认您是否具有访问所请求资源的权限。
     401.3 未授权：由于资源中的 ACL 而未授权
     此错误表明客户所传输的证书没有对服务器中特定资源的访问权限。此资源可能是客户机中的地址行所列出的网页或文件，也可能是处理客户机中的地址行所列出的文件所需服务器上的其他文件。
     请记录试图访问的完整地址，并与 Web 服务器的管理员联系以确认您是否具有访问所请求资源的权限。
     401.4 未授权：授权服务被筛选程序拒绝
     此错误表明 Web 服务器已经安装了筛选程序，用以验证连接到服务器的用户。此筛选程序拒绝连接到此服务器的真品证书的访问。
     请记录试图访问的完整地址，并与 Web 服务器的管理员联系以确认您是否具有访问所请求资源的权限。
     401.5 未授权：ISAPI/CGI 应用程序的授权失败
     此错误表明试图使用的 Web服务器中的地址已经安装了 ISAPI 或 CGI程序，在继续之前用以验证用户的证书。此程序拒绝用来连接到服务器的真品证书的访问。
     请记录试图访问的完整地址，并与 Web服务器的管理员联系以确认您是否具有访问所请求资源的权限

     HTTP 错误 403
     403.1 禁止：禁止执行访问
     如果从并不允许执行程序的目录中执行 CGI、ISAPI或其他执行程序就可能引起此错误。
     如果问题依然存在，请与 Web 服务器的管理员联系。
     403.2 禁止：禁止读取访问
     如果没有可用的默认网页或未启用此目录的目录浏览，或者试图显示驻留在只标记为执行或脚本权限的目录中的HTML 页时就会导致此错误。
     如果问题依然存在，请与 Web 服务器的管理员联系。
     403.3 禁止：禁止写访问
     如果试图上载或修改不允许写访问的目录中的文件，就会导致此问题。
     如果问题依然存在，请与 Web服务器的管理员联系。
     403.4 禁止：需要 SSL
     此错误表明试图访问的网页受安全套接字层（SSL）的保护。要查看，必须在试图访问的地址前输入https:// 以启用 SSL。
     如果问题依然存在，请与 Web服务器的管理员联系。
     403.5 禁止：需要 SSL 128
     此错误消息表明您试图访问的资源受 128位的安全套接字层（SSL）保护。要查看此资源，需要有支持此SSL 层的浏览器。
     请确认浏览器是否支持 128 位 SSL安全性。如果支持，就与 Web服务器的管理员联系，并报告问题。
     403.6 禁止：拒绝 IP 地址
     如果服务器含有不允许访问此站点的 IP地址列表，并且您正使用的 IP地址在此列表中，就会导致此问题。
     如果问题依然存在，请与 Web服务器的管理员联系。
     403.7 禁止：需要用户证书
     当试图访问的资源要求浏览器具有服务器可识别的用户安全套接字层（SSL）证书时就会导致此问题。可用来验证您是否为此资源的合法用户。
     请与 Web服务器的管理员联系以获取有效的用户证书。
     403.8 禁止：禁止站点访问
     如果 Web服务器不为请求提供服务，或您没有连接到此站点的权限时，就会导致此问题。
     请与 Web 服务器的管理员联系。
     403.9 禁止访问：所连接的用户太多
     如果 Web太忙并且由于流量过大而无法处理您的请求时就会导致此问题。请稍后再次连接。
     如果问题依然存在，请与 Web 服务器的管理员联系。
     403.10 禁止访问：配置无效
     此时 Web 服务器的配置存在问题。
     如果问题依然存在，请与 Web服务器的管理员联系。
     403.11 禁止访问：密码已更改
     在身份验证的过程中如果用户输入错误的密码，就会导致此错误。请刷新网页并重试。
     如果问题依然存在，请与 Web服务器的管理员联系。
     403.12 禁止访问：映射程序拒绝访问
     拒绝用户证书试图访问此 Web 站点。
     请与站点管理员联系以建立用户证书权限。如果必要，也可以更改用户证书并重试。

     HTTP 错误 404
     404 找不到
     Web 服务器找不到您所请求的文件或脚本。请检查URL 以确保路径正确。
     如果问题依然存在，请与服务器的管理员联系。
     */
}
