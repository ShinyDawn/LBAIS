$(function () {
    // personal_show()
    general_show('personal_pre');
    redar("general_analyze")
});

function redar(id) {
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
                    {text: '出勤', max: 100},
                    {text: '纪律', max: 100},
                    {text: '课堂活跃', max: 100},
                    {text: '课堂参与', max: 100}
                ],
                center: ['50%','50%'],
                radius: 90
            }
        ],
        color: ['#67e0e3', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3','#37a2da',  '#ffe080', '#fbb8a2', '#e58dc2', '#61a0a8', '#d48265'],
        series: [
            {
                type: 'radar',
                tooltip: {
                    trigger: 'item'
                },
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                data: [
                    {
                        value: [60,73,85,40],
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

function lineTableNew(id) {
    var myChart = echarts.init(document.getElementById(id));

    option = {
        title: {
            text: '课堂综合表现情况',

        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['语文', '数学', '英语', '品德', '科学', '平均']
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
            data: ['2018-04-23']
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
                name: '语文',
                type: 'line',
                data: [20, 32, 10, 34, 90]
            },
            {
                name: '数学',
                type: 'line',
                data: [22, 82, 91, 34, 90]
            },
            {
                name: '英语',
                type: 'line',
                data: [50, 23, 20, 54, 90]
            },
            {
                name: '品德',
                type: 'line',
                data: [32, 0, 0, 0, 0]
            },
            {
                name: '科学',
                type: 'line',
                data: [0, 0, 90, 0, 0]
            },
            {
                name: '平均',
                type: 'line',
                data: [20, 32, 32, 32, 32]
            }

        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}


function subjectTest(id) {
    var myChart = echarts.init(document.getElementById(id));

    option = {
        title: {
            text: '语文课表现情况'

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
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data: ['活跃度', '专注度', '综合表现']
        },
        xAxis: [
            {
                type: 'category',
                data: ['2018-04-23', '2018-04-24', '2018-04-25', '2018-04-26', '2018-04-27'],
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
        color: ['#37a2da', '#67e0e3', '#ffe080'],
        series: [
            {
                name: '活跃度',
                type: 'bar',
                data: [23.2, 25.6, 76.7, 32.6, 20.0]
            },
            {
                name: '专注度',
                type: 'bar',
                data: [9.0, 26.4, 28.7, 70.7, 48.7]
            },
            {
                name: '综合表现',
                type: 'line',
                data: [23, 33, 44, 25, 66]
            }
        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}

function lineTable(id) {
    var myChart = echarts.init(document.getElementById(id));

    option = {
        title: {
            // text: '课堂活跃度与举手次数',

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
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data: ['活跃度', '专注度', '举手次数']
        },
        xAxis: [
            {
                type: 'category',
                data: ['语文', '数学', '英语', '科学', '品德', '美术'],
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
            },
            {
                type: 'value',
                name: '次数',
                min: 0,
                max: 10,
                interval: 2,
                axisLabel: {
                    formatter: '{value} 次'
                }
            }
        ],
        color: ['#37a2da', '#67e0e3', '#ffe080'],
        series: [
            {
                name: '活跃度',
                type: 'bar',
                data: [23.2, 25.6, 76.7, 32.6, 20.0, 6.4],
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            },
            {
                name: '专注度',
                type: 'bar',
                data: [9.0, 26.4, 28.7, 70.7, 48.7, 18.8]
            },
            {
                name: '举手次数',
                type: 'line',
                yAxisIndex: 1,
                data: [2, 3, 4, 2, 6, 5]
            }
        ]
    };


    $(window).resize(function () {
        myChart.resize();
    });
    myChart.setOption(option);
}

function show_target_charts(id) {
    $('#personal_pre').css('display', 'none');
    $('#personal_yuwen').css('display', 'none');

    // $('#line1').css('display', 'none');
    // $('#line2').css('display', 'none');
    // $('#line3').css('display', 'none');
    // $('#line4').css('display', 'none');
    // $('#line5').css('display', 'none');

    $('#' + id).css('display', 'block');
}

function general_show(id){
    lineTableNew(id);
    show_target_charts(id);
}

function subject_show(id){
    subjectTest(id);
    show_target_charts(id);
}
function personal_show() {
    lineTable('personal_pre');
    // show_target_charts('line')

    // $.ajax({
    //     url: '',
    //     type: 'post',
    //     dataType: 'json',
    //     success: function (data) {
    //         if(data.result == true) {
    //             var object = data.object;
    //             date = getArray(object,'date')
    //             price = getArray(object,'price')
    //             high = getArray(object,'high')
    //             low = getArray(object,'low')
    //             console.log(price);
    //             console.log(high);
    //             console.log(low);
    //             lineTable(date,price,high,low)
    //         }else {
    //             swal("初始化失败", data.reason, "error");
    //         }
    //     },
    //     error:function(data){
    //         swal("OMG", "服务器错误,请稍后重试!", "error");
    //     }
    // });
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