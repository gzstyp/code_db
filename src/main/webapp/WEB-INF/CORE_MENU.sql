CREATE TABLE CORE_MENU (
"KID" CHAR(32 BYTE) NOT NULL ,
"TITLE" NVARCHAR2(64) NOT NULL ,
"MENU_PID" CHAR(32 BYTE) DEFAULT 88888888888888888888888888888888  NOT NULL ,
"PATH" NVARCHAR2(20) DEFAULT 'javascript:;'  NOT NULL ,
"ICON" NVARCHAR2(20) NULL ,
"MENU_LEVEL" NUMBER(1) DEFAULT 1  NOT NULL 
);
COMMENT ON TABLE CORE_MENU IS '系统菜单';
COMMENT ON COLUMN CORE_MENU."KID" IS '菜单主键';
COMMENT ON COLUMN CORE_MENU."TITLE" IS '菜单名称';
COMMENT ON COLUMN CORE_MENU."MENU_PID" IS '父级菜单的主键';
COMMENT ON COLUMN CORE_MENU."PATH" IS '菜单url';
COMMENT ON COLUMN CORE_MENU."ICON" IS '导航菜单的图标';
COMMENT ON COLUMN CORE_MENU."MENU_LEVEL" IS '导航菜单的层次数';

-- ----------------------------
-- Records of CORE_MENU
-- ----------------------------
INSERT INTO CORE_MENU VALUES ('ff91c54757ad4d018196b0abaccb90fd', '业务菜单', '88888888888888888888888888888888', '''javascript:;''', null, '1');
INSERT INTO CORE_MENU VALUES ('fe79a625fa084472901cd44667eff503', '系统菜单', 'ff91c54757ad4d018196b0abaccb90fd', '''javascript:;''', null, '2');
INSERT INTO CORE_MENU VALUES ('e1a2bf3d9cc646a1a328f4fe550f0aa9', '区域地区管理', 'ff91c54757ad4d018196b0abaccb90fd', '''javascript:;''', null, '2');
INSERT INTO CORE_MENU VALUES ('1458a625fa084472901cd44667eff503', '公告管理', 'ff91c54757ad4d018196b0abaccb90fd', '''javascript:;''', null, '2');
INSERT INTO CORE_MENU VALUES ('wseef22ea9d7465398190c2d41640db2', '国家级管理', 'e1a2bf3d9cc646a1a328f4fe550f0aa9', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('234ef22ea9d7465398190c2d41640db7', '省级管理', 'e1a2bf3d9cc646a1a328f4fe550f0aa9', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('567ef22ea9d7465398190c2d41640db8', '市区管理', 'e1a2bf3d9cc646a1a328f4fe550f0aa9', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('d6106b4901a4437185a47b8d50d84b43', '系统设置', 'fe79a625fa084472901cd44667eff503', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('d6c9cf174e4842ebac6bfa9d0f5dd292', '账号用户管理', 'fe79a625fa084472901cd44667eff503', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('ddb3f7021167496eb58ddc9ba92dfeeb', '菜单管理', 'fe79a625fa084472901cd44667eff503', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('e034c9f12fe04d77bf9f594575f1de60', '角色权限', 'fe79a625fa084472901cd44667eff503', '''javascript:;''', null, '3');
INSERT INTO CORE_MENU VALUES ('c4220914c0814db7ba36a205f99aa29c', '数据库', 'd6106b4901a4437185a47b8d50d84b43', '''javascript:;''', null, '4');
INSERT INTO CORE_MENU VALUES ('cc3fe48bf7bf4c5a9ae0eb52b43c3eb3', '软件版本', 'd6106b4901a4437185a47b8d50d84b43', '''javascript:;''', null, '4');
INSERT INTO CORE_MENU VALUES ('cd1635bcf9674019ba53e09006ae83b3', '数据备份', 'd6106b4901a4437185a47b8d50d84b43', '''javascript:;''', null, '4');
INSERT INTO CORE_MENU VALUES ('8277340aa39849019988fdedbc02ff25', '软件开发', '88888888888888888888888888888888', 'javascript:;', null, '1');
INSERT INTO CORE_MENU VALUES ('0077389293e947e2a35cfbd3f513181c', 'javaweb', '8277340aa39849019988fdedbc02ff25', 'javascript:;', null, '1');
INSERT INTO CORE_MENU VALUES ('86389ac1c80a47478ae0774a7aa0769e', 'android', '8277340aa39849019988fdedbc02ff25', 'javascript:;', null, '1');

-- ----------------------------
-- Indexes structure for table CORE_MENU
-- ----------------------------

-- ----------------------------
-- Checks structure for table CORE_MENU
-- ----------------------------
ALTER TABLE CORE_MENU ADD CHECK ("KID" IS NOT NULL);
ALTER TABLE CORE_MENU ADD CHECK ("TITLE" IS NOT NULL);
ALTER TABLE CORE_MENU ADD CHECK ("MENU_PID" IS NOT NULL);
ALTER TABLE CORE_MENU ADD CHECK ("PATH" IS NOT NULL);
ALTER TABLE CORE_MENU ADD CHECK ("MENU_LEVEL" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table CORE_MENU
-- ----------------------------
ALTER TABLE CORE_MENU ADD PRIMARY KEY ("KID");
