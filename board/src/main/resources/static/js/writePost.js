document.addEventListener('DOMContentLoaded', function () {
    const writeForm = document.getElementById('writeForm');
    const cancelButton = document.getElementById('cancelButton');

    const redirectIndex = '/study/board/page/boardIndex';


    const quill = new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{'header': [1, 2, false]}],
                ['bold', 'italic', 'underline'],
                ['image'] // 이미지 업로드 버튼
            ]
        }
    });

// 이미지 업로드 핸들러
    quill.getModule('toolbar').addHandler('image', () => {
        selectLocalImage();
    });

// 이미지 선택 및 서버 업로드 처리
    function selectLocalImage() {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = () => {
            const file = input.files[0];
            if (file) {
                uploadImage(file);
            }
        };
    }

// 이미지 서버 업로드 함수
    function uploadImage(file) {
        const formData = new FormData();
        formData.append('image', file);

        fetch('/study/board/api/upload-image', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(result => {
                const range = quill.getSelection();
                quill.insertEmbed(range.index, 'image', result.imageUrl);
            })
            .catch(error => {
                console.error('Error uploading image:', error);
            });
    }

// 작성하기 버튼 클릭 시 에디터 내용을 textarea로 복사하여 서버로 전송
    writeForm.addEventListener('submit', function (e) {
        e.preventDefault();  // 기본 폼 제출 동작을 막습니다.
        const content = quill.root.innerHTML;  // Quill 에디터에서 작성된 내용
        const formData = {
            tabName: document.getElementById('tab').value,
            title: document.getElementById('title').value,
            content: content
        };

        fetch('/study/board/api/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (response.ok) {  // 응답이 200(OK)일 경우
                    alert('게시글이 작성되었습니다.');
                    window.location.href = redirectIndex;  // 리다이렉트
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || '게시글 작성에 실패했습니다.');
                    });
                }
            })
            .catch(error => {
                console.error('Error submitting post:', error);
            });
    });

    cancelButton.addEventListener('click', function () {
        window.location.href = redirectIndex;
    });
});
