<template>
  <el-container>
    <el-main>


      <!-- 搜索和排序区域 -->
      <el-row :gutter="20" class="filter-options">
        <el-col :span="3">
          <!-- 增加博客按钮 -->
          <el-button type="success" @click="openCreateBlogDialog">新增博客</el-button>
        </el-col>

        <el-col :span="6">
          <el-input
              v-model="keyword"
              placeholder="请输入关键词"
              clearable
              @clear="handleKeywordChange"
              @input="debouncedKeywordChange"
          />
        </el-col>
        <el-col :span="6">
          <el-select v-model="orderField" placeholder="选择排序字段" @change="handleOrderChange">
            <el-option label="创建时间" value="create_time"></el-option>
            <el-option label="更新时间" value="update_time"></el-option>
            <el-option label="标题" value="title"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="orderDirection" placeholder="选择排序方向" @change="handleOrderChange">
            <el-option label="升序" value="asc"></el-option>
            <el-option label="降序" value="desc"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <!-- 博客列表 -->
      <el-row :gutter="20">
        <el-col v-for="blog in blogs" :key="blog.id" :span="12">
          <el-card class="blog-item" shadow="hover">
            <el-descriptions title="我的博客" column="1">
              <el-descriptions-item label="Title">{{ blog.title }}</el-descriptions-item>
              <el-descriptions-item label="Description">{{ blog.description }}</el-descriptions-item>
            </el-descriptions>
            <!-- 编辑按钮 -->
            <el-button type="primary" @click="openEditBlogDialog(blog)">编辑</el-button>

            <!-- 删除按钮 -->
            <el-button type="danger" @click="deleteBlog(blog.id)">删除</el-button>

            <!-- 跳转博客详情 -->
            <el-button @click="goToBlogDetail(blog.id)">查看详情</el-button>
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

      <!-- 新增/编辑博客对话框 -->
      <el-dialog v-model="isBlogDialogVisible" title="博客编辑">
        <el-form :model="currentBlog">
          <el-form-item label="标题">
            <el-input v-model="currentBlog.title"/>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="currentBlog.description"/>
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" v-model="currentBlog.content"/>
          </el-form-item>
          <el-form-item label="权限">
            <el-select v-model="currentBlog.permission" placeholder="选择权限">
              <el-option label="所有人可见" :value="2"></el-option>
              <el-option label="仅用户" :value="1"></el-option>
              <el-option label="仅自己" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="isBlogDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBlog">保存</el-button>
        </span>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import axiosUser from "@/axios/user.js"
import {ElMessage} from "element-plus"
import {debounce} from 'lodash-es'
import {useUserStore} from "@/stores/user.js"

const userStore = useUserStore()
const authId = ref(userStore.info.id);
const blogs = ref([])
const page = ref(1)
const size = ref(10)
const keyword = ref("")
const orderField = ref("create_time")
const orderDirection = ref("asc")
const pageSizes = [10, 20, 50]
const total = ref(0)

// 对话框控制
const isBlogDialogVisible = ref(false)
const currentBlog = ref({
  id: null,
  title: '',
  description: '',
  content: ''
})

// 查询博客的参数
const checkBlogsParams = ref({
  authId: authId.value,
  page: page.value,
  size: size.value,
  keyword: keyword.value,
  order: `${orderField.value},${orderDirection.value}`,
})

// 查询博客的函数
const checkBlogs = async function () {
  try {
    const resp = await axiosUser.get("/check/blogs", {
      params: {
        ...checkBlogsParams.value,
        keyword: keyword.value,
        order: `${orderField.value},${orderDirection.value}`
      }
    });
    if (resp.data.code === 200) {
      blogs.value = resp.data.data.records;
      total.value = resp.data.data.total;
    } else {
      ElMessage.error(resp.data.msg + " " + resp.data.description);
    }
  } catch (e) {
    ElMessage.error("发送请求失败");
  }
}

// 首次加载博客
checkBlogs()

// 处理页码变化
const handlePageChange = (newPage) => {
  page.value = newPage;
  checkBlogs();
}

// 处理每页显示数量变化
const handlePageSizeChange = (newSize) => {
  size.value = newSize;
  page.value = 1;
  checkBlogs();
}

// 处理关键词变化
const handleKeywordChange = () => {
  page.value = 1;
  checkBlogs();
}

// 处理排序变化
const handleOrderChange = () => {
  page.value = 1;
  checkBlogs();
}

// 跳转到博客详情
const router = useRouter()
const goToBlogDetail = (id) => {
  router.push(`/one/blog/${id}`);
}

// 打开新增博客对话框
const openCreateBlogDialog = () => {
  currentBlog.value = {
    id: null,
    title: '',
    description: '',
    content: '',
    permission: 2,
  }
  isBlogDialogVisible.value = true;
}

// 打开编辑博客对话框
const openEditBlogDialog = (blog) => {
  currentBlog.value = {...blog}
  isBlogDialogVisible.value = true;
}

// 保存博客（新增或更新）
const saveBlog = async () => {
  try {
    if (currentBlog.value.id) {
      // 更新博客
      const resp = await axiosUser.post("/update/blog", currentBlog.value);
      if (resp.data.code === 200) {
        ElMessage.success("博客更新成功");
      } else {
        ElMessage.error(resp.data.description);
      }
    } else {
      // 新增博客
      const resp = await axiosUser.post("/send/blog", currentBlog.value);
      if (resp.data.code === 200) {
        ElMessage.success("博客创建成功");
      } else {
        ElMessage.error(resp.data.description);
      }
    }
    isBlogDialogVisible.value = false;
    checkBlogs(); // 重新加载博客列表
  } catch (e) {
    ElMessage.error("操作失败");
  }
}

// 删除博客
const deleteBlog = async (id) => {
  try {
    const resp = await axiosUser.get("/delete/blog", {
      params: {
        blogId: id,
      }
    });
    if (resp.data.code === 200) {
      ElMessage.success("博客删除成功");
      checkBlogs(); // 重新加载博客列表
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
    ElMessage.error("删除博客失败");
  }
}
</script>

<style scoped>
.blog-item {
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
