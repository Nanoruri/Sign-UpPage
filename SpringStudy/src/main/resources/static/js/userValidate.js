// 공통적으로 사용되는 유효성 검사 함수 정의
function commonValidate() {// todo : signupPage, findIdPage 완료
    var userIdInput = document.getElementById('inputAdress');
    var nameInput = document.getElementById('inputName');
    var passwordInput = document.getElementById('inputPassword');
    var birthInput = document.getElementById('inputBirth');
    var emailInput = document.getElementById('inputEmail');
    var phoneNumInput = document.getElementById('inputPhoneNum');
    var button = document.getElementById('button');

    // 요소가 존재하지 않는 경우에는 기본값으로 null을 할당
    var userId = userIdInput ? userIdInput.value.trim() : null;
    var name = nameInput ? nameInput.value.trim() : null;
    var password = passwordInput ? passwordInput.value.trim() : null;
    var birth = birthInput ? birthInput.value.trim() : null;
    var email = emailInput ? emailInput.value.trim() : null;
    var phoneNum = phoneNumInput ? phoneNumInput.value.trim() : null;
    var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;//이메일 유효성
    var phoneNumRegex = /^\d{3}-\d{4}-\d{4}$/

    if (userId === null || userId === '') {
        showAlertAndDisableButton('아이디를 입력해주세요', button);
    } else if (name === null || name === '') {
        showAlertAndDisableButton('이름을 입력해주세요', button);
    } else if (password === null || password === '') {
        showAlertAndDisableButton('비밀번호를 입력해주세요', button);
    } else if (birth === null || birth === '') {
        showAlertAndDisableButton('생년월일을 입력해주세요', button);
    } else if (!validateBirthday(birth)) {
        showAlertAndDisableButton('올바른 날짜 형식이 아닙니다.다시 입력하여 주십시오', button)
    } else if (email === null || email === '') {
        showAlertAndDisableButton('이메일을 입력해주세요', button);
    } else if (!emailRegex.test(email)) {
        showAlertAndDisableButton('이메일이 형식에 맞지 않습니다. @를 추가하여 형식에 맞게 다시 입력해주세요', button)
    } else if (phoneNum === null || phoneNum === '') {
        showAlertAndDisableButton('전화번호를 입력해주세요', button);
    } else if (!phoneNumRegex.test(phoneNum)) {
        showAlertAndDisableButton('전화번호는 11자리 숫자로 입력해주세요', button)
    } else {
        button.disabled = false;

    }
}


//생년월일 유효성 검사
function validateBirthday(birthday) {
    // 정규식을 사용하여 '년-월-일' 형식 체크
    var regex = /^\d{4}-\d{1,2}-\d{1,2}$/;


    if (!regex.test(birthday)) {
        return false;
    }

    // 입력된 생년월일을 '-'로 분리
    var parts = birthday.split('-');

    // 년, 월, 일 추출
    var year = parseInt(parts[0], 10);
    var month = parseInt(parts[1], 10);
    var day = parseInt(parts[2], 10);

    console.log('Year:', year);
    console.log('Month:', month);
    console.log('Day:', day);


    // 유효한 날짜인지 검사
    if (isNaN(year) || isNaN(month) || isNaN(day) ||
        month < 1 || month > 12 || day < 1 || day > 31) {
        return false;
    }

    //    // 입력된 년도가 4자리 숫자가 아니면 오류
    //    if (year.toString().length !== 4) {
    //        alert('올바른 년도를 입력해주세요.');
    //        return false;
    //    }
    console.log(validateBirthday(birthday));
    return true;
}

