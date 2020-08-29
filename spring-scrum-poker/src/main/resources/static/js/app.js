let cookie = {};
const cookieUsername = `username_${id}`;
const cookieUserId = `user_id_${id}`;
console.log(document.cookie.split(';'));
document.cookie.split(';').forEach((parameter) => {
    const key = parameter.split('=')[0].trim();
    cookie[key] = parameter.split('=')[1].trim();
})
const username = cookie[cookieUsername];
const userId = parseInt(cookie[cookieUserId]);
if (typeof username === "undefined" || typeof userId === "undefined" ) {
    window.location.href = window.location + "/name";
}
const socket = new SockJS('/app-socket');
const stompClient = Stomp.over(socket);
stompClient.connect({}, (frame) => {
    stompClient.subscribe(`/broker/poker/${id}/listOfUsers`, (msg) => {
        console.log(msg);
    })
    stompClient.send(`/app/poker/${id}`, {}, JSON.stringify({'id': userId, 'userName': username}))
});