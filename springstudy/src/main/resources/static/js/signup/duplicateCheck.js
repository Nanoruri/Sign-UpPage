document.addEventListener('DOMContentLoaded', function () {
    const userIdInput = document.getElementById('inputAdress');
    const emailInput = document.getElementById('inputEmail');

    userIdInput.addEventListener('input', checkDuplicate);
    emailInput.addEventListener('input', checkDuplicateEmail);
});

function checkDuplicate() {
    const userIdInput = document.getElementById('inputAdress');
    const userId = userIdInput.value.trim(); // 입력값의 앞뒤 공백 제거
    const duplicateIdMessage = document.getElementById('duplicateIdMessage');
    const signupButton = document.getElementById('button');

    // 사용자가 입력하지 않은 경우 처리
    if (userId === "") {
        duplicateIdMessage.innerText = '';
        return; // 중복 확인을 수행하지 않고 함수 종료
    }

    fetch('/study/user/api/idCheck', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',

        },
        body: JSON.stringify({
            userId: userId
        }),
    })
    .then(function (response) {
        if (response.status === 409) {
            duplicateIdMessage.innerText = '중복된 ID입니다. 다른 ID를 사용해주세요.';
            duplicateIdMessage.style.color = 'red';
            signupButton.disabled = true;
        } else if (response.ok) {
            duplicateIdMessage.innerText = '사용가능한 ID입니다.';
            duplicateIdMessage.style.color = 'blue';
            updateButtonState();
        }
    })
    .catch(function (error) {
        console.error('중복 검사 중 오류 발생:', error);
    });
}

function checkDuplicateEmail() {
    const emailInput = document.getElementById('inputEmail');
    const email = emailInput.value.trim(); // 입력값의 앞뒤 공백 제거
    const duplicateEmailMessage = document.getElementById('duplicateEmailMessage');
    const signupButton = document.getElementById('button');

    // 사용자가 입력하지 않은 경우 처리
    if (email === "") {
        duplicateEmailMessage.innerText = '';
        return; // 중복 확인을 수행하지 않고 함수 종료
    }

    fetch('/study/user/api/emailCheck', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email
        }),
    })
    .then(function (response) {
        if (response.status === 409) {
            duplicateEmailMessage.innerText = '중복된 Email입니다. 다른 Email을 사용해주세요.';
            duplicateEmailMessage.style.color = 'red';
            signupButton.disabled = true;
        } else if (response.ok) {
            duplicateEmailMessage.innerText = '사용가능한 Email입니다.';
            duplicateEmailMessage.style.color = 'blue';
            updateButtonState();
        }
    })
    .catch(function (error) {
        console.error('중복 검사 중 오류 발생:', error);
    });
}
function updateButtonState() {
    const signupButton = document.getElementById('button');
    const duplicateIdMessage = document.getElementById('duplicateIdMessage');
    const duplicateEmailMessage = document.getElementById('duplicateEmailMessage');

    signupButton.disabled = !(duplicateIdMessage.style.color === 'blue' && duplicateEmailMessage.color === 'blue');
}
