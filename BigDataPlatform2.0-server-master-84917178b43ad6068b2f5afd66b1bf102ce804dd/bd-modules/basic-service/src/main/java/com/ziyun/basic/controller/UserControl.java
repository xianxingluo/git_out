package com.ziyun.basic.controller;

import com.ziyun.basic.realm.UserRealm;
import com.ziyun.basic.server.StudentServer;
import com.ziyun.basic.tools.LoginManager;
import com.ziyun.common.model.auth.DataPermission;
import com.ziyun.common.model.auth.FuncPermission;
import com.ziyun.common.model.auth.User;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录认证的控制器
 */
@Controller
public class UserControl {

    @Resource(name = "redisTemplate")
    private RedisTemplate RedisTemplate;

    @Autowired
    private StudentServer studentServer;


    /**
     * @api {POST} /login
     * @apiName login
     * @apiGroup login
     * @apiVersion 2.0.0
     * @apiDescription 登录
     * @apiPermission 登录
     * @apiSampleRequest http://127.0.0.1:80/login
     * @apiParam {String} [username] 开始时间
     * @apiParam {String} [password] 结束时间
     * @apiParamExample {json} 请求例子:
     * {
     * "schoolCode": "jkd"
     * }
     * @apiSuccess (200) {String} message 提示信息code 根据code去查找错误提示信息
     * @apiSuccess (200) {int} statusCode 200
     * @apiSuccessExample {json} 返回样例:
     * {
     * "statusCode": 200,
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     * @apiErrorExample {json} 错误返回:
     * {
     * "statusCode": 14695
     * "message": "提示信息code 根据code去查找错误提示信息"
     * }
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map login(User user, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        UserRealm realm = new UserRealm();
        Long userId = 173L;
        //获取功能权限
        List<FuncPermission> authFuncPermission = realm.getAuthFuncPermission(userId);
        List<String> collect = authFuncPermission.stream().map(r -> r.getPerms()).collect(Collectors.toList());
        resultMap.put("statusCode", 200);
        resultMap.put("message", "登录成功");
        //backUrl
        resultMap.put("back_url", "/api/v2/student/pagestudent");
        resultMap.put("menus", collect);
        //用户登陆成功，返回本学期，上学期，上学年的开始时间和结束时间
        List<LinkedHashMap> termDate = studentServer.getTermDate();
        resultMap.put("termDate", termDate);
        //此段代码先注释掉
		/*try {
			//ShiroToken token = new ShiroToken(user.getUsername(), user.getPassword());
			//SecurityUtils.getSubject().login(token);
			LoginManager.login(user.getUsername(),user.getPassword(),true);
			 resultMap.put("statusCode", 200);
			 resultMap.put("message", "登录成功");
			//用户登陆成功，返回本学期，上学期，上学年的开始时间和结束时间
			List<LinkedHashMap> termDate = studentServer.getTermDate();
			resultMap.put("termDate", termDate);
			//将用户的账号base64转码后存入redis，并返回给前端
			String tokenKey= TokenCasheManager.generateToken(user.getUsername(),user.getPassword());
			//String nameBase = Base64.encodeBase64String((user.getUsername()+" "+user.getPassword()).getBytes());
			//将这个base返回给前端
			resultMap.put(CacheConstant.TOKEN_CACHE,tokenKey);

			 //将菜单权限放入resultMap，返回给前端
			resultMap.put(CacheConstant.MENUS_CACHE, TokenCasheManager.getMenusCache(LoginManager.getToken()));
			resultMap.put(CacheConstant.USER_CACHE, TokenCasheManager.getUserCache(LoginManager.getToken()));
			 //resultMap.put("menus", SecurityUtils.getSubject().getSession().getAttribute("menus"));
			 //清除session中的menus权限，由前端缓存
			 //SecurityUtils.getSubject().getSession().removeAttribute("menus");
			 //request.getSession().setAttribute("username", user.getUsername());

			//生成一个list<map>用来存储用户信息
			//Map<String,Object> map = new HashedMap();
			//map.put("user",user);
			////将用户信息存入redis中，存在时长3600秒
			//redisUtils.set(nameBase,map,3600);
			 *//**
         * shiro 获取登录之前的地址 之前0.1版本这个没判断空。
         *//*
			SavedRequest savedRequest = WebUtils.getSavedRequest(request);
			String url = null;
			if (null != savedRequest) {
				url = savedRequest.getRequestUrl();
			}

			// 如果登录之前没有地址，那么就跳转到首页。
			if (StringUtils.isBlank(url)||("/".equals(url))) {
				url = request.getContextPath() + "/index";
			}
			// 跳转地址
			 resultMap.put("back_url", url);
			*//**
         * 这里其实可以直接catch Exception，然后抛出 message即可，但是最好还是各种明细catch 好点。。
         *//*
		} catch (DisabledAccountException e) {
			 resultMap.put("statusCode", 500);
			 resultMap.put("message", "帐号已经禁用。");
		} catch (AccountException e) {
			 resultMap.put("statusCode", 500);
			 resultMap.put("message", e.getMessage());
		} catch (Exception e) {
			 resultMap.put("statusCode", 500);
			 resultMap.put("message", "帐号或密码错误");
		}*/
        return resultMap;
    }

    /**
     * 退出系统
     *
     * @param session Session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) throws Exception {
        LoginManager.logout();
        return "redirect:/index";
    }

    /**
     * 退出系统
     *
     * @param session Session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logoutajax")
    @ResponseBody
    public Map logoutAjax(HttpSession session) throws Exception {
        LoginManager.logout();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        return resultMap;
    }

//	@RequestMapping(value = "/testapi")
//	@ResponseBody
//	public SysUser testapi(String username) throws Exception {
////		   Map<String, Object> paramsx = new HashMap<String, Object>();
////		    paramsx.put("username", username);
//		    String serviceUrl = com.ziyun.utils.api.ApiUrl.USER_GETUSER;
//		    BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiPost(Constant.C_TYPE_JSON, serviceUrl, username);
//		    JsonNode data = boResult.getData();
//		    if (data != null) {
//		    	SysUser user = JsonParams.formJson(data, SysUser.class);
//		    	return user;
//		    }
//
//		return null;
//	}

}