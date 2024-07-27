function passwordValidate() {
    // 비밀번호와 비밀번호 확인 값 검사
    const passwordInput = document.getElementById('inputPassword');
    const confirmPasswordInput = document.getElementById('inputPasswordConfirm') || document.getElementById('confirmPassword');
    const matchingMessage = document.getElementById('matchingMessage') || document.getElementById('passwordMissMatchMessage');

    const password = passwordInput ? passwordInput.value : '';
    const confirmPassword = confirmPasswordInput ? confirmPasswordInput.value : '';

    if (password === '' || confirmPassword === '') {
        if (matchingMessage) {
            matchingMessage.innerText = '';
        }
        return false;
    }

    if (password !== confirmPassword) {
        if (matchingMessage) {
            matchingMessage.innerText = '비밀번호가 일치하지 않습니다.';
            matchingMessage.style.color = 'red';
        }
        return false;
    } else {
        if (matchingMessage) {
            matchingMessage.innerText = '비밀번호가 일치합니다.';
            matchingMessage.style.color = 'blue';
        }
        return true;
    }
}

function setupFormValidation() {
    // 요소 초기화
    const passwordInput = document.getElementById('inputPassword');
    const confirmPasswordInput = document.getElementById('inputPasswordConfirm') || document.getElementById('confirmPassword');
    const missMatchModal = document.getElementById('missMatchModal');

    // 모달 띄우기
    function displayModal(message) {
        if (!missMatchModal) return;

        const modal = new bootstrap.Modal(missMatchModal);
        const modalMessage = document.getElementById('passwordMissMatchMessage');
        if (modalMessage) {
            modalMessage.innerHTML = message;
            modalMessage.style.color = 'red';
        }
        modal.show();

        const closeModalButtons = document.querySelectorAll('[data-bs-dismiss="modal"]');
        closeModalButtons.forEach(button => {
            button.addEventListener('click', () => modal.hide());
        });
    }

    // 비밀번호 입력 이벤트 리스너 등록
    if (passwordInput && confirmPasswordInput) {
        passwordInput.addEventListener('input', passwordValidate);
        confirmPasswordInput.addEventListener('input', passwordValidate);
    }

    // 폼 제출 이벤트 리스너 등록
    function handleFormSubmit(event) {
        if (!passwordValidate()) {
            event.preventDefault();
            if (missMatchModal) {
                displayModal("비밀번호가 일치하지 않습니다.");
            }
        }
    }

    const passwordChangeForm = document.getElementById('passwordChangeForm');
    const joinForm = document.querySelector('.joinForm');

    if (passwordChangeForm) {
        passwordChangeForm.addEventListener('submit', handleFormSubmit);
    }

    if (joinForm) {
        joinForm.addEventListener('submit', handleFormSubmit);
    }
}

document.addEventListener('DOMContentLoaded', setupFormValidation);
