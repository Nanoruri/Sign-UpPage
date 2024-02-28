function doFindId() {// todo : 모달로 아이디 보여주기

    var name = document.getElementById('inputName').value;
    var email = document.getElementById('inputEmail').value;
    var modalMessage = document.getElementById('findIdModal')


    fetch('/study/findId', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'name=' + encodeURIComponent(name) + '&email=' + encodeURIComponent(email),
    })
        .then(function (response) {
            if (!response.status=== 404) {
                alert('사용자를 찾을 수 없습니다.');
            } else {
                return response.text();
            }
        })
        .then(function (userId) {
            alert(userId);
        })
        .catch(function (error) {
            console.error('아이디를 찾는 중 오류 발생:', error);
        });

        return false;// 폼제출 X

}
