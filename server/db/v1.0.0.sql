ALTER TABLE work_order_log CHANGE work_order_id biz_id bigint(20) NOT NULL COMMENT '业务Id';
ALTER TABLE work_order_log ADD biz_type tinyint(10) NOT NULL COMMENT '业务类型 1 工单 2 部署' AFTER biz_id;
ALTER TABLE work_order_log ADD biz_title varchar(255) NOT NULL COMMENT '业务标题' AFTER biz_type;
ALTER TABLE work_order_log CHANGE type action varchar(20) NOT NULL COMMENT '操作 1 新建 2 提交 3 编辑 4 同意 5 拒绝 6 完成 7 撤销';
ALTER TABLE work_order_log RENAME TO biz_log;
ALTER TABLE flow_template CHANGE flow_engine_definition_id flow_engine_definition_id varchar(50) NOT NULL;

DROP TABLE IF EXISTS `deploy`;
CREATE TABLE `deploy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '标题',
  `type` tinyint(4) NOT NULL COMMENT '发版类型 1 版本迭代 2 bugfix 3 代码优化',
  `version` varchar(20) NOT NULL COMMENT '版本',
  `status` tinyint(8) NOT NULL COMMENT '状态 1 新建 2 审批中 3 已拒绝 4 审批完成 5 撤销',
  `content` text NOT NULL COMMENT '说明',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `flow_engine_definition_id` varchar(50) NOT NULL COMMENT '流程处理模板id',
  `flow_engine_instance_id` varchar(20) DEFAULT NULL,
  `create_id` bigint(20) DEFAULT NULL COMMENT '提交人Id',
  `create_time` datetime NOT NULL,
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='版本发布表';

-- ----------------------------
-- Table structure for deploy_project
-- ----------------------------
DROP TABLE IF EXISTS `deploy_project`;
CREATE TABLE `deploy_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deploy_id` int(11) NOT NULL,
  `project_id` int(11) NOT NULL COMMENT '项目Id',
  `project_name` varchar(50) NOT NULL COMMENT '项目名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COMMENT='部署申请项目表';

-- ----------------------------
-- Table structure for deploy_step
-- ----------------------------
DROP TABLE IF EXISTS `deploy_step`;
CREATE TABLE `deploy_step` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deploy_id` bigint(20) NOT NULL,
  `step_order` int(11) NOT NULL COMMENT '顺序',
  `type` tinyint(255) NOT NULL COMMENT '类型 1 SQL 2 Nginx 3 Apollo 4 deploy 5 other',
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 COMMENT='部署执行步骤表';

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '项目Id',
  `name` varchar(50) NOT NULL,
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='项目表';