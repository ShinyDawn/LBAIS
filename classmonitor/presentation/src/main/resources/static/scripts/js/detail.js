var sid;
// 默认为1，即今天


window.onload = function () {
    changeUserInfo();
    sid = getParameters('sid');
    var period = $.session.get('current_period');//默认为1，即今天

    if (period === null) {
        period = 1;
    }
    // console.log(period);

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

    changeName();
    getAnalysis(period);
    getAddendance(period);
    getLessonALL();
    getDicipline(period);

};

function getAnalysis(period) {
    // var sid = getParameters('sid');
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }

    $.ajax({
            url: 'http://localhost:10002/studentInfo',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                $.session.set('sid', sid);
                var data = [];
                data.push(obj['attendanceRate']);
                data.push(obj['deciplineRate']);
                data.push(obj['generalRate'])
                // data.push(obj['livenessRate']);
                // data.push(obj['concentrationRate']);
                redar(data, "general_analyze");
                var problem = (obj['problem']);

                for (var i = 0; i < problem.length; i++) {
                    $('#problem_analyze').append(creatProblems(problem[i]))
                }
            }
        }
    )
}

function creatProblems(data) {
    var d = $('<div class="row-fluid"></div>');
    var d1 = $('<div class="module-option clearfix"></div>');
    var s = data['title'] + ":" + data['detail'];
    d1.html(s);
    d.append(d1);
    return d;
}

function changeName() {
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }

    $.ajax({
            url: 'http://localhost:10002//student/get',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid},
            success: function (obj) {
                $.session.set('sid', sid);
                var name = obj['name'];
                $('#current_sname').html(name);
            }
        }
    )
}

function getAddendance(period) {
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }

    // $.ajax({
    //         url: 'http://localhost:10002/student/attendanceRate',
    //         dataType: 'json',
    //         type: 'get',
    //         data: {'sid': sid, 'cid': cid, 'period': period},
    //         success: function (obj) {
    //             $('#attendance_rate').html(toPercent(obj));
    //         }
    //     }
    // );

    $.ajax({
            url: 'http://localhost:10002/student/attendanceRate',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                $('#attendance_percent').html(toPercent(obj));
            }
        }
    );

    $('#attencen_list').append(addtitle_attendance());
    $.ajax({
            url: 'http://localhost:10002//student/attendance',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                for (var i = 0; i < obj.length; i++) {
                    $('#attencen_list').append(creatAttendanceList(obj[i]))
                }
            }
        }
    )
}

function creatAttendanceList(data) {

    var tr = $('<tr class="task"></tr>');
    var td1 = $('<td class="cell-time"></td>');
    var td2 = $('<td class="cell-time"></td>');
    var td3 = $('<td class="cell-status hidden-phone hidden-tablet"></td>');

    td1.html(data['date']);
    var s = "第" + data['tid'] + "节" + data['subject'] + '课' + data['behavior'];
    td2.html(s);
    td3.html(data['status']);
    tr.append(td1, td2, td3);
    return tr;
}

function addtitle_attendance() {
    var tr = $('<tr class="heading"></tr>');
    var td1 = $('<td class="cell-time">日期</td>');
    var td2 = $('<td class="cell-time">详情</td>');
    var td3 = $('<td class="cell-status hidden-phone hidden-tablet">状态</td>');
    tr.append(td1, td2, td3);
    return tr;
}

function getLessonALL() {
    var period = $.session.get('current_period');
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }
    $('#subject_filter').html('全部');

    $.ajax({
            url: 'http://localhost:10002/student/livenessPercent',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                $('#lesson_percent').html(toPercent(obj['generalRate']));
            }
        }
    );
    console.log(period);

    if (period === '1' || period === '7' || period === '3') {
        $.ajax({
                url: 'http://localhost:10002/student/lesson',
                dataType: 'json',
                type: 'get',
                data: {'sid': sid, 'cid': cid, 'period': period},
                success: function (obj) {
                    lineTableNew(obj, period);
                }
            }
        );
    } else {
        $.ajax({
                url: 'http://localhost:10002/student/lessonweek',
                dataType: 'json',
                type: 'get',
                data: {'sid': sid, 'cid': cid, 'period': period},
                success: function (obj) {
                    lineTableAll(obj, period);
                }
            }
        );
    }
    ;


}
function getLesson(subject) {
    var period = $.session.get('current_period');
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }
    $('#subject_filter').html(subject);

    $.ajax({
            url: 'http://localhost:10002/student/livenessPercent/subject',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period, 'subject': subject},
            success: function (obj) {
                $('#lesson_percent').html(toPercent(obj['generalRate']));
            }
        }
    );

    $.ajax({
            url: 'http://localhost:10002/student/lesson/subject',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period, 'subject': subject},
            success: function (obj) {
                lineTableSubject(obj, period);
            }
        }
    )
}

