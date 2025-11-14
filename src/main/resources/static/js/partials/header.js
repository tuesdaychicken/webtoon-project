// 헤더 전용 스크립트(요소 생성 없음, 표시/토글만)
(function () {
    document.addEventListener('DOMContentLoaded', async () => {
        const guestBox = document.getElementById('guest-actions');
        const authBox  = document.getElementById('auth-actions');
        const nameEl   = document.getElementById('display-name');
        const btnOut   = document.getElementById('btn-logout');

        function showGuest() {
            if (guestBox) guestBox.classList.remove('hidden');
            if (authBox)  authBox.classList.add('hidden');
        }
        function showAuth(displayName) {
            if (nameEl)  nameEl.textContent = displayName || '사용자';
            if (guestBox) guestBox.classList.add('hidden');
            if (authBox)  authBox.classList.remove('hidden');
        }

        // 세션 상태 확인
        try {
            const me = await API.me();
            const displayName = me?.nickname || me?.name || me?.username || '사용자';
            showAuth(displayName);
        } catch (_) {
            showGuest();
        }

        // 로그아웃
        if (btnOut) {
            btnOut.addEventListener('click', async () => {
                try { await API.logout(); } catch (_) {}
                // 전체 페이지 새로고침(헤더는 iframe일 수 있음)
                if (window.top && window.top.location) {
                    window.top.location.reload();
                } else {
                    window.location.reload();
                }
            });
        }
    });
})();
