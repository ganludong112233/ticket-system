/*
Navicat MySQL Data Transfer

Source Server         : 10.40.1.200_3308_develop
Source Server Version : 50722
Source Host           : 10.40.1.200:3308
Source Database       : ticket_system

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-03-05 11:05:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for flow_template
-- ----------------------------
DROP TABLE IF EXISTS `flow_template`;
CREATE TABLE `flow_template` (
  `id` bigint(20) NOT NULL,
  `flow_engine_definition_id` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '模板名',
  `description` varchar(255) DEFAULT NULL COMMENT '模板描述',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_1` (`flow_engine_definition_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审核流程模板表';

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '组名',
  `user_id` bigint(20) DEFAULT NULL COMMENT '组负责人Id',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL,
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(30) NOT NULL COMMENT '密码',
  `realname` varchar(50) NOT NULL COMMENT '真实姓名',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `type` tinyint(4) NOT NULL COMMENT '用户类型 1 管理员 2 普通用户',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 正常 2 禁用',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `create_time` datetime NOT NULL,
  `forbidden_time` datetime DEFAULT NULL COMMENT '禁用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for work_order
-- ----------------------------
DROP TABLE IF EXISTS `work_order`;
CREATE TABLE `work_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '标题',
  `type` tinyint(4) NOT NULL COMMENT '类型 1 SQL 2 Apollo 3 Nginx 4 其他',
  `discription` varchar(255) NOT NULL COMMENT '说明',
  `content` text NOT NULL COMMENT '工单内容',
  `status` tinyint(4) NOT NULL COMMENT '状态 1 新建 2 审批中 3 已拒绝 4 审批完成 5 撤销',
  `flow_engine_definition_id` varchar(20) NOT NULL COMMENT '流程引擎模板ID',
  `flow_engine_instance_id` varchar(20) NOT NULL COMMENT '流程引擎示实例ID',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_id` bigint(20) NOT NULL COMMENT '提交人Id',
  `create_time` datetime NOT NULL,
  `subimit_time` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单表';

-- ----------------------------
-- Table structure for work_order_log
-- ----------------------------
DROP TABLE IF EXISTS `work_order_log`;
CREATE TABLE `work_order_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `work_order_id` bigint(20) NOT NULL COMMENT '工单申请Id',
  `type` varchar(255) NOT NULL COMMENT '操作 1 新建 2 提交 3 编辑 4 同意 5 拒绝 6 完成 7 插销',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `processor_id` bigint(20) NOT NULL COMMENT '审核人Id',
  `process_time` datetime NOT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单申请审核日志表';
