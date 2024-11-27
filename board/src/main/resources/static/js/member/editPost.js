document.addEventListener('DOMContentLoaded', function () {
    const editButton = document.getElementById('editButton');

    editButton.addEventListener('click', function () {
        const postId = getPostId();
        fetchBoardDetails(postId);
    });
});

function fetchBoardDetails(boardId) {
    const token = sessionStorage.getItem('aToken');

    fetch(`/study/board/api/getBoardInfo/${boardId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch board details');
            }
            return response.json();
        })
        .then(board => {
            sessionStorage.setItem('boardToEdit', JSON.stringify(board));
            window.location.href = '/study/board/page/postWrite';
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