function getDicipline(period) {
    var cid = $.session.get('current_cid');
    while (cid === null) {
        cid = $.session.get('current_cid');
    }

    $.ajax({
            url: 'http://localhost:10002/student/disciplinePercent',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                $('#dicipline_percent').html(toPercent(obj));
            }
        }
    );

    $('#dicipline_list').append(addtitle_dicipline());
    $.ajax({
            url: 'http://localhost:10002/student/discipline',
            dataType: 'json',
            type: 'get',
            data: {'sid': sid, 'cid': cid, 'period': period},
            success: function (obj) {
                for (var i = 0; i < obj.length; i++) {
                    $('#dicipline_list').append(creatDiciplineList(obj[i]))
                }
            }
        }
    )
}


function creatDiciplineList(data) {

    var tr = $('<tr class="task"></tr>');
    var td1 = $('<td class="cell-time"></td>');
    var td2 = $('<td class="cell-time"></td>');
    var td3 = $('<td class="cell-problem"></td>');
    td1.html(data['date']);
    td2.html(data['discover_time']);
    var time = data['total_time'] * 1.0 / 60;
    var s = "检测出" + data['action'] + time + '分钟';
    td3.html(s);
    tr.append(td1, td2, td3);
    return tr;
}

function addtitle_dicipline() {
    var tr = $('<tr class="heading"></tr>');
    var td1 = $('<td class="cell-time">日期</td>');
    var td2 = $('<td class="cell-time">发现时间</td>');
    var td3 = $('<td class="cell-problem">详情</td>');
    tr.append(td1, td2, td3);
    return tr;
}