// 자동으로 년-월-일 형식 맞춰주기
function formatDatePickerInput(inputBirthday) {
    if (inputBirthday) {
        inputBirthday.addEventListener('input', function (event) {
            var value = inputBirthday.value.replace(/[^\d]/g, ''); // 숫자 이외의 문자 제거
            var cursorPosition = inputBirthday.selectionStart;

            if (event.inputType === 'deleteContentBackward' && cursorPosition > 0 && value.charAt(cursorPosition - 1) === '-') {
                // 백스페이스로 '-' 제거 처리
                inputBirthday.value = value.slice(0, cursorPosition - 1) + value.slice(cursorPosition);
                inputBirthday.setSelectionRange(cursorPosition - 1, cursorPosition - 1);
                return;
            }

            // 변수를 사용하여 년, 월, 일 추출
            var year = value.slice(0, 4);
            var month = value.slice(4, 6);
            var day = value.slice(6, 8); // 일자의 '-'를 포함하여 추출

            // 날짜 형식 처리
            // 수정된 부분: 월이 있는 경우에만 '-'를 추가
            if (month && month.length >= 2) {
                value = year + '-' + month + (day ? '-' + day : '');
            } else {
                value = year + (month ? '-' + month : '') + (day ? '-' + day : '');
            }

            // 입력값 업데이트
            inputBirthday.value = value;
        });
    }
}



// 전화번호 형식 맞추기 //todo (2/15) : 여기부터. 생년월일 받는 것과 비슷하게 처리하기
function formatPhoneNumberInput(inputElement) {
    if (inputElement) {
        inputElement.addEventListener('input', function () {
            var value = inputElement.value.trim();

            // 입력된 값이 텍스트인경우 삭제
            if (!/^\d+$/.test(value) && !/^\d{3}-\d{4}-?$/.test(value)) {
                inputElement.value = value.replace(/[^\d-]/g, '');
            }

            //입력된 값이 3자리 숫자인경우 '-' 추가  
            if (/^\d{3}$/.test(value)) {
                inputElement.value = value + '-';
            } else if (/^\d{3}-\d{4}$/.test(value)) {
                inputElement.value = value + '-';
            }

            if (/^\d{3}-\d{4}-\d{4}$/.test(value)) {
                console.log('전화번호 형식이 올바름:', value);
            } else {
                console.log('전화번호 형식이 올바르지 않음:', value);
            }
        });// todo : 전화번호 형식 010 자동입력 고려해보기
    }
}


// 공통적으로 사용되는 알림 및 버튼 비활성화 함수 정의
function showAlertAndDisableButton(message, button) {
    alert(message);
    button.disabled = false;
}


// 초기화 함수
function init() {
    // 문서가 로드될 때 실행되는 초기화 코드
    var button = document.getElementById('button');
    var birthInput = document.getElementById('inputBirth')
    var phoneNumInput = document.getElementById('inputPhoneNum')

    if (button) {
        button.addEventListener('click', commonValidate);
    }

    formatDatePickerInput(birthInput)
    formatPhoneNumberInput(phoneNumInput)
}

// 초기화 함수 호출
window.onload = init;








//document.addEventListener('DOMContentLoaded', function() {
//    var form = document.querySelector('.form');
//
//    form.addEventListener('submit', function(event) {
//        // 입력 필드 가져오기
//        var userId = document.getElementById('inputAdress').value.trim();
//        var name = document.getElementById('inputName').value.trim();
//        var password = document.getElementById('inputPassword').value.trim();
//        var confirmPassword = document.getElementById('confirmPassword').value.trim();
//        var birth = document.getElementById('inputBirth').value.trim();
//        var email = document.getElementById('inputEmail').value.trim();
//        var phoneNum = document.getElementById('inputPhoneNum').value.trim();
//
//        // 필드 중 하나라도 비어 있는지 확인
//        if (userId === '') {
//            alert('아이디를 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (name === '') {
//            alert('이름을 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (password === '') {
//            alert('비밀번호를 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (confirmPassword === '') {
//            alert('비밀번호를 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (birth === '') {
//            alert('생년월일을 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (email === '') {
//            alert('이메일을 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        } else if (phoneNum === '') {
//            alert('전화번호를 입력해주세요.');
//            event.preventDefault(); // 폼 제출 방지
//        }
//    });
//});