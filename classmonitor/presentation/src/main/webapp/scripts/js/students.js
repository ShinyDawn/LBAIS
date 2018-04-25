/**
 * Created by elva on 2018/4/25.
 */
window.onload = function () {

    $('#time-tip').html(timeTip());
    $('#name').html($.session);


    // changeUserInfo()
    // var username = $.cookie('username')
    // $.ajax({
    //     url: "/index.php/sportquickinfo",
    //     type: 'get',
    //     dataType: 'json',
    //     data: {
    //         'username': username
    //     },
    //     async: false,
    //     success: function (obj) {
    //         $('#total_days').html(obj.totalDays + '天')
    //         $('#total_times').html(obj.trainTimes + '次')
    //         $('#total_time').html(obj.trainTime + '分钟')
    //         $('#total_energy').html(obj.energyCost + '千卡')
    //     }
    // })
    //
    // $.ajax({
    //     url: "/index.php/activity/get_coming",
    //     type: 'get',
    //     dataType: 'json',
    //     data: {'username': username},
    //     success: function (obj) {
    //         for (var i = 0; i < obj.length; i++) {
    //             createActivityShow('coming_activities', obj[i])
    //         }
    //     }
    // })
    //
    // var option = {
    //     color: ['#3398DB'],
    //     tooltip: {
    //         trigger: 'axis',
    //         axisPointer: {            // 坐标轴指示器，坐标轴触发有效
    //             type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    //         }
    //     },
    //     grid: {},
    //     xAxis: [
    //         {
    //             type: 'category',
    //             splitArea: {
    //                 show: false
    //             },
    //             splitLine: {
    //                 show: false
    //             },
    //             data: ['0min-20min', '20min-40min', '40min-60min', '60min-80min', '80min-100min', '100min-120min', '120min+'],
    //             axisTick: {
    //                 alignWithLabel: true
    //             }
    //         }
    //     ],
    //     yAxis: [
    //         {
    //             scale: true,
    //             splitArea: {
    //                 show: false
    //             },
    //             splitLine: {
    //                 show: false
    //             },
    //             type: 'value'
    //         }
    //     ],
    //     series: [
    //         {
    //             name: '人数',
    //             type: 'line',
    //             barWidth: '60%',
    //             areaStyle: {normal: {}},
    //             data: [1, 3, 10, 15, 17, 4, 3]
    //         }
    //     ]
    // };
    //
    //
    // var myChart = echarts.init(document.getElementById('sportTimeChart'));
    // myChart.setOption(option);
}