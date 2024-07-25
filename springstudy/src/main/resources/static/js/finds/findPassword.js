document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('findPasswordForm').addEventListener('submit', function (event) {
        event.preventDefault();


        if (commonValidate()) { //동일한 html에 있는 js파일의 내용을 가져다 쓸 수 있음.
        var userId = document.getElementById('inputAdress').value;
        var name = document.getElementById('inputName').value;
        var phoneNum = document.getElementById('inputPhoneNum').value;


        fetch('/study/user/api/findPassword', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: userId,
                name: name,
                phoneNum: phoneNum

            }),
        })
            .then(function (response) {
                if (response.status === 404) {
                    alert('해당 정보로 가입한 가입자가 없습니다!');
                    throw new Error('사용자가 없습니다.');
                } else {
                    return response.json();
                }
            })
            .then(function (data) {
                console.log('사용자를 찾았습니다:', data);
                window.location.href = '/study/passwordChange';
            })

            .catch(function (error) {
                console.error('사용자를 찾는 중 오류 발생:', error);
            })
    }});
});