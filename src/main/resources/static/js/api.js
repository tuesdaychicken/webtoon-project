// api.js

const API = (() => {
    const JSON_HEADERS = {'Content-Type': 'application/json'};

    //fetch 요청 처리 메서드
    async function request(path, {method = 'GET', body} = {}) {
        const res = await fetch(path, {
            method,
            headers: JSON_HEADERS,
            credentials: 'same-origin', // 쿠키 붙여서
            body: body ? JSON.stringify(body) : undefined,
        });

        if (res.status >= 200 && res.status < 300) {

            if (res.status === 204) return null;
            try {
                return await res.json();
            } catch {
                return null;
            }
        }

        // 에러 ErrorResponse(code,message)
        let err;
        try {
            err = await res.json();
        } catch {
            err = {message: 'Unknown error'};
        }
        const e = new Error(err.message || `HTTP ${res.status}`);
        e.code = err.code;
        e.status = res.status;
        throw e;
    }

    // 세션 api
    const login = ({username, password}) => request('/api/session', {method: 'POST', body: {username, password}});
    const me = () => request('/api/session', {method: 'GET'});
    const logout = () => request('/api/session', {method: 'DELETE'});


    return {login, me, logout};
})();
