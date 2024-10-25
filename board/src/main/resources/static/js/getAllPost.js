function getAllPost() {
    const boardTableSection = document.getElementById('generalTabContent'); // 게시글 목록 섹션
    const boardDetailSection = document.getElementById('boardDetail');
    const backButton = document.getElementById('backButton');
    const writeButton = document.getElementById('writeButton');
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
            .catch(error => {
                console.error('Error loading board list:', error);
                alert('게시글 목록을 불러오는 중 오류가 발생했습니다.');
                window.location.href = '/study/';
            });
    }



    // 목록으로 돌아가기
    backButton.addEventListener('click', function () {
        // 게시글 상세보기 섹션 숨기기
        boardDetailSection.style.display = 'none';
        // 게시글 목록 섹션 보이기
        boardTableSection.style.display = 'block';
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
}

let currentPostId = null;

// 게시글 상세보기
function showBoardDetail(postId) {
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardDate = document.getElementById('boardDate');
    const boardTableSection = document.getElementById('generalTabContent');
    const memberTabContent = document.getElementById('memberTabContent');
    const boardDetailSection = document.getElementById('boardDetail');

    const editButton = document.getElementById('editButton');
    const token = sessionStorage.getItem('aToken'); // JWT 토큰 가져오기


    currentPostId = postId;

    const headers = {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`; // 토큰이 존재하면 헤더에 추가
    }

    fetch(`/study/board/api/detail/${postId}`,
        {
            method: 'GET',// 생략 가능
            headers: headers
        })
        .then(response => {
            if (response.status === 404) {
                throw new Error('NotFound');
            } else if (!response.ok) {
                throw new Error('BadRequest');
            }
            return response.json();
        })
        .then(data => {
            const post = data.board;
            boardTitle.textContent = post.title; // 제목 표시
            boardContent.innerHTML = post.content; // 내용 표시
            boardDate.textContent = new Date(post.date).toLocaleString(); // 작성일 표시
            post.comments = post.comments || [];
            data.isCreator = data.isCreator || false;

            displayComments(post.comments);

            if (data.isCreator === true) {  // 'data.isCreator'가 true일 때만 수정 버튼 표시
                editButton.style.display = 'inline-block';
            } else {
                editButton.style.display = 'none';
            }


            // 게시글 목록 섹션 숨기기
            boardTableSection.style.display = 'none';
            memberTabContent.style.display = 'none';
            // 게시글 상세보기 섹션 보이기
            boardDetailSection.style.display = 'block';
        })
        .catch(error => {
            if (error.message === 'NotFound') {
                alert('존재하지 않는 게시글입니다.');
            } else {
                alert('잘못된 요청입니다. 다시 시도해 주세요.');
            }
            window.location.reload();
            console.error('Error loading board detail:', error);
        });

    function displayComments(comments) {
        const commentSection = document.getElementById('commentSection');
        commentSection.innerHTML = ''; // 기존 댓글 초기화

        if (comments && comments.length > 0) {
            comments.forEach(comment => {
                const commentDiv = document.createElement('div');
                commentDiv.classList.add('comment');

                const commentContent = document.createElement('p');
                commentContent.textContent = comment.content; // 댓글 내용 표시

                const commentDate = document.createElement('span');
                commentDate.textContent = new Date(comment.date).toLocaleString(); // 작성일 표시
                commentDate.classList.add('comment-date');

                commentDiv.appendChild(commentContent);
                commentDiv.appendChild(commentDate);
                commentSection.appendChild(commentDiv);
            });
        } else {
            commentSection.innerHTML = '<p>댓글이 없습니다.</p>'; // 댓글이 없을 경우 메시지 표시
        }
    }
}

// 페이지 로드 시 getAllPost 호출
document.addEventListener('DOMContentLoaded', getAllPost); {
}
