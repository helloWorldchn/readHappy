项目介绍：
-----------------------------------

HappyRead是一个前后端分离的微信小程序项目。后端使用了JeecgBoot框架。通过redis缓存用户数据，通过MySQL存储数据。前台通过uniapp对小程序页面进行开发。同时使用阿里云oss对文章图片进行存储。用户可以发表、阅读、编辑自己的文章。

JeecgBoot 是一款基于代码生成器的J2EE快速开发平台！采用前后端分离架构：SpringBoot2.x，Ant Design&Vue，Mybatis-plus，Shiro，JWT。


技术架构：
-----------------------------------

#### 开发环境

- 语言：Java 8
- IDE(JAVA)： IDEA / Eclipse安装lombok插件 
- IDE(前端)： WebStorm 或者 IDEA和Hbuilder
- 依赖管理：Maven
- 数据库：MySQL5.7
- 缓存：Redis


后台开发环境和依赖
----

- java
- maven
- jdk8
- mysql
- redis
- 数据库脚本：jeecg-boot\docs\jeecg-boot-mysql.sql
- 默认登录账号： admin/123456


前端开发环境和依赖
----

- node
- yarn
- webpack
- eslint

## 项目结构

happyread：小程序前台程序，推荐使用Hbuilder打开

ant-design-vue-jeecg： jeecg-boot自带的前台，可以使用idea或WebStorm打开，后需增加功能是可以使用该模块

jeecg-boot-parent：项目的后端，用IDEA打开

image：存储着文章的插图

readHappy.sql：开心阅读的数据库文件，需要先执行jeecg-boot-parent的sql文件创建jeecgboot依赖的数据库后再执行改文件

项目下载和运行
----

- 拉取项目代码

1. 安装node.js
2. 切换到ant-design-jeecg-vue文件夹下

```
# 安装yarn
npm install -g yarn

# 下载依赖
yarn install

# 启动
yarn run serve

# 编译项目
yarn run build

# Lints and fixes files
yarn run lint
```

3.使用sql文件创建数据库

4.在jeecg-boot-parent的jeecg-boot-module-system模块中找到applicationi-dev.yml，修改其中的mysql、redis和阿里云oss信息。

如果想将文章图片存储到本地，只需将type改为local。
