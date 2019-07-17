const robotsController = 'http://localhost:8085/api/v1/robots/';
const robotsServiceController = 'http://localhost:8085/api/v1/robots-service/';
const tasksController = 'http://localhost:8085/api/v1/tasks/';

function startRobotsServiceExecution() {
    get(robotsServiceController + 'start');
}

function stopRobotsServiceExecution() {
    get(robotsServiceController + 'stop');
}

function startAllRobots() {
    get(robotsServiceController + 'robots/?command=start');
}

function stopAllRobots() {
    get(robotsServiceController + 'robots/?command=stop');
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