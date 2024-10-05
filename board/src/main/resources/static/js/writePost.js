document.addEventListener('DOMContentLoaded', function () {
    const writeForm = document.getElementById('writeForm');
    const cancelButton = document.getElementById('cancelButton');

    // Quill editor 초기화
    const quill = new Quill('#editor', {
        theme: 'snow',
        placeholder: '내용을 입력하세요...',
    });

    // 글 작성 폼 제출 시
    writeForm.addEventListener('submit', function (e) {
        e.preventDefault();

        // Quill 에디터에서 HTML 형태의 내용 가져오기
        const editorContent = quill.root.innerHTML;
        const editorText = quill.getText();


        if (editorText.trim() === '') {
            alert('내용을 입력하세요!');
            return;
        }

        // 숨겨진 textarea에 Quill 에디터 HTML 내용 설정
        document.getElementById('content').value = editorContent;

        // 글 제목, 내용, 탭 정보 수집
        const formData = {
            title: document.getElementById('title').value,
            content: editorContent, // HTML 형태로 저장
            tabName: document.getElementById('tab').value
        };

        // 서버로 POST 요청 보내기
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
                    window.location.href = '/study/board/page/boardIndex'; // 글 작성 완료 후 이동
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
