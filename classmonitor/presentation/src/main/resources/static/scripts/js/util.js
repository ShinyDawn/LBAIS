/**
 * Created by elva on 2018/4/25.
 */

var id;
var period;
var cnames=[];
var cids =[];

/**
 * 按照传入的键值，提取出datas数组里的对应值
 * 如datas=[{'date':'1970-01-01','value'=1},{'date':'1970-01-02','value'=2}]
 * 调用getArray(datas,'date')将返回datas中的date域构成的数组['1970-01-01','1970-01-02']
 * @param datas
 * @returns {Array}
 */
function getArray(datas) {
    var array = [];
    if (arguments.length < 2) {
        alert("wrong size");
        return null;
    }
    for (var i = 0; i < datas.length; i++) {
        var tem;
        if (arguments.length >= 3) {
            tem = [];
            for (var j = 1; j < arguments.length; j++) {
                tem.push(datas[i][arguments[j]]);
            }
        } else {
            tem = datas[i][arguments[1]];
        }
        array[i] = tem;
    }
    return array;
}


function timeTip() {
    var localDate = new Date();
    hour = localDate.getHours();
    if ((hour >= 0 && hour <= 5) || (hour >= 18 && hour <= 23))
        return "晚上好,";
    else if (hour >= 6 && hour <= 11)
        return "早上好,";
    else if (hour >= 12 && hour <= 2)
        return "中午好,";
    else
        return "下午好,";
}

function toPercent(point){
    var str=Number(point*100).toFixed(0);
    str+="%";
    return str;
}


function changeUserInfo() {
    var name = $.session.get('name');
    $('#teacher_name').html(name);
    $('#time_tip').html(timeTip());
}

function getClassInfo(sequenceId) {
    var uid = $.session.get('uid');
    $.ajax({
        url: "http://localhost:10002/classes",
        type: 'GET',
        dataType: 'json',
        data: {'uid': uid},
        success: function (obj) {
            for (var i = 0; i < obj.length; i++){
                var classInfo = obj[i];
                cnames.push(classInfo.cname);
                cids.push(classInfo.id);
                var li = $('<li><a href="#"></a></li>');
                var a = $('<a onclick="changeClass(' + i + ')"></a>');
                li.append(a);
                a.html(cnames[i]);
                $('#class_list').append(li);
            }

            $.session.set('current_sequence_id',sequenceId);
            $.session.set('current_cid',cids[sequenceId]);
            $('#current_cname').html(cnames[sequenceId]);
        }
    });
}


function changeClass(sequenceId) {
    $.session.set('current_cid',cids[sequenceId]);
    $.session.set('current_sequence_id',sequenceId);
    $('#current_cname').html(cnames[sequenceId]);
    // changeApprovalList(cids[sequenceId], 1);
    window.location.reload();
}

function changeTime(period){
    $.session.set('current_period',period);
    var divider;
    if (period === 1) {
        divider = '今天';
    } else if (period === 3) {
        divider = '未来三天';
    } else {
        divider = '未来一周';
    }
    $('#time_filter').html(divider);
    window.location.reload();
}



