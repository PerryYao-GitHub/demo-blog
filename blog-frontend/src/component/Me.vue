<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import axiosInsUser from '@/axios/user.js';
import {useUserStore} from "@/stores/user.js";
import axiosUser from '@/axios/user.js';
import router from "@/router/index.js";
import {ICON_URL} from "../../public/icon.js";

// 获取当前登录用户的 store
const userStore = useUserStore();
const info = ref({});

// 请求用户信息
const getMe = async () => {
  try {
    const resp = await axiosUser.get("/check_account");
    if (resp.data.code === 200) {
      info.value = resp.data.data;
      userStore.setInfo(resp.data.data);
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to load user information');
  }
};

getMe()

// 编辑信息
const editInfoFormVisible = ref(false);
const editInfoForm = ref({});
const openEditInfoForm = () => {
  editInfoForm.value.name = info.value.name;
  editInfoForm.value.email = info.value.email;
  editInfoForm.value.phone = info.value.phone;
  editInfoForm.value.avatar = info.value.avatar;
  editInfoFormVisible.value = true;
};

const handleEditInfo = async () => {
  if (editInfoForm.value.name.trim() === "") {
    ElMessage.error("Name can't be empty");
    return;
  }

  try {
    const resp = await axiosInsUser.post('/edit_account', {...editInfoForm.value});
    if (resp.data.code === 200) {
      ElMessage.success('User information updated successfully');
      editInfoFormVisible.value = false;
      await getMe(); // 更新用户信息
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to update user information');
  }
};

// 重置密码
const resetPasswordFormVisible = ref(false);
const accountPasswordForm = ref({});
const openResetPasswordForm = () => {
  accountPasswordForm.value.password = "";
  accountPasswordForm.value.newPassword = "";
  accountPasswordForm.value.confirmPassword = "";
  resetPasswordFormVisible.value = true;
};

const handleResetPassword = async () => {
  if (accountPasswordForm.value.password.trim() === "" || accountPasswordForm.value.newPassword.trim() === "") {
    ElMessage.error("Password can't be empty");
    return;
  }
  if (accountPasswordForm.value.newPassword !== accountPasswordForm.value.confirmPassword) {
    ElMessage.error("Confirm your new password correctly");
    return;
  }

  try {
    const resp = await axiosInsUser.post("/change_password", {...accountPasswordForm.value});
    if (resp.data.code === 200) {
      ElMessage.success("Password reset successfully");
      resetPasswordFormVisible.value = false;
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    console.error(e);
    ElMessage.error('Failed to reset password');
  }
};

// 退出登录
const handleLogout = async () => {
  userStore.delUser();
  router.push("/auth");
};

// 删除账号
const delAccountFormVisible = ref(false);
const openDelAccountForm = () => {
  accountPasswordForm.value.account = "";
  accountPasswordForm.value.password = "";
  accountPasswordForm.value.confirmPassword = "";
  delAccountFormVisible.value = true;
};

const handleDelAccount = async () => {
  if (accountPasswordForm.value.account.trim() === "" || accountPasswordForm.value.password.trim() === "") {
    ElMessage.error("Account or password can't be empty");
    return;
  }
  if (accountPasswordForm.value.password !== accountPasswordForm.value.confirmPassword) {
    ElMessage.error("Confirm your password correctly");
    return;
  }
};
</script>

<template>
  <el-card>
    <img :src="info.avatar ? info.avatar : ICON_URL" alt="Avatar" style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover; margin-bottom: 20px;">
    <el-descriptions title="我的信息" column="2">
      <el-descriptions-item label="用户名">{{ info.account }}</el-descriptions-item>
      <el-descriptions-item label="昵称">{{ info.name }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ info.phone }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ info.email }}</el-descriptions-item>
      <el-descriptions-item label="发帖数">{{ info.blogCnt }}</el-descriptions-item>
      <el-descriptions-item label="粉丝数">{{ info.followerCnt }}</el-descriptions-item>
      <el-descriptions-item label="关注数">{{ info.followedCnt }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">{{ info.createTime }}</el-descriptions-item>
      <el-descriptions-item label="上次登录">{{ info.loginTime }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ info.email }}</el-descriptions-item>
    </el-descriptions>

    <div style="margin-top: 15px">
      <el-row :gutter="20">
        <el-col :span="10">
          <el-button style="width: 100px" type="primary" block @click="openEditInfoForm">Edit</el-button>
        </el-col>
        <el-col :span="10">
          <el-button style="width: 200px" type="warning" block @click="openResetPasswordForm">Reset Password</el-button>
        </el-col>
      </el-row>
    </div>

    <div style="margin-top: 15px">
      <el-row :gutter="20">
        <el-col :span="10">
          <el-button style="width: 100px" type="danger" block @click="handleLogout">Logout</el-button>
        </el-col>
        <el-col :span="10">
          <el-button style="width: 200px" type="danger" block @click="openDelAccountForm">Delete Account</el-button>
        </el-col>
      </el-row>
    </div>


  </el-card>

  <!-- 编辑个人信息的模态框 -->
  <el-dialog title="Edit Info" v-model="editInfoFormVisible" width="500px">
    <el-form :model="editInfoForm" label-width="100px">
      <el-form-item label="Name">
        <el-input v-model="editInfoForm.name"></el-input>
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="editInfoForm.email"></el-input>
      </el-form-item>
      <el-form-item label="Phone">
        <el-input v-model="editInfoForm.phone"></el-input>
      </el-form-item>
      <el-form-item label="Avatar URL">
        <el-input v-model="editInfoForm.avatar"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="editInfoFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleEditInfo">Save</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- Reset Password 模态框 -->
  <el-dialog title="Reset Password" v-model="resetPasswordFormVisible" width="500px">
    <el-form :model="accountPasswordForm" label-width="200px">
      <el-form-item label="Old Password">
        <el-input type="password" show-password v-model="accountPasswordForm.password"></el-input>
      </el-form-item>
      <el-form-item label="New Password">
        <el-input type="password" show-password v-model="accountPasswordForm.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="Confirm your Password">
        <el-input type="password" show-password v-model="accountPasswordForm.confirmPassword"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="resetPasswordFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleResetPassword">Save</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- Delete Account 模态框 -->
  <el-dialog title="Delete Account" v-model="delAccountFormVisible" width="500px">
    <el-form :model="accountPasswordForm" label-width="200px">
      <el-form-item label="Account">
        <el-input v-model="accountPasswordForm.account"></el-input>
      </el-form-item>
      <el-form-item label="Password">
        <el-input type="password" show-password v-model="accountPasswordForm.password"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="delAccountFormVisible = false">Cancel</el-button>
        <el-button type="danger" @click="handleDelAccount">DELETE</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>
/* 可根据需要添加样式 */
</style>
