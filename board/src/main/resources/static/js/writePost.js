document.addEventListener('DOMContentLoaded', function () {
    const writeForm = document.getElementById('writeForm');
    const cancelButton = document.getElementById('cancelButton');
    const redirectIndex = '/study/board/page/boardIndex';
    const quill = initializeQuill();
    setupImageUploadHandler(quill);

    writeForm.addEventListener('submit', createPost);
    cancelButton.addEventListener('click', () => window.location.href = redirectIndex);
});

function initializeQuill() {
    return new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{'header': [1, 2, false]}],
                ['bold', 'italic', 'underline'],
                ['image']
            ]
        }
    });
}

function setupImageUploadHandler(quill) {
    quill.getModule('toolbar').addHandler('image', () => selectLocalImage(quill));
}

function selectLocalImage(quill) {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.onchange = () => {
        const file = input.files[0];
        if (file) {
            uploadImage(file, quill);
        }
    };
}

function uploadImage(file, quill) {
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
    .catch(error => console.error('Error uploading image:', error));
}

function createPost(event) {
    event.preventDefault();
    const quill = Quill.find(document.querySelector('#editor'));
    const content = quill.root.innerHTML;
    const accessToken = sessionStorage.getItem('aToken');
    const formData = {
        tabName: document.getElementById('tab').value,
        title: document.getElementById('title').value,
        content: content
    };

    fetch('/study/board/api/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            alert('게시글이 작성되었습니다.');
            window.location.href = '/study/board/page/boardIndex';
        } else {
            return response.json().then(errorData => {
                throw new Error(errorData.message || '게시글 작성에 실패했습니다.');
            });
        }
    })
    .catch(error => console.error('Error submitting post:', error));
}
//todo: 이벤트 중복 처리 문제, 업데이트 시 에디터 내용 불러오기, 이미지 업로드 로직 gpt참고하여 수정