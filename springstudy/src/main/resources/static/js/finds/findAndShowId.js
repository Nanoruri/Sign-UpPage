function doFindId() {

    const name = document.getElementById('inputName').value;
    const phoneNum = document.getElementById('inputPhoneNum').value;


    fetch('/study/user/api/id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: name,
            phoneNum: phoneNum

        }),
    })
        .then(function (response) {
            if (response.status === 404) {
                displayModal('해당 정보로 가입한 가입자가 없습니다!');
                throw new Error('사용자가 없습니다.')
            } else {
                return response.json();
            }
        })
        .then(function (data) {
            displayModal("아이디는" + data.userId +"입니다.");
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