'use strict';

function toRobotTable(data) {
    let taskRow = [];

    let tbody = data.map((task) => {
        taskRow = [];
        taskRow.push(React.createElement("td", null, task.id % 100));
        taskRow.push(React.createElement("td", null, task.name));
        taskRow.push(React.createElement("td", null, task.allowedTasks));
        taskRow.push(React.createElement("td", null, Object.keys(task.taskQueue).length ));
        taskRow.push(React.createElement("td", null, task.running ? 1 : 0));
        return React.createElement("tr", null, taskRow);
    });

    return React.createElement("table", {class: "table table-hover table-bordered w-auto"},
        React.createElement("thead", {class: "thead-light"},
            React.createElement("tr", null,
                React.createElement("th", {scope: "col"}, "#id"),
                React.createElement("th", {scope: "col"}, "name"),
                React.createElement("th", {scope: "col"}, "allowed tasks"),
                React.createElement("th", {scope: "col"}, "queue"),
                React.createElement("th", {scope: "col"}, "run")
            )),
        React.createElement("tbody", null, tbody));
}

const domRobotTableContainer = document.querySelector('#table-robots');

function updateRobotsTable() {
    getAllRobot().then((data) => {
        ReactDOM.render(toRobotTable(data), domRobotTableContainer);
    });
}

// document.addEventListener("DOMContentLoaded", updateRobotsTable);
setInterval(updateRobotsTable, 500);