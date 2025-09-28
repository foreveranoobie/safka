function closeUserDetails(){
    document.getElementById('user-info-overlay').style.display = 'none'
    document.getElementById('user-info').style.display = 'none'
}

function openUserDetails(){
    document.getElementById('user-info-overlay').style.display = 'block'
    document.getElementById('user-info').style.display = 'block'
}

function closeSendMsg() {
    document.getElementById('message-overlay').style.display = 'none'
    document.getElementById('send-msg').style.display = 'none'
}

function openSendMsg() {
    document.getElementById('message-overlay').style.display = 'block'
    document.getElementById('send-msg').style.display = 'flex'
}