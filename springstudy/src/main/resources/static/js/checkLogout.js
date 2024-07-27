document.addEventListener('DOMContentLoaded', function () {
    const loginLogoutButtonDiv = document.getElementById('loginLogoutButton');

    // 초기 로그인 상태 확인
    const isLoggedIn = sessionStorage.getItem('aToken') !== null;
    updateLoginStatus(isLoggedIn);

    function updateLoginStatus(isLoggedIn) {
        loginLogoutButtonDiv.innerHTML = '';

        if (isLoggedIn) {
            // 로그인 상태일 때 로그아웃 버튼 추가
            const logoutForm = document.createElement('form');
            logoutForm.method = 'post';
            logoutForm.action = '/study/logout';

            const logoutButton = document.createElement('button');
            logoutButton.type = 'submit';
            logoutButton.className = 'btn btn-danger border-2';
            logoutButton.textContent = '로그아웃';

            // 로그아웃 버튼 클릭 시 세션 스토리지에서 aToken 제거
            logoutForm.addEventListener('submit', function (event) {
                event.preventDefault(); // 기본 폼 제출 동작 방지
                sessionStorage.removeItem('aToken');
                sessionStorage.removeItem('rToken');

                // 서버에 로그아웃 요청 보내기
                fetch('/study/user/api/logout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({

                    })
                }).then(response => {
                    if (response.ok) {

                        window.location.reload();
                    } else {
                        // 로그아웃 실패 처리
                        console.error('로그아웃 실패');
                    }
                }).catch(error => {
                    console.error('서버 요청 중 오류 발생:', error);
                });
            });

            logoutForm.appendChild(logoutButton);
            loginLogoutButtonDiv.appendChild(logoutForm);
        } else {
            // 로그아웃 상태일 때 로그인 버튼 추가
            const loginForm = document.createElement('form');
            loginForm.method = 'get';
            loginForm.action = '/study/login';

            const loginButton = document.createElement('button');
            loginButton.type = 'submit';
            loginButton.className = 'btn btn-primary border-2';
            loginButton.textContent = '로그인';

            loginForm.appendChild(loginButton);
            loginLogoutButtonDiv.appendChild(loginForm);
        }
    }
});
