//socket = new WebSocket("ws://localhost:9094/starManager/websocket/张三");  
        var socket;  
        if(typeof(WebSocket) == "undefined") {  
            console.log("您的浏览器不支持WebSocket");  
        }else{  
            console.log("您的浏览器支持WebSocket");
              
            // 实现化WebSocket对象，指定要连接的服务器地址与端口 建立连接
            // socket = new
			// WebSocket("ws://localhost:9094/starManager/websocket/张三")
            socket = new WebSocket("ws://localhost:10002/websocket");
            // 打开事件
            socket.onopen = function() {
                console.log("Socket 已打开");
                // socket.send("这是来自客户端的消息" + location.href + new Date());
            };  
            
            // 获得消息事件
            socket.onmessage = function(msg) {
                console.log(msg.data);
//                alert(msg.data);
                if(msg.data.indexOf("警报")>=0){
                	var id=msg.data.substring(msg.data.indexOf(":")+1);
//                	var obj = eval('(' + msg.data + ')'); 
//                	document.cookie="vedio_path="+obj["vedio_path"];
                	layer.open({
                		  type: 2 //Page层类型
                		  ,skin: 'layui-layer-lan'
                		  ,closeBtn: 1
                		  ,area: ['1000px', '670px']
                		  ,title: '警报信息'
                		  ,shade: 0.2 //遮罩透明度
                		  ,maxmin: true //允许全屏最小化
                		  ,anim: 2 //0-6的动画形式，-1不开启
                		  ,content: 'http://localhost:10001/alarm_layer.html?alarm='+id
                		  });
                }
                if(msg.data.indexOf("close")>=0){
                	//关闭弹出层
                	var index = layer.getFrameIndex(window.name);
                	layer.close(index);
                }
                // 发现消息进入 调后台获取
//                var data=
//                alert(msg.data);
//                getCallingList();
            };  
            // 关闭事件
            socket.onclose = function() {  
                console.log("Socket已关闭");  
            };  
            // 发生了错误事件
            socket.onerror = function() {  
                alert("Socket发生了错误");  
            }  
             $(window).unload(function(){  
                  socket.close();  
                });  
        }
        
        
        function setCookie(c_name,value)
        {
        document.cookie=c_name+ "=" +value;
        }
        
        function getContent(){
        	return ""
        }