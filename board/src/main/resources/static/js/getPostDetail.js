// 게시글 상세보기 페이지 로드 시 호출되는 함수
function showBoardDetail(postId, tabName) {
    const boardTitle = document.getElementById('boardTitle');
    const boardContent = document.getElementById('boardContent');
    const boardMeta = document.getElementById('boardMeta');
    const boardDate = document.getElementById('boardDate');
    const editButton = document.getElementById('editButton');
    const backButton = document.getElementById('backButton');
    const deleteButton = document.getElementById("deleteButton");
    const token = sessionStorage.getItem('aToken'); // JWT 토큰 가져오기

    const headers = {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`; // 토큰이 존재하면 헤더에 추가
    }

    fetch(`/study/board/api/detail/${postId}?tabName=${tabName}`, {
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

            // 작성자와 작성일 표시
            boardMeta.innerHTML = `
                <span class="post-author">작성자: ${post.creator}</span>
                <span class="post-date">${new Date(post.date).toLocaleString()}</span>
            `;

            post.comments = post.comments || [];
            data.isCreator = data.isCreator || false;
            data.currentUserId = data.currentUserId || null

            displayComments(post.comments, token, data.currentUserId);

            if (data.isCreator) {  // 'data.isCreator'가 true일 때만 수정 버튼 표시
                editButton.style.display = 'inline-block';
                deleteButton.style.display = 'inline-block';
            } else {
                editButton.style.display = 'none';
                deleteButton.style.display = 'none';
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
function displayComments(comments, token, currentUserId) {
    const commentSection = document.getElementById('commentSection');
    commentSection.innerHTML = '';

    if (comments && comments.length > 0) {
        comments.forEach(comment => {
            const commentDiv = document.createElement('div');
            commentDiv.classList.add('comment-box');

            const headerDiv = document.createElement('div');
            headerDiv.classList.add('comment-header');

            const date = document.createElement('span');
            date.textContent = new Date(comment.date).toLocaleString();
            date.classList.add('comment-date');

            // 댓글 작성자 표시
            const commentAuthor = document.createElement('span');
            commentAuthor.textContent = `작성자: ${comment.creator}`;
            commentAuthor.classList.add('comment-author');


            headerDiv.appendChild(date);
            headerDiv.appendChild(commentAuthor); // 댓글 작성자 추가

            const contentDiv = document.createElement('div');
            contentDiv.classList.add('comment-content');
            contentDiv.textContent = comment.content;

            // 수정 버튼 생성
            const commentEditButton = document.createElement('button');
            commentEditButton.textContent = '수정';

            // 삭제 버튼 생성
            const commentDeleteButton = document.createElement('button');
            commentDeleteButton.textContent = '삭제';

            // 댓글 작성자와 현재 사용자가 같은 경우에만 수정 버튼 활성화
            if (comment.creator === currentUserId) {
                commentEditButton.addEventListener('click', () => editComment(comment.id, contentDiv, commentEditButton, token));
                commentDeleteButton.addEventListener('click', () => deleteComment(comment.id, token));
            } else {
                commentEditButton.style.display = 'none';
                commentDeleteButton.style.display = 'none';
            }

            commentDiv.appendChild(headerDiv);
            commentDiv.appendChild(contentDiv);
            commentDiv.appendChild(commentEditButton);
            commentDiv.appendChild(commentDeleteButton);
            commentSection.appendChild(commentDiv);
        });
    } else {
        commentSection.innerHTML = '<p>댓글이 없습니다.</p>'; // 댓글이 없을 경우 메시지 표시
    }
}

function getPostId() {
    return new URLSearchParams(window.location.search).get('postId');
}

function getTabName(){
    return new URLSearchParams(window.location.search).get('tabName')
}

// 페이지 로드 시 showBoardDetail 호출
document.addEventListener('DOMContentLoaded', () => {
    const mainPageButton = document.getElementById("mainButton");
    mainPageButton.addEventListener('click', function () {
        window.location.href = '/study/';
    });
    const postId = getPostId();
    const tabName = getTabName();
    showBoardDetail(postId,tabName);
});
