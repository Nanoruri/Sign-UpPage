import fetchWithHeaders  from "./headerAdd.js";

// 페이지 로드 시 쿠키 확인 및 서버 요청 함수
async function checkCookieAndRequest() {
    sendRequest();

//    const studyCookie = getCookie("JSESSIONID");
//    if (studyCookie) {
//        await sendRequest();
//    } else {
//        alert("필수 쿠키가 설정되어 있지 않습니다.");
//    }
}


// 서버로 요청을 보내는 함수
async function sendRequest() {
    
    try {
        const response = await fetchWithHeaders("/study/", {
            method: "GET",
            credentials: "include" // 쿠키를 포함하여 요청을 보냄
        });

        if (response.ok) {
            const responseText = await response.text();
        } else {
            console.error("서버 응답 오류:", response.status);
            window.location.href = "/study/error";
        }
    } catch (error) {
        console.error("요청 중 오류 발생:", error);
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// 페이지 로드 시 checkCookieAndRequest 함수 실행
document.addEventListener('DOMContentLoaded', checkCookieAndRequest);