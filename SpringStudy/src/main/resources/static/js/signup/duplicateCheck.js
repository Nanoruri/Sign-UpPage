function checkDuplicate() {//todo : 일관성을 위해 함수형이 아닌 객체지향형으로 짜는 것도 고민해보기

    var userId = document.getElementById('inputAdress').value;
    var duplicateMessage = document.getElementById('duplicateMessage');
    var signupButton = document.getElementById('signupButton');

    // fetch를 사용하여 POST 요청 보내기()
    fetch('/study/idCheck', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'userId=' + encodeURIComponent(userId),
    })

    .then(function(response) {
        if (!response.ok) {
            duplicateMessage.innerText = '중복된 ID입니다. 다른 ID를 사용해주세요.';
            duplicateMessage.style.color = 'red'
            signupButton.disabled = true;
        } else{
            duplicateMessage.innerText = '사용가능한 ID입니다.';
            duplicateMessage.style.color = 'blue'
            signupButton.disabled = false;
        }
        return response.text();
    })
    .catch(function(error) {
        console.error('중복 검사 중 오류 발생:', error);
    });
}
document.getElementById('inputAdress').addEventListener('input', checkDuplicate);
document.getElementById('joinForm').addEventListener('submit', checkDuplicate);