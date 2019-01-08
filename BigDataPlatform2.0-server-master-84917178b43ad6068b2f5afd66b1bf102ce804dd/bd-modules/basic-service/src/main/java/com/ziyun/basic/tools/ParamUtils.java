package com.ziyun.basic.tools;


import com.ziyun.basic.entity.OwnSchoolOrg;
import com.ziyun.basic.entity.SysUser;
import com.ziyun.basic.vo.Params;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ParamUtils {
    public static void main(String[] args) {
        OwnSchoolOrg par = new OwnSchoolOrg();
        par.setSchoolName("张家港");
//		par.setFacultyCode("机械学院");
//		par.setMajorCode("123");
//		par.setClassSelect("1111");
        System.out.println(getOrganString(par));
    }

    /**
     * 删除param参数里面和组织机构相关的参数
     *
     * @param param
     */
    public static void emptyParamsCode(Params param) {
        param.setSchoolCode(null);
        param.setFacultyCode(null);
        param.setClassSelect(null);
        param.setMajorCode(null);
    }

    /**
     * 返回组织机构的字符串
     *
     * @param own
     * @return
     */
    public static String getOrganString(OwnSchoolOrg own) {
        StringBuilder sb = new StringBuilder();
        if (own.getSchoolName() != null) {
            sb.append(own.getSchoolName() + "-");
        }
        if (own.getFacultyName() != null) {
            sb.append(own.getFacultyName() + "-");
        }
        if (own.getMajorName() != null) {
            sb.append(own.getMajorName() + "系-");
        }
        if (own.getClassCode() != null) {
            sb.append(own.getClassCode() + "班-");
        }
        int sbLength = sb.length();
        return sb.substring(0, sbLength - 1).toString();
    }

    //获得对象的属性数组
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }


    //通过属性和对象获得属性值
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object value = method.invoke(o);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public static List limitPage(List list, int start, int length) {
        int end = 0;
        //null值判断
        if (list == null) {
            return null;
        }
        //如果start大于list的长度则从0开始
        if (list.size() < start) {
            start = 0;
        }
        //如果截取的长度大于list的size则为size
        end = start + length > list.size() ? list.size() : start + length;
        return list.subList(start, end);
    }

    public static Map convertBase64(Map<String, Object> map) {
        String base64 = (String) map.get("base");
        //将base64解码
        if (StringUtils.isBlank(base64))
            return null;
        String user = new String(Base64.decodeBase64(base64));
        String[] arr = user.split(" ");
        map.put("username", arr[0]);
        return map;
    }

    public static Map getUserByToken(Map<String, Object> map) {
        String token = String.valueOf(map.get("token"));
        SysUser user = (SysUser) TokenCasheManager.getUserCache(token);
        if (user == null)
            return null;
        map.put("username", user.getUsername());
        return map;
    }
}
