document.addEventListener('DOMContentLoaded', function () {
    const searchButton = document.getElementById('searchButton');
    const searchInput = document.getElementById('searchInput');
    const searchType = document.getElementById('searchType');
    const boardTableBody = document.getElementById('boardTableBody');

    // 검색 버튼 클릭 시 검색 실행
    searchButton.addEventListener('click', function () {
        const query = searchInput.value;
        const type = searchType.value;
        searchPosts(query, type);
    });

// 검색 API 호출
    function searchPosts(query, type) {
        fetch(`/study/board/api/search?query=${encodeURIComponent(query)}&type=${type}`) // 검색 API 호출
            .then(response => response.json())
            .then(data => {
                boardTableBody.innerHTML = '';
                data.forEach((post, searchIndex) => {
                    const row = document.createElement('tr');
                    row.setAttribute('data-id', post.id); // 게시글 ID 저장
                    row.innerHTML = `
                        <td>${searchIndex + 1}</td>
                        <td>${post.title}</td> <!-- 제목 클릭 없이도 전체 영역 클릭 가능 -->
                        <td>${post.content}</td> <!-- 게시글 내용도 출력 -->
                        <td>${new Date(post.date).toLocaleDateString()}</td>
                    `;

                    // 게시글 행 전체에 클릭 이벤트 추가
                    row.addEventListener('click', function () {
                        const postId = this.getAttribute('data-id');
                        showBoardDetail(postId);
                    });

                    boardTableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error searching posts:', error));
    }
});