document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('loginButton');
    const isLoggedIn = sessionStorage.getItem('aToken') !== null;

    updateLoginButton(isLoggedIn);

    function updateLoginButton(isLoggedIn) {
        if (isLoggedIn) {
            loginButton.textContent = '로그아웃';
            loginButton.style.backgroundColor = '#ff4d4d'; // Set color for logout
            loginButton.style.color = '#ffffff'
            loginButton.removeEventListener('click', handleLogin);
            loginButton.addEventListener('click', handleLogout);
        } else {
            loginButton.textContent = '로그인';
            loginButton.style.backgroundColor = '#ffffff'; // Set color for login
            loginButton.removeEventListener('click', handleLogout);
            loginButton.addEventListener('click', handleLogin);
        }
    }

    function handleLogin() {
        window.location.href = '/study/login';
    }

    function handleLogout() {
        sessionStorage.removeItem('aToken');
        fetch('/study/user/api/logout', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken: sessionStorage.getItem('rToken') }),
        }).then(response => {
            if (response.ok) {
                sessionStorage.removeItem('rToken');
                window.location.href = '/study/board/page/boardIndex'
            } else {
                console.error('로그아웃 실패');
            }
        }).catch(error => {
            console.error('서버 요청 중 오류 발생:', error);
        });
    }
});