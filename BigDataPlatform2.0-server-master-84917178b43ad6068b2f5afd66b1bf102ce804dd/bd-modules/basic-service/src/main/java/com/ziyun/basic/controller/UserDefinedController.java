package com.ziyun.basic.controller;

import com.ziyun.basic.entity.SysorgTree;
import com.ziyun.basic.server.UserDefinedServer;
import com.ziyun.basic.vo.Params;
import com.ziyun.utils.requests.CommResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by admin on 2018/1/25.
 */
@Controller
@RequestMapping("/v2/definition")
@Api(tags = "用户自定义", description = "用户自定义为相关api")
public class UserDefinedController {
    @Autowired
    private UserDefinedServer userDefinedServer;

    @RequestMapping("/option")
    @ResponseBody
    public CommResponse getClasscodeByDefined(Params params) {
        return CommResponse.success(userDefinedServer.getClasscodeByDefined(params));
    }

//    @ApiOperation(value = "获取班级数据权限", notes = "根据机构编码获取班级数据权限", httpMethod = "POST")
//    @ApiImplicitParam(paramType = "query", name = "orgCode", dataType = "String", required = true, value = "机构、单位编码")
//    @RequestMapping("/organ")
//    @ResponseBody
//    public CommResponse getOrganAOP(Params params) {
//        /**
//         //如果是班主任，从缓存中拿到班级返回
//         Set<String> classes = TokenCasheManager.getDataPermissionsCache(LoginManager.getToken());
//         if (classes.size() == 410) {
//         return CommResponse.success(userDefinedServer.getOrgans(params));
//         } else {
//         List<Map<String, String>> classList = new ArrayList<>();
//         for (String classStr : classes) {
//         Map<String, String> orgName = new HashMap<>();
//         orgName.put("orgCode", classStr);
//         classList.add(orgName);
//         }
//         return CommResponse.success(classList);
//         }
//         */
//        Set<String> classes = TokenCasheManager.getPermissionsCache(LoginManager.getToken());
//        List<SysorgTree> campusList = userDefinedServer.getDataAuthTree(params);
//
//        //拥有所有班级数据权限（校长）
//        if (classes.size() == 410) {
//            return CommResponse.success(campusList);
//        } else {
//            List<String> authClassList = new ArrayList<>(classes);
//            //1. 遍历校区
//            Iterator<SysorgTree> iteratorCampus = campusList.iterator();
//            while (iteratorCampus.hasNext()) {
//                SysorgTree itemCampus = iteratorCampus.next();
//                //2. 遍历学院
//                List<SysorgTree> instituteList = itemCampus.getSysorgtree();
//                Iterator<SysorgTree> iteratorInstitute = instituteList.iterator();
//                while (iteratorInstitute.hasNext()) {
//                    SysorgTree itemInstitute = iteratorInstitute.next();
//                    //3. 遍历专业
//                    List<SysorgTree> majorList = itemInstitute.getSysorgtree();
//                    Iterator<SysorgTree> iteratorMajor = majorList.iterator();
//                    while (iteratorMajor.hasNext()) {
//                        SysorgTree itemMajor = iteratorMajor.next();
//                        //4. 遍历班级
//                        List<SysorgTree> classList = itemMajor.getSysorgtree();
//                        classList.removeIf(itemClass -> !authClassList.contains(itemClass.getOrgCode()));
//                        //如果专业下班级已被清空，则清除该系
//                        if (classList.size() == 0) {
//                            iteratorMajor.remove();
//                        }
//                    }
//                    //如果学院下专业已被清空，则清除该专业
//                    if (majorList.size() == 0) {
//                        iteratorInstitute.remove();
//                    }
//                }
//                //如果分校下学院已被清空，则清除该学院
//                if (instituteList.size() == 0) {
//                    iteratorCampus.remove();
//                }
//            }
//            return CommResponse.success(campusList);
//        }
//    }

