document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('passwordChangeForm').addEventListener('submit', function (event) {
        event.preventDefault();


        if (commonValidate() && checkPasswordMatch()) {
        var userId = document.getElementById('userId').value;
        var name = document.getElementById('name').value;
        var phoneNum = document.getElementById('phoneNum').value;
        var newPassword = document.getElementById('inputPassword').value;
        
        fetch('/study/passwordChange', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: userId,
                name: name,
                phoneNum: phoneNum,
                newPassword: newPassword
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
            window.location.href = '/study/passwordChangeSuccess';
        })
        .catch(function (error) {
            console.error('비밀번호 변경 중 오류 발생:', error);
            alert('비밀번호 변경에 실패했습니다.');
        });
    }});
});