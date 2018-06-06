/*<![CDATA[*/
function add() {
	var num = document.getElementById("t_body").children.length;
	var html = '<tr><td id="-1" ondblclick="edit(this,' + 0 + ')" onchange="save(this,'
			+ 0 + ')"> </td>';
	for (var i = 1; i <= 7; i++) {
		html += '<td class="block" ondblclick="edit(this,' + i
				+ ')" onchange="save(this,' + i + ')"></td>';
	}
	html += '<td><span class="glyphicon glyphicon-trash" aria-hidden="true" onclick="del(this)"></span></td></tr>';
	document.getElementById("t_body").innerHTML += html;

}

function edit(div, day) {
	if (day != 0) {
		var str = $(div).parent().children().get(0).innerText;
		if (str == "" || str == " ")
			toastr.warning('请先输入课堂起止时间');
		else
			div.innerHTML = '<input class="form-control aaa" placeholder="输入课程" autofocus="true"/>';
	} else {
		div.innerHTML = '<input class="form-control aaa" onkeypress="keyPress(this)" '
				+ 'placeholder="HH:mm-HH:mm" autofocus="true"/>';
	}
}

function jump(div) {
	var course = div.innerText;
}

function save(div, day) {
	var str = div.children[0].value;
	var tid = $(div).parent().children().get(0).id;
	var cid = $.session.get('cid');

	if (day == 0) {
		if (str.length < 11 && str.length > 0) {
			toastr.error('课堂起止时间输入不完整');
			return;
		}
		$.ajax({
			type : "POST",
			url : "http://localhost:10002/curriculum/at",
			data : {
				id : tid,
				cid : cid,
				time : str
			},
			crossDomain : true,
			success : function(data) {
				$(div).parent().children().get(0).id=data;
				toastr.success('保存成功');
			}
		});
	} else {

		$.ajax({
			type : "POST",
			url : "http://localhost:10002/curriculum/ac",
			data : {
				cid : cid,
				tid : tid,
				day : day,
				course : str
			},
			crossDomain : true,
			success : function(data) {
				toastr.success('保存成功');
			}
		});
	}

	div.innerHTML = '';
	div.innerText = str;
}

function del(div) {
	var line = $(div).parent().parent();
	var tid = line.children().get(0).id;
	var cid = $.session.get('cid');
	var time = line.children().get(0).innerText;
	
	line.remove();
	if (time == "" || time == " ")
		toastr.success('删除成功');
	else {
		$.ajax({
			type : "POST",
			url : "http://localhost:10002/curriculum/delete",
			data : {
				cid : cid,
				tid : tid
			},
			crossDomain : true,
			success : function(data) {
				toastr.success('删除成功');
			}
		});
	}
}
/* ]]> */