document.addEventListener('DOMContentLoaded', function () {
    const boardTableBody = document.getElementById('boardTableBody');
    const boardDetailSection = document.getElementById('boardDetail');
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardDate = document.getElementById('boardDate');
    const backButton = document.getElementById('backButton');
    const writeButton = document.getElementById('writeButton');

    // 게시글 목록 불러오기
    function loadBoardList() {
        fetch('/study/board/api/read')  // 게시글 목록을 불러오는 API 엔드포인트
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

                document.querySelectorAll('.board-link').forEach(link => {
                    link.addEventListener('click', function (e) {
                        e.preventDefault();
                        const postId = this.getAttribute('data-id');
                        showBoardDetail(postId);
                    });
                });
            })
            .catch(error => console.error('Error loading board list:', error));
    }

    // // 게시글 상세보기
    //
    // function showBoardDetail(postId) {
    //     fetch(`/study/board/api/${postId}`)  // TODO : 특정 게시글의 상세 정보를 불러오는 API 엔드포인트 추가하기
    //         .then(response => response.json())
    //         .then(post => {
    //             boardTitle.textContent = post.title;
    //             boardContent.textContent = post.content;
    //             boardDate.textContent = new Date(post.date).toLocaleString();
    //             document.querySelector('table').style.display = 'none';
    //             boardDetailSection.style.display = 'block';
    //         })
    //         .catch(error => console.error('Error loading board detail:', error));
    // }

    // 목록으로 돌아가기
    backButton.addEventListener('click', function () {
        boardDetailSection.style.display = 'none';
        document.querySelector('table').style.display = 'table';
    });

    // 글쓰기 버튼 클릭 시 글쓰기 페이지로 이동
    writeButton.addEventListener('click', function () {
        window.location.href = '/study/board/page/postWrite';  // 글쓰기 페이지로 이동
    });

    // 페이지 로드 시 게시글 목록 로드
    loadBoardList();
});