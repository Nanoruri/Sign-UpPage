import refreshAccessToken  from "./refreshTokenHandler.js";

const fetchWithAuth = async (url, options = {}) => {
  const aToken = sessionStorage.getItem('aToken');
  const defaultHeaders = createDefaultHeaders(aToken);
  const headers = { ...defaultHeaders, ...options.headers };

  let response = await fetch(url, { ...options, headers });

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
      return await fetch(url, { ...options, headers });
    }
  }
  return new Response(null, { status: 401, statusText: 'Unauthorized' });
};

const updateSessionStorage = (tokens) => {
  sessionStorage.setItem('aToken', tokens.accessToken);
  sessionStorage.setItem('rToken', tokens.refreshToken);
};

document.addEventListener('DOMContentLoaded', () => {
  const url = '/study/';
  fetchWithAuth(url).then(response => {
    console.log('Request made to:', url, 'with response:', response);
  });
});