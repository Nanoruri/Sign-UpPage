function deleteComment(commentId, token) {
    fetch(`/study/comment/api/delete/${commentId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (response.status !== 204) {
                throw new Error('Failed to delete comment');
            }
            alert('댓글이 성공적으로 삭제되었습니다.')
            location.reload();
        })
        .catch(error => {
            console.error('Error deleting comment:', error);
            alert('댓글 삭제에 실패했습니다.');
        });
}