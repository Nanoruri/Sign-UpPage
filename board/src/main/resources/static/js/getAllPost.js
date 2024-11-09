function getAllPost() {
    const mainPageButton = document.getElementById('mainButton');
    const boardTableBody = document.getElementById('boardTableBody');
    const pageSize = 5; // 페이지 당 표시할 게시글 수
    let currentPage = 0; // 현재 페이지 번호 초기화

    // 게시글 목록 불러오기
    function loadBoardList(page = 0) {
        fetch(`/study/board/api/generalBoard?page=${page}&size=${pageSize}`)
            .then(response => response.json())
            .then(data => {
                displayBoardList(data.content); // 게시글 목록 표시
                updatePagination(data); // 페이징 정보 업데이트
                currentPage = page; // 현재 페이지 업데이트
            })
            .catch(error => {
                console.error('Error loading board list:', error);
                alert('게시글 목록을 불러오는 중 오류가 발생했습니다.');
                window.location.href = '/study/';
            });
    }

    // 페이지 변경 시 호출되는 함수
    function changePage(page) {
        if (page < 0) return; // 페이지가 0보다 작아지지 않도록
        loadBoardList(page);
    }

    // 게시글 목록을 화면에 표시하는 함수
    function displayBoardList(posts) {
        boardTableBody.innerHTML = ''; // 기존 내용을 지움

        posts.forEach((post, index) => {
            const row = document.createElement('tr');
            row.setAttribute('data-id', post.id); // 게시글 ID 저장
            row.innerHTML = `
                <td>${index + 1 + (currentPage * pageSize)}</td>
                <td>${post.title}</td>
                <td>${new Date(post.date).toLocaleDateString()}</td>
            `;

            // 게시글 행에 클릭 이벤트 추가
            row.addEventListener('click', function () {
                const postId = this.getAttribute('data-id');
                window.location.href = `/study/board/page/detail?postId=${postId}`;
            });

            boardTableBody.appendChild(row);
        });
    }


    // '이전' 버튼 클릭 시 페이지 변경
    document.getElementById('prev-btn').addEventListener('click', function () {
        changePage(currentPage - 1);
    });

    // '다음' 버튼 클릭 시 페이지 변경
    document.getElementById('next-btn').addEventListener('click', function () {
        changePage(currentPage + 1);
    });

    // 초기 페이지 로딩
    loadBoardList(currentPage);

    // 메인 페이지로 이동
    mainPageButton.addEventListener('click', function () {
        window.location.href = '/study/';
    });
}

// 페이지 로드 시 getAllPost 호출
document.addEventListener('DOMContentLoaded', getAllPost);


// 페이지 정보를 업데이트하는 함수
function updatePagination(data) {
    const pageInfo = document.getElementById('page-info');
    // totalPages가 0일 때 최소 1페이지로 표시
    const totalPages = data.totalPages > 0 ? data.totalPages : 1;
    pageInfo.innerText = `Page ${data.number + 1} of ${totalPages}`;

    // '이전' 버튼은 첫 페이지에서 비활성화
    document.getElementById('prev-btn').disabled = data.first;
    // '다음' 버튼은 마지막 페이지에서 비활성화
    document.getElementById('next-btn').disabled = data.last;
}