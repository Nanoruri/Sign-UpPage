// validationPassword.js
function checkPasswordMatch() {


    // 비밀번호와 비밀번호 확인 값 가져오기
    var newPassword = document.getElementById('inputPassword').value;
    var validatePassword = document.getElementById('inputPasswordConfirm').value;
    // 비밀번호가 일치하지 않으면 모달 띄우기
    
    
    if (newPassword !== validatePassword || newPassword === '') {
        displayModal(newPassword === '' ? "공백은 비밀번호로 할 수 없습니다." : "비밀번호가 일치하지 않습니다.");
        return false;
    } 
    // 비밀번호가 일치하면 폼 제출 
    return true;        
}

// 모달 띄우기
function displayModal(message) {
    var missMatchModal = new bootstrap.Modal(document.getElementById('missMatchModal'));

    // 모달 내용 설정
    var modalMessage = document.getElementById('passwordMisttMatchMessage');
    modalMessage.innerHTML = message;

    // 모달 열기
    missMatchModal.show();

    // 모달 닫기 버튼에 이벤트 리스너 추가
    var closeModalButtons = document.querySelectorAll('[data-bs-dismiss="modal"]');
    closeModalButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            missMatchModal.hide();
        });
    });
}

//// validationPassword.js
//
//function checkPasswordMatch(event) {
//      if (event) {
//            event.preventDefault(); // 이벤트 객체가 존재하면 기본 동작을 막습니다.
//        }
//
//    // 비밀번호와 비밀번호 확인 값 가져오기
//    var newPassword = document.getElementById('inputPassword').value;
//    var validatePassword = document.getElementById('validPassword').value;
//
//    // 비밀번호가 일치하지 않으면 모달 띄우기
//    if (newPassword !== validatePassword) {
//        displayModal("비밀번호가 일치하지 않습니다.");
//    } else {
//        // 비밀번호가 일치하면 폼 제출
//        document.getElementById('passwordChangeForm').submit();
//    }
//}
//
//// 모달 띄우기
//function displayModal(message) {
//    var modal = document.getElementById('missMatchModal');
//    var modalContent = document.querySelector('.modal-content');
//    var modalMessage = document.getElementById('passwordMissMatchMessage');
//    modalMessage.innerHTML = message;
//    modal.style.display = 'block';
//
//    // 모달 닫기 버튼에 이벤트 리스너 추가
//    var closeModalButton = document.createElement('span');
//    closeModalButton.innerHTML = '&times;';
//    closeModalButton.className = 'close';
//    closeModalButton.onclick = function() {
//        modal.style.display = 'none';
//    };
//
//    // 모달 내부에 닫기 버튼 추가
//    modalContent.innerHTML = ''; // 기존 내용 초기화
//    modalContent.appendChild(closeModalButton);
//    modalContent.appendChild(modalMessage);
//}


