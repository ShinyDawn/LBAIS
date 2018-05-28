$("#plus").click(function(){
	window.alarm_count=window.alarm_count+1;
//	alert(window.alarm_count+"!!!!!!!!!!!");
	var html=
	'<div>'+
	'<div>'+
	'<span id="multistu'+window.alarm_count+'" value=""  class="choosespan" background-image="" onclick="choose_stu(this)">'+
		'学生'+
	'</span>'+
	'<select id="behavior'+window.alarm_count+'" style="display:inline-block;margin:10px 10px 0px 0px">'+
		'<option value="" style="display:none;">请选择行为</option>'+
		'<option value="1">迟到</option>'+
		'<option value="2">游荡</option>'+
		'<option value="3">睡觉</option>'+
		'<option value="4">缺席</option>'+
		'<option value="5">早退</option>'+
	'</select>'+
	'</div>'+
	'<div id="choose_div'+window.alarm_count+'" style="border:1px solid #000; width:500px;display:none">'+
	'<ul class="dowebok">';
	//var sid=new Array(1,2,3,4,5,6,7,8);
	//var name=new Array('张小梅','蔡小丁','何小月','何晓西','朱晓楠','王晓山','宋晓军','周晓宁');
	var sid=getId();
	var name=getName();
	/* <![CDATA[ */
	for(var i=0;i<sid.length;i++){
		html=html+"<li><input id='student"+window.alarm_count*100+sid[i]+"' type='checkbox' name='checkbox"+window.alarm_count+"' data-labelauty='"+name[i]+"' value='"+name[i]+"'></input></li>";
	}
	 /* ]]> */
	html=html+
	'</ul>'+
	'<div style = "text-align:right; margin:15px 0px 0px 0px">'+
		'<span id="confirm'+window.alarm_count+'" class="btn btn-primary" style="margin:0px 10px 10px;" onclick="stu_confirm(this)">&nbsp;&nbsp;确定&nbsp;&nbsp;</span>'+
	'</div></div></div>';
	$("#select_team").append(html);
	$('input[name="checkbox'+window.alarm_count+'"]').labelauty();
	
});


//弹出弹窗
function choose_stu(button){
	//button: multistu(n)
	var did=button.id.substring(8);	
//	alert(document.getElementById("choose_div"+did).style.display);
	document.getElementById("choose_div"+did).style.display="";
//	alert(document.getElementById("choose_div"+did).style.display);
}

function stu_confirm(button){
	//button: confirm(n)
	var did=button.id.substring(7);
	var test = $("input[name='checkbox"+did+"']:checked");
    var checkBoxValue = ""; 
    var stu_value="";
    /* <![CDATA[ */
	for(var i=0;i<test.length;i++){
		checkBoxValue=checkBoxValue+test[i].getAttribute("value")+",";
		stu_value=stu_value+test[i].getAttribute("id")+",";
		}
	/* ]]> */
	$("#multistu"+did).val(stu_value);
	if(checkBoxValue.length>14) checkBoxValue=checkBoxValue.substring(0,14)+"......";
    checkBoxValue = checkBoxValue.substring(0,checkBoxValue.length-1); 
    $("#multistu"+did).text(checkBoxValue);
    document.getElementById("choose_div"+did).style.display="none";
}
function getId(){
	var cid=2;
	var sid=[];
	/* <![CDATA[ */
	var max=16;
	for(var i=1;i<=max;i++){
		$.ajax({
	        url: 'http://localhost:10002/student/get',
	        //dataType: 'String',
	        type: 'POST',
	        data: {'cid': cid, 'sid': i},
	        async: false,
	        success: function (obj) {
	        		sid.push(obj['sid']);
	        	}
		});
		
		}
	/* ]]> */
//	alert(sid);
	return sid;
}
function getName(){
	var cid=2;
	var name=[];
	/* <![CDATA[ */
	var max=16;
	for(var i=1;i<=max;i++){
		$.ajax({
	        url: 'http://localhost:10002/student/get',
	        //dataType: 'String',
	        type: 'POST',
	        data: {'cid': cid, 'sid': i},
	        async: false,
	        success: function (obj) {
	        		name.push(obj['name']);
	        }
		});
		
		}
	/* ]]> */
//	alert(name);
	return name;
}