document.addEventListener('DOMContentLoaded', function () {
    const writeForm = document.getElementById('writeForm');
    const cancelButton = document.getElementById('cancelButton');

    // 글 작성 폼 제출 시
    writeForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value,
            tabName: document.getElementById('tab').value
        };

        fetch('/study/board/api/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => {
                if (response.ok) {
                    alert('글이 성공적으로 작성되었습니다.');
                    window.location.href = '/study/board/page/boardIndex';
                } else {
                    throw new Error('글 작성에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다. 다시 시도해 주세요.');
            });
    });

    cancelButton.addEventListener('click', function () {
        window.location.href = '/study/board/page/boardIndex';
    });
});