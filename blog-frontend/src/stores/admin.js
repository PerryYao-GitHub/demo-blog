import {defineStore} from "pinia";
import {computed, ref} from "vue";

export const useAdminStore = defineStore('admin', () => {
    const token = ref("");
    const isLogin = computed(() => !!token.value);
    function setToken ($token) {
        token.value = $token;
    }
    function delAdmin () {
        token.value = ""
    }

    return { token, isLogin, setToken, delAdmin };
})
