function getAllRobot() {
    //fake data
    // return fetch(robotsController + 'jsons'
    return fetch(robotsController
        , {
            method: 'GET',
            mode: 'cors',
            cache: 'no-cache',
        })
        .then((result) => {
            return result.json()
        });
}