// login.pages.js

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('login-form');
    const $u = document.getElementById('username');
    const $p = document.getElementById('password');
    const $msg = document.getElementById('message');

    form?.addEventListener('submit', async (e) => {
        e.preventDefault();
        $msg.textContent = '';
        const username = $u.value.trim();
        const password = $p.value;

        if (!username || !password) {
            $msg.textContent = '아이디와 비밀번호를 입력해 주세요.';
            return;
        }

        try {
            await API.login({ username, password });
            location.href = '/main.html';
        } catch (err) {
            if (err.status === 401) {
                $msg.textContent = '아이디 또는 비밀번호가 올바르지 않습니다.';
            } else if (err.status === 400) {
                $msg.textContent = '입력 형식이 올바르지 않습니다.';
            } else {
                $msg.textContent = err.message || '로그인 중 오류가 발생했습니다.';
            }
        }
    });
});
