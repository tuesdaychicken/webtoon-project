// signup.page.js

(function () {
    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('signup-form');
        const msgEl = document.getElementById('msg');

        const usernameEl = document.getElementById('username');
        const passwordEl = document.getElementById('password');
        const password2El = document.getElementById('passwordConfirm');
        const nameEl = document.getElementById('name');
        const nicknameEl = document.getElementById('nickname');
        const emailEl = document.getElementById('email');

        const usernameErrEl = document.getElementById('username-error');
        const passwordErrEl = document.getElementById('password-error');
        const password2ErrEl = document.getElementById('passwordConfirm-error');
        const nameErrEl = document.getElementById('name-error');
        const nicknameErrEl = document.getElementById('nickname-error');
        const emailErrEl = document.getElementById('email-error');

        if (!form) return;

        // 매핑 헬퍼 사용
        const mapping = UI.Error.createMappingByIds([
            'username', 'password', 'passwordConfirm', 'name', 'nickname', 'email'
        ]);

        form.addEventListener('submit', async (ev) => {
            ev.preventDefault();

            UI.Error.clearErrors({
                msgEl,
                fields: [
                    {inputEl: usernameEl, errEl: usernameErrEl},
                    {inputEl: passwordEl, errEl: passwordErrEl},
                    {inputEl: password2El, errEl: password2ErrEl},
                    {inputEl: nameEl, errEl: nameErrEl },
                    {inputEl: nicknameEl, errEl: nicknameErrEl},
                    {inputEl: emailEl, errEl: emailErrEl}
                ]
            });

            const username = (usernameEl?.value || '').trim();
            const password = passwordEl?.value || '';
            const password2 = password2El?.value || '';
            const name = (nameEl?.value || '').trim();
            const nickname = (nicknameEl?.value || '').trim();
            const email = (emailEl?.value || '').trim();

            let hasError = false;
            if (!username) { UI.Error.setFieldError(usernameEl, usernameErrEl, '아이디를 입력하세요.'); hasError = true; }
            if (!password) { UI.Error.setFieldError(passwordEl, passwordErrEl, '비밀번호를 입력하세요.'); hasError = true; }
            if (!password2) { UI.Error.setFieldError(password2El, password2ErrEl, '비밀번호 확인을 입력하세요.'); hasError = true; }
            if (password && password2 && password !== password2) {
                UI.Error.setFieldError(password2El, password2ErrEl, '비밀번호가 일치하지 않습니다.'); hasError = true;
            }
            if (!name) { UI.Error.setFieldError(nameEl, nameErrEl, '이름을 입력하세요.'); hasError = true; }
            if (hasError) return;

            try {
                if (!window.API?.register) {
                    throw Object.assign(new Error('API 로드 안됨'), { status: 500 });
                }

                UI.Error.setMsg(msgEl, '회원가입 중...');

                await API.register({ username, password, nickname, email, name });

                UI.Error.setMsg(msgEl, '회원가입이 완료되었습니다. 로그인 화면으로 이동합니다...');
                window.location.assign('/login');
            } catch (err) {
                UI.Error.showApiError(err, {
                    msgEl,
                    mapping,
                    defaults: {
                        conflict: '이미 사용 중인 아이디(또는 이메일/닉네임)입니다.',
                        badRequest: '입력값을 다시 확인해주세요.',
                        general: '회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.'
                    },
                    scrollIntoViewFirstError: true,
                    focusFirstError: true
                });
                console.error('[signup.page] error:', err);
            }
        });
    });
})();
