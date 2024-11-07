document.addEventListener('DOMContentLoaded', function () {
    const memberTab = document.getElementById('memberTab');
    const accessToken = sessionStorage.getItem('aToken');
    const writeButton = document.getElementById('writeButton');
    const pageSize = 5; // 페이지 당 게시글 수
    let currentPage = 0; // 현재 페이지 초기화

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
    function loadMemberBoardList(page =0) {
        fetch(`/study/board/api/memberBoard?page=${page}&size=${pageSize}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })
            .then(response => {
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                } else if (response.status === 500) {
                    alert('서버 오류 발생!');
                    throw new Error('Server Error');
                }
                return response.json();
            })
            .then(data => {
                memberTab.style.display = 'inline-block';
                writeButton.style.display = 'inline-block';

                // 게시글 목록 표시
                const memberTableBody = document.getElementById('memberTableBody');
                memberTableBody.innerHTML = '';
                data.content.forEach((post, index) => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${index + 1 + (currentPage * pageSize)}</td>
                        <td>${post.title}</td>
                        <td>${new Date(post.date).toLocaleDateString()}</td>
                    `;
                    row.setAttribute('data-id', post.id);

                    row.addEventListener('click', function () {
                        const postId = this.getAttribute('data-id');
                        window.location.href = `/study/board/page/detail?postId=${postId}`;
                    });

                    memberTableBody.appendChild(row);
                });

                // 페이징 정보 업데이트
                updatePagination(data);
            })
            .catch(error => console.error('Error loading member board list:', error));
    }


    // 페이지 변경 시 호출되는 함수
    function changePage(page) {
        if (page < 0) return; // 페이지가 0보다 작아지지 않도록
        loadMemberBoardList(page);
    }

    // '이전' 버튼 클릭 시 페이지 변경
    document.getElementById('prev-btn').addEventListener('click', function () {
        changePage(currentPage - 1);
    });

    // '다음' 버튼 클릭 시 페이지 변경
    document.getElementById('next-btn').addEventListener('click', function () {
        changePage(currentPage + 1);
    });

    // 글쓰기 버튼 클릭 시 글쓰기 페이지로 이동
    writeButton.addEventListener('click', function () {
        if (sessionStorage.getItem('boardToEdit')) {
            sessionStorage.removeItem('boardToEdit');
        }
        window.location.href = '/study/board/page/postWrite';
    });

    // 페이지 로드 시 회원 전용 게시글 목록 로드
    if (accessToken) {
        loadMemberBoardList(currentPage);
    }
});
