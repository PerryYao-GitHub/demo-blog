# learning

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```
页面及组件设计方案：
个人中心页面 (Profile Page)

功能：
用户查看并编辑自己的个人信息。
用户查看自己的 BlogList，并执行增删改查功能。
用户查看 FollowerList 和 FollowedUserList。
组件：
ProfilePage 组件：用于展示用户个人中心的整体页面，分为个人信息、博客管理、关注/粉丝列表等。
UserInfo 组件：展示并编辑个人信息。
BlogList 组件：用于展示和管理用户的博客列表（带增删改查功能）。
FollowerList 和 FollowedList 组件：分别用于显示用户的粉丝和关注用户。
首页 (Home Page)

功能：
显示所有用户的博客列表。
允许用户查看别人的博客详情。
组件：
HomePage 组件：主页展示所有用户的博客列表，可以按照时间、分类等过滤。
BlogList 组件：重用展示博客列表的功能，只是不具备增删改查功能。
查看他人用户信息页面 (User Info Page)

功能：
显示其他用户的个人信息和 BlogList。
不提供增删改查博客的功能。
组件：
OtherUserPage 组件：展示其他用户的个人信息和博客列表。
UserInfo 组件：用于展示其他用户的个人信息（不带编辑功能）。
BlogList 组件：展示其他用户的博客列表，不提供管理功能。
具体实现细节：
ProfilePage 设计：

个人信息和博客管理分为两部分，左侧展示用户信息，右侧使用 Tabs 展示博客管理和关注/粉丝列表。
权限控制：在 BlogList 组件中，通过 props 或 store 判断是否显示增删改查按钮。
HomePage 设计：

用于展示所有博客的公共页面，用户可以查看所有人的博客。
权限控制：不允许对博客执行任何修改操作。
OtherUserPage 设计：

用于展示其他用户的个人信息和博客列表。
通过 URL 动态传递用户 ID 来展示对应用户的信息，去除博客的增删改查功能。
组件划分：
UserInfo.vue: 负责展示用户个人信息，支持自己的信息编辑，其他用户时仅展示。
BlogList.vue: 支持增删改查和查询的博客列表组件，可以通过属性控制是否显示编辑按钮。
FollowerList.vue 和 FollowedList.vue: 展示用户的粉丝和关注用户列表。
HomePage.vue: 展示所有博客的主页，不允许用户增删改查。
OtherUserPage.vue: 展示其他用户信息及其博客列表的页面，去除修改功能。
路由设计：
/profile: 用户个人中心页面，展示个人信息、博客管理、关注/粉丝。
/home: 首页，展示所有博客。
/user/:id: 用户信息页面，展示某个用户的个人信息和博客。
权限控制：
在 BlogList.vue 中，使用 props 或 Vuex 中的用户状态来判断当前是否可以执行博客的增删改查。
对于查看其他用户时，只展示博客和个人信息，不允许编辑。
这样设计能有效地将功能模块化，同时根据用户的角色或身份控制不同操作权限。