function getParameters(name) {
    var href = window.location.href.replace(/[#]/, "");
    var args = href.split('?')
    if (args.length < 2) {
        return null;
    }
    args = args[1].split("&")
    for (var i = 0; i < args.length; i++) {
        var arg = args[i].split("=")
        if (arg[0] == name)
            return decodeURI(arg[1])
    }
}

function redar(data, id) {
    var myChart = echarts.init(document.getElementById(id));

    option = {
        title: {
            // text: '雷达图'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            x: 'center'
            // data:['某软件']
        },
        radar: [
            {
                indicator: [
                    {text: '出勤', max: 1},
                    {text: '自习', max: 1},
                    {text: '课堂', max: 1}
                    // {text: '课堂专注', max: 1}
                ],
                center: ['50%', '50%'],
                radius: 90
            }
        ],
        color: ['#67e0e3', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3', '#37a2da', '#ffe080', '#fbb8a2', '#e58dc2', '#61a0a8', '#d48265'],
        series: [
            {
                type: 'radar',
                tooltip: {
                    trigger: 'item'
                },
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                data: [
                    {
                        value: data,
                        name: '综合表现'
                    }
                ]
            }
        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}

function lineTableAll(obj, flag) {
    var myChart = echarts.init(document.getElementById('lesson_show'));
    var subject = getArray(obj, 'subject');
    var data = getArray(obj, 'data');
    var date = getArray(data[0], 'date');
    // var data = getArray(obj, 'generalRate');
    // console.log(data);
    var data0 = arrayToFix(getArray(data[0], 'generalRate'));
    var data1 = arrayToFix(getArray(data[1], 'generalRate'));
    var data2 = arrayToFix(getArray(data[2], 'generalRate'));
    var data3 = arrayToFix(getArray(data[3], 'generalRate'));
    var data4 = arrayToFix(getArray(data[4], 'generalRate'));
    var avg = arrayAverage(data0, data1, data2, data3, data4);

    var type = ['line', 'bar'];
    var t = type[0];
    subject.push('平均');

    option = {
        title: {
            text: '课堂综合表现情况'

        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: subject
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: true},
                magicType: {show: true, type: ['line', 'bar']},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: date
        },
        yAxis: {
            type: 'value',
            name: '优于？%的同学',
            min: 0,
            max: 100,
            interval: 20,
            axisLabel: {
                formatter: '{value} %'
            }
        },
        color: ['#37a2da', '#67e0e3', '#ffe080', '#fbb8a2', '#e58dc2', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
        series: [
            {
                name: subject[0],
                type: t,
                data: data0
            },
            {
                name: subject[1],
                type: t,
                data: data1
            },
            {
                name: subject[2],
                type: t,
                data: data2
            },
            {
                name: subject[3],
                type: t,
                data: data3
            },
            {
                name: subject[4],
                type: t,
                data:data4
            },
            {
                name: '平均',
                type: t,
                data: avg
            }

        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}


function lineTableNew(obj, flag) {
    var myChart = echarts.init(document.getElementById('lesson_show'));
    var subject = getArray(obj, 'subject');
    // var data = getArray(obj, 'data');
    // var date = getArray(data[0], 'date');
    var data = arrayToFix(getArray(obj, 'generalRate'));
    console.log(data);
    // var data1 = arrayToFix(getArray(data[1], 'generalRate'));
    // var data2 = arrayToFix(getArray(data[2], 'generalRate'));
    // var data3 = arrayToFix(getArray(data[3], 'generalRate'));
    // var data4 = arrayToFix(getArray(data[4], 'generalRate'));
    // var avg = arrayAverage(data0, data1, data2, data3, data4);

    var type = ['line', 'bar'];
    var t = type[1];
    // subject.push('平均');



    option = {
            title: {
                text: '课堂综合表现情况'

            },
        tooltip : {
            trigger : 'axis'
        },
        legend : {
            data : subject
        },
        grid : {
            left : '3%',
            right : '4%',
            bottom : '3%',
            containLabel : true
        },
        xAxis : {
            type : 'category',
            data : subject,
            axisTick : {
                alignWithLabel : true
            }
        },

        yAxis: {
            type: 'value',
            name: '优于？%的同学',
            min: 0,
            max: 100,
            interval: 20,
            axisLabel: {
                formatter: '{value} %'
            }
        },
        //
        // label : {
        //     normal : {
        //         show : true,
        //         position : 'top',
        //         textStyle : {
        //             color : 'grey'
        //         }
        //     }
        // },
        series : [ {
            type : 'bar',
            data : data,
            barWidth : '50%',
            itemStyle : {
                normal : {
                    color : function(params) {
                        var colorList = ['#37a2da', '#67e0e3', '#ffe080', '#fbb8a2', '#e58dc2', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
                        return colorList[params.dataIndex];
                    },
                    lineStyle : {
                        color : '#37a2da'
                    }
                }
            },
        } ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}

function lineTableSubject(data, flag) {
    var myChart = echarts.init(document.getElementById('lesson_show'));
    var data0 = [];
    var data1 = [];
    var data2 = [];
    for (var i = 0; i < data.length; i++) {
        var obj = data[i];
        data0.push(obj['livenessRate']);
        data1.push(obj['concentrationRate']);
        data2.push(obj['generalRate'])
    }
    option = {
        title: {
            text: '学科表现情况'

        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        // toolbox: {
        //     feature: {
        //         dataView: {show: true, readOnly: false},
        //         magicType: {show: true, type: ['line', 'bar']},
        //         restore: {show: true},
        //         saveAsImage: {show: true}
        //     }
        // },
        legend: {
            data: ['活跃度', '专注度', '综合表现']
        },
        xAxis: [
            {
                type: 'category',
                data: getArray(data, 'date'),
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '优于',
                min: 0,
                max: 100,
                interval: 20,
                axisLabel: {
                    formatter: '{value} %'
                }
            }
        ],
        color:  ['#37a2da','#91c7ae','#ffe080', '#67e0e3',  '#fbb8a2', '#e58dc2', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
        series: [
            {
                name: '活跃度',
                type: 'bar',
                data: arrayToFix(data0)
            },
            {
                name: '专注度',
                type: 'bar',
                data: arrayToFix(data1)
            },
            {
                name: '综合表现',
                type: 'line',
                data: arrayToFix(data2)
            }
        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}


function toPercent(point) {
    var str = Number(point * 100).toFixed(0);
    str += "%";
    return str;
}

function arrayToFix(data) {
    for (var i = 0; i < data.length; i++) {
        data[i] = Number(data[i] * 100).toFixed(0)
    }
    return data;
}

function arrayAverage(data, b, c, d, e) {
    var result = [];
    for (var i = 0; i < data.length; i++) {
        var sum = Number(data[i]) + Number(b[i]) + Number(c[i]) + Number(d[i]) + Number(e[i]);
        var avg = Number(sum / 5.0);
        result.push(avg);
    }
    return result;
}

function changeTimeBack(period) {
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
    window.location.reload();
}
