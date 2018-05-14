/**
 * Created by elva on 2018/5/13.
 */
window.onload = function () {
    changeUserInfo();

    var sequenceId = $.session.get('current_sequence_id');//默认为0
    var period = $.session.get('current_period');//默认为1，即今天
    getClassInfo(sequenceId);//在这里获得cid

    var divider;
    if (period === '1') {
        divider = '今天';
    } else if (period === '3') {
        divider = '未来三天';
    } else {
        divider = '未来一周';
    }
    $('#time_filter').html(divider);

    var cid = $.session.get('current_cid');
    changeApprovalList(cid, period);
};

function changeApprovalList(cid, period) {
    $('#approval_list').append(addtitle());
    $.ajax({
        url: 'http://localhost:10002/approval',
        dataType: 'json',
        type: 'POST',
        data: {'cid': cid, 'period': period},
        success: function (obj) {
            for (var i = 0; i < obj.length; i++) {
                $('#approval_list').append(createApprovalList(obj[i]))
            }
        }
    })
}

function addtitle() {
    var tr = $('<tr class="heading"></tr>');
    var td1 = $('<td class="cell-time">日期</td>');
    var td2 = $('<td class="cell-name">姓名</td>');
    var td3 = $('<td class="cell-name">课时</td>');
    var td4 = $('<td class="cell-name">课程</td>');
    var td5 = $('<td class="cell-name">类型</td>');
    var td6 = $('<td class="cell-reason">原因</td>');
    // var td7 = $('<td class="cell-icon">修改</td>');
    var td8 = $('<td class="cell-icon">删除</td>');

    tr.append(td1, td2, td3, td4, td5, td6, td8);
    return tr;
}
function createApprovalList(data) {

    var tr = $('<tr class="task"></tr>');
    var td1 = $('<td class="cell-time"></td>');
    var td2 = $('<td class="cell-name"></td>');
    var td3 = $('<td class="cell-name"></td>');
    var td4 = $('<td class="cell-name"></td>');
    var td5 = $('<td class="cell-name"></td>');
    var td6 = $('<td class="cell-reason"></td>');
    // var td7 = $('<td class="cell-icon"><i class="icon-edit" onclick="click_modify_Approval(this)"</i></td>');
    var td8 = $('<td class="cell-icon"></td>');

    var i = $('<i class="icon-remove" onclick="click_delete_Approval(this)"></i>');
    i.attr('id', data['id']);
    td8.append(i);
    // tr2.attr('id', data['sid']);
    // td7.attr('id', data['id']);


    td1.html(data['date']);
    td2.html(data['name']);
    td3.html(data['courseTime']);
    td4.html(data['course']);
    td5.html(data['type']);
    td6.html(data['reason']);

    tr.append(td1, td2, td3, td4, td5, td6, td8);
    return tr;
}

function addApproval() {
    var cid = $.session.get('current_cid');

    var sid = $("#sid").val();
    var date = $('#date').val();
    var tids = [];
    var obj = document.getElementsByName('timeId');
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].checked) {
            tids.push(obj[i].value);
        } //如果选中，将value添加到变量s中
    }
    var type = $('#type :input:checked').val();
    var reason = $('#reason').val();

    $.ajax({
        url: "http://localhost:10002//approval/add",
        type: 'PUT',
        data: {
            'sid': sid, 'cid': cid, 'date': date, 'tids': tids.toString(), 'type': type,
            'reason': reason
        },
        // async: false,
        success: function (obj) {
            if (obj['result'] === true) {

                $('#ApprovalForLeaving').modal('hide');
                $('#success-modal').modal()

            } else {
                $('#alertInfo').html("添加失败! " + obj['reason']);
                $('#alertInfo').addClass('alert');
            }

        }
    });

}


function click_modify_Approval(id) {

}


