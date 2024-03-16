document.addEventListener('DOMContentLoaded', function() {
    // DOM이 로드되면 실행될 코드
    document.getElementById('confirmPassword').addEventListener('input', passwordValidate);

    // 폼 제출 이벤트에 대한 리스너 추가
    document.querySelector('.joinForm').addEventListener('submit', function(event) {// 이 부분 userValidate의 init함수의 joinForm 불러오는 부분과 같으니까 묶을 방법 생각하기
        // 비밀번호가 일치하지 않으면 폼 제출을 막음
        if (!passwordValidate()) {
            event.preventDefault();
        }
    });
});



function passwordValidate() { // todo : passwordValidation.js와 비슷한 기능이니 고민해서 둘중 하나로 변경하기
    var password = document.getElementById('inputPassword').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    var MatchingMessage = document.getElementById('matchingMessage');
    // var signupButton = document.getElementById('button');

    if (password !== confirmPassword) {
        MatchingMessage.innerText = '비밀번호가 일치하지 않습니다.';
        MatchingMessage.style.color = 'red'
        return false;
    } else {
        MatchingMessage.innerText = '비밀번호가 일치합니다.'
        MatchingMessage.style.color = 'blue'
        return true;
    }
}

