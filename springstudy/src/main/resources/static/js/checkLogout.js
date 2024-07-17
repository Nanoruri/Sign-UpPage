document.addEventListener('DOMContentLoaded', function () {
    var loginLogoutButtonDiv = document.getElementById('loginLogoutButton');
    // 초기 로그인 상태 확인
    const isLoggedIn = sessionStorage.getItem('aToken') !== null;
        updateLoginStatus(isLoggedIn);

    function updateLoginStatus(isLoggedIn) {
        loginLogoutButtonDiv.innerHTML = '';



        if (isLoggedIn) {
            // 로그인 상태일 때 로그아웃 버튼 추가
            var logoutForm = document.createElement('form');
            logoutForm.method = 'post';
            logoutForm.action = '/study/logout';
            var logoutButton = document.createElement('button');
            logoutButton.type = 'submit';
            logoutButton.className = 'btn btn-danger border-2';
            logoutButton.textContent = '로그아웃';
            logoutForm.appendChild(logoutButton);
            loginLogoutButtonDiv.appendChild(logoutForm);
            sessionStorage.removeItem('aToken');
        } else {
            // 로그아웃 상태일 때 로그인 버튼 추가
            var loginForm = document.createElement('form');
            loginForm.method = 'get';
            loginForm.action = '/study/login';
            var loginButton = document.createElement('button');
            loginButton.type = 'submit';
            loginButton.className = 'btn btn-primary border-2';
            loginButton.textContent = '로그인';
            loginForm.appendChild(loginButton);
            loginLogoutButtonDiv.appendChild(loginForm);
        }

    }
});
