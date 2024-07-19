
/**
 * fetch API를 사용하여 요청을 보내는 클래스
 * 기본 헤더를 설정할 수 있으며, fetch 요청을 보낼 때 헤더를 추가할 수 있다.
 * fetchWithHeaders 메서드는 URL과 옵션을 받아 fetch 요청을 보내며, 헤더를 추가할 수 있다.
 */

 // 헤더를 포함한 fetch 요청을 보내는 함수
const fetchWithHeaders = async (url, options = {}) => {
  const defaultHeaders = {};
  const aToken = sessionStorage.getItem('aToken');

  // jwt가 존재하면 Authorization 헤더에 추가
  if (aToken) {
    defaultHeaders['Authorization'] = `Bearer ${aToken}`;
  }
  
  
  // 추가하려는 헤더 설정
  const customHeader = {
  };

  // 기본 헤더, 옵션 헤더, 커스텀 헤더를 병합
  const headers = {
    ...defaultHeaders,
    ...options.headers,
    ...customHeader
  };

  // 지정된 URL, 옵션 및 헤더로 fetch 요청 수행
  const response = await fetch(url, { ...options, headers });

  // 응답 반환
  return response;
};

export default fetchWithHeaders;

