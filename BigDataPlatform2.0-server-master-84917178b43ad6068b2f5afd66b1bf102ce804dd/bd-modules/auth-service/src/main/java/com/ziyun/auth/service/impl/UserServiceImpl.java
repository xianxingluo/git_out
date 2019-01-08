package com.ziyun.auth.service.impl;

import com.ziyun.auth.constant.AuthConstant;
import com.ziyun.auth.constant.CacheConstant;
import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.mapper.DataPermissionMapper;
import com.ziyun.auth.mapper.FuncPermissionMapper;
import com.ziyun.auth.mapper.RoleMapper;
import com.ziyun.auth.mapper.UserMapper;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import com.ziyun.common.tools.RedisUtils;
import com.ziyun.common.tools.ValidatorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/26 15:19
 */
@Service
@Transactional
public class UserServiceImpl extends BaseService<User> implements IUserService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DataPermissionMapper dataPermissionMapper;
    @Autowired
    private FuncPermissionMapper funcPermissionMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    /**
     * 默认密码：888888
     */
    private static final String DEFAULT_PASSWORD = "56853f4e8a6b7d2121c8953ef45732bb";

    /**
     * 保存用户
     */
    @Override
    public int saveUser(User entity) {
        if (ArrayUtils.isNotEmpty(entity.getRoleIds()) && entity.getRoleIds().length == 1 && entity.getRoleIds()[0].equals(Constant.NO_ROLE_ID)) {
            entity.setStatus((byte) 0);
        } else {
            entity.setStatus((byte) 1);
        }
        //第一步：插入用户表
        int i = userMapper.saveUser(entity);
        //第二步：插入用户角色中间表（auth_user_role）
        userMapper.insertUserAndRole(entity);
        return i;
    }

    /**
     * 删除用户
     *
     * @param user
     */
    @Override
    public void deleteUser(User user) {
    /*    //1:注销登录用户
        logoutUserByIds(user.getIds());
        // 2：根据用户id，删除用户角色中间表（auth_user_role)，支持批量
        userMapper.deleteUserAndRoles(user);
        // 3：根据用户id，删除（auth_user）表的数据，支持批量
        userMapper.deleteUsers(user);*/
        //说明： 前端传过来的参数 access_token，createUserId,createRoleId,ids
        if (user.getCreateRoleId() != null) {
            user.setCreateRoleId(null);
            user.setCreateUserId(null);
        }
        //将删除的userId，设置为createUserId，查询该用户下所创建的用户
        if (ArrayUtils.isNotEmpty(user.getIds())) {
            user.setId(user.getIds()[0]);
            user.setIds(null);

        }
        List<Map<String, Object>> userRoleMapList = new ArrayList<>();
        recursionDeleteUser(user);

    }

    /**
     * 递归删除
     *
     * @param user
     */
    public void recursionDeleteUser(User user) {


        if (StringUtils.isNotBlank(user.getUsername())) {
            user.setUsername(null);
        }
        if (user.getId() != null) {
            user.setCreateUserId(user.getId());
        }
        List<User> userList = userMapper.selectByParam(user);

        if (CollectionUtils.isNotEmpty(userList)) {
            //循环递归
            userList.forEach(u -> {
                recursionDeleteUser(u);
            });
        }
        List<Role> roleList = roleMapper.getRoleByUser(user);
        if (CollectionUtils.isNotEmpty(roleList)) {
            roleList.forEach(r -> {
                r.setRoleId(r.getId());
                roleMapper.deleteRoles(r);
                //删除角色数据权限中间表
                dataPermissionMapper.deleteRoleData(r.getId(), null);
                funcPermissionMapper.deleteRoleFunc(r.getId(), null);
            });
        }
        user.setUserId(user.getId());
        //登出
        logoutUserByIds(new Long[]{user.getId()});
        userMapper.deleteUserAndRoles(user);
        userMapper.deleteUsers(user);
    }

    /**
     * 查询用户
     *
     * @param user
     * @return
     */
    @Override
    public List<User> listUsers(User user) {
        List<User> userList = userMapper.listUsers(user);

        if (CollectionUtils.isNotEmpty(userList)) {
            //获取用户下的角色
            userList.forEach(r -> {
                //获取创建人
                Long createUserId = r.getCreateUserId();
                User user1 = userMapper.selectByPrimaryKey(createUserId);
                r.setCreateUsername(user1.getUsername());
                Long id = r.getId();
                //查询用户关联的角色
                List<Role> roleList = roleMapper.userRelationRole(id);
                r.setRoleList(roleList);
                if (CollectionUtils.isEmpty(roleList)) {
                    Role role = new Role();
                    role.setId(Constant.NO_ROLE_ID);
                    role.setRoleName(Constant.NO_ROLE_NAME);
                    roleList.add(role);
                }

            });
        }
        return userList;
    }

    /**
     * 查询用户列表size
     *
     * @param user
     * @return
     */
    @Override
    public int listUserSize(User user) {
        return userMapper.listUserSize(user);
    }

    /**
     * 修改用户
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        if (null == user){
            return;
        }
        //无角色用户
        if (ArrayUtils.isNotEmpty(user.getRoleIds()) && user.getRoleIds().length == 1 && user.getRoleIds()[0].equals(Constant.NO_ROLE_ID)) {
            userMapper.deleteUserAndRoles(user);
            //禁用
            user.setStatus((byte) 0);
            //销毁access_token
            logoutUserByIds(new Long[]{user.getUserId()});
        } else {
            userMapper.deleteUserAndRoles(user);
            userMapper.insertUserAndRole(user);
            user.setStatus((byte) 1);
            logoutUserByIds(new Long[]{user.getUserId()});
        }
        //第三步：修改用户
        userMapper.updateUser(user);

    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User getUser(String username) {
        User userTemp = new User();
        userTemp.setUsername(username);
        User user = mapper.selectOne(userTemp);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public String importUserByExcel(MultipartFile file, String createUserId) {
        Workbook wb;
        try {
            if (isExcel2007(file.getOriginalFilename())) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                wb = new HSSFWorkbook(file.getInputStream());
            }
        } catch (IOException e) {
            return "文件为非excel文件，无法读取。";
        }

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            return "文件内容为空";
        }

        Row titles = sheet.getRow(0);
        // 标题总列数
        String vTitles = validateTitles(titles);
        if (StringUtils.isNotBlank(vTitles)) {
            return vTitles;
        }
        List<User> userList = new ArrayList<>();

        StringBuffer validateResult = dealUsers(createUserId, sheet, userList);

        if (StringUtils.isBlank(validateResult.toString())) {
//            mapper.insertList(userList);
        }
        return validateResult.toString();
    }

    /**
     * 校验excel表头
     *
     * @param titles
     * @return
     */
    private String validateTitles(Row titles) {
        int colNum = titles.getPhysicalNumberOfCells();
        if (colNum != com.ziyun.common.constant.Constant.INT_TEN) {
            return "excel表头不对。";
        }
        return "";
    }

    private StringBuffer dealUsers(String createUserId, Sheet sheet, List<User> userList) {
        StringBuffer validateResult = new StringBuffer();
        User user;
        Date createTime = new Date();
        Map<String, Integer> usernameMap = new HashMap<>(Constant.INT_TWO);
        for (int r = 1; r < sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            //excel尾部说明文字的判断
            int last = row.getLastCellNum();
            if (r == sheet.getLastRowNum() - 1 && last == 4) {
                continue;
            }

            StringBuffer sb = new StringBuffer();
            String name = getCellString(row.getCell(0));
            if (!ValidatorUtil.isUserName(name)) {
                sb.append("姓名：" + name + " 格式不对； ");
            }

            String username = getCellString(row.getCell(1));
            if (!ValidatorUtil.isUserName(username)) {
                sb.append("用户名：" + username + " 格式不对； ");
            } else if (existUser(username)) {
                sb.append("用户名：" + username + " 已存在； ");
            } else if (usernameMap.containsKey(username)) {
                //excel用户名是否重复
                int first = usernameMap.get(username);
                sb.append("第" + (r + 1) + "行用户名：" + username + " 与第" + (first + 1) + "行用户名重复； ");
            }

            //性别
            String sex = getCellString(row.getCell(3));
            if (!(StringUtils.isBlank(sex) || "男".equals(sex) || "女".equals(sex))) {
                sb.append("性别：" + sex + " 输入有误；");
            }

            //手机号
            String mobile = getCellString(row.getCell(5));
            if (!ValidatorUtil.isMobile(mobile)) {
                sb.append("手机号：" + mobile + " 格式不对； ");
            }

            //邮箱
            String mail = getCellString(row.getCell(5));
            if (!ValidatorUtil.isMobile(mail)) {
                sb.append("邮箱：" + mail + " 格式不对； ");
            }

            if (StringUtils.isBlank(sb.toString())) {
                //备注
                String remark = getCellString(row.getCell(7));

                user = new User();
                user.setUsername(username);
                user.setNickname(name);
                user.setMobile(mobile);
                user.setEmail(mail);
                user.setSex(sex);
                user.setRemark(remark);
                if (StringUtils.isNotBlank(createUserId)) {
                    user.setCreateUserId(Long.parseLong(createUserId));
                }
                user.setCreateTime(createTime);

                //默认值
                //"状态  0：禁用   1：正常,默认传1"
                //密码：默认888888
                user.setPassword(DEFAULT_PASSWORD);
                user.setStatus(Byte.valueOf("1"));
                usernameMap.put(username, r);
                userList.add(user);
            } else {
                sb.insert(0, "第" + (r + 1) + "行数据存在问题：");
                sb.append("</br>");
            }
            validateResult.append(sb);
        }
        return validateResult;
    }

    private boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    private boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 用户名是否已存在
     *
     * @param username
     * @return
     */
    private boolean existUser(String username) {
        User userTemp = new User();
        userTemp.setUsername(username);
        User user = null;// mapper.selectOne(userTemp);
        return user != null ? true : false;
    }

    /**
     * 把EXCEL Cell原有数据转换成String类型
     * @param cell
     * @return
     */
    private String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }

        String cellSring = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                cellSring = cell.getStringCellValue().trim();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                double num = cell.getNumericCellValue();
                NumberFormat df = new DecimalFormat("#0");
                cellSring = df.format(num);
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                cellSring = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                cellSring = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                cellSring = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                cellSring = "";
                break;
            default:
                cellSring = "ERROR";
                break;
        }
        return cellSring;
    }

    /**
     * 根据用户名查用户信息
     *
     * @param user
     * @return
     */
    @Override
    public int queryUserByusername(User user) {
        List<User> userList = userMapper.selectByParam(user);
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        } else {
            return userList.size();
        }
    }

    /**
     * 返回数据权限树
     *
     * @return
     */
    @Override
    public List<DataPermission> listDataPerfuncs() {
        return dataPermissionMapper.selectAll();
    }

    /**
     * 获取当前登录用户名
     *
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
        User user = getUserCache(token);
        if (null == user) {
            return null;
        }
        return user.getUsername();
    }

    /**
     * 设置username到参数列表
     *
     * @param params
     */
    @Override
    public void setParamOfUsername(Map<String, Object> params) {
        params.put(AuthConstant.PARAM_USERNAME, getUsername(params.get(AuthConstant.PARAM_TOKEN).toString()));
    }

    /**
     * （缓存）获取：当前登录的用户（SysUser）
     *
     * @return
     */
    public User getUserCache(String token) {
        String key = CacheConstant.USER_CACHE + CacheConstant.DELIMITER_CACHE + token;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        return (User) redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据用户id数组注销登录用户
     *
     * @param ids
     */
    private void logoutUserByIds(Long[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }
        try {
            // 1、根据用户ID数组查询所有用户名
            List<User> userList = userMapper.selectUserByIds(ids);

            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            // 2、注销用户
            userList.stream().forEach(user -> {
                List<Role> roleList = roleMapper.findRoleByUserid(user.getId());
                if (CollectionUtils.isNotEmpty(roleList)) {
                    roleList.forEach(role -> {
                        String accessToken = redisUtils.getAccessTokenCache(user.getUsername(), role.getId());
                        if (null != accessToken) {
                            consumerTokenServices.revokeToken(accessToken);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 编辑用户状态 status 0：禁用   1：正常
     *
     * @param user
     * @return
     */
    @Override
    public int updateUserStatus(User user) {
        if (user.getStatus() == 0) {
            //禁用用户，清空缓存
            logoutUserByIds(new Long[]{user.getUserId()});
        }
        return userMapper.updateUserStatus(user);
    }

    /**
     * 根据参数查询用户
     *
     * @param user
     * @return
     */
    @Override
    public List<User> selectUserByParam(User user) {
        return userMapper.selectUserByParam(user);
    }

    /**
     * 修改用户的角色时，先删除用户下的角色
     *
     * @param user
     * @return
     */
    @Override
    public int deleteUserUnderRole(User user) {
        try {
            //角色id不为空
            if (ArrayUtils.isNotEmpty(user.getRoleIds())) {
                for (Long roleId : user.getRoleIds()) {
                    User paramUser = new User();
                    paramUser.setUserId(user.getUserId());
                    paramUser.setRoleId(roleId);
                    recursionDeleteUserUnderRole(paramUser);
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 递归删除用户下的角色
     *
     * @param paramUser
     */
    private void recursionDeleteUserUnderRole(User paramUser) {
        //将用户id，角色id，分为设置为createUserId和createRoleId
        if (paramUser.getUserId() != null) {
            paramUser.setCreateUserId(paramUser.getUserId());
        }
        if (paramUser.getRoleId() != null) {
            paramUser.setCreateRoleId(paramUser.getRoleId());
        }
        //根据userId和roleId去查已经创建的用户
        List<User> userList = userMapper.selectByParam(paramUser);
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(u -> {
                //将username设置为null
                if (StringUtils.isNotBlank(u.getUsername())) {
                    u.setUsername(null);
                }
                u.setUserId(u.getId());
                recursionDeleteUser(u);
            });
        }
        //根据userId和roleId去查已经创建的角色
        Role paramRole = new Role();
        //删除用户角色中间表
        paramRole.setCreateUserId(paramUser.getUserId());
        paramRole.setCreateRoleId(paramUser.getRoleId());
        roleMapper.deleteRoles(paramRole);
        //销毁token
        logoutUserByIds(new Long[]{paramUser.getUserId()});
        userMapper.deleteUserAndRoles(paramUser);

    }
}
