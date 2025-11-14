// login.page.js
(function () {
    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('login-form');
        const usernameEl = document.getElementById('username');
        const passwordEl = document.getElementById('password');
        const msgEl = document.getElementById('msg');
        const usernameErrEl = document.getElementById('username-error');
        const passwordErrEl = document.getElementById('password-error');

        // 상단 메시지 표시(요소가 없으면 조용히 무시)
        function setMsg(text) { if (msgEl) msgEl.textContent = text || ''; }

        // 필드별 에러 표시/초기화
        function setFieldError(inputEl, errEl, text) {
            if (errEl) errEl.textContent = text || '';
            if (inputEl) {
                if (text) inputEl.setAttribute('aria-invalid', 'true');
                else inputEl.removeAttribute('aria-invalid');
            }
        }

        // 제출 전 매번 깨끗하게
        function clearErrors() {
            setMsg('');
            setFieldError(usernameEl, usernameErrEl, '');
            setFieldError(passwordEl, passwordErrEl, '');
        }

        if (!form) return; // 폼이 없으면 안전 탈출

        form.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            clearErrors();

            const usernameVal = (usernameEl?.value || '').trim();
            const passwordVal = passwordEl?.value || '';

            // 최소 클라이언트 검증
            let hasError = false;
            if (!usernameVal) { setFieldError(usernameEl, usernameErrEl, '아이디를 입력하세요.'); hasError = true; }
            if (!passwordVal) { setFieldError(passwordEl, passwordErrEl, '비밀번호를 입력하세요.'); hasError = true; }
            if (hasError) return;

            try {
                if (!window.API?.login) {                              // [추가] 방어: API.login 미존재 시
                    throw Object.assign(new Error('API module not loaded'), { status: 500 });
                }

                setMsg('로그인 중...');

                // api.js 계약 사용: 성공 시 resolve(data), 실패 시 throw(err{ status, data })
                await API.login({ username: usernameVal, password: passwordVal }); // [수정]

                // 성공: /main으로 이동
                setMsg('로그인 성공. 잠시만 기다려주세요...');
                window.location.assign('/main');
            } catch (err) {
                const status = err?.status || 0;
                const data   = err?.data || null;

                if (status === 400) {
                    setMsg('입력값을 다시 확인해주세요.');
                    // 서버 검증오류 포맷 유연 처리
                    const errors = data?.errors ?? data ?? {};
                    const uErr = errors.username ?? errors['username'];
                    const pErr = errors.password ?? errors['password'];
                    if (uErr) setFieldError(usernameEl, usernameErrEl, Array.isArray(uErr) ? uErr[0] : String(uErr));
                    if (pErr) setFieldError(passwordEl, passwordErrEl, Array.isArray(pErr) ? pErr[0] : String(pErr));
                    return;
                }

                if (status === 401) {
                    setMsg('아이디 또는 비밀번호가 올바르지 않습니다.');
                    // 레이아웃 흔들림 방지: 공백이라도 채워 포커스 유도
                    setFieldError(usernameEl, usernameErrEl, ' ');
                    setFieldError(passwordEl, passwordErrEl, ' ');
                    return;
                }

                setMsg('로그인에 실패했습니다. 잠시 후 다시 시도해주세요.');
                console.error('[login.page] error:', err);
            }
        });
    });
})();
