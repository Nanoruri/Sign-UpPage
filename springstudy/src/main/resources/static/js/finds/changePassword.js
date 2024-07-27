document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('passwordChangeForm');
    const token = sessionStorage.getItem('findPassword');

    // If token is not present, redirect to an error page
    if (!token) {
        window.location.href = '/study/findPassword';
    }


    document.getElementById('passwordChangeForm').addEventListener('submit', function (event) {
        event.preventDefault();


        if (commonValidate() && checkPasswordMatch()) {
            const newPassword = document.getElementById('inputPassword').value;

            fetch('/study/user/api/passwordChange', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    newPassword: newPassword,
                    passwordToken: token
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
                    window.location.href = '/study/passwordChangeSuccess';
                })
                .catch(function (error) {
                    console.error('비밀번호 변경 중 오류 발생:', error);
                    alert('비밀번호 변경에 실패했습니다.');
                });
        }
    });
});