document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('passwordChangeForm');
    const passwordToken = sessionStorage.getItem('findPassword');

    // If token is not present, redirect to an error page
    if (!passwordToken) {
        window.location.href = '/study/user/page/findPassword';
    }


    document.getElementById('passwordChangeForm').addEventListener('submit', function (event) {
        event.preventDefault();


        if (commonValidate() && passwordValidate()) {
            const newPassword = document.getElementById('inputPassword').value;

            fetch('/study/user/api/password2', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    newPassword: newPassword,
                    passwordToken: passwordToken
                })
            })
                .then(function (response) {
                    if (!response.ok) {
                        throw new Error('비밀번호 변경 요청 실패');
                    }
                    return response.json();
                })
                .then(function (data) {
                    console.log('비밀번호 변경 성공:', data);
                    sessionStorage.removeItem('findPassword');
                    window.location.href = '/study/user/page/passwordChangeSuccess';
                })
                .catch(function (error) {
                    console.error('비밀번호 변경 중 오류 발생:', error);
                    alert('비밀번호 변경에 실패했습니다. 비밀번호 찾기를 다시 진행해주세요.');
                    window.location.href = '/study/findPassword';
                });
        }
    });
});