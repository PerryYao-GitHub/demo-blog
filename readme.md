# 博客平台

## 部署网站

http://8.148.21.185:80

## 主要功能

### 对于用户

1. 用户可以在注册后获得一个帐户，并在登录后进行操作。
2. 用户可以发送、编辑、删除自己提交的博客。
3. 用户可以通过设置博客权限来决定哪些博客可以被其他人看到，哪些博客只能被自己看到。(需完善)
4. 用户可以关注和取消关注其他用户。
5. 用户可以浏览其他人的博客，这些人将博客的权限设置为“所有人都可以看到”。
6. 用户可以收集其他用户的博客。

### 对于管理员 (需完善)

1. 管理员可以通过密码登录。
2. 管理员可以禁止或反应用户。一旦用户被禁止，他或她就不能使用该平台，直到得到解封。
3. 管理员可以查看用户的详细信息。

## 技术栈

### 后端 (Spring Boot 2.7.x + MySQL + MyBatis-Plus + JWT + Swagger2)

1. JWT Token 实现了基本的 account-password 登录注册, 以及身份校验
2. MyBatis-Plus 实现对数据库的增删改查

### 前端 (Vue3, Pinia, Axios, Vite)

1. 利用 Pinia 管理用户 token 及其它信息
2. 利用 Axios 发送请求

# A Blog Platform, developed by Peiyou-Yao

## Website
http://8.148.21.185:80

## Functions

### for user
1. User can get an account after registering, and operate after login.
2. User can send, edit, delete blogs that were submitted by him or her self.
3. User can decide which blogs can be seen by other people or just by him or her self by setting permission of blogs.
4. User can follow and unfollow other users.
5. User can browse the blogs of other people who set the permission of the blogs as "everyone can see".
6. User can collect other users' blogs.
### for admin
1. Admin can login by password.
2. Admin can ban or react users. Once the user is banned, he or she can't use the platform until being reacted.
3. Admin can check the detail information of users.

## Technology Stack
1. Frontend: vue3 + vite
2. Backend: SpringBoot2
3. Database: MySQL
4. Authentication: JWT

## Features
1. Frontend and backend separation