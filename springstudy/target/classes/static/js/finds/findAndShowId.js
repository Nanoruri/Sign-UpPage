function doFindId() {// todo : 모달로 아이디 보여주기

    var name = document.getElementById('inputName').value;
    var phoneNum = document.getElementById('inputPhoneNum').value;


    fetch('/study/findId', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'name=' + encodeURIComponent(name) + '&phoneNum=' + encodeURIComponent(phoneNum),
    })
        .then(function (response) {
            if (response.status === 404) {
                displayModal('해당 정보로 가입한 가입자가 없습니다!');
                throw new Error('사용자가 없습니다.')
            } else {
                return response.text();
            }
        })
        .then(function (userId) {
            displayModal(userId);
        })
        .catch(function (error) {
            console.error('아이디를 찾는 중 오류 발생:', error);
        });

    return false;// 폼제출 X

}


function displayModal(message) {
    var IdInfoModal = new bootstrap.Modal(document.getElementById('showIdInfoModal'));
    var idModalMessage = document.getElementById('showUserIdMessage');
    idModalMessage.innerHTML = message;

    IdInfoModal.show();

    var closeModalButtons = document.querySelectorAll('[data-bs-dismiss="modal"]');
    closeModalButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            IdInfoModal.hide();
        });
    });

}