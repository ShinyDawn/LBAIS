/**
 * Created by elva on 2018/4/25.
 */
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

/**
 * 发起一次请求到url，成功失败都只提示
 * @param type "POST" or "GET"
 * @param form_id 表单id
 * @param url 请求的url
 * @param success_redirect_url 成功后跳转的url
 */
function my_ajax_redirect(type, form_id, url, success_redirect_url) {
    $.ajax({
        type: type,
        url: url,
        data: new FormData($('#' + form_id)[0]),
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.status == "SUCCESS") {
                window.location.href = success_redirect_url;
            } else {
                alert(data.reason);
            }
        },
        error: function (request) {
            alert("服务器出错啦，请联系管理员");
        }
    });
}

/**
 * 发起一次请求到url，成功失败都只提示
 * @param type "POST" or "GET"
 * @param form_id 表单id
 * @param url 请求的url
 */
function my_ajax_alert(type, form_id, url) {
    $.ajax({
        type: type,
        url: url,
        data: new FormData($('#' + form_id)[0]),
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            alert(data.reason);
        },
        error: function (request) {
            alert("服务器出错啦，请联系管理员");
        }
    });
}

/**
 * 发起一次请求到url，使用handle_result处理结果
 * @param type "POST"/"GET"
 * @param form_id 表单id，input要有对应的name属性和后台controller对应
 * @param url 发送的目标url
 * @param handle_result 处理结果的函数对象，data作为参数
 */
function my_ajax(type, form_id, url, handle_result) {
    $.ajax({
        type: type,
        url: url,
        data: new FormData($('#' + form_id)[0]),
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            handle_result(data);
        },
        error: function (request) {
            alert("服务器出错啦，请联系管理员");
        }
    });
}

function my_ajax_a(url, success_redirect_url) {
    $.ajax({
        type: 'GET',
        url: url,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.status == "SUCCESS") {
                window.location.href = success_redirect_url;
            } else {
                alert(data.reason);
            }
        },
        error: function (request) {
            alert("服务器出错啦，请联系管理员");
        }
    });
}

function changeUserInfo() {
    // $.ajax({
    //     url: "/index.php/user/",
    //     type: 'get',
    //     dataType: 'json',
    //     data: {'username': $.cookie('username')},
    //     success: function (obj) {
    //         $('.usernickname').html(obj.nickname)
    //         $('.user_image_nav').attr('src', '/img/user/' + obj.username + '.jpg')
    //     }
    // })
}