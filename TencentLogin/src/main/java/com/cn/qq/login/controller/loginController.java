/**   
 * Copyright © 2017 公司名. All rights reserved.
 * 
 * @Title: loginController.java 
 * @Prject: TencentLogin
 * @Package: com.cn.qq.login.controller 
 * @Description: TODO
 * @author: hong   
 * @date: 2017年10月23日 上午10:02:42 
 * @version: V1.0   
 */
package com.cn.qq.login.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

/** 
 * @ClassName: loginController 
 * @Description: TODO
 * @author: hong
 * @date: 2017年10月23日 上午10:02:42  
 */
@Controller
@RequestMapping("qq")
public class loginController {
	
	@RequestMapping("do_login.chm")
	public void diLogin(HttpServletRequest request,HttpServletResponse response) throws IOException, QQConnectException {
		
			//调取用户授权页面
			response.sendRedirect(new Oauth().getAuthorizeURL(request));
		
	}
	
	@RequestMapping("login")
	public void login(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//根据Query获取Token
		AccessToken accessTokenObj=new Oauth().getAccessTokenByQueryString(request.getQueryString(), request.getParameter("state"));
		//从request 中获取到Code 来拿去Token
		String token=null;
		//有效期
		long  expireIn;
		//获取到的Token
		token=accessTokenObj.getAccessToken();
		//获取
		expireIn=accessTokenObj.getExpireIn();
		//准备获取OpenID		
		OpenID openIDObj=new OpenID(token);
		//获取openid
		String openid=openIDObj.getUserOpenID();
		//获取qq空间信息
		UserInfo qzone=new UserInfo(token, openid);
		//获取用户对象
		UserInfoBean qzoneUser=qzone.getUserInfo();
		response.getWriter().write(qzoneUser.toString());
		
	}
}
