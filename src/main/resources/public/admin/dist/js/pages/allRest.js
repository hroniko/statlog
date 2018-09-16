var lastCountLog = 0; // Переменная для хранения количества логов в сейвере с предыдущего запроса
var currentCountLog = 0; // Переменная для хранения количества логов в сейвере с текущего запроса
// var host = '172.23.132.111';
var host = document.getElementById("hostid").innerText; // '10.106.2.126';
var loadFlag = 0; // флаг состояния чтения с сервера, 0 - нет чтения, 1  - чтение идет
var volume = 100; // объем одной выборки

var autoupdateFlag = 1;

// $('label').click(function(e){
//     var id = $(this).data('id');
//     if (id == 'on'){
//         autoupdateFlag = 1;
//     }
//     if (id == 'off'){
//         autoupdateFlag = 0;
//     }
// });

$(document).ready(function(){
    $("#Demo-boot-on .btn").click(function(){
        $(this).button('toggle');
        autoupdateFlag = 1;
        document.getElementById("outputOnOff").innerHTML = 'Автообновление :: Включено';
    });
    $("#Demo-boot-off .btn").click(function(){
        $(this).button('toggle');
        autoupdateFlag = 0;
        document.getElementById("outputOnOff").innerHTML = 'Автообновление :: Выключено';
    });
});

