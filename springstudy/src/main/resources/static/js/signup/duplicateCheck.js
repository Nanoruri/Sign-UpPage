document.addEventListener('DOMContentLoaded', function () {
    var userIdInput = document.getElementById('inputAdress');
    var emailInput = document.getElementById('inputEmail');
 
    userIdInput.addEventListener('input', checkDuplicate);
    emailInput.addEventListener('input', checkDuplicateEmail);
});

function checkDuplicate() {
    var userIdInput = document.getElementById('inputAdress');
    var userId = userIdInput.value.trim(); // 입력값의 앞뒤 공백 제거
    var duplicateIdMessage = document.getElementById('duplicateIdMessage');
    var signupButton = document.getElementById('button');

    // 사용자가 입력하지 않은 경우 처리
    if (userId === "") {
        duplicateIdMessage.innerText = '';
        return; // 중복 확인을 수행하지 않고 함수 종료
    }

    fetch('/study/idCheck', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Study' : 'signupProject'
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
    var emailInput = document.getElementById('inputEmail');
    var email = emailInput.value.trim(); // 입력값의 앞뒤 공백 제거
    var duplicateEmailMessage = document.getElementById('duplicateEmailMessage');
    var signupButton = document.getElementById('button');

    // 사용자가 입력하지 않은 경우 처리
    if (email === "") {
        duplicateEmailMessage.innerText = '';
        return; // 중복 확인을 수행하지 않고 함수 종료
    }

    fetch('/study/emailCheck', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Study' : 'signupProject'
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
    var signupButton = document.getElementById('button');
    var duplicateIdMessage = document.getElementById('duplicateIdMessage');
    var duplicateEmailMessage = document.getElementById('duplicateEmailMessage');

    if (!duplicateIdMessage.style.color === 'blue' && !duplicateEmailMessage.color === 'blue') {
        signupButton.disabled = true;
    } else {
        signupButton.disabled = false;
    }
}
