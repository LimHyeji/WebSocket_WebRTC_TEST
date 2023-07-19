//sockjs를 이용한 서버와 연결되는 객체
var ws=null;

function setConnected(connected){
}

function showMessage(message){
    var jsonMessage=JSON.parse(message);//json 형식으로 전송된 메시지를 파싱 후
    $("#chatArea").append(jsonMessage.name+' : '+jsonMessage.message+'\n');//받은 메시지로 화면에 띄움

    $("#chatArea").scrollTop(textArea[0].scrollHeight-textArea.height());//스크롤 화면으로 생성
}

function connect(){
    //SockJS 라이브러리를 이용하여 서버에 연결하는 메소드
    ws=new SockJS('/ws');//SockJS 객체 생성 (이때 /ws는 url path?)
    //서버가 메시지를 보내주면 함수가 호출된다
    ws.onmessage=function(message){//메시지가 있다면
        showMessage(message.data);//화면에 출력
    }
}

