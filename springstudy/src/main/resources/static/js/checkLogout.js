document.addEventListener('DOMContentLoaded', function () {
    const loginLogoutButtonDiv = document.getElementById('loginLogoutButton');

    // 초기 로그인 상태 확인
    const isLoggedIn = sessionStorage.getItem('aToken') !== null;// todo: 로그인 상태 조건문 수정해야함.존재 여부가 아닌 토큰의 만료시간을 측정하여 로그인 상태를 판단해야함.
    updateLoginStatus(isLoggedIn);

    function updateLoginStatus(isLoggedIn) {
        loginLogoutButtonDiv.innerHTML = '';

        if (isLoggedIn) {
            const logoutForm = createForm('/study/logout', 'post');
            const logoutButton = createButton('button', 'btn btn-danger border-2', '로그아웃');
            logoutForm.appendChild(logoutButton);
            logoutForm.addEventListener('click', handleLogout);
            loginLogoutButtonDiv.appendChild(logoutForm);
        } else {
            const loginForm = createForm('/study/login', 'get');
            const loginButton = createButton('button', 'btn btn-primary border-2', '로그인');
            loginButton.addEventListener('click', function () {
                window.location.href = '/study/user/page/login';
            });
            loginForm.appendChild(loginButton);
            loginLogoutButtonDiv.appendChild(loginForm);
        }
    }

    function createForm(action, method) {
        const form = document.createElement('form');
        form.action = action;
        form.method = method;
        return form;
    }

    function createButton(type, className, textContent) {
        const button = document.createElement('button');
        button.type = type;
        button.className = className;
        button.textContent = textContent;
        return button;
    }

    function handleLogout(event) {
        event.preventDefault();
        sessionStorage.removeItem('aToken');
        fetch('/study/user/api/logout', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken: sessionStorage.getItem('rToken') }),
        }).then(response => {
            if (response.ok) {
                sessionStorage.removeItem('rToken');
                window.location.reload();
            } else {
                console.error('로그아웃 실패');
            }
        }).catch(error => {
            console.error('서버 요청 중 오류 발생:', error);
        });
    }
});