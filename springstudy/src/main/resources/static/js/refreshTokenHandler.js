const refreshAccessToken = async (refreshToken) => {
  try {
    const response = await fetch('/study/auth/refresh', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ refreshToken }),
    });

    if (!response.ok) {
      alert('로그인이 필요합니다.');
      window.location.href = '/study/user/page/login';
      throw new Error('리프레시 토큰 처리 실패');
    }

    return await response.json();
  } catch (error) {
    console.error('Error refreshing access token:', error);
    return null;
  }
};

export default refreshAccessToken;
