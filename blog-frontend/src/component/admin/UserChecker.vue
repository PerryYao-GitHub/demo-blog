<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import axiosAdmin from '@/axios/admin.js';

// 获取传入的 userId 属性
const props = defineProps(['userId']);

// 存储用户信息和博客列表
const userInfo = ref({});
const blogs = ref([]);

// 获取用户信息及其博客
const getUserInfo = async () => {
  try {
    const resp = await axiosAdmin.get(`/user/${props.userId}`);

    if (resp.data.code === 200) {
      userInfo.value = resp.data.data.user;
      blogs.value = resp.data.data.blogs;
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    ElMessage.error('Failed to load user data');
    console.error(e);
  }
};

// 在组件加载时，如果 userId 有效，获取用户信息
if (props.userId !== 0) {
  getUserInfo();
}
</script>

<template>
  <!-- 返回按钮 -->
  <el-button @click="$emit('goBack')" style="margin-top: 20px;"><< Back</el-button>
  <el-row gutter="20">
    <!-- 左栏：用户信息 -->
    <el-col :span="8">
      <el-card shadow="hover">
        <img :src="userInfo.avatar" alt="Avatar" style="width: 150px; height: 150px; margin-bottom: 20px; object-fit: cover;">
        <p><strong>Username:</strong> {{ userInfo.username }}</p>
        <p><strong>Email:</strong> {{ userInfo.email }}</p>
        <p><strong>Role:</strong> {{ userInfo.role }}</p>
        <p><strong>Status:</strong> {{ userInfo.status }}</p>
        <p><strong>Registered At:</strong> {{ userInfo.registerAt }}</p>
        <p><strong>Last Login At:</strong> {{ userInfo.lastLoginAt }}</p>
      </el-card>
    </el-col>

    <!-- 右栏：用户博客 -->
    <el-col :span="16">
      <el-row gutter="20">
        <el-col v-for="blog in blogs" :key="blog.id" :span="24">
          <el-card shadow="hover">
            <div slot="header">
              <span><strong>{{ blog.title }}</strong></span>
              <div style="float: right;">
                <span>Created: {{ blog.created }}</span> |
                <span>Edited: {{ blog.edited }}</span>
              </div>
            </div>
            <p><strong>Description:</strong> {{ blog.description }}</p>
            <p><strong>Content:</strong> {{ blog.content }}</p>
          </el-card>
        </el-col>
      </el-row>
    </el-col>
  </el-row>
</template>

<style scoped>
/* 样式可以根据需要进一步调整 */
</style>
