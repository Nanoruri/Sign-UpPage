function getAllPost() {
    const mainPageButton = document.getElementById('mainButton');

    // 게시글 목록 불러오기
    function loadBoardList() {
        fetch('/study/board/api/generalBoard')  // 게시글 목록을 불러오는 API 엔드포인트
            .then(response => response.json())
            .then(data => {
                const boardTableBody = document.getElementById('boardTableBody'); // 게시글 목록 테이블 바디
                boardTableBody.innerHTML = '';
                data.forEach((post, index) => {
                    const row = document.createElement('tr');
                    row.setAttribute('data-id', post.id); // 게시글 ID 저장
                    row.innerHTML = `
                        <td>${index + 1}</td>
                        <td>${post.title}</td> <!-- 제목 클릭 없이도 전체 영역 클릭 가능 -->
                        <td>${new Date(post.date).toLocaleDateString()}</td>
                    `;

                    // 게시글 행 전체에 클릭 이벤트 추가
                    row.addEventListener('click', function () {
                        const postId = this.getAttribute('data-id');
                        window.location.href = `/study/board/page/detail?postId=${postId}`;
                    });

                    boardTableBody.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Error loading board list:', error);
                alert('게시글 목록을 불러오는 중 오류가 발생했습니다.');
                window.location.href = '/study/';
            });
    }



    // 페이지 로드 시 게시글 목록 로드
    loadBoardList();


    // 메인 페이지로 이동
    mainPageButton.addEventListener('click', function () {
        window.location.href = '/study/';
    });
}


// 페이지 로드 시 getAllPost 호출
document.addEventListener('DOMContentLoaded', getAllPost);
{
}
