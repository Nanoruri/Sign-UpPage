// 공통적으로 사용되는 유효성 검사 함수 정의
function commonValidate() {
    console.debug('commonValidate함수 실행')
    const userIdInput = document.getElementById('inputAdress');
    const nameInput = document.getElementById('inputName');
    const passwordInput = document.getElementById('inputPassword');
    const birthInput = document.getElementById('inputBirth');
    const emailInput = document.getElementById('inputEmail');
    const phoneNumInput = document.getElementById('inputPhoneNum');

    // 요소가 존재하지 않는 경우에는 기본값으로 null을 할당
    const userId = userIdInput ? userIdInput.value.trim() : null;
    const name = nameInput ? nameInput.value.trim() : null;
    const password = passwordInput ? passwordInput.value.trim() : null;
    const birth = birthInput ? birthInput.value.trim() : null;
    const email = emailInput ? emailInput.value.trim() : null;
    const phoneNum = phoneNumInput ? phoneNumInput.value.trim() : null;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;//이메일 유효성
    const phoneNumRegex = /^\d{3}-\d{4}-\d{4}$/

    if (userId === '') {
        console.debug('아이디 유효성 검증 실행');
        alert('아이디를 입력해주세요');
        return false;
    } else if (name === '') {
        console.debug('이름 유효성 검증 실행');
        alert('이름을 입력해주세요');
        return false;
    } else if (password === '') {
        console.debug('비밀번호 유효성 검증 실행')
        alert('비밀번호를 입력해주세요');
        return false;
    } else if (birth === '') {
        console.debug('생년월일 유효성 검증 실행')
        alert('생년월일을 입력해주세요');
        return false;
    } else if (birth !== null && !validateBirthday(birth)) {
        console.debug('생년월일 유효성 검증 실행 됨')
        alert('올바른 날짜 형식이 아닙니다.다시 입력하여 주십시오')
        return false;
    } else if (email === '') {
        console.debug('이메일 실행 됨')
        alert('이메일을 입력해주세요');
        return false;
    } else if (email !== null && !emailRegex.test(email)) {
        console.debug('이메일 유효성 검증 실행 됨.')
        alert('이메일이 형식에 맞지 않습니다. @를 추가하여 형식에 맞게 다시 입력해주세요')
        return false;
    } else if (phoneNum === '') {
        console.debug('전화번호 실행 됨.')
        alert('전화번호를 입력해주세요');
        return false;
    } else if (phoneNum !== null && !phoneNumRegex.test(phoneNum)) {
        console.debug('전화번호 검증 실행 됨')
        alert('전화번호는 11자리 숫자로 입력해주세요')
        return false;
    }
    console.debug('commonValidate함수 종료')
    return true;
}


//생년월일 유효성 검사
function validateBirthday(birthday) {
    // 정규식을 사용하여 '년-월-일' 형식 체크
    const regex = /^\d{4}-\d{1,2}-\d{1,2}$/;

    console.debug('validateBirthday 실행')

    if (!regex.test(birthday)) {
        console.debug('생년월일 유효성 검사 실패')
        return false;
    }

    // 입력된 생년월일을 '-'로 분리
    const parts = birthday.split('-');

    // 년, 월, 일 추출
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10);
    const day = parseInt(parts[2], 10);

    console.log('Year:', year);
    console.log('Month:', month);
    console.log('Day:', day);


    // 유효한 날짜인지 검사
    if (isNaN(year) || isNaN(month) || isNaN(day) ||
        month < 1 || month > 12 || day < 1 || day > 31) {
        console.log('날짜 유효성 검사 실패:', birthday);
        return false;
    }

    //    // 입력된 년도가 4자리 숫자가 아니면 오류
    //    if (year.toString().length !== 4) {
    //        alert('올바른 년도를 입력해주세요.');
    //        return false;
    //    }
    return true;
}