$(function () {

  'use strict';

  /* ChartJS
   * -------
   * Here we will create a few charts using ChartJS
   */

  // -----------------------
  // - MONTHLY SALES CHART -
  // -----------------------

    getInnerIp();
    drawStat();
    getCountLog();



    var timerAId = setInterval(main, 2000);

    function main() {
        getCountLog();
        if (lastCountLog < currentCountLog){
            if (autoupdateFlag == 1){
                drawStat();
            }
        }

        // lastCountLog = currentCountLog;

    }



    // Получение текущего количества логов
    function getCountLog() {
         var param = 'host=' + host;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/getCountLogsInSaver',
            type: 'GET',
             data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {
                currentCountLog = parseInt(data);
            }
        });

    }


    // Получение и вывод на страницу текущих папраметров клиента
    function getInnerIp() {
        $.ajax({
            url: '/getClientIpInfo',
            type: 'GET',
            // data: param3, //'{"host":"' + param + '"}',
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {
                // Подписываем имя пользователя / имя компьютера
                document.getElementById("outputUsername").innerHTML = '' + data.username;
                // Подписываем внутренний айпи пользователя
                document.getElementById("outputHostIp").innerHTML = '' + data.ip;
            }
        });

    }





 // Вывод всех запросов
    function drawStat() {
        if (loadFlag == 1) return;
        loadFlag = 1;

        var param = "host=" + host + '&position=' + lastCountLog  + '&volume=' + volume;
        // var param = "host=" + host; // + '&user_id=' + user_id;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/getAllEntityNext',
            // url: '/getAllEntity',
            type: 'GET',
            data: param, //'{"host":"' + param + '"}',
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {

                var items = "";
                // var maxPos = lastCountLog;
                for (var i = 0; i < data.length; i++){
                    // if (data.id > maxPos){
                    //     maxPos = data.id;
                    // }

                    // items += '<li class="item">' +
                    // '<div class="product-info">' +
                    // '<a href="javascript:void(0)" class="product-title" style="background: #002240; color: white;"><span style="color: #ffee80;">'
                    //     + data[i].id +' </span><span style="color: #ff9d00;">'
                    //     + data[i].datetime + ' </span>'
                    //     + data[i].ip + ' <span style="color: #e1efff;">'
                    //     + data[i].hostname + '</span><span style="color: #3ad900;"></span><br>'
                    //     + data[i].status + ' <span style="color: #3ad900;">'
                    //     + data[i].type + ' </span>'+
                    // '<span class="label label-warning pull-right"> [+]</span></a>' +
                    // '<span class="product-description" style="color: #e1efff;">'
                    //     + data[i].rest +
                    // '</span>' +
                    // '</div>' +
                    // '</li>';


                    // items += '<pre style="background: #002240; color: white;"><span style="color: #ffee80;">'
                    //     + '<a href="javascript:void(0)">' /////
                    //     + data[i].id +' </span><span style="color: #ff9d00;">'
                    //     + data[i].datetime + ' </span>'
                    //     + data[i].ip + ' <span style="color: #e1efff;">'
                    //     + data[i].hostname + '</span><span style="color: #3ad900;"></span><br>'
                    //     + '<span class="label label-warning pull-right"> [+]</span></a>' + /////
                    //     + data[i].status + ' <span style="color: #3ad900;">'
                    //     + data[i].type + ' </span><span style="color: #e1efff;">'
                    //     + data[i].rest + ' </span>'
                    //     + '</pre>';

                    // if (data[i].bodyIn == null || data[i].bodyIn == NaN){
                    //     data[i].bodyIn = '{}';
                    // }
                    // if (data[i].bodyOut == null || data[i].bodyOut == NaN){
                    //     data[i].bodyOut = '{}';
                    // }
                    // var bodyIn = JSON.parse(data[i].bodyIn);
                    // var bodyOut = JSON.parse(data[i].bodyOut);

                    items += '<pre style="background: #002240; color: white;"><span style="color: #ffee80;">'
                        // + '<a href="#demo">' /////
                        + '<a href="#demo'+ data[i].id +'" data-toggle="collapse" aria-expanded="false"> ' ///// + '<a href="#demo'+ data[i].id +'" data-toggle="collapse" aria-expanded="false"> ' /////
                        + data[i].id +' </span><span style="color: #ff9d00;">'
                        + data[i].datetime + ' </span>'
                        + data[i].ip + ' <span style="color: #e1efff;">'
                        + data[i].hostname + '</span><span style="color: #3ad900;"></span><br>'
                        + '<span class="label label-warning pull-right"></span></a>' + /////
                        + data[i].status + ' <span style="color: #3ad900;">'
                        + data[i].type + ' </span><span style="color: #e1efff;">'
                        + data[i].rest + ' </span>'
                        // + '  <button type="button" class="btn btn-info" data-toggle="collapse" data-target="#demo">Simple collapsible</button>'
                        +
                        '  <div id="demo'+ data[i].id +'" class="collapse">'
                        + '<span style="color: #ff9d00;"> Тело запроса: </span>'
                        + '<br>';


                    try {
                        if(data[i].bodyIn){
                            var rr = JSONTree.create(JSON.parse(data[i].bodyIn)); // John
                            if (rr.toString()){
                                items += rr;
                            }

                        }


                    } catch (e) {
                    }


                        // + '<span id="a_demo' + data[i].id +'" style="color: #8effe7;">' + ' </span>'
                        // + JSONTree.create(JSON.parse(data[i].bodyIn))
                        // + '<span style="color: #8effe7;">' + data[i].bodyIn + ' </span>'
                    items +=  + '<br>'
                        + '<span style="color: #ff9d00;"> Тело ответа: </span>'
                        + '<br>';

                    try {
                        if(data[i].bodyOut){
                            var rr = JSONTree.create(JSON.parse(data[i].bodyOut)); // John
                            items += rr;
                        }

                    } catch (e) {
                    }

                    // + '<span id="b_demo' + data[i].id +'" style="color: #c8c8ff;">' + ' </span>'
                        // + JSONTree.create(JSON.parse(data[i].bodyOut))
                        // + '<span style="color: #c8c8ff;">' + data[i].bodyOut + ' </span>' +
                    items += '</div>'

                        + '</pre>'
                        // +
                        // '<div id="demo'+ data[i].id +' class="collapse" style="background: #002240; color: white;">' +
                        //
                        // + '<span style="color: #ff9d00;"> Тело запроса: </span>'+
                        // + '<br>'
                        // + '<span style="color: #8effe7;">' + data[i].bodyIn + ' </span>'
                        // // + '<span style="color: #8effe7;">' + JSON.stringify(data[i].bodyIn, "", 4) + ' </span>'
                        // + '<br>'
                        // + '<span style="color: #ff9d00;"> Тело ответа: </span>'
                        // + '<br>'
                        // + '<span style="color: #c8c8ff;">' + data[i].bodyOut + ' </span>' +
                        // // + '<span style="color: #c8c8ff;">' + JSON.stringify(data[i].bodyOut, "", 4)  + ' </span>' +
                        //
                        // '</div>'
                        ;

                    items = items.replace('NaN', '');

                    // try{
                    //     document.getElementById('a_demo' + data[i].id).innerHTML = JSONTree.create(JSON.parse(data[i].bodyIn)); // John
                    // } catch (e) {
                    // }
                    //
                    // try{
                    //     document.getElementById('b_demo' + data[i].id).innerHTML = JSONTree.create(JSON.parse(data[i].bodyOut)); // John
                    //     // console.log(JSONTree.create(JSON.parse(data[i].bodyOut))); // John
                    // } catch (e) {
                    //     //
                    // }



                }

                document.getElementById("world-map-markers").innerHTML = items + document.getElementById("world-map-markers").innerHTML;
                lastCountLog += data.length;
                loadFlag = 0; // чтобы можно было заново грузить

            }
        });

    }



});
