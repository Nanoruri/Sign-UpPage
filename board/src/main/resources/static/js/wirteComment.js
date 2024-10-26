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