//todo : 생년월일, 전화번호 숫자를 수정하고 난 뒤에 입력커서가 문자의 끝으로 가는 문제 처리하기
// 자동으로 년-월-일 형식 맞춰주기
function formatDatePickerInput(inputBirthday) {
    if (inputBirthday) {
        inputBirthday.addEventListener('input', function (event) {
            let value = inputBirthday.value.replace(/[^\d]/g, ''); // 숫자 이외의 문자 제거
            const cursorPosition = inputBirthday.selectionStart;

            if (event.inputType === 'deleteContentBackward' && cursorPosition > 0 && value.charAt(cursorPosition - 1) === '-') {
                // 백스페이스로 '-' 제거 처리
                inputBirthday.value = value.slice(0, cursorPosition - 1) + value.slice(cursorPosition);
                inputBirthday.setSelectionRange(cursorPosition - 1, cursorPosition - 1);
                return;
            }

            // 변수를 사용하여 년, 월, 일 추출
            const year = value.slice(0, 4);
            const month = value.slice(4, 6);
            const day = value.slice(6, 8); // 일자의 '-'를 포함하여 추출

            // 날짜 형식 처리
            // 수정된 부분: 월이 있는 경우에만 '-'를 추가
            if (month && month.length >= 2) {
                value = year + '-' + month + (day ? '-' + day : '');
            } else {
                value = year + (month ? '-' + month : '') + (day ? '-' + day : '');
            }

            // 입력값 업데이트
            inputBirthday.value = value;

            if (event.inputType === 'deleteContentBackward' && cursorPosition > 0) {
                inputBirthday.setSelectionRange(cursorPosition, cursorPosition);
            }
        });
    }
}



// 전화번호 형식 맞추기
function formatPhoneNumberInput(inputPhoneNumber) {
    if (inputPhoneNumber) {
        inputPhoneNumber.addEventListener('input', function (event) {
            let value = inputPhoneNumber.value.replace(/[^\d]/g, ''); // 숫자 이외의 문자 제거
            const cursorPosition = inputPhoneNumber.selectionStart;

            if (event.inputType === 'deleteContentBackward') {
                // 백스페이스로 삭제 시
                if (cursorPosition > 0 && value.charAt(cursorPosition - 1) === '-') {
                    // '-'를 삭제하는 경우 처리
                    inputPhoneNumber.value = value.slice(0, cursorPosition - 2) + value.slice(cursorPosition - 1);
                    inputPhoneNumber.setSelectionRange(cursorPosition - 2, cursorPosition - 2);
                    return;
                }
            }

            // 지역번호, 중간번호, 끝번호 추출
            const areaCode = value.slice(0, 3);
            const middleNumber = value.slice(3, 7);
            const lastNumber = value.slice(7, 11);

            // 전화번호 형식 처리
            if (middleNumber && middleNumber.length >= 4) {
                value = areaCode + '-' + middleNumber.slice(0, 4) + (lastNumber ? '-' + lastNumber : '');
            } else {
                value = areaCode + (middleNumber ? '-' + middleNumber : '') + (lastNumber ? '-' + lastNumber : '');
            }

            // 입력값 업데이트
            inputPhoneNumber.value = value;

            // 수정 후 커서 위치 조정
            if (event.inputType === 'deleteContentBackward' && cursorPosition > 0) {
                inputPhoneNumber.setSelectionRange(cursorPosition, cursorPosition);
            }


            // //입력된 값이 3자리 숫자인경우 '-' 추가  
            // if (/^\d{3}$/.test(value)) {
            //     inputPhoneNum.value = value + '-';
            // } else if (/^\d{3}-\d{4}$/.test(value)) {
            //     inputPhoneNum.value = value + '-';
            // }

            // if (/^\d{3}-\d{4}-\d{4}$/.test(value)) {
            //     console.log('전화번호 형식이 올바름:', value);
            // } else {
            //     console.log('전화번호 형식이 올바르지 않음:', value);
            // }
        });// todo : 전화번호 형식 010 자동입력 고려해보기
    }
}


// 공통적으로 사용되는 알림 및 버튼 비활성화 함수 정의
// function alert(message) {
//     alert(message);
//     // button.disabled = true;
// }


// 초기화 함수
function init() {
    // 문서가 로드될 때 실행되는 초기화 코드
    const form = document.getElementById('joinForm'); // 폼 요소 가져오기

    if (form) {
        // 폼이 존재할 경우에만 이벤트 리스너 추가
        form.addEventListener('submit', function(event) {
            event.preventDefault(); // 폼 제출의 기본 동작 막기
        });
    }

    const birthInput = document.getElementById('inputBirth');
    const phoneNumInput = document.getElementById('inputPhoneNum');

    formatDatePickerInput(birthInput);
    formatPhoneNumberInput(phoneNumInput);
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