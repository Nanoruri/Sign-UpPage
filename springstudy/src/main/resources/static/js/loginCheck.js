document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('loginForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const userId = document.getElementById('inputAdress').value;
        const password = document.getElementById('inputPassword').value;

        const loginData = {
            userId: userId,
            password: password
        };

        if (commonValidate()) {

            try {
                const response = await fetch('/study/user/api/loginCheck', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });

                if (response.ok) {
                    const jwt = await response.json();
                    const accessToken = jwt.accessToken;
                    const refreshToken = jwt.refreshToken;
                    sessionStorage.setItem('aToken', accessToken);
                    sessionStorage.setItem('rToken', refreshToken);
                    alert('로그인 성공!');
                    window.location.href = '/study/';
                } else {
                    alert('로그인 실패! 아이디와 비밀번호를 확인해주세요.');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('에러 발생! 잠시 후 시도 바랍니다.');
            }
        }
    });
});