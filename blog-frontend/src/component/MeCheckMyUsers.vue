<script setup>
import {ref} from "vue";
import axiosUser from "@/axios/user.js";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const props = defineProps({
  kind: {
    type: String, // followed / follower
    required: true,
  }
})

const users = ref([])
const router = useRouter();
const checkMyUsers = async function () {
  try {
    const resp = await axiosUser.get(`/check/my/${props.kind}/users`);
    if (resp.data.code === 200) {
      users.value = resp.data.data
      ElMessage.success(resp.data.description)
    } else {
      ElMessage.error(resp.data.msg + " " + resp.data.description);
    }
  } catch (e) {
    ElMessage.error("发送请求失败");
  }
}

// 首次加载博客
checkMyUsers()

const goToUserDetail = (targetId) => {
  router.push(`/one/user/${targetId}`);
}
</script>

<template>
  <!-- 用户列表 -->
  <el-row :gutter="20">
    <el-col v-for="user in users" :key="user.id" :span="12">
      <el-card class="user-item" shadow="hover" @click="goToUserDetail(user.id)">
        <el-descriptions :title="user.account" column="2">
          <el-descriptions-item label="昵称">{{ user.name }}</el-descriptions-item>
          <el-descriptions-item label="粉丝数">{{ user.followerCnt }}</el-descriptions-item>
          <el-descriptions-item label="博客数">{{ user.blogCnt }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped>

</style>
