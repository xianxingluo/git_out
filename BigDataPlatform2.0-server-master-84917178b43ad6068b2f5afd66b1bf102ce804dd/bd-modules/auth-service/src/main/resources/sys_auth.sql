/*
 Navicat Premium Data Transfer

 Source Server         : HW_test
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : 192.168.100.103
 Source Database       : datacenter

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : utf-8

 Date: 04/25/2018 15:25:52 PM
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_func_permission
-- ----------------------------
DROP TABLE IF EXISTS `auth_func_permission`;
CREATE TABLE `auth_func_permission` (
  `func_permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `func_permission_name` varchar(30) DEFAULT NULL COMMENT '权限名称',
  `func_permission_url` varchar(255) DEFAULT NULL COMMENT '权限url地址',
  `func_permission_desc` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`func_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能权限表';

-- ----------------------------
-- Records of auth_func_permission
-- ----------------------------

-- ----------------------------
-- Table structure for auth_data_permission
-- ----------------------------
DROP TABLE IF EXISTS `auth_data_permission`;
CREATE TABLE `auth_data_permission` (
  `data_permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_permission_name` varchar(30) DEFAULT NULL COMMENT '权限名称',
  `data_permission_desc` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`data_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';

-- ----------------------------
-- Records of auth_data_permission
-- ----------------------------

-- ----------------------------
-- Table structure for auth_permission_template
-- ----------------------------
DROP TABLE IF EXISTS `auth_permission_template`;
CREATE TABLE `auth_permission_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `func_permission_id` bigint(20) DEFAULT NULL COMMENT '功能权限id',
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限模板中间表';

-- ----------------------------
-- Records of sys_auth_template
-- ----------------------------

-- ----------------------------
-- Table structure for auth_org_tree
-- ----------------------------
DROP TABLE IF EXISTS `auth_org_tree`;
CREATE TABLE `auth_org_tree` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_code` varchar(50) DEFAULT NULL COMMENT '机构code：：校区、院系2级没有编码；是我们自己编的',
  `org_name` varchar(50) DEFAULT NULL COMMENT '机构名称',
  `parent_code` varchar(50) DEFAULT NULL COMMENT '父级机构 code',
  `org_level` int(5) DEFAULT NULL COMMENT '机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级',
  `update_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_code` (`org_code`),
  KEY `org_name` (`org_name`),
  KEY `parent_code_org_level` (`parent_code`,`org_level`)
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='组织机构表';

-- ----------------------------
-- Records of auth_org_tree
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板编号',
  PRIMARY KEY (`role_id`),
  KEY `create_user_id` (`create_user_id`),
  KEY `template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for auth_template
-- ----------------------------
DROP TABLE IF EXISTS `auth_template`;
CREATE TABLE `auth_template` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `template_name` varchar(30) DEFAULT NULL COMMENT '模板名',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建模板的userid',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';

-- ----------------------------
-- Records of auth_template
-- ----------------------------

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(20) DEFAULT NULL COMMENT '用户名',
  `username` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `organ_name` varchar(30) DEFAULT NULL COMMENT '组织机构名',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `create_user_id` (`create_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表：包含系统用户';

-- ----------------------------
-- Records of auth_user
-- ----------------------------

-- ----------------------------
-- Table structure for auth_user_org
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_org`;
CREATE TABLE `sys_user_org` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_code` varchar(50) NOT NULL COMMENT '机构code',
  `user_id` bigint(20) NOT NULL COMMENT '账号id',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_code_user_id` (`org_code`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='组织机构用户账号关联表';

-- ----------------------------
-- Records of auth_user_org
-- ----------------------------

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色中间表';

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
