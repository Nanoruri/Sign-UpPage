document.addEventListener('DOMContentLoaded', function () {
    const commentForm = document.getElementById('commentForm');

    commentForm.addEventListener('submit', function (e) {
        e.preventDefault();  // Prevent default form submission

        const accessToken = sessionStorage.getItem('aToken');
        const boardId = new URLSearchParams(window.location.search).get('postId');
        const commentContent = document.getElementById('commentContent').value;

        const formData = {
            board: {id: boardId},
            content: commentContent
        };

        fetch('/study/comment/api/create-comment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (response.ok) {  // If response is 200(OK)
                    alert('댓글이 작성되었습니다.');
                    window.location.reload();  // Reload the page
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || '댓글 작성에 실패했습니다.');
                    });
                }
            })
            .catch(error => {
                console.error('Error submitting comment:', error);
                alert('댓글 작성 중 문제가 발생했습니다.');
            });
    });
});

function editComment(commentId, contentDiv, commentEditButton, token) {
    const originalContent = contentDiv.textContent;
    contentDiv.innerHTML = `<input type="text" value="${originalContent}" style="width: 100%;">`;
    const inputField = contentDiv.querySelector('input');

    const saveButton = document.createElement('button');
    saveButton.textContent = '저장';
    saveButton.addEventListener('click', () => updateComment(commentId, inputField.value, contentDiv, token, commentEditButton));

    const cancelButton = document.createElement('button');
    cancelButton.textContent = '취소';
    cancelButton.addEventListener('click', () => {
        contentDiv.innerHTML = originalContent; // 원래 내용으로 되돌리기
        commentEditButton.style.display = 'inline'; // 수정 버튼 다시 보이기
    });

    contentDiv.appendChild(saveButton);
    contentDiv.appendChild(cancelButton);
    commentEditButton.style.display = 'none'; // 수정 버튼 숨기기
}

function updateComment(commentId, newContent, contentDiv, token, commentEditButton) {
    const formData = {
        id: commentId,
        content: newContent
    };

    fetch('/study/comment/api/update-comment', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('댓글 업데이트에 실패했습니다.');
            }else{
            contentDiv.innerHTML = newContent; // DOM에서 내용 업데이트
            alert('댓글이 성공적으로 업데이트되었습니다.');
                loadComments(); // 댓글 목록을 다시 불러오는 함수 호출
            }
        })
        .catch(error => {
            console.error('댓글 업데이트 중 오류 발생:', error);
            alert('댓글 업데이트에 실패했습니다.');
            contentDiv.innerHTML = newContent; // 실패하더라도 입력 필드를 원래 내용으로 되돌리기
            commentEditButton.style.display = 'inline'; // 수정 버튼 다시 보이기
        });

    function loadComments() {
        const postId = getPostId();// todo: API 나누는 방식도 고려해보기
        fetch(`/study/board/api/detail/${postId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                displayComments(data.board.comments, token, data.currentUserId); // 댓글 표시 함수 호출
            })
            .catch(error => {
                console.error("Error loading comments:", error);
            });
    }

}