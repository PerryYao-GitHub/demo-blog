import {createRouter, createWebHistory} from 'vue-router'
import {useUserStore} from "@/stores/user.js";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // admin
        {
            path: "/admin",
            name: "admin",
            component: () => import('@/views/AdminView.vue'),
        },

        // user
        {
            path: '/auth',
            name: 'auth',
            component: () => import('@/views/AuthView.vue')
        },

        {
            path: '/home/blogs',
            component: () => import('@/views/HomeBlogsView.vue')
        },
        {
            path: '/home/users',
            component: () => import('@/views/HomeUsersView.vue')
        },
        {
            path: "/one/user/:targetId",
            component: () => import('@/views/OneUserView.vue'),
            meta: {
                requiresAuth: true, // 需要登录
            }
        },
        {
            path: "/one/blog/:blogId",
            component: () => import("@/views/OneBlogView.vue"),
            meta: {
                requiresAuth: true,
            }
        },
        {
            path: "/me",
            component: () => import('@/views/MeView.vue'),
            meta: {
                requiresAuth: true,
            }
        }


    ]
})

// 全局导航守卫
router.beforeEach((to, from, next) => {
    // 在导航守卫中调用 useUserStore
    const userStore = useUserStore();

    // 检查路由是否需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!userStore.isLogin) {
            // 如果用户没有登录，重定向到 /auth 页面
            next({name: 'auth'});
        } else {
            // 如果已经登录，继续导航
            next();
        }
    } else {
        // 对于不需要认证的路由，直接继续导航
        next();
    }
});

export default router;
