document.addEventListener('DOMContentLoaded', function () {
    const writeForm = document.getElementById('writeForm');
    const cancelButton = document.getElementById('cancelButton');
    const redirectIndex = '/study/board/page/boardIndex';
    const quill = initializeQuill();
    setupImageUploadHandler(quill);

    const board = JSON.parse(sessionStorage.getItem('boardToEdit'));
    if (board) {
        // 수정 모드
        populateBoardForm(board, quill);
        writeForm.addEventListener('submit', function (event) {
            event.preventDefault();
            updateBoard(board.id, quill);
        });
    } else {
        // 작성 모드
        writeForm.addEventListener('submit', createPost);
    }

    cancelButton.addEventListener('click', () => window.location.href = redirectIndex);
});


function initializeQuill() {

    const toolbarOption = [
        [{'header': [1, 2, false]}],
        ['bold', 'italic', 'underline'],
        ['image']]


    return new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: toolbarOption,
            resize: {
                locale: {
                    center: "center",
                },
            },
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
    const accessToken = sessionStorage.getItem('aToken')
    const formData = new FormData();
    formData.append('image', file);

    fetch('/study/board/api/upload-image', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        },
        body: formData
    })
        .then(response => response.json())
        .then(result => {
            const range = quill.getSelection();
            quill.insertEmbed(range.index, 'image', result.imageUrl);
            const insertedImage = quill.root.querySelector(`img[src="${result.imageUrl}"]`);
            if (insertedImage) {
                insertedImage.style.maxWidth = '100%';  // 부모 요소의 너비를 초과하지 않도록 설정
                insertedImage.style.height = 'auto';    // 비율 유지
            }
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

function populateBoardForm(board, quill) {
    document.getElementById('title').value = board.title;
    quill.root.innerHTML = board.content;  // Quill 에디터에 가져온 게시글 내용 삽입
}

function updateBoard(boardId, quill) {
    const token = sessionStorage.getItem('aToken');
    const updatedBoard = {
        title: document.getElementById('title').value,
        content: quill.root.innerHTML  // Get content from Quill editor
    };

    fetch(`/study/board/api/update/${boardId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(updatedBoard)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글 수정에 실패했습니다.');
            }
            alert('게시글이 수정되었습니다.');
            sessionStorage.removeItem('boardToEdit');
            window.location.href = '/study/board/page/boardIndex';
        })
        .catch(error => console.error('Error updating post:', error));
}