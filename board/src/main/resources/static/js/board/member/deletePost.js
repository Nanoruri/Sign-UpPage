document.addEventListener('DOMContentLoaded', function () {
    const deletePostButton = document.getElementById('deleteButton');
    const postId = getPostId(); // postId를 얻는 방법에 따라 수정 필요

    // 삭제 버튼 클릭 시 deletePost 함수 실행
    deletePostButton.addEventListener('click', function () {
        deletePost(postId);
    });
});

function deletePost(boardId) {
    const token = sessionStorage.getItem('aToken');

    fetch(`/study/board/api/delete/${boardId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (response.status === 401) {
                alert('삭제 권한이 없습니다. 다시 시도해주십시오');
            } else if (response.status === 500) {
                alert('서버 에러');
            } else if (response.status === 204) {
                alert('성공적으로 삭제되었습니다.');
                window.location.href = '/study/board/page/boardIndex';
            }
        })
        .catch(error => {
            console.error('Error deleting post:', error);
            alert('게시글 삭제 중 문제가 발생했습니다.');
        });
}
