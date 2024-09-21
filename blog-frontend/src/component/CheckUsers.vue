<!-- Finish -->
<template>
  <el-container>
    <el-main>
      <!-- 搜索和排序区域 -->
      <el-row :gutter="20" class="filter-options">
        <!-- 关键词搜索框 -->
        <el-col :span="12">
          <el-input
              v-model="keyword"
              placeholder="请输入关键词"
              clearable
              @clear="handleKeywordChange"
              @input="debouncedKeywordChange"
          />
        </el-col>

        <!-- 排序字段选择框 -->
        <el-col :span="6">
          <el-select v-model="orderField" placeholder="选择排序字段" @change="handleOrderChange">
            <el-option label="注册时间" value="create_time"></el-option>
            <el-option label="博客数" value="blog_cnt"></el-option>
            <el-option label="粉丝数" value="follower_cnt"></el-option>
          </el-select>
        </el-col>

        <!-- 排序方向选择框 -->
        <el-col :span="6">
          <el-select v-model="orderDirection" placeholder="选择排序方向" @change="handleOrderChange">
            <el-option label="升序" value="asc"></el-option>
            <el-option label="降序" value="desc"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <!-- 用户列表 -->
      <el-row :gutter="20">
        <el-col v-for="user in users" :key="user.id" :span="12">
          <el-card class="user-item" shadow="hover" @click="goToUserDetail(user.id)">
            <el-row>
              <!-- 左侧用户头像 -->
              <el-col :span="6">
                <el-image
                    :src="user.avatar ? user.avatar : ICON_URL"
                    alt="用户头像"
                    style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;"
                />
              </el-col>

              <!-- 右侧用户信息 -->
              <el-col :span="18">
                <el-descriptions :title="user.account">
                  <el-descriptions-item label="用户名">{{ user.name }}</el-descriptions-item>
                  <el-descriptions-item label="粉丝数">{{ user.followerCnt }}</el-descriptions-item>
                  <el-descriptions-item label="博客数">{{ user.blogCnt }}</el-descriptions-item>
                  <el-descriptions-item label="备注">
                    <el-tag size="small">Java</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>

      <!-- 分页组件 -->
      <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="size"
          :current-page.sync="page"
          @current-change="handlePageChange"
      />

      <!-- 每页显示数量选择框 -->
      <div class="pagination-options">
        <span>每页显示：</span>
        <el-select v-model="size" placeholder="每页显示数量" @change="handlePageSizeChange">
          <el-option v-for="size in pageSizes" :key="size" :label="size + ' 条'" :value="size"></el-option>
        </el-select>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import axiosUser from "@/axios/user.js"
import {ElMessage} from "element-plus"
import {debounce} from 'lodash-es'
import {ICON_URL} from "../../public/icon.js";  // 导入lodash的防抖函数

const users = ref([])

const page = ref(1)
const size = ref(10)
const keyword = ref("")
const orderField = ref("blog_cnt") // 默认排序字段
const orderDirection = ref("desc") // 默认排序方向
const pageSizes = [10, 20, 50]
const total = ref(0)

const checkUsersParams = ref({
  page: page.value,
  size: size.value,
  keyword: keyword.value,
  order: `${orderField.value},${orderDirection.value}`,
})

// 关键词变化时，处理搜索
const debouncedKeywordChange = debounce(() => {
  handleKeywordChange()
}, 500)  // 防抖处理，避免每次输入都发送请求


const checkUsers = async function () {
  try {
    const resp = await axiosUser.get("/check/users", {
      params: {
        ...checkUsersParams.value,
        keyword: keyword.value,  // 更新关键词
        order: `${orderField.value},${orderDirection.value}`  // 更新排序
      }
    });
    if (resp.data.code === 200) {
      users.value = resp.data.data.records;
      total.value = resp.data.data.total;
    } else {
      ElMessage.error(resp.data.msg + " " + resp.data.description);
    }
  } catch (e) {
    ElMessage.error("发送请求失败");
  }
}

// 首次加载博客
checkUsers()

// 处理页码变化
const handlePageChange = (newPage) => {
  page.value = newPage;
  checkUsers();
}

// 处理每页显示数量变化
const handlePageSizeChange = (newSize) => {
  size.value = newSize;
  page.value = 1;  // 切换每页数量后，回到第一页
  checkUsers();
}

// 处理关键词变化
const handleKeywordChange = () => {
  page.value = 1;  // 搜索时重置到第一页
  checkUsers();
}

// 处理排序变化
const handleOrderChange = () => {
  page.value = 1;  // 排序变化时重置到第一页
  checkUsers();
}

// 跳转到博客详情
const router = useRouter()
const goToUserDetail = (targetId) => {
  router.push(`/one/user/${targetId}`);
}
</script>

<style scoped>
.user-item {
  cursor: pointer;
  margin-bottom: 20px;
}

el-pagination {
  margin-top: 20px;
  text-align: center;
}

.pagination-options {
  margin-top: 20px;
  text-align: center;
  width: 150px;
}

.filter-options {
  margin-bottom: 20px;
}
</style>

