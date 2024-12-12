document.addEventListener('DOMContentLoaded', function () {
    const searchButton = document.getElementById('searchButton');
    const searchInput = document.getElementById('searchInput');
    const searchType = document.getElementById('searchType');
    const boardTableBody = document.getElementById('boardTableBody');
    const memberTableBody = document.getElementById('memberTableBody');
    const pageSize = 5; // 페이지 당 표시할 게시글 수
    let currentPage = 0; // 현재 페이지 번호 초기화

    // 검색 버튼 클릭 시 검색 실행
    searchButton.addEventListener('click', function () {
        const query = searchInput.value;
        const type = searchType.value;

        // 현재 활성화된 탭 확인
        const activeTab = document.querySelector('.tab-button.active').getAttribute('data-tab');

        // 활성화된 탭에 맞게 검색 실행
        if (activeTab === 'general') {
            searchPosts(query, type, 'general'); // 일반 탭에서 검색
        } else if (activeTab === 'member') {
            searchPosts(query, type, 'member'); // 자유 탭에서 검색
        }
    });

    // 검색 API 호출
    function searchPosts(query, type, tab) {
        const url = `/study/board/api/search?query=${encodeURIComponent(query)}&type=${type}&tabName=${tab}&page=${currentPage}&size=${pageSize}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                // 검색 결과를 해당 탭에 맞게 표시
                if (tab === 'general') {
                    renderPosts(data.content, boardTableBody);  // 일반탭에 게시글 렌더링
                    updatePagination(data); // 페이징 정보 업데이트
                } else if (tab === 'member') {
                    renderPosts(data.content, memberTableBody);  // 자유탭에 게시글 렌더링
                    updatePagination(data); // 페이징 정보 업데이트
                }
            })
            .catch(error => console.error('Error searching posts:', error));
    }

    // 게시글 목록 렌더링 공통 함수
    function renderPosts(posts, tableBody) {
        tableBody.innerHTML = '';  // 기존 테이블 비우기
        posts.forEach((post, index) => {
            const row = document.createElement('tr');
            row.setAttribute('data-id', post.id); // 게시글 ID 저장
            row.innerHTML = `
                <td>${index + 1 + (currentPage * pageSize)}</td>
                <td>${post.title}</td>
                <td>${new Date(post.date).toLocaleDateString()}</td>
            `;

            // 게시글 클릭 시 상세보기
            row.addEventListener('click', function () {
                const postId = this.getAttribute('data-id');
                const tab = document.querySelector('.tab-button.active').getAttribute('data-tab');
                window.location.href = `/study/board/page/postDetail?postId=${postId}&tab=${tab}`;
            });

            tableBody.appendChild(row);
        });
    }

    // 페이지 변경 시 호출되는 함수
    function changePage(page) {
        if (page < 0) return; // 페이지가 0보다 작아지지 않도록
        currentPage = page;  // 현재 페이지 업데이트
        searchPosts(searchInput.value, searchType.value, 'general');  // 다시 검색하여 표시
    }

    // '이전' 버튼 클릭 시 페이지 변경
    document.getElementById('prev-btn').addEventListener('click', function () {
        changePage(currentPage - 1);
    });

    // '다음' 버튼 클릭 시 페이지 변경
    document.getElementById('next-btn').addEventListener('click', function () {
        changePage(currentPage + 1);
    });

});
