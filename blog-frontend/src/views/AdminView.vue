<script setup>
import {reactive, ref, shallowRef} from "vue";
import {ElMessage} from "element-plus";
import axiosAdmin from "@/axios/admin.js";
import {useAdminStore} from "@/stores/admin.js";
import UserManager from "@/component/admin/UserManager.vue";
import UserChecker from "@/component/admin/UserChecker.vue";

const showComponent = shallowRef(UserManager);  // 引用组件
const adminStore = useAdminStore()

const form = reactive({
  username: "",
  password: "",
})

const login = async function () {
  let username = form.username;
  let password = form.password;
  if (username === "" || password === "") {
    ElMessage.error("empty !")
    return
  }

  try {
    const resp = await axiosAdmin.post("/login", {
      username,
      password,
    });

    if (resp.data.code === 200) {
      adminStore.setToken(resp.data.data);
      form.username = "";
      form.password = "";
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
  }
}
const userId = ref(0);
const handleCheckUser = (_userId) => {
  userId.value = _userId;
  showComponent.value = UserChecker;
}
const goBackToUserManager = () => {
  showComponent.value = UserManager;
}
</script>


<template>
  <div class="admin-container">
    <div class="main">
      <h3>Admin Page</h3>
      <div v-if="adminStore.isLogin">
        <component
            :is="showComponent"
            @checkUser="handleCheckUser"
            @goBack="goBackToUserManager"
            :userId="userId"
        />
      </div>

      <div v-else>
        <el-card shadow="hover" style="width: 60%; width: 60%; margin: 0 auto;">
          <el-form :model="form" label-width="180">
            <el-form-item label="Admin Name">
              <el-input v-model="form.username"></el-input>
            </el-form-item>

            <el-form-item label="Password">
              <el-input type="password" show-password v-model="form.password"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="login">login</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>


<style scoped>
.admin-container {
  /* Admin 页面样式 */
}
</style>
