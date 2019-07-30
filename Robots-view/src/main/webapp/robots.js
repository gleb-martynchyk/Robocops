function getAllRobot() {
    //fake data
    // return fetch(robotsControllerUrl + 'jsons'
    return fetch(robotsControllerUrl
        , {
            method: 'GET',
            mode: 'cors',
            cache: 'no-cache',
        })
        .then((result) => {
            return result.json()
        });
}