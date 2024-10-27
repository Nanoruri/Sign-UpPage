// 게시글 상세보기 페이지 로드 시 호출되는 함수
function showBoardDetail(postId) {
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardDate = document.getElementById('boardDate');
    const editButton = document.getElementById('editButton');
    const backButton = document.getElementById('backButton');
    const token = sessionStorage.getItem('aToken'); // JWT 토큰 가져오기

    const headers = {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`; // 토큰이 존재하면 헤더에 추가
    }

    fetch(`/study/board/api/detail/${postId}`, {
        method: 'GET',
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

            if (data.isCreator) {  // 'data.isCreator'가 true일 때만 수정 버튼 표시
                editButton.style.display = 'inline-block';
            } else {
                editButton.style.display = 'none';
            }
        })
        .catch(error => {
            if (error.message === 'NotFound') {
                alert('존재하지 않는 게시글입니다.');
            } else {
                alert('잘못된 요청입니다. 다시 시도해 주세요.');
            }
            console.error('Error loading board detail:', error);
        });

    // 목록으로 돌아가기
    backButton.addEventListener('click', function () {
        window.location.href = '/study/board/page/boardIndex';
    });
}

// 댓글 표시 함수
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

function getPostId(){
    return new URLSearchParams(window.location.search).get('postId');
}

// 페이지 로드 시 showBoardDetail 호출
document.addEventListener('DOMContentLoaded', () => {
    const postId = getPostId();
    showBoardDetail(postId);
});
