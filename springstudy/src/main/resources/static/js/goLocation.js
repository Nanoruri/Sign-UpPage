 function buttonsSetUp(buttons) {
        buttons.signUpButton.addEventListener('click', function () {
            window.location.href = '/study/user/page/signup';
        });

        buttons.findPasswordButton.addEventListener('click', function () {
            window.location.href = '/study/user/page/findPassword';
        });

        buttons.findIdButton.addEventListener('click', function () {
            window.location.href = '/study/user/page/findId';
        });

        buttons.boardButton.addEventListener('click', function () {
            window.location.href = '/study/board/page/boardIndex';
        });
    }

//클릭 이벤트 생성
document.addEventListener('DOMContentLoaded', function () {
    const buttons = {
        signUpButton: document.getElementById('signupButton'),
        findPasswordButton: document.getElementById('findPasswordButton'),
        findIdButton: document.getElementById('findIdButton'),
        boardButton: document.getElementById('boardButton')
    };
    buttonsSetUp(buttons);
});