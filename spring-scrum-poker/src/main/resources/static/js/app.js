let cookie = {};
let estimates = {};
const cookieUsername = `username_${id}`;
const cookieUserId = `user_id_${id}`;
const buttonDescriptionShow = 'Show estimates';
const buttonDescriptionHide = 'Hide estimates';
var button = $('#showButton');

document.cookie.split(';').forEach((parameter) => {
    const key = parameter.split('=')[0].trim();
    cookie[key] = parameter.split('=')[1].trim();
})

const username = cookie[cookieUsername];
const userId = parseInt(cookie[cookieUserId]);
if (typeof username === "undefined" || typeof userId === "undefined") {
    window.location.href = window.location + "/name";
}

function refreshEstimates() {
    const tableBody = $('#tableBody');
    tableBody.empty();
    let i = 1;
    estimates["estimates"].forEach((estimate) => {
        if (estimates["showEstimates"] === true) {
            tableBody.append(`<tr>
<td>${i}</td>
<td>${estimate['userName']}</td>
<td>${estimate['estimate'] === null ? '' : estimate['estimate']}</td>
</tr>`)
        } else {
            tableBody.append(`<tr>
<td>${i}</td>
<td>${estimate['userName']}</td>
<td>-</td>
</tr>`)
        }
        i++;
    });
}

function setShowButtonDescription() {
    if (estimates['showEstimates'] === true) {
        button.text(`${buttonDescriptionHide}`);
    } else {
        button.text(`${buttonDescriptionShow}`);
    }
}

const socket = new SockJS('/app-socket');
const stompClient = Stomp.over(socket);
stompClient.connect({}, (frame) => {
    stompClient.subscribe(`/broker/poker/${id}/listOfUsers`, (msg) => {
        estimates = JSON.parse(msg["body"]);
        refreshEstimates();
        setShowButtonDescription();
    });
    stompClient.subscribe(`/broker/poker/${id}/show`, (msg) => {
        const state = JSON.parse(msg["body"]);
        estimates['showEstimates'] = state["state"];
        refreshEstimates();
        setShowButtonDescription();
    });
    stompClient.send(`/app/poker/${id}`, {}, JSON.stringify({'id': userId, 'userName': username}));
});

$(document).ready(() => {
    setShowButtonDescription();
    $('#showButton').click(() => {
        if (typeof estimates['showEstimates'] !== "undefined") {
            stompClient.send(`/app/poker/${id}/show`, {}, JSON.stringify({'state': !estimates['showEstimates']}))
        }
    })
})