let running = false;

function startStopRobots(buttonId) {
    if (running) {
        running = false;
        let elem = document.getElementById(buttonId);
        elem.innerText = "Start";
        elem.classList.replace("btn-success", "btn-secondary");
        stopAllRobots();
    } else {
        running = true;
        let elem = document.getElementById(buttonId);
        elem.innerText = "Stop";
        elem.classList.replace("btn-secondary", "btn-success");
        startAllRobots();
    }
}


let runningService = false;

function startStopService(buttonId) {
    if (runningService) {
        runningService = false;
        let elem = document.getElementById(buttonId);
        elem.innerText = "Start";
        elem.classList.replace("btn-success", "btn-secondary");
        stopRobotsServiceExecution();
    } else {
        runningService = true;
        let elem = document.getElementById(buttonId);
        elem.innerText = "Stop";
        elem.classList.replace("btn-secondary", "btn-success");
        startRobotsServiceExecution();
    }
}

