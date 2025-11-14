// signup.page.js


(function () {
    document.addEventListener('DOMContentLoaded', () => {
        // [추가] HTML 요소 참조 (요소는 모두 HTML에 존재해야 함)
        const form = document.getElementById('signup-form');
        const msgEl = document.getElementById('msg');
        const usernameEl = document.getElementById('username');
        const passwordEl = document.getElementById('password');
        const nicknameEl = document.getElementById('nickname');
        const emailEl = document.getElementById('email');

        // [추가] 상단 메시지 한 줄만 표시
        function setMsg(text, type) {
            msgEl.textContent = text || '';
            msgEl.className = 'msg' + (type ? ` msg--${type}` : '');
        }

        // [추가] 폼 제출: 값 수집 → API.register 호출 → 성공/실패 한 줄 처리
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            setMsg('가입 요청을 보내는 중입니다…', 'info');

            const payload = {
                username: usernameEl.value,
                password: passwordEl.value,
                nickname: nicknameEl.value,
                email: emailEl.value,
            };

            try {
                await API.register(payload);               // [추가] 서버에 회원가입 요청(2xx가 아니면 throw)
                setMsg('가입 완료! 로그인 페이지로 이동합니다…', 'success');
                setTimeout(() => { window.location.href = '/login.html'; }, 800); // [추가]
            } catch (err) {
                // [추가] 서버가 내려준 message 있으면 그대로 표시
                const msg = err?.data?.message || '가입에 실패했습니다. 입력값을 다시 확인해 주세요.';
                setMsg(msg, 'error');
            }
        });
    });
})();
