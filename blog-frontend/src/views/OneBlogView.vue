<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from '@/axios/user.js'; // 假设 axios 是你的请求工具
import { ElMessage } from 'element-plus';

// 获取路由
const route = useRoute();
const blogId = ref(Number(route.params.blogId)); // 从路由中获取 blogId

// 定义博客详情
const blog = ref({});

// 请求博客详情
const getBlogDetail = async () => {
  try {
    const response = await axios.get('/check/blog', { params: { blogId: blogId.value } });
    if (response.data.code === 200) {
      blog.value = response.data.data;
    } else {
      ElMessage.error(response.data.description);
    }
  } catch (error) {
    ElMessage.error("Failed to load blog details.");
  }
};

// 权限文本映射
const permissionText = (permission) => {
  switch (permission) {
    case 0:
      return "仅自己可见";
    case 1:
      return "用户可见";
    case 2:
      return "所有人可见";
    default:
      return "未知权限";
  }
};

// 初次加载时调用
onMounted(() => {
  getBlogDetail();
});
</script>

<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>{{ blog.title }}</span>
    </div>

    <!-- 博客作者信息 -->
    <div class="auth-info">
      <img :src="blog.authAvatar" alt="Author Avatar" class="avatar" />
      <div class="auth-details">
        <p><strong>{{ blog.authName }}</strong> ({{ blog.authAccount }})</p>
        <p>Published on: {{ blog.createTime }}</p>
        <p>Last updated: {{ blog.updateTime }}</p>
      </div>
    </div>

    <el-divider></el-divider>

    <!-- 博客描述 -->
    <div class="blog-description">
      <p>{{ blog.description }}</p>
    </div>

    <el-divider></el-divider>

    <!-- 博客内容 -->
    <div class="blog-content">
      <p>{{ blog.content }}</p>
    </div>

    <el-divider></el-divider>

    <!-- 权限信息 -->
    <div class="blog-permission">
      <p><strong>Permission:</strong> {{ permissionText(blog.permission) }}</p>
    </div>
  </el-card>
</template>

<style scoped>
.box-card {
  margin: 20px;
}

.auth-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.auth-details p {
  margin: 0;
  line-height: 1.5;
}

.blog-description,
.blog-content {
  margin-top: 20px;
}
</style>
