function fetchWithHeaders(url, options = {}) {
  // 기본 헤더 설정
  const defaultHeaders = {
    'Study': 'signupProject',
    'Authorization': 'Bearer your_token_here'
  };

  // 사용자 정의 헤더와 기본 헤더를 병합
  const headers = new Headers(options.headers || {});
  Object.entries(defaultHeaders).forEach(([key, value]) => {
    headers.append(key, value);
  });

  // 수정된 옵션으로 fetch 호출
  return fetch(url, {...options, headers});
}

// 사용 예
fetchWithHeaders('/study/')
  .then(response => response.json())
  .then(data => console.log(data));