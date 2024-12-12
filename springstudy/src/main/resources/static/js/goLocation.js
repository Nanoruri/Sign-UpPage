 function buttonsSetUp(buttons) {
        buttons.signUpButton.addEventListener('click', function () {
            window.location.href = '/study/signup';
        });

        buttons.findPasswordButton.addEventListener('click', function () {
            window.location.href = '/study/findPassword';
        });

        buttons.findIdButton.addEventListener('click', function () {
            window.location.href = '/study/findId';
        });

    }

//클릭 이벤트 생성
document.addEventListener('DOMContentLoaded', function () {
    const buttons = {
        signUpButton: document.getElementById('signupButton'),
        findPasswordButton: document.getElementById('findPasswordButton'),
        findIdButton: document.getElementById('findIdButton'),
    };
    buttonsSetUp(buttons);
});