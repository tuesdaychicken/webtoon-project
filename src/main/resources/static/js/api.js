// api.js

const API = (() => {
    const BASE = '/api';
    const JSON_HEADERS = { 'Content-Type': 'application/json' };

    // 공통 응답 처리
    async function handle(res) {
        const data = await res.json().catch(() => ({}));
        if (!res.ok) {
            const err = new Error(data?.message || res.statusText || 'ERROR');
            err.status = res.status;
            err.data = data;
            throw err;
        }
        return data;
    }

    return {
        // 회원가입: 201 Created 기대
        register: ({ username, password, nickname, email }) =>
            fetch(`${BASE}/users`, {
                method: 'POST',
                headers: JSON_HEADERS,
                credentials: 'same-origin',
                body: JSON.stringify({ username, password, nickname, email }),
            }).then(handle),

        // 로그인: 200/201/204 기대
        login: ({ username, password }) =>
            fetch(`${BASE}/session`, {
                method: 'POST',
                headers: JSON_HEADERS,
                credentials: 'same-origin',
                body: JSON.stringify({ username, password }),
            }).then(handle),

        // 내 세션 상태 조회
        me: () =>
            fetch(`${BASE}/session`, {
                credentials: 'same-origin',
            }).then(handle),

        // 로그아웃
        logout: () =>
            fetch(`${BASE}/session`, {
                method: 'DELETE',
                credentials: 'same-origin',
            }).then(handle),
    };
})();

window.API = API;
