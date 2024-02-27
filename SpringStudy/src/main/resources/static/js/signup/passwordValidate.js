function passwordValidate() { // todo : passwordValidation.js와 비슷한 기능이니 고민해서 둘중 하나로 변경하기
    var password = document.getElementById('inputPassword').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    var MatchingMessage = document.getElementById('matchingMessage');
    var signupButton = document.getElementById('button');

    if (password !== confirmPassword) {
        MatchingMessage.innerText = '비밀번호가 일치하지 않습니다.';
        MatchingMessage.style.color = 'red'
        signupButton.disabled = true;
    } else {
        MatchingMessage.innerText = '비밀번호가 일치합니다.'
        MatchingMessage.style.color = 'blue'
        signupButton.disabled = false;
    }
}

document.getElementById('confirmPassword').addEventListener('input', passwordValidate)