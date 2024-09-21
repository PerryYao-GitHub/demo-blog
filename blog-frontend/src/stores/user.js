import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useUserStore = defineStore('user', () => {
    // states
    const storedToken = localStorage.getItem('token') || "";
    const token = ref(storedToken);

    // 使用 ref 来定义 info 对象
    const info = ref({
        id: null,
        account: "",
        name: "",
        email: "",
        avatar: "",
    });

    // getters
    const isLogin = computed(() => !!token.value);

    // methods
    function setToken(_token) {
        token.value = _token;
        localStorage.setItem('token', _token);
    }

    function setInfo(_info) {
        info.value = {
            id: _info.id,
            account: _info.account,
            name: _info.name,
            email: _info.email,
            avatar: _info.avatar,
        };
    }

    function delUser() {
        token.value = "";
        info.value = {
            id: null,
            account: "",
            name: "",
            email: "",
            avatar: "",
        };
        localStorage.removeItem('token');
    }

    return {
        token,
        info,
        isLogin,
        setToken,
        setInfo,
        delUser,
    };
});

