-- phpMyAdmin SQL Dump
-- version 3.3.7
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2014 年 07 月 06 日 06:56
-- 服务器版本: 5.0.90
-- PHP 版本: 5.2.14

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `gy404webdisk`
--

-- --------------------------------------------------------

--
-- 表的结构 `dir`
--

DROP TABLE IF EXISTS `dir`;
CREATE TABLE IF NOT EXISTS `dir` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) default '' COMMENT '文件夹名称',
  `createtime` timestamp NULL default NULL COMMENT '创建时间',
  `updatetime` timestamp NULL default NULL COMMENT '修改时间',
  `sonfilenum` varchar(255) default '0' COMMENT '目录下子文件数量',
  `hdfspath` varchar(1000) default '' COMMENT '云盘路径',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;


-- --------------------------------------------------------

--
-- 表的结构 `favoritetb`
--

DROP TABLE IF EXISTS `favoritetb`;
CREATE TABLE IF NOT EXISTS `favoritetb` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `fileid` varchar(100) NOT NULL COMMENT '文件id',
  `filename` varchar(100) NOT NULL COMMENT '文件名称',
  `favoritedate` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '共享时间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- 转存表中的数据 `favoritetb`
--


-- --------------------------------------------------------

--
-- 表的结构 `filesystem`
--

DROP TABLE IF EXISTS `filesystem`;
CREATE TABLE IF NOT EXISTS `filesystem` (
  `id` int(11) NOT NULL auto_increment,
  `filename` varchar(100) default NULL COMMENT '文件名称',
  `filepath` varchar(1000) default NULL COMMENT '文件存放路径',
  `filesize` varchar(100) default NULL COMMENT '文件大小',
  `filetype` varchar(100) default NULL COMMENT '文件类型',
  `fileextension` varchar(100) default NULL COMMENT '文件后缀',
  `uploadtime` timestamp NULL default NULL COMMENT '文件上传时间',
  `mendtime` timestamp NULL default NULL COMMENT '文件更新时间',
  `download_num` varchar(100) default '0' COMMENT '文件下载次数',
  `dir` int(11) default '0' COMMENT '所属文件夹ID号，0表示存储空间根目录',
  `filetext` longtext,
  PRIMARY KEY  (`id`),
  FULLTEXT KEY `filename` (`filename`,`filetext`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=97 ;

-- --------------------------------------------------------

--
-- 表的结构 `sharetb`
--

DROP TABLE IF EXISTS `sharetb`;
CREATE TABLE IF NOT EXISTS `sharetb` (
  `id` int(11) NOT NULL auto_increment,
  `fromuser` varchar(100) NOT NULL COMMENT '用户名',
  `fileid` varchar(100) NOT NULL COMMENT '文件id',
  `filename` varchar(100) NOT NULL COMMENT '文件名称',
  `sharedate` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '共享时间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- 转存表中的数据 `sharetb`
--


-- --------------------------------------------------------

--
-- 表的结构 `userinfo`
--

DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE IF NOT EXISTS `userinfo` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `userpass` varchar(100) NOT NULL COMMENT '密码',
  `useremail` varchar(100) NOT NULL COMMENT '邮箱',
  `regdate` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '注册时间',
  `userip` varchar(100) NOT NULL COMMENT '用户注册ip',
  `userspace` varchar(100) default '1' COMMENT '网盘空间',
  `usedspace` varchar(100) default NULL COMMENT '已用空间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

--
-- 转存表中的数据 `userinfo`
--

INSERT INTO `userinfo` (`id`, `username`, `userpass`, `useremail`, `regdate`, `userip`, `userspace`, `usedspace`) VALUES
(9, 'admin', '4775fe9c9fac6e8335cfb53e46932594 ', '603369821@qq.com', '2014-06-30 09:27:38', '127.0.0.1', '1', '0');
