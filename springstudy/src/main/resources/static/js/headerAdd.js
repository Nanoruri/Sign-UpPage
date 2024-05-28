
export default class CustomFetch {//todo : 리팩토링 필요
  constructor(defaultHeaders = {}) {
    this.defaultHeaders = defaultHeaders;
  }

  async fetchWithHeaders(url, options = {}) {
    // 기존 옵션에서 헤더를 가져오거나 빈 객체를 생성
    options.headers = options.headers || {};

    // 기본 헤더를 기존 헤더에 병합
    options.headers = { ...this.defaultHeaders, ...options.headers };

    //헤더 추가
    options.headers['Study'] = 'signupProject';


    // fetch 함수 호출
    const response = await fetch(url, options);

    // 응답 반환
    return response;
  }
}

