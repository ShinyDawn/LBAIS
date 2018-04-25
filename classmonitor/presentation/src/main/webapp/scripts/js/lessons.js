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

$(function(){
    lesson_show();
});

function lesson_show() {


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
    //             lineTable(date,price,high,low,id)
    //         }else {
    //             swal("初始化失败", data.reason, "error");
    //         }
    //     },
    //     error:function(data){
    //         swal("OMG", "服务器错误,请稍后重试!", "error");
    //     }
    // });
    lineTable('lesson_pre');
}


function lineTable(id){
    var myChart = echarts.init(document.getElementById(id));

    option = {
        title: {
            // text: '折线图堆叠'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['语文','数学','英语','品德','科学','平均']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['周一','周二','周三','周四','周五','周六','周日']
        },
        yAxis: {
            type: 'value'
        },
        color:['#37a2da','#67e0e3','#ffe080','#fbb8a2','#e58dc2', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'],
        series: [
            {
                name:'语文',
                type:'line',
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'数学',
                type:'line',
                data:[220, 182, 191, 234, 290, 330, 310]
            },
            {
                name:'英语',
                type:'line',
                data:[150, 232, 201, 154, 190, 330, 410]
            },
            {
                name:'品德',
                type:'line',
                data:[320, 332, 301, 334, 390, 330, 320]
            },
            {
                name:'科学',
                type:'line',
                data:[820, 932, 901, 934, 1290, 1330, 1320]
            },
            {
                name:'平均',
                type:'line',
                data:[320, 320, 320, 320, 320, 320, 320]
            }

        ]
    };


    $(window).resize(function(){
        myChart.resize();
    });
    myChart.setOption(option);
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