document.addEventListener('DOMContentLoaded', function () {
    const memberTab = document.getElementById('memberTab');
    const accessToken = sessionStorage.getItem('aToken');
    const writeButton = document.getElementById('writeButton');


    writeButton.style.display = 'none';

    // 탭 버튼 클릭 이벤트
    document.querySelectorAll('.tab-button').forEach(button => {
        button.addEventListener('click', function () {
            const tab = this.getAttribute('data-tab');

            // 모든 탭 콘텐츠 숨기기 (단, 버튼들은 유지)
            document.querySelectorAll('.tab-content').forEach(content => {
                content.style.display = 'none';
            });

            // 모든 탭 버튼에서 active 클래스 제거
            document.querySelectorAll('.tab-button').forEach(button => {
                button.classList.remove('active');
            });

            // 클릭한 탭에 active 클래스 추가
            this.classList.add('active');

            // 클릭된 탭의 콘텐츠만 표시
            document.getElementById(tab + 'TabContent').style.display = 'block';
        });
    });

    // 회원 게시글 로드 함수
    function loadMemberBoardList() {
        fetch('/study/board/api/memberBoard', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
            .then(response => {
                if (!response.ok) {

                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(posts => {

                memberTab.style.display = 'inline-block';
                writeButton.style.display = 'inline-block';

                const memberTableBody = document.getElementById('memberTableBody');
                memberTableBody.innerHTML = '';
                posts.forEach(post => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${post.id}</td>
                        <td>${post.title}</td>
                        <td>${post.content}</td>
                        <td>${new Date(post.date).toLocaleDateString()}</td>
                    `;
                    row.setAttribute('data-id', post.id);

                    row.addEventListener('click', function () {
                        const postId = this.getAttribute('data-id');
                        window.location.href = `/study/board/page/detail?postId=${postId}`;
                    });

                    memberTableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error loading member board list:', error));
    }

    // 글쓰기 버튼 클릭 시 글쓰기 페이지로 이동
    writeButton.addEventListener('click', function () {
        if(sessionStorage.getItem('boardToEdit')){
            sessionStorage.removeItem('boardToEdit')
           }
        window.location.href = '/study/board/page/postWrite';  // 글쓰기 페이지로 이동
    });

    // 페이지 로드 시 회원전용 게시글 목록 로드
    if (accessToken) {
        loadMemberBoardList();
    }
});
