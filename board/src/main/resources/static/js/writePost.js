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

        // Quill 에디터에서 내용 가져오기
        const editorContent = quill.root.innerHTML;
        const editorText = quill.getText();


        if (editorText.trim() === '') {
            alert('내용을 입력하세요!');
            return;
        }

        const cleanContent = stripHtmlTags(editorContent);

        document.getElementById('content').value = cleanContent;

        const formData = {
            title: document.getElementById('title').value,
            content: cleanContent, // Quill 에디터에서 가져온 HTML 내용
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

    // HTML 태그를 벗겨내는 함수
    function stripHtmlTags(html) {
        const doc = new DOMParser().parseFromString(html, 'text/html');
        return doc.body.textContent || "";
    }
});
