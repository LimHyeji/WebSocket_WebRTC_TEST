//sockjs를 이용한 서버와 연결되는 객체
var ws=null;

function setConnected(connected){
}

function showMessage(message){
    var jsonMessage=JSON.parse(message);//json 형식으로 전송된 메시지를 파싱 후
    $("#chatArea").append(jsonMessage.name+' : '+jsonMessage.message+'\n');//받은 메시지로 화면에 띄움

    var textArea = $('#chatArea');
    textArea.scrollTop(textArea[0].scrollHeight-textArea.height());//스크롤 화면으로 생성
}

function connect(){
    //SockJS 라이브러리를 이용하여 서버에 연결하는 메소드
    ws=new SockJS('/ws');//SockJS 객체 생성 (이때 /ws는 url path?)
    //서버가 메시지를 보내주면 함수가 호출된다
    ws.onmessage=function(message){//메시지가 있다면
        showMessage(message.data);//화면에 출력
    }
}

function disconnect(){
    if(ws!=null){
        ws.close();
    }
    setConnected(false);
}

function send(){
   //웹소켓 서버에 메시지 JSON화 후 전송
   ws.send(JSON.stringify({'message':$("#chatInput").val()}));

   $("#chatInput").val('');//채팅 입력창을 지움
   $("#chatInput").focus();//포커싱
}

/*
$(함수(){
});

jquery에서 문서를 다 읽은 후 호출됨
*/
$(function (){
    connect();//소켓 연결

    $("#chatInput").keypress(function(e){
        if(e.keyCode==13){//엔터를 치면
            send();//메시지 전송
        }
    });

    $("#sendBtn").click(function(){//버튼을 클릭하면
    send();//메시지 전송
    });
});
