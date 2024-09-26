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
                data.forEach((post, index) => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                            <td>${index + 1}</td>
                            <td><a href="#" class="board-link" data-id="${post.id}">${post.title}</a></td>
                            <td>${new Date(post.date).toLocaleDateString()}</td>
                        `;
                    boardTableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error searching posts:', error));
    }
});