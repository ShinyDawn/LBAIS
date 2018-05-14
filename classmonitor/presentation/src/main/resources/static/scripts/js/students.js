/**
 * Created by elva on 2018/4/25.
 */
window.onload = function () {
    changeUserInfo();
    var sequenceId = $.session.get('current_sequence_id');//默认为0
    var period = $.session.get('current_period');//默认为1，即今天
    getClassInfo(sequenceId);//在这里获得cid

//        var divider;
//        if (period === '1') {
//            divider = '今天';
//        } else if (period === '3') {
//            divider = '未来三天';
//        } else {
//            divider = '未来一周';
//        }
//        $('#time_filter').html(divider);
//
    var cid = $.session.get('current_cid');
    changeStudentList(cid, period);
};

function changeStudentList(cid,period){
    $('#students_list').append(addtitle());
    $.ajax({
        url: 'http://localhost:10002/student',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid, 'period': 30},
        success: function (obj) {
            for (var i = 0; i < obj.length; i++) {
                $('#students_list').append(createStudentsList(obj[i]))
            }
        }
    })
};

function addtitle() {

    var tr = $('<tr class="heading"></tr>');
    var td1 = $('<td class="cell-name">学号</td>');
    var td2 = $('<td class="cell-name">姓名</td>');
    var td3 = $('<td class="cell-name">出勤表现</td>');
    var td4 = $('<td class="cell-name">课堂表现</td>');
    var td5 = $('<td class="cell-name">自习表现</td>');
    var td6 = $('<td class="cell-problem hidden-phone hidden-tablet">可能存在的问题</td>');
    tr.append(td1, td2, td3, td4, td5, td6);
    return tr;
}
function createStudentsList(data) {


        // <td class="cell-problem hidden-phone hidden-tablet">
        // <c>请假较多</c>
        // <d>兴趣较低</d>
        // <e>纪律较差</e>
        // <f>退步较大</f>
        // </td>
    
    var tr = $('<tr class="task"></tr>');
    var td1 = $('<td class="cell-name"></td>');
    var td2 = $('<td class="cell-name"></td>');
    var td3 = $('<td class="cell-name"></td>');
    var td4 = $('<td class="cell-name"></td>');
    var td5 = $('<td class="cell-name"></td>');
    var td6 = $('<td class="cell-problem hidden-phone hidden-tablet">');
    var a = $('<a></a>')
    var sid = data['sid'];

    td1.html(data['sid']);
    td2.html(data['name']);
    td3.html(toPercent(data['attendanceRate']));
    td4.html(toPercent(data['generalRate']));
    td5.html(toPercent(data['deciplineRate']));
    tr.append(td1, td2, td3, td4, td5, td6);

    return tr;
}

function click_student() {

}

function toPercent(point){
    var str=Number(point*100).toFixed(0);
    str+="%";
    return str;
}
