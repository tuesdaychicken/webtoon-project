// ui.error.js

(function () {
    'use strict';

    // 전역 네임스페이스 통일
    const UI = window.UI || (window.UI = {});
    const Error = UI.Error || (UI.Error = {});

    // 상단 메시지 출력
    Error.setMsg = function setMsg(msgEl, text) {
        if (msgEl) msgEl.textContent = text || '';
    };

    // 필드 에러 표시 + aria-invalid 토글
    Error.setFieldError = function setFieldError(inputEl, errEl, text) {
        if (errEl) errEl.textContent = text || '';
        if (inputEl) {
            if (text) inputEl.setAttribute('aria-invalid', 'true');
            else inputEl.removeAttribute('aria-invalid');
        }
    };

    // 지난 시도의 메시지/필드 에러 초기화
    Error.clearErrors = function clearErrors(opts) {
        if (!opts) return;
        const { msgEl, fields } = opts;
        Error.setMsg(msgEl, '');
        if (Array.isArray(fields)) {
            for (const f of fields) {
                Error.setFieldError(f?.inputEl || null, f?.errEl || null, '');
            }
        }
    };

    // 서버 검증 에러 JSON을 { field: message }로 정규화
    Error.parseFieldErrors = function parseFieldErrors(data) {
        if (!data) return {};
        // 맵 형태: { errors: { field: [msg]|msg } }
        if (data && data.errors && !Array.isArray(data.errors) && typeof data.errors === 'object') {
            const out = {};
            for (const k of Object.keys(data.errors)) {
                const v = data.errors[k];
                if (Array.isArray(v)) out[k] = String(v[0] ?? '');
                else out[k] = String(v ?? '');
            }
            return out;
        }
        // 배열 형태: { errors: [{ field, message }, ...] }
        if (data && Array.isArray(data.errors)) {
            const out = {};
            for (const it of data.errors) {
                const field = it?.field;
                const msg = it?.message;
                if (field && msg && !out[field]) out[field] = String(msg);
            }
            return out;
        }
        // 기본: 필드 단위 정보 없음(서버가 message만 보낼 때)
        return {};
    };

    // 정규화된 필드 에러를 화면 요소에 적용
    Error.applyErrors = function applyErrors(fieldErrors, mapping) {
        if (!fieldErrors || !mapping) return;
        for (const key of Object.keys(fieldErrors)) {
            const pair = mapping[key];
            if (pair) {
                Error.setFieldError(pair.inputEl || null, pair.errEl || null, fieldErrors[key]);
            }
        }
    };

    /**
     * 필드 id 규칙을 바탕으로 { field: {inputEl, errEl} } 매핑을 생성한다.
     * @param {string[]|Record<string,{inputId?:string, errId?:string}>} spec
     *  - 배열: ['username','password'] → inputId='#username', errId='#username-error'
     *  - 객체: { field: { inputId:'custom-input', errId:'custom-err' } }
     * @returns {Record<string,{inputEl:HTMLElement|null, errEl:HTMLElement|null}>}
     */
    Error.createMappingByIds = function createMappingByIds(spec) {
        const mapping = {};
        if (Array.isArray(spec)) {
            for (const field of spec) {
                const inputEl = document.getElementById(field);
                const errEl = document.getElementById(`${field}-error`);
                mapping[field] = { inputEl: inputEl || null, errEl: errEl || null };
            }
            return mapping;
        }
        if (spec && typeof spec === 'object') {
            for (const field of Object.keys(spec)) {
                const conf = spec[field] || {};
                const inputId = conf.inputId || field;
                const errId = conf.errId || `${field}-error`;
                mapping[field] = {
                    inputEl: document.getElementById(inputId) || null,
                    errEl: document.getElementById(errId) || null,
                };
            }
        }
        return mapping;
    };

    // 상태코드/서버 메시지 기반 일관 출력 (+ UX 옵션)
    Error.showApiError = function showApiError(err, opts = {}) {
        const status = err?.status || 0;
        const data = err?.data || null;
        const serverMsg = typeof data?.message === 'string' ? data.message : '';
        const m = opts?.defaults || {};

        // 상태코드별 기본 메시지(서버 메시지 우선)
        const msgByStatus = {
            400: serverMsg || m.badRequest || '입력값을 다시 확인해주세요.',
            401: serverMsg || m.unauthorized || '아이디 또는 비밀번호가 올바르지 않습니다.',
            403: serverMsg || m.forbidden || '접근 권한이 없습니다.',
            404: serverMsg || m.notFound || '대상을 찾을 수 없습니다.',
            409: serverMsg || m.conflict || '이미 사용 중인 값이 있습니다.',
            422: serverMsg || m.unprocessable || '요청을 처리할 수 없습니다.',
            429: serverMsg || m.tooManyRequests || '요청이 너무 많습니다. 잠시 후 다시 시도하세요.',
            x:   serverMsg || m.general || '처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
        };

        // 상단 메시지 우선 표시
        const outMsg = msgByStatus[status] ?? msgByStatus.x;
        Error.setMsg(opts.msgEl || null, outMsg);

        // 필드 단위 적용(서버가 제공할 경우)
        const fieldErrors = Error.parseFieldErrors(data);
        if (Object.keys(fieldErrors).length && opts.mapping) {
            Error.applyErrors(fieldErrors, opts.mapping);
        }

        // UX 옵션: 첫 에러 스크롤/포커스
        const needScroll = !!opts.scrollIntoViewFirstError;
        const needFocus = !!opts.focusFirstError;

        if ((needScroll || needFocus) && opts.mapping) {
            // 우선순위: 실제 fieldErrors → (401의 경우) hintFieldKeysOn401
            let firstKey = null;
            const keys = Object.keys(fieldErrors);
            if (keys.length) {
                firstKey = keys.find(k => opts.mapping && opts.mapping[k]) || null;
            } else if (status === 401 && Array.isArray(opts.hintFieldKeysOn401)) {
                firstKey = opts.hintFieldKeysOn401.find(k => opts.mapping && opts.mapping[k]) || null;
            }

            if (firstKey) {
                const pair = opts.mapping[firstKey];
                const target = pair?.inputEl || pair?.errEl || null;
                if (target) {
                    if (needScroll && target.scrollIntoView) {
                        try { target.scrollIntoView({ behavior: 'smooth', block: 'center' }); } catch (_) {}
                    }
                    if (needFocus && target.focus) {
                        setTimeout(() => { try { target.focus(); } catch (_) {} }, 0);
                    }
                }
            }
        }

        // 401 시 레이아웃 흔들림 방지 공백 힌트(옵션)
        if (status === 401 && Array.isArray(opts.hintFieldKeysOn401) && opts.mapping) {
            for (const k of opts.hintFieldKeysOn401) {
                const pair = opts.mapping[k];
                if (pair) Error.setFieldError(pair.inputEl || null, pair.errEl || null, ' ');
            }
        }
    };

    window.UI = UI;
})();
