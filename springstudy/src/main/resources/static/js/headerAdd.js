
/**
 * fetch API를 사용하여 요청을 보내는 클래스
 * 기본 헤더를 설정할 수 있으며, fetch 요청을 보낼 때 헤더를 추가할 수 있다.
 * fetchWithHeaders 메서드는 URL과 옵션을 받아 fetch 요청을 보내며, 헤더를 추가할 수 있다.
 */

export default class CustomFetch {// todo : JS일관성을 위해 함수형으로 변경하기
  constructor(defaultHeaders = {}) {
    this.defaultHeaders = defaultHeaders;
  }

  // 헤더를 포함한 fetch 요청을 보내는 함수
  async fetchWithHeaders(url, options = {}) {
    
    // 추가하려는 헤더 설정
    const customHeader = {
      'Study': 'signupProject'
    };

    // 기본 헤더, 옵션 헤더, 커스텀 헤더를 병합
    const headers = {
      ...this.defaultHeaders,
      ...options.headers,
      ...customHeader
    };


    // 지정된 URL, 옵션 및 헤더로 fetch 요청 수행
    const response = await fetch(url, { ...options, headers });

    // 응답 반환
    return response;
  }
}

