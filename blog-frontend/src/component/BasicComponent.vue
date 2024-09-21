<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router';
import { useUserStore } from "@/stores/user.js";
import { ICON_URL } from "@/../public/icon.js";
import { computed, ref, onBeforeMount, watchEffect } from "vue";

// 获取当前路由
const route = useRoute();

// 获取用户 Store
const userStore = useUserStore();

// 初始化 targetId
const targetId = ref(0);

// 在组件挂载之前计算 targetId
onBeforeMount(() => {
  if (userStore.isLogin) {
    targetId.value = userStore.info.id || 0;
  }
});

// 监听用户登录状态，并更新 targetId
watchEffect(() => {
  if (userStore.isLogin) {
    targetId.value = userStore.info.id || 0;
  } else {
    targetId.value = 0;
  }
});

// 计算 avatarUrl，当用户没有登录或没有头像时显示默认头像
const avatarUrl = computed(() => {
  return userStore.isLogin && userStore.info.avatar
    ? userStore.info.avatar
    : '/public/favicon.ico';
});
</script>

<template>
  <header v-if="!route.meta.standAlone">
    <nav class="nav-bar">
      <div class="nav-left">
        <img alt="Vue logo" class="logo" :src="ICON_URL" width="50" height="50"/>

        <RouterLink to="/home/blogs">最新博客</RouterLink>
        <RouterLink to="/home/users">活跃用户</RouterLink>
      </div>

      <div class="nav-right">
        <!-- 如果用户登录，则显示用户头像和账户信息，并带上 targetId -->
        <RouterLink
          v-if="userStore.isLogin && targetId"
          :to="'/me'"
        >
          <img :src="avatarUrl" alt="Avatar" class="avatar"/>
          {{ userStore.info.account }}
        </RouterLink>

        <!-- 如果未登录，则显示登录链接 -->
        <RouterLink v-else :to="{ name: 'auth' }">Login</RouterLink>
      </div>
    </nav>
  </header>

  <main>
    <RouterView/>
  </main>
</template>

<style scoped>
header {
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 1rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo {
  margin-right: 1rem;
}

.nav-left, .nav-right {
  display: flex;
  align-items: center;
}

.nav-left a, .nav-right a {
  margin-right: 1rem;
  text-decoration: none;
  color: #333;
}

.nav-right a {
  margin-right: 0;
}

nav a.router-link-exact-active {
  font-weight: bold;
  color: #007bff;
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
  object-fit: cover;
  vertical-align: middle;
}

main {
  padding: 1rem;
}
</style>

