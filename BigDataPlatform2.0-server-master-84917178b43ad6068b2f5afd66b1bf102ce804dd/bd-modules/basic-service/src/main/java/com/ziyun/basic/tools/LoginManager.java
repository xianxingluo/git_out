package com.ziyun.basic.tools;

import com.ziyun.basic.realm.ShiroToken;
import com.ziyun.utils.cache.RedisUtils;
import com.ziyun.utils.cache.TokenCasheManager;
import com.ziyun.utils.spring.SpringContextUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * Shiro管理下的Token工具类
 */
//@Service
public class LoginManager {
    //用户登录管理
    //@Resource
    //public static final UserRealm realm;
    //public static final UserRealm realm = SpringContextUtil.getBean("userRealm",UserRealm.class);

//    public static final RedisUtils redisUtils = SpringContextUtil.getBean("redisUtils", RedisUtils.class);

    /**
     * 获取当前登录用户的token
     * <p>
     * 获取认证
     *
     * @return
     */
    public static String getToken() {
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        return token;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param rememberMe
     */
//    public static void login(String username, String password, Boolean rememberMe) {
//        ShiroToken token = new ShiroToken(username, password);
//        //token.setRememberMe(rememberMe);
//        SecurityUtils.getSubject().login(token);
//    }

    ///**
    // * 判断是否登录
    // * @return
    // */
    //public static boolean isLogin() {
    //	return null != SecurityUtils.getSubject().getPrincipal();
    //}

    /**
     * 退出登录
     */
    public static void logout() {
        //清理该用户的所有缓存，没有缓存，下次操作的时候也会主动踢下线
        TokenCasheManager.clearAllLoginData(LoginManager.getToken());
        //
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
    }

    ///**
    // * 清空当前用户权限信息。
    // * 目的：为了在判断权限的时候，再次会再次 <code>doGetAuthorizationInfo(...)  </code>方法。
    // * ps：	当然你可以手动调用  <code> doGetAuthorizationInfo(...)  </code>方法。
    // * 		这里只是说明下这个逻辑，当你清空了权限，<code> doGetAuthorizationInfo(...)  </code>就会被再次调用。
    // */
    //public static void clearNowUserAuth(){
    //	/**
    //	 * 这里需要获取到shrio.xml 配置文件中，对Realm的实例化对象。才能调用到 Realm 父类的方法。
    //	 */
    //	/**
    //	 * 获取当前系统的Realm的实例化对象，方法一（通过 @link org.apache.shiro.web.mgt.DefaultWebSecurityManager 或者它的实现子类的{Collection<Realm> getRealms()}方法获取）。
    //	 * 获取到的时候是一个集合。Collection<Realm>
    //		RealmSecurityManager securityManager =
    //	    			(RealmSecurityManager) SecurityUtils.getSecurityManager();
    //	  	SampleRealm realm = (SampleRealm)securityManager.getRealms().iterator().next();
    //	 */
    //	/**
    //	 * 方法二、通过ApplicationContext 从Spring容器里获取实列化对象。
    //	 */
    //	realm.clearCachedAuthorizationInfo();
    //	/**
    //	 * 当然还有很多直接或者间接的方法，此处不纠结。
    //	 */
    //}


    ///**
    // * 根据UserIds 	清空权限信息。
    // * @param id	用户ID
    // */
    //public static void clearUserAuthByUserId(Long...userIds){
    //
    //	if(null == userIds || userIds.length == 0)	return ;
    //	List<SimplePrincipalCollection> result = customSessionManager.getSimplePrincipalCollectionByUserId(userIds);
    //
    //	for (SimplePrincipalCollection simplePrincipalCollection : result) {
    //		realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
    //	}
    //}


    ///**
    // * 方法重载
    // * @param userIds
    // */
    //public static void clearUserAuthByUserId(List<Long> userIds) {
    //	if(null == userIds || userIds.size() == 0){
    //		return ;
    //	}
    //	clearUserAuthByUserId(userIds.toArray(new Long[0]));
    //}
}