    /**
     * 获取入学年份
     *
     * @param params token：YWRtaW46MjEyMzJmMjk3YTU3YTVhNzQzODk0YTBlNGE4MDFmYzNkc14jKCYoQCklJSNmZGFzMiNAQCQ=
     * @return {"2017","2016"}
     */
    @ApiOperation(value = "获取入学年份", notes = "获取入学年份")
    @RequestMapping("/enrollmentYear")
    @ResponseBody
    public CommResponse getEnrollmentYear(Params params) {
        List<Map<String, Object>> result = userDefinedServer.getEnrollmentYear();
        return CommResponse.success(result);
    }

    @ApiOperation(value = "获取班级数据权限", notes = "根据机构编码获取班级数据权限--目前暂时的", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orgCode", dataType = "String", required = true, value = "机构、单位编码"),
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Long", required = true, value = "角色ID")
    })
    @RequestMapping("/organ")
    @ResponseBody
    public CommResponse getOrgan(Params params) {
        List<SysorgTree> list = userDefinedServer.getDataAuthTree(params);
        if (CollectionUtils.isNotEmpty(list)) {
            //1. 遍历校区
            Iterator<SysorgTree> iteratorCampus = list.iterator();
            while (iteratorCampus.hasNext()) {
                SysorgTree itemCampus = iteratorCampus.next();
                //2. 遍历学院
                List<SysorgTree> instituteList = itemCampus.getSysorgtree();
                Iterator<SysorgTree> iteratorInstitute = instituteList.iterator();
                while (iteratorInstitute.hasNext()) {
                    SysorgTree itemInstitute = iteratorInstitute.next();
                    //3. 遍历专业
                    List<SysorgTree> majorList = itemInstitute.getSysorgtree();
                    Iterator<SysorgTree> iteratorMajor = majorList.iterator();
                    while (iteratorMajor.hasNext()) {
                        SysorgTree itemMajor = iteratorMajor.next();
                        //4. 遍历班级
                        List<SysorgTree> classList = itemMajor.getSysorgtree();
                        //如果专业下班级已被清空，则清除该系
                        if (classList.size() == 0) {
                            iteratorMajor.remove();
                        } else {
                            //如果班级不为空，则在专业和班级之间添加 18届，17届，16届等字段
                            classList = classList.stream().sorted((r2, r1) -> r1.getOrgCode().compareTo(r2.getOrgCode())).collect(Collectors.toList());
                            List<SysorgTree> treeList = getDiffSession(classList);
                            itemMajor.setSysorgtree(treeList);

                        }
                    }
                    //如果学院下专业已被清空，则清除该专业
                    if (majorList.size() == 0) {
                        iteratorInstitute.remove();
                    }
                }
                //如果分校下学院已被清空，则清除该学院
                if (instituteList.size() == 0) {
                    iteratorCampus.remove();
                }
            }
        }
        SysorgTree tree = new SysorgTree();
        tree.setOrgName("全部");
        tree.setOrgCode("");
        list.add(0, tree);
        return CommResponse.success(list);
    }

    List<SysorgTree> getDiffSession(List<SysorgTree> treeList) {
        if (null == treeList) {
            return null;
        }
        //根据treeList产生一个新的List
        Set<String> sets = new LinkedHashSet<>();
        if (treeList.size() > 0) {
            treeList.forEach(r -> sets.add(r.getOrgCode().substring(0, 2)));
        }
        List<SysorgTree> newTree = new ArrayList<>();
        sets.forEach(r -> {
            SysorgTree tree = new SysorgTree();
            tree.setOrgCode(r + "级");
            tree.setOrgName(r + "级");
            newTree.add(tree);
        });
        Map<String, List<SysorgTree>> map = new HashMap<>();
        treeList.forEach(r -> {
            if (!map.containsKey(r.getOrgCode().substring(0, 2))) {
                List<SysorgTree> tree = new ArrayList<>();
                tree.add(r);
                map.put(r.getOrgCode().substring(0, 2), tree);
            } else {
                List<SysorgTree> treeList1 = map.get(r.getOrgCode().substring(0, 2));
                treeList1.add(r);
                map.put(r.getOrgCode().substring(0, 2), treeList1);
            }
        });
        newTree.forEach(r -> {
            if (map.containsKey(r.getOrgCode().substring(0, 2))) {
                r.setSysorgtree(map.get(r.getOrgCode().substring(0, 2)));
            }
        });
        return newTree;
    }
}
