<script setup>
import {reactive, ref} from "vue";
import {useAdminStore} from "@/stores/admin.js";
import axiosAdmin from "@/axios/admin.js";
import {ElMessage} from "element-plus";

const adminStore = useAdminStore()
const users = reactive([])
const searchParams = reactive({
  id: null,
  username: "",
  email: "",
  is_not_banned: true,
  sort: "id",
  order: "asc",
});

const getUsers = async function () {
  if (adminStore.isLogin === false) return

  try {
    const resp = await axiosAdmin.get("/users", {
      params: {
        ...searchParams,
        order_by: searchParams.sort + "_" + searchParams.order,
        page_no: pageNo.value,
        page_size: pageSize.value,
      }
    });
    if (resp.data.code === 200) {
      users.splice(0, users.length, ...resp.data.data.records); // 清空当前数组并将新数据插入
      total.value = resp.data.data.total;
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
  }
}

const handleSearch = function () {
  pageNo.value = 1;

  getUsers();
}

const handleReset = function () {
  pageNo.value = 1;

  searchParams.id = null;
  searchParams.username = "";
  searchParams.email = "";
  searchParams.status = null;
  searchParams.sort = "id";
  searchParams.order = "asc";

  getUsers();
}

const handleSwitchStatus = async function (id) {
  try {
    const resp = await axiosAdmin.get(`/user/switch_status/${id}`);

    if (resp.data.code == 200) {
      getUsers();
    } else {
      ElMessage.error(resp.data.description);
    }
  } catch (e) {
  }
}
// 分页逻辑
const total = ref(0);
const pageNo = ref(1);
const pageSize = ref(10);


const handlePageChange = (page) => {
  pageNo.value = page;
  getUsers();
};
const handlePageSizeChange = (size) => {
  pageSize.value = size;
  pageNo.value = 1; // 切换每页显示数量时，重置到第一页
  getUsers();
};


const logout = function () {
  adminStore.delAdmin()
}

getUsers();
</script>

<template>
  <!-- 搜索框和按钮 -->
  <el-card class="search-card" shadow="hover">
    <el-form :model="searchParams" inline>
      <el-form-item label="ID">
        <el-input v-model="searchParams.id" placeholder="ID" style="width: 100px"></el-input>
      </el-form-item>

      <el-form-item label="Username">
        <el-input v-model="searchParams.username" placeholder="Username"></el-input>
      </el-form-item>

      <el-form-item label="Email">
        <el-input v-model="searchParams.email" placeholder="Email"></el-input>
      </el-form-item>

      <el-form-item label="Status">
        <el-select v-model="searchParams.status">
          <el-option label="Available" value="1"/>
          <el-option label="Banned" value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="Sort by">
        <el-col :span="50" style="width: 100px">
          <el-select v-model="searchParams.sort">
            <el-option label="ID" value="id"></el-option>
            <el-option label="Username" value="username"/>
            <el-option label="Register At" value="register_at"/>
            <el-option label="Last Login At" value="last_login_at"/>
          </el-select>
        </el-col>
        <el-col :span="50">
          <el-select v-model="searchParams.order">
            <el-option label="Ascending" value="asc"></el-option>
            <el-option label="Descending" value="desc"></el-option>
          </el-select>
        </el-col>
      </el-form-item>

      <div style="display: flex; justify-content: flex-end; width: 100%;">
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleReset">Reset</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="danger" @click="logout">Logout</el-button>
        </el-form-item>
      </div>
    </el-form>
  </el-card>
  <!-- 表格呈现 -->
  <el-table :data="users" style="width: 100%">
    <el-table-column type="selection" width="55"></el-table-column>

    <el-table-column prop="id" label="ID" width="50"></el-table-column>

    <el-table-column prop="username" label="Username"></el-table-column>

    <el-table-column prop="email" label="Email"></el-table-column>

    <el-table-column prop="avatar" label="Avatar" width="100">
      <template #default="scope">
        <img :src="scope.row.avatar" alt="Avatar" width="50">
      </template>
    </el-table-column>

    <el-table-column prop="status" label="Status">
      <template v-slot="scope">
        <span v-show="scope.row.status === 1">Available</span>
        <span v-show="scope.row.status === 0">Banned</span>
      </template>
    </el-table-column>

    <el-table-column prop="registerAt" label="Register At"></el-table-column>

    <el-table-column prop="lastLoginAt" label="Last Login At"></el-table-column>

    <el-table-column label="Operate">
      <template #default="scope">
        <el-button
            @click="handleSwitchStatus(scope.row.id)"
            :type="scope.row.status === 1 ? 'warning' : 'success'"
            size="small">
          {{ scope.row.status === 1 ? 'Ban' : 'React' }}
        </el-button>
        <!--                        <el-button @click="handleDelete(scope.row)" type="danger" size="mini">Delete</el-button>-->
        <el-button @click="$emit('checkUser', scope.row.id)" size="small">
          Check
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页 -->
  <el-pagination
      @current-change="handlePageChange"
      @size-change="handlePageSizeChange"
      :current-page="pageNo"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50]"
      :total="total">
  </el-pagination>
</template>

<style scoped>

</style>
