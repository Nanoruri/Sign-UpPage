//boardIndex.html의 로그인 버튼을 누르면 goSignin.js의 코드가 실행된다.

document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('loginButton');

    loginButton.addEventListener('click', function () {
        window.location.href = '/study/login';
    });
});