<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import axiosUser from "@/axios/user.js";
import { useUserStore } from "@/stores/user.js";
import {useRouter} from "vue-router";

const userStore = useUserStore();
const router = useRouter()
// 使用 ref 来管理表单对象
const form = ref({
  account: "",
  password: "",
  confirmPassword: "",
});

// 重置表单
const resetForm = () => {
  form.value.account = "";
  form.value.password = "";
  form.value.confirmPassword = "";
};

const action = ref("login");

const handleSubmit = async () => {
  const {account, password, confirmPassword} = form.value;

  if (action.value === "register" && password !== confirmPassword) {
    ElMessage.error("Two passwords must be the same");
    return;
  }
  if (account.trim() === "" || password === "") {
    ElMessage.error("Account or password cannot be empty");
    return;
  }

  try {
    const resp = await axiosUser.post(`/${action.value}`, {
      ...form.value,
    });
    if (resp.data.code === 200) {
      ElMessage.success(`${action.value} successfully`);
      resetForm();
      if (action.value === "register") {
        action.value = "login";
      } else {
        userStore.setToken(resp.data.data.token);
        userStore.setInfo(resp.data.data.userInfo);
        router.push("/me")
      }
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("An error occurred during the request");
  }
};
</script>

<template>
  <div class="main">
    <el-card shadow="hover" style="width: 60%; margin: 0 auto;">
      <!-- 导航栏部分 -->
      <el-tabs v-model="action">
        <el-tab-pane label="Login" name="login"></el-tab-pane>
        <el-tab-pane label="Register" name="register"></el-tab-pane>
      </el-tabs>

      <el-form :model="form" label-width="180px">
        <el-form-item label="Account">
          <el-input v-model="form.account"></el-input>
        </el-form-item>

        <el-form-item label="Password">
          <el-input type="password" show-password v-model="form.password"></el-input>
        </el-form-item>

        <el-form-item label="Confirm your Password" v-show="action === 'register'">
          <el-input type="password" show-password v-model="form.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">Submit</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.main {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to bottom right, #ffdd88, #dd8899);
}
</style>
