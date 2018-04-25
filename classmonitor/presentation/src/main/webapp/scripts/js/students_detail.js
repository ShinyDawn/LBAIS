
$(function(){
    // personal_show()
    lineTable('personal_pre')
});


function lineTable(id){
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
            data:['活跃度','专注度','举手次数']
        },
        xAxis: [
            {
                type: 'category',
                data: ['语文','数学','英语','科学','品德','美术'],
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
        color:['#37a2da','#67e0e3','#ffe080'],
        series: [
            {
                name:'活跃度',
                type:'bar',
                data:[23.2, 25.6, 76.7,  32.6, 20.0, 6.4],
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
                name:'专注度',
                type:'bar',
                data:[9.0, 26.4, 28.7, 70.7, 48.7, 18.8]
            },
            {
                name:'举手次数',
                type:'line',
                yAxisIndex: 1,
                data:[2, 3, 4, 2, 6,5]
            }
        ]
    };


    $(window).resize(function(){
        myChart.resize();
    });
    myChart.setOption(option);
}

function show_target_charts(id) {
    $('#line').css('display','none');
    $('#line1').css('display','none');
    $('#line2').css('display','none');
    $('#line3').css('display','none');
    $('#line4').css('display','none');
    $('#line5').css('display','none');

    $('#'+id).css('display','block');
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


function timeTip(){
    var localDate = new Date();
    hour = localDate.getHours();
    if((hour>=0 && hour<=5)||(hour>=18&&hour<=23))
        return "晚上好,";
    else if (hour>=6&&hour<=11)
        return "早上好,";
    else if (hour>=12&&hour<=2)
        return "中午好,";
    else
        return "下午好,";
}