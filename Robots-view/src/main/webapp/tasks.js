function getTask() {
    //fake data
    // return fetch(tasksController + 'jsons'
    return fetch(tasksController
        , {
            method: 'GET', // *GET, POST, PUT, DELETE, etc.
            mode: 'cors', // no-cors, cors, *same-origin
            cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        })
        .then((result) => {
            return result.json()
        });
}

//предыдущая реализация отображения данных
function showTask() {
    getTask()
        .then(function (data) {
            let result = '';
            data.forEach((task) => {
                const date = new Date(task.createDate).toLocaleTimeString();
                result +=
                    `<tr>
                        <td class="align-middle">${task.id}</td>
                        <td>${task.description}</td>
                        <td>${task.taskPriority}</td>
                        <td>${task.status}</td>
                        <td>${date}</td>
                        <td>${task.difficultyMilliseconds}</td>
                     </tr>`;
                document.getElementById('result').innerHTML = result;
            });
        });
}