document.addEventListener('DOMContentLoaded', function () {goBoard()});




function goBoard() {
    document.getElementById('boardButton').addEventListener('click', function () {

        const apiEndpoint = '/study/board/page/boardIndex';

        fetch(apiEndpoint, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                // Add any other headers you need
            },
        })
            .then(function (response) {
                if (response.ok) {
                    return window.location.href = '/study/board/page/boardIndex'; // Parse the JSON response
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(function (data) {
                console.log('Data from other module:', data);
                // Process the data from the other module
            })
            .catch(function (error) {
                console.error('Error calling other module API:', error);
            });
    });


}