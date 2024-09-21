<script setup>
import { ref, watch, computed } from 'vue';
import CheckBlogs from "@/component/CheckBlogs.vue";
import MeCheckMyUsers from "@/component/MeCheckMyUsers.vue";
import { useUserStore } from "@/stores/user.js";
import Me from "@/component/Me.vue";
import MeCheckBlogs from "@/component/MeCheckBlogs.vue";

const action = ref('CheckBlogs'); // 默认显示 BlogList
</script>

<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <Me />
    </el-col>

    <!-- 右侧 Tabs 和展示内容 -->
    <el-col :span="16">
      <el-tabs v-model="action">
        <el-tab-pane label="Blogs" name="CheckBlogs"></el-tab-pane>
        <el-tab-pane label="Followed Users" name="CheckMyFollowedUsers"></el-tab-pane>
        <el-tab-pane label="Follower Users" name="CheckMyFollowerUsers"></el-tab-pane>
      </el-tabs>

      <!-- 根据选择的选项卡展示相应的组件 -->
      <div v-if="action === 'CheckBlogs'">
        <MeCheckBlogs />
      </div>
      <div v-else-if="action === 'CheckMyFollowedUsers'">
        <MeCheckMyUsers kind="followed" />
      </div>
      <div v-else-if="action === 'CheckMyFollowerUsers'">
        <MeCheckMyUsers kind="follower"/>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped>
/* 自定义样式 */
.el-row {
  margin-top: 20px;
}
</style>
