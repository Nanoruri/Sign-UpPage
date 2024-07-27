document.addEventListener('DOMContentLoaded', function () {
    const loginLogoutButtonDiv = document.getElementById('loginLogoutButton');

    // 초기 로그인 상태 확인
    const isLoggedIn = sessionStorage.getItem('aToken') !== null;
    updateLoginStatus(isLoggedIn);

    function updateLoginStatus(isLoggedIn) {
        loginLogoutButtonDiv.innerHTML = '';

        if (isLoggedIn) {
            const logoutForm = createForm('/study/logout', 'post');
            const logoutButton = createButton('submit', 'btn btn-danger border-2', '로그아웃');
            logoutForm.appendChild(logoutButton);
            logoutForm.addEventListener('submit', handleLogout);
            loginLogoutButtonDiv.appendChild(logoutForm);
        } else {
            const loginForm = createForm('/study/login', 'get');
            const loginButton = createButton('submit', 'btn btn-primary border-2', '로그인');
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
        sessionStorage.removeItem('rToken');
        fetch('/study/user/api/logout', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({})
        }).then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('로그아웃 실패');
            }
        }).catch(error => {
            console.error('서버 요청 중 오류 발생:', error);
        });
    }
});