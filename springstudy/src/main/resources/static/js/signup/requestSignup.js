function requestSignup(event) {
    event.preventDefault(); // Prevent the default form submission


    const userId = document.getElementById('inputAdress').value.trim();
    const name = document.getElementById('inputName').value.trim();
    const password = document.getElementById('inputPassword').value.trim();
    const birth = document.getElementById('inputBirth').value.trim();
    const email = document.getElementById('inputEmail').value.trim();
    const phoneNum = document.getElementById('inputPhoneNum').value.trim();


    if (commonValidate()) {
        const signupBody = {
            userId: userId,
            name: name,
            password: password,
            birth: birth,
            email: email,
            phoneNum: phoneNum,
        };

        fetch('/study/user/api/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(signupBody),
        })
            .then(function (response) {

                if (!response.ok) {
                    switch (response.status) {
                        case 400:
                            alert('입력값을 확인해주세요! 아이디나 이메일이 너무 짧거나 잘못된 형식입니다.');
                            break;
                        case 409:
                            alert('이미 가입된 정보입니다.');
                            break;
                        case 500:// todo: 동일 전화번호 가입에 대한 임시조치. SignupService에서 전화번호 동일성 검사가 없어 서버에서 500에러 발생 중
                            alert('다른 전화번호를 입력해주세요.');
                            break;
                        default:
                            alert('알 수 없는 오류가 발생했습니다.');
                            break;
                    }
                } else if (response.ok) {
                    window.location.href = '/study/signupSuccess';
                }
            })
            .catch(function (error) {
                console.error('중복 검사 중 오류 발생:', error);
            });
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('joinForm');

    if (form) {
        form.addEventListener('submit', requestSignup);
    }
});