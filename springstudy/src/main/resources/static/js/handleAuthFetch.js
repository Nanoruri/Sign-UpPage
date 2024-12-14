import refreshAccessToken from "./refreshTokenHandler.js";

/**
 * accessToken을 포함한 fetch 요청을 보내는 함수.
 * 만약 응답이 401이면 refreshToken으로 갱신을 시도하고 다시 요청을 보냅니다.
 * refreshToken이 만료되었거나 갱신에 실패하면 401 응답
 * @see refreshAccessToken - accessToken 갱신 함수.
 *
 */
const fetchWithAuth = async (url, options = {}) => {
    const aToken = sessionStorage.getItem('aToken');
    const defaultHeaders = createDefaultHeaders(aToken);
    const headers = {...defaultHeaders, ...options.headers};

    let response = await fetch(url, {...options, headers});

    if (response.status === 401) {
        response = await handleTokenExpiration(url, options, headers);
    }

    return response;
};


const createDefaultHeaders = (aToken) => {
    const headers = {};
    if (aToken) {
        headers['Authorization'] = `Bearer ${aToken}`;
    }
    return headers;
};

const handleTokenExpiration = async (url, options, headers) => {
    const refreshToken = sessionStorage.getItem('rToken');
    if (refreshToken) {
        const tokens = await refreshAccessToken(refreshToken);
        if (tokens && tokens.accessToken) {
            updateSessionStorage(tokens);
            headers['Authorization'] = `Bearer ${tokens.accessToken}`;
            return await fetch(url, {...options, headers});
        }
    }
    return new Response(null, {status: 401, statusText: 'Unauthorized'});
};

const updateSessionStorage = (tokens) => {
    sessionStorage.setItem('aToken', tokens.accessToken);
    sessionStorage.setItem('rToken', tokens.refreshToken);
};

document.addEventListener('DOMContentLoaded', () => {
    const url = '/study/';
    fetchWithAuth(url).then(response => {
        console.log('Request made to:', url, 'with response:', response);

        const event = new CustomEvent('authFetchCompleted', {detail: response});
        document.dispatchEvent(event);
    });
});