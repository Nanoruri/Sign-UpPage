document.addEventListener('DOMContentLoaded', function () {
    const boardTableBody = document.getElementById('boardTableBody');
    const boardDetailSection = document.getElementById('boardDetail');
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardDate = document.getElementById('boardDate');
    const backButton = document.getElementById('backButton');
    const writeButton = document.getElementById('writeButton');
    const mainPageButton = document.getElementById('mainButton');

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
            .catch(error => {
                console.error('Error loading board list:', error);
                alert('게시글 목록을 불러오는 중 오류가 발생했습니다.');
                window.location.href = '/study/';
            });
    }

    // 게시글 상세보기

    function showBoardDetail(postId) {
        fetch(`/study/board/api/detail/${postId}`)
            .then(response => {
                if (response.status === 404) {
                    throw new Error('NotFound');
                } else if (!response.ok) {
                    throw new Error('BadRequest');
                }
                return response.json();
            })
            .then(post => {
                boardTitle.textContent = post.title;
                boardContent.textContent = post.content;
                boardDate.textContent = new Date(post.date).toLocaleString();
                document.querySelector('table').style.display = 'none';
                boardDetailSection.style.display = 'block';
            })
            .catch(error => {
                if (error.message === 'NotFound') {
                    alert('존재하지 않는 게시글입니다.');
                } else {
                    alert('잘못된 요청입니다. 다시 시도해 주세요.');
                }
                window.reload();
                console.error('Error loading board detail:', error);
            });

    }

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


    // 메인 페이지로 이동
    mainPageButton.addEventListener('click', function () {
        window.location.href = '/study/';
    });
});