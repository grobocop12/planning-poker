let cookie = {};
let estimates = {};
const cookieUsername = `username_${id}`;
const cookieUserId = `user_id_${id}`;
const buttonDescriptionShow = 'Show';
const buttonDescriptionHide = 'Hide';

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
            tableBody.append(
`<tr>
<td>${i}</td>
<td>${estimate['userName']}</td>
<td>${estimate['estimate'] === null ? '' : estimate['estimate']}</td>
</tr>`)
        } else {
            tableBody.append(
`<tr>
<td>${i}</td>
<td>${estimate['userName']}</td>
<td>${estimate['estimate'] === null ? '' : '-'}</td>
</tr>`)
        }
        i++;
    });
}

function setShowButtonDescription() {
    if (estimates['showEstimates'] === true) {
        $('#showButton').html(`${buttonDescriptionHide}`);
    } else {
        $('#showButton').html(`${buttonDescriptionShow}`);
    }
}

const socket = new SockJS('/app-socket');
const stompClient = Stomp.over(socket);
stompClient.connect({}, () => {
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
    stompClient.subscribe(`/broker/poker/${id}/vote`, (msg) => {
        const vote = JSON.parse(msg["body"]);
        const toChange = estimates['estimates'].find(obj => {
            return obj.id === vote['id']
        });
        if (typeof toChange !== "undefined") {
            toChange.estimate = vote.estimate
        }
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
    });
    $('#deleteButton').click(()=> {
        stompClient.send(`/app/poker/${id}/delete`,{},{});
    });
    const cards = $('.poker-card');
    cards.each((i) => {
        cards[i].onclick = () => {
            const attr = cards[i].attributes['data-id']['value'];
            stompClient.send(`/app/poker/${id}/vote`, {}, JSON.stringify({'userId': userId, 'userEstimate': attr}))
        }
    });
})