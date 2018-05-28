function AlarmList(cid) {
	//add title
	var period = $.session.get('chooseAlarm');
	if(period==null){
		period="最近一周";
		$.session.set('chooseAlarm',period);
	}
	$('#alarmtype').html(period);
	var title='<tr class="heading">'+
        '<td class="cell-time align-center">日期</td>'+
        '<td class="cell-time align-center">时间</td>'+
        '<td class="cell-title">详情</td>'+
        '<td class="cell-status hidden-phone hidden-tablet">状态</td>'+
        '</tr>';
    $('#alarmlist').html("");
    $('#alarmlist').append(title);
    $.ajax({
        url: 'http://localhost:10002/study/getalarm',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid, 'type': period},
        success: function (obj) {
            for (var i = 0; i < obj.length; i++) {
                $('#alarmlist').append(createAlarmUnit(obj[i]))
            }

        }
    })
};

function newAlarmList(period){
	$.session.set('chooseAlarm', period);
	AlarmList(window.cid);
}

function createAlarmUnit(data) {

	var isHandle=data['isHandle'];
	var tr="";
	if(isHandle==3){
		tr='<tr class="task resolved">';
	}else{
		tr='<tr class="task">';
	}
	var td1='<td class="cell-time align-right">'+data['date']+'</td>';
	var td2='<td class="cell-time align-center">'+data['time']+'</td>';
	var td3='<td class="cell-title"><div>'+data['destribute']+'</div></td>';
	var td4='';
	if(isHandle==0){
		td4='<td class="cell-status hidden-phone hidden-tablet" ><a class="due" onclick="handle_alarm('+data['id']+')">未审核</a></td>'+
		'</tr>'
	}else if(isHandle==1){
		td4='<td class="cell-status hidden-phone hidden-tablet" ><a class="due" onclick="handle_alarm('+data['id']+')">未处理</a></td>'+
		'</tr>'
	}else{
		td4='<td class="cell-status hidden-phone hidden-tablet" ><a class="due">已处理</a></td>'+
		'</tr>'
	}
    tr=tr+td1+td2+td3+td4;
    return tr;
}

function PatternList(cid) {
	var period = $.session.get('choosePattern');
	if(period==null){
		period="最近一周";
		$.session.set('choosePattern',period);
	}
	$('#patterntype').html(period);
	var title='<tr class="heading">'+
        '<td class="cell-time align-center">日期</td>'+
        '<td class="cell-title">描述</td>'+
        '<td class="cell-status hidden-phone hidden-tablet">状态</td>'+
        '</tr>';
    $('#patternlist').html("");
    $('#patternlist').append(title);
    $.ajax({
        url: 'http://localhost:10002/study/getpattern',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid, 'type': period},
        success: function (obj) {
            for (var i = 0; i < obj.length; i++) {
                $('#patternlist').append(createPatternUnit(obj[i]));
            }

        }
    })
};

function newPatternList(period){
	$.session.set('choosePattern', period);
	PatternList(window.cid);
}

function createPatternUnit(data) {

	var isHandle=data['isHandle'];
	var tr="";
	if(isHandle==1){
		tr='<tr class="task resolved">';
	}else{
		tr='<tr class="task">';
	}
	var td1='<td class="cell-time align-right">'+data['date']+'</td>';
	var td3='<td class="cell-title"><div>'+data['destribute']+'</div></td>';
	var td4='';
	if(isHandle==0){
		td4='<td class="cell-status hidden-phone hidden-tablet" ><a class="due" onclick="handle_pattern('+data['id']+')">未处理</a></td>'+
		'</tr>'
	}else {
		td4='<td class="cell-status hidden-phone hidden-tablet" ><a class="due" >已处理</a></td>'+
		'</tr>'
	}
    tr=tr+td1+td3+td4;
    return tr;
}

function getNum(cid){
	$.ajax({
        url: 'http://localhost:10002/study/getNum',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid},
        success: function (obj) {
        	$('#numA_handle').html("待处理历史警报："+obj[0]);
        	$('#numA_confirm').html("待核实历史警报："+obj[1]);
        	$('#numP_handle').html("待处理模式行为："+obj[2]);
        }
    })
}