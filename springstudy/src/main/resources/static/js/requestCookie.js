import CustomFetch  from "./headerAdd.js";

// 페이지 로드 시 쿠키 확인 및 서버 요청 함수
async function checkCookieAndRequest() {
    const studyCookie = getCookie("Study");
    if (studyCookie === "signupProject") {
        await sendRequest();
    } else {
        alert("필수 쿠키가 설정되어 있지 않습니다.");
    }
}

const customFetch = new CustomFetch();
// 서버로 요청을 보내는 함수
async function sendRequest() {
    
    try {
        const response = await customFetch.fetchWithHeaders("http://localhost:8082/study/", {
            method: "GET",
            credentials: "include" // 쿠키를 포함하여 요청을 보냄
        });

        if (response.ok) {
            const responseText = await response.text();
        } else {
            alert("에러: " + response.status);// todo : alert말고  에러페이지 반환하게 변경
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
window.onload = checkCookieAndRequest;