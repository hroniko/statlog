var lastCountLog = 0; // Переменная для хранения количества логов в сейвере с предыдущего запроса
var currentCountLog = 0; // Переменная для хранения количества логов в сейвере с текущего запроса
// var host = '172.23.132.111';
var host = document.getElementById("hostid").innerText; // '10.106.2.126';
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

$(document).ready(function() {
    $("#search-btn").click(function(){
        //alert("Hello World!")
        var findWord = document.getElementById("searchText").value;
        alert(findWord);
        searchRS(findWord);
    });



    // Получение и вывод результатов запроса
    function searchRS(word) {

        var fields = ['bodyIn', 'bodyOut', 'rest', 'hostname', 'type'];
        var param = "host=" + host + '&fields=' + fields + '&word=' + word; // + '&word=' + request;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/fuzzyFindLog',
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

                    items += '</div>'

                        + '</pre>'

                    ;

                    items = items.replace('NaN', '');



                }
                items = items.replace(new RegExp(word, 'g'), '<span style="background: #a10e12; color: #55f0ff;">' + word + '</span>');
                document.getElementById("world-map-markers").innerHTML = items; // + document.getElementById("world-map-markers").innerHTML;



            }
        });




    }


})


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
    getCountLog();
    //searchRS();



    // var timerAId = setInterval(main, 2000);

    // function main() {
    //     searchRS()
    //
    //     // lastCountLog = currentCountLog;
    //
    // }



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





 // Получение и вывод результатов запроса
    function searchRS(word) {
        //if (loadFlag == 1) return;
        // loadFlag = 1;

        //var word = 'description'; // document.getElementById('searchText').innerText;
        // var request = $_POST['searchText'].text();


        // var parameters = '{ ' +
        //     '    "query": { ' +
        //     '        "more_like_this" : { ' +
        //     '            "fields" : ["bodyIn", "bodyOut"], ' +
        //     '            "like" : "' + request + '", ' +
        //     '            "min_term_freq" : 1, ' +
        //     '            "max_query_terms" : 12 ' +
        //     '        } ' +
        //     '    } ' +
        //     '}';

        // var parm = {
        //     query: {
        //         more_like_this: {
        //             fields: ["bodyIn", "bodyOut"],
        //             like: request,
        //             min_term_freq: 1,
        //             max_query_terms: 12
        //         }
        //     }
        // };


        var fields = ['bodyIn', 'bodyOut'];
        var param = "host=" + host + '&fields=' + fields + '&word=' + word; // + '&word=' + request;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/fuzzyFindLog',
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
                    // items = items.replace(word, '<span style="background: #670b0e; color: #55f0ff;">' + word + '</span>');


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
                items = items.replace(new RegExp(word, 'g'), '<span style="background: #a10e12; color: #55f0ff;">' + word + '</span>');
                document.getElementById("world-map-markers").innerHTML = items + document.getElementById("world-map-markers").innerHTML;



            }
        });




    }



});
