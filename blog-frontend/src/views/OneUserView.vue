<script setup>
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import CheckBlogs from "@/component/CheckBlogs.vue";
import axiosUser from "@/axios/user.js";
import { ElMessage } from "element-plus";
import axiosInsUser from "@/axios/user.js";
import {ICON_URL} from "../../public/icon.js";

// 路由相关
const route = useRoute();
const targetId = ref(Number(route.params.targetId)); // 使用 ref 追踪路由参数的变化并转成 Number

// 监听路由的变化，更新 targetId
watch(() => route.params.targetId, (newId) => {
  targetId.value = Number(newId); // 确保 targetId 是 Number 类型
  checkOneUser(); // 路由变化时重新获取用户信息
});

// 用户信息
const info = ref({});

// 请求指定用户信息
const checkOneUser = async () => {
  try {
    const resp = await axiosUser.get('/check/one/user', { params: { targetId: targetId.value } });
    if (resp.data.code === 200) {
      info.value = resp.data.data;
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to load user information');
  }
};

// 关注用户
const handleFollow = async () => {
  try {
    const resp = await axiosInsUser.get('/follow/user', { params: { targetId: targetId.value } });
    if (resp.data.code === 200) {
      ElMessage.success('Followed successfully');
      await checkOneUser(); // 重新拉取信息
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    ElMessage.error('Failed to follow user');
  }
};

// 取关用户
const handleUnfollow = async () => {
  try {
    const resp = await axiosInsUser.get('/unfollow/user', { params: { targetId: targetId.value } });
    if (resp.data.code === 200) {
      ElMessage.success('Unfollowed successfully');
      await checkOneUser(); // 重新拉取信息
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    ElMessage.error('Failed to unfollow user');
  }
};

// 初次加载用户信息
checkOneUser();
</script>

<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card>
        <img :src="info.avatar? info.avatar : ICON_URL" alt="Avatar" style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover; margin-bottom: 20px;">
        <p><strong>Account:</strong> {{ info.account }}</p>
        <p><strong>Name:</strong> {{ info.name }}</p>
        <p><strong>Email:</strong> {{ info.email }}</p>
        <p><strong>Phone:</strong> {{ info.phone }}</p>
        <p><strong>Status:</strong> {{ info.status }}</p>
        <p><strong>Blogs Count:</strong> {{ info.blogCnt }}</p>
        <p><strong>Followed Count:</strong> {{ info.followedCnt }}</p>
        <p><strong>Fans Count:</strong> {{ info.followerCnt }}</p>
        <p><strong>Register at:</strong> {{ info.createTime }}</p>
        <p><strong>Last Login at:</strong> {{ info.loginTime }}</p>

        <!-- 关注/取关按钮 -->
        <div>
          <el-button
            type="primary"
            v-if="!info.isFollowed"
            @click="handleFollow"
          >
            Follow
          </el-button>
          <el-button
            type="danger"
            v-if="info.isFollowed"
            @click="handleUnfollow"
          >
            Unfollow
          </el-button>
        </div>
      </el-card>
    </el-col>

    <!-- 右侧 Blogs 展示 -->
    <el-col :span="16">
      <el-card>
        <div>Blogs</div>
        <CheckBlogs :auth-id="targetId" />
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped>
.el-row {
  margin-top: 20px;
}
</style>
