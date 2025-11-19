// login.page.js 추후 유효성 검사 분리

(function () {
    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('login-form');
        const usernameEl = document.getElementById('username');
        const passwordEl = document.getElementById('password');
        const msgEl = document.getElementById('msg');
        const usernameErrEl  = document.getElementById('username-error');
        const passwordErrEl  = document.getElementById('password-error');

        if (!form) return;

        // 매핑 헬퍼 사용
        const mapping = UI.Error.createMappingByIds(['username', 'password']);

        form.addEventListener('submit', async (ev) => {
            ev.preventDefault();

            UI.Error.clearErrors({
                msgEl,
                fields: [
                    { inputEl: usernameEl, errEl: usernameErrEl },
                    { inputEl: passwordEl, errEl: passwordErrEl }
                ]
            });

            const usernameVal = (usernameEl?.value || '').trim();
            const passwordVal = passwordEl?.value || '';

            let hasError = false;
            if (!usernameVal) { UI.Error.setFieldError(usernameEl, usernameErrEl, '아이디를 입력하세요.'); hasError = true; }
            if (!passwordVal) { UI.Error.setFieldError(passwordEl, passwordErrEl, '비밀번호를 입력하세요.'); hasError = true; }
            if (hasError) return;

            try {
                if (!window.API?.login) {
                    throw Object.assign(new Error('API 로드 안됨'), { status: 500 });
                }

                UI.Error.setMsg(msgEl, '로그인 중...');

                await API.login({ username: usernameVal, password: passwordVal });

                UI.Error.setMsg(msgEl, '로그인 성공. 잠시만 기다려주세요...');
                window.location.assign('/main');
            } catch (err) {
                UI.Error.showApiError(err, {
                    msgEl,
                    mapping,
                    defaults: {
                        unauthorized: '아이디 또는 비밀번호가 올바르지 않습니다.',
                        badRequest: '입력값을 다시 확인해주세요.',
                        general: '로그인에 실패했습니다. 잠시 후 다시 시도해주세요.'
                    },
                    hintFieldKeysOn401: ['username', 'password'],
                    scrollIntoViewFirstError: true,
                    focusFirstError: true
                });
                console.error('[login.page] error:', err);
            }
        });
    });
})();