//main.page.js

document.addEventListener('DOMContentLoaded', async () => {
    const guest = document.getElementById('guest-actions');
    const auth  = document.getElementById('auth-actions');
    const nameEl = document.getElementById('display-name');
    const btnLogin  = document.getElementById('btn-go-login');
    const btnLogout = document.getElementById('btn-logout');

    // 로그인 버튼, /login.html
    btnLogin?.addEventListener('click', () => {
        location.href = '/login.html';
    });

    // 로그아웃 버튼, 세션 종료 후 로그인 페이지로
    btnLogout?.addEventListener('click', async () => {
        try {
            await API.logout(); // DELETE /api/session
            location.href = '/main.html';
        } catch {
            alert('로그아웃에 실패했습니다. 잠시 후 다시 시도해 주세요.');
        }
    });

    // 세션 확인 로그인 상태, 로그인 중이면 보이고 아니면 숨기기
    try {
        // GET /api/session
        const me = await API.me();
        
        // 로그인때 로그인 정보 보이기
        if (me?.exists) {
            const displayName = me.nickname || me.name || me.username;
            if (nameEl) nameEl.textContent = displayName;
            if (auth)  auth.style.display  = 'block';
            if (guest) guest.style.display = 'none';
        } else {
            // 비로그인 때 로그인 정보 숨기기
            if (auth)  auth.style.display  = 'none';
            if (guest) guest.style.display = 'block';
        }
    } catch (e) {
        // 오류 시에도 비로그인 화면으로 유도
        if (auth)  auth.style.display  = 'none';
        if (guest) guest.style.display = 'block';
        console.error('[main] session check error:', e);
    }
});
