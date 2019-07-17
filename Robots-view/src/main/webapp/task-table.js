'use strict';

function toTaskTable(data) {
    let taskRow = [];

    let tbody = data.map((task) => {
        taskRow = [];
        taskRow.push(React.createElement("td", null, task.id % 100));
        taskRow.push(React.createElement("td", null, task.description));
        taskRow.push(React.createElement("td", null, task.taskPriority));
        taskRow.push(React.createElement("td", null, task.status));
        taskRow.push(React.createElement("td", null, new Date(task.createDate).toLocaleTimeString()));
        taskRow.push(React.createElement("td", null, task.difficulty));
        taskRow.push(React.createElement("button", {
            type: "button", class: "btn btn-primary btn-sm my-1",
            "data-toggle": "modal",
            "data-target": "#exampleModal"
        }, "Assign"));
        return React.createElement("tr", null, taskRow);
    });

    return React.createElement("table", {class: "table table-hover table-bordered w-auto"},
        React.createElement("thead", {class: "thead-light"},
            React.createElement("tr", null,
                React.createElement("th", {scope: "col"}, "#id"),
                React.createElement("th", {scope: "col"}, "description"),
                React.createElement("th", {scope: "col"}, "priority"),
                React.createElement("th", {scope: "col"}, "status"),
                React.createElement("th", {scope: "col"}, "create date"),
                React.createElement("th", {scope: "col"}, "difficulty"),
                React.createElement("th", {scope: "col"},)
            )
        ),
        React.createElement("tbody", null, tbody));
}

const domTaskTableContainer = document.querySelector('#table-task');

function updateTasksTable() {
    getTask().then((data) => {
        ReactDOM.render(toTaskTable(data), domTaskTableContainer);
    });
}

// document.addEventListener("DOMContentLoaded", updateTasksTable);
setInterval(updateTasksTable, 500);