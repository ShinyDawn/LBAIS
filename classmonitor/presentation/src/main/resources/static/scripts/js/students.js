/**
 * Created by elva on 2018/4/25.
 */
window.onload = function () {
    changeUserInfo();
    var sequenceId = $.session.get('current_sequence_id');//默认为0
    var period = $.session.get('current_period');//默认为1，即今天
    getClassInfo(sequenceId);//在这里获得cid

    if (period === null) {
        period = 1;
    }

    var divider;
    if (period === '1') {
        divider = '今天';
    } else if (period === '3') {
        divider = '最近三天';
    } else if (period === '7') {
        divider = '最近一周';
    } else if (period === '30') {
        divider = '最近一月';
    } else {
        divider = '本学期';
    }
    $('#time_filter').html(divider);
    $('#time_pro').html(divider);
    $('#time_pro1').html(divider);

    var cid = $.session.get('current_cid');

    while (cid === null) {
        cid = $.session.get('current_cid');
    }
    changeStudentList(cid, period);
};

function changeStudentList(cid, period) {
    $('#students_list').append(addtitle());
    $.ajax({
        url: 'http://218.94.159.98:10002/students',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid, 'period': period},
        success: function (obj) {
            createProgress(obj);
            for (var i = 0; i < obj.length; i++) {
                $('#students_list').append(createStudentsList(obj[i]))
            }

        }
    })
};

function createProgress(data) {
    var stuNum = 0;
    var proNum = 0;
    var name = [];
    var fallNum = 0;
    var fallName=[];
    for (var i = 0; i < data.length; i++) {
        stuNum++;
        var obj = data[i];
        var problemList = obj['problem'];
        for (var j = 0; j < problemList.length; j++) {
            var problem = problemList[j];
            if (problem['isProgress'] === 1) {
                proNum++;
                name.push(obj['name']);
                break;
            }
            if (problem['isProgress'] === -1) {
                fallNum++;
                fallName.push(obj['name']);
                break;
            }
        }

    }

    $('#pro_Num').html(proNum);
    $('#pro_Num1').html(fallNum);
    if (stuNum===0){
        $('#pro_percent').html(0);
        var d = $('<div class="bar bar-info" style="width: 0%;"></div>');
        $('#pro_cent').append(d);
        $('#pro_percent1').html(0);
        var d1 = $('<div class="bar bar-info" style="width: 0%;"></div>');
        $('#pro_cent1').append(d1);

    } else{
        var pro_percent = toPercent(Number(proNum) *1.0/Number(stuNum));
        $('#pro_percent').html(pro_percent);
        var d2 = $('<div class="bar bar-info" style="width: '+pro_percent+'"></div>');
        $('#pro_cent').append(d2);
        var pro_percent1 = toPercent(Number(fallNum) *1.0/Number(stuNum));
        $('#pro_percent1').html(pro_percent1);
        var d3 = $('<div class="bar bar-info" style="width: '+pro_percent1+'"></div>');
        $('#pro_cent1').append(d3);
    }
    $('#pro_name').html(name.toString());
    $('#pro_name1').html(fallName.toString());


}
function addtitle() {

    var tr = $('<tr class="heading"></tr>');
    var td1 = $('<td class="cell-name">学号</td>');
    var td2 = $('<td class="cell-name">姓名</td>');
    var td3 = $('<td class="cell-name">出勤表现</td>');
    var td4 = $('<td class="cell-name">课堂表现</td>');
    var td5 = $('<td class="cell-name">自习表现</td>');
    var td6 = $('<td class="cell-problem hidden-phone hidden-tablet">综合分析</td>');
    tr.append(td1, td2, td3, td4, td5, td6);
    return tr;
}
function createStudentsList(data) {

    var sid = data['sid'];
    var tr = $('<tr class="task" onclick="detail_show(' + sid + ')"></tr>');
    var td1 = $('<td class="cell-name"></td>');
    var td2 = $('<td class="cell-name"></td>');
    var td3 = $('<td class="cell-name"></td>');
    var td4 = $('<td class="cell-name"></td>');
    var td5 = $('<td class="cell-name"></td>');
    var td6 = $('<td class="cell-problem hidden-phone hidden-tablet">');


    td1.html(data['sid']);
    td2.html(data['name']);
    td3.html(toPercent(data['attendanceRate']));
    td4.html(toPercent(data['generalRate']));
    td5.html(toPercent(data['deciplineRate']));
    var problemList = data['problem'];
    for (var i = 0; i < problemList.length; i++) {
        var problem = problemList[i];
        if (problem['title'] === "出勤较少") {
            var c = $('<c style="margin-left: 4px">出勤较少</c>&nbsp;');
            td6.append(c);
        }
        if (problem['title'] === "课堂低迷") {
            var d = $('<c style="margin-left: 4px">偏科</c>&nbsp;');
            td6.append(d);

        }
        if (problem['title'] === "纪律较差") {
            var e = $('<c style="margin-left: 4px">纪律较差</c>&nbsp;');
            td6.append(e);

        }
        if (problem['title'] === "退步较大") {
            var f = $('<c style="margin-left: 4px">退步</c>&nbsp;')
            td6.append(f);
        }
        if (problem['title'] === "进步较大") {
            var g = $('<c style="margin-left: 4px">进步</c>&nbsp;')
            td6.append(g);
        }
        // console.log(problem['title']);
    }
    tr.append(td1, td2, td3, td4, td5, td6);

    return tr;
}
function detail_show(sid) {
    window.location.href = "http://218.94.159.98:10001/student?sid=" + sid;
}


function toPercent(point) {
    var str = Number(point * 100).toFixed(0);
    str += "%";
    return str;
}

function changeTimeStu(period) {
    $.session.set('current_period', period);
    var divider;
    if (period === 1) {
        divider = '今天';
    } else if (period === 3) {
        divider = '最近三天';
    } else if (period === 7) {
        divider = '最近一周';
    } else if (period === 30) {
        divider = '最近一月';
    } else {
        divider = '本学期';
    }
    $('#time_filter').html(divider);
    $('#time_pro').html(divider);
    $('#time_pro1').html(divider);
    window.location.reload();
}
