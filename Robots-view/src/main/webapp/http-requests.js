const robotsControllerUrl = new URL('http://localhost:8085/api/v1/robots/');
const robotsServiceControllerUrl = new URL('http://localhost:8085/api/v1/robots-service/');
const tasksControllerUrl = new URL('http://localhost:8085/api/v1/tasks/');

function startRobotsServiceExecution() {
    get(new URL('start', robotsServiceControllerUrl));
}

function stopRobotsServiceExecution() {
    get(new URL('stop', robotsServiceControllerUrl));
}

function startAllRobots() {
    get(new URL('robots/?command=start', robotsServiceControllerUrl));
}

function stopAllRobots() {
    get(new URL('robots/?command=stop', robotsServiceControllerUrl));
}

function get(link) {
    return fetch(link
        , {
            method: 'GET',
            mode: 'cors',
            cache: 'no-cache',
        })
        .then((result) => {
            return result.json()
        });
}