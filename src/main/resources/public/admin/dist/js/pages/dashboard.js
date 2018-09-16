var lastCountLog = 0; // Переменная для хранения количества логов в сейвере с предыдущего запроса
var currentCountLog = 0; // Переменная для хранения количества логов в сейвере с текущего запроса
// var host = '172.23.132.111';
var host = '10.106.5.148';
var oldPie = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

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
    getServerMonitor();
    getCountAllAndGoodAndBadRest();
    getIpPie();
    plotStat();
    drawStat();
    getCountLog();
    getCountOfGetPostPutDelete();



    var timerAId = setInterval(main, 4000);

    function main() {
        getCountLog();
        getServerMonitor();
        if ((lastCountLog + 10) < currentCountLog){
            plotStat();
        }
        if (lastCountLog < currentCountLog){
            drawStat();
            getCountAllAndGoodAndBadRest();
            getIpPie();
            getCountOfGetPostPutDelete();
        }

        lastCountLog = currentCountLog;

    }


    // Получение статистики по серверу (процессор, память, клиенты за 30 минут и запросы за 30 минут)
    function getServerMonitor() {
        var param = 'host=' + host;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/getServerMonitor',
            type: 'GET',
            data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {
                // Подписываем использование процессора
                if(data.cpu != '-'){
                    document.getElementById("outputCPU").innerHTML = data.cpu + '<small>%</small>';
                }
                // Подписываем использование памяти
                if(data.memory != '-'){
                    document.getElementById("outputMemory").innerHTML = data.memory + '<small>%</small>';
                }
                // Подписываем количество подключенных клиентов за последние 30 минут
                document.getElementById("outputUser30min").innerHTML = data.user30min;
                // Подписываем количество отправленных за последние 30 минут запросов
                document.getElementById("outputRest30min").innerHTML = data.rest30min;
            }
        });

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




    // var timer0Id = setInterval(plotStat, 4000);
    // -------------------------------------------------------

    // Получение, формирование и вывод графика за сутки с интервалом в 1 час
    function plotStat() {
        var t_labels =[];
        var t_itemsGood = [];
        var t_itemsBad = [];

        var param = "host=" + host + "&interval=1h"; //
        // var param = "host=" + host + "&start=2018-04-25 01:26:13&end=2018-04-27 18:05:47&interval=2h"; //
        $.ajax({
            url: '/getCountStatGoodAndBad24h',
            type: 'GET',
            data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (dataRes) {
                for (var i = 0; i < dataRes.length; i++){
                    var countGood = parseInt(dataRes[i].bulkCount.good);
                    t_itemsGood.push(countGood);

                    var countBad = parseInt(dataRes[i].bulkCount.bad);
                    t_itemsBad.push(countBad);

                    t_labels.push(dataRes[i].middleDate) //t_labels.push('1') //
                }

                // +++++++++++++++++++++++++++

                // // Get context with jQuery - using jQuery's .get() method.
                // var salesChartCanvas = $('#salesChart').get(0).getContext('2d');
                // // This will get the first returned node in the jQuery collection.
                // var salesChart       = new Chart(salesChartCanvas);

                var salesChartData = {
                    labels  : t_labels, //['J-anuary', 'February', 'March', 'April', 'May', 'June', 'July'],
                    datasets: [
                        {
                            label               : 'Успешные REST', //'Digital Goods',
                            fillColor           : 'rgb(60,141,188)',
                            strokeColor         : 'rgb(60,141,188)',
                            pointColor          : 'rgb(60,141,188)',
                            pointStrokeColor    : '#3b8bba',
                            pointHighlightFill  : '#fff',
                            pointHighlightStroke: 'rgba(60,141,188,1)',
                            data                : t_itemsGood // [28, 48, 40, 19, 86, 27, 90]
                        },
                        {
                            label               : 'Неудачные REST', //'Electronics',
                            fillColor           : 'rgba(255, 80, 80, 0.9)', // 'rgb(210, 214, 222)',
                            strokeColor         : 'rgba(255, 80, 80, 0.8)', // 'rgb(210, 214, 222)',
                            pointColor          : 'rgba(255, 80, 80, 1)', // 'rgb(210, 214, 222)',
                            pointStrokeColor    : '#ff5050', //'#c1c7d1',
                            pointHighlightFill  : '#fff',
                            pointHighlightStroke: 'rgba(255, 80, 80, 1)', //'rgb(220,220,220)',
                            data                : t_itemsBad // [65, 59, 80, 81, 56, 55, 40]
                        }
                    ]
                };

                var salesChartOptions = {
                    // Boolean - If we should show the scale at all
                    showScale               : true,
                    // Boolean - Whether grid lines are shown across the chart
                    scaleShowGridLines      : false,
                    // String - Colour of the grid lines
                    scaleGridLineColor      : 'rgba(0,0,0,.05)',
                    // Number - Width of the grid lines
                    scaleGridLineWidth      : 1,
                    // Boolean - Whether to show horizontal lines (except X axis)
                    scaleShowHorizontalLines: true,
                    // Boolean - Whether to show vertical lines (except Y axis)
                    scaleShowVerticalLines  : true,
                    // Boolean - Whether the line is curved between points
                    bezierCurve             : true,
                    // Number - Tension of the bezier curve between points
                    bezierCurveTension      : 0.3,
                    // Boolean - Whether to show a dot for each point
                    pointDot                : false,
                    // Number - Radius of each point dot in pixels
                    pointDotRadius          : 4,
                    // Number - Pixel width of point dot stroke
                    pointDotStrokeWidth     : 1,
                    // Number - amount extra to add to the radius to cater for hit detection outside the drawn point
                    pointHitDetectionRadius : 20,
                    // Boolean - Whether to show a stroke for datasets
                    datasetStroke           : true,
                    // Number - Pixel width of dataset stroke
                    datasetStrokeWidth      : 2,
                    // Boolean - Whether to fill the dataset with a color
                    datasetFill             : true,
                    // String - A legend template
                    legendTemplate          : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<datasets.length; i++){%><li><span style=\'background-color:<%=datasets[i].lineColor%>\'></span><%=datasets[i].label%></li><%}%></ul>',
                    // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
                    maintainAspectRatio     : true,
                    // Boolean - whether to make the chart responsive to window resizing
                    responsive              : true
                };



                // var salesChartCanvas = $('#salesChart').get(0).getContext('2d');
                // // This will get the first returned node in the jQuery collection.
                // var salesChart       = new Chart(salesChartCanvas);
                //
                //
                //
                // // Create the line chart
                // salesChart.Line(salesChartData, salesChartOptions);
                // // Подписываем заголовок графика
                // document.getElementById("outputPlotHeader").innerHTML = 'За сутки с интервалом 1ч';
                // // document.getElementById("outputPlotHeader").innerHTML = 'За сутки с интервалом 1ч (с ' + dataRes[0].startDate + ' по ' + dataRes[dataRes.length-1].endDate + ')';



                // Удаляем (очищаем) старый график таким вот хитрым способом,
                // источник https://stackoverflow.com/questions/24815851/how-to-clear-a-chart-from-a-canvas-so-that-hover-events-cannot-be-triggered/25064035
                document.getElementById("salesChartPlot").innerHTML = '&nbsp;';
                document.getElementById("salesChartPlot").innerHTML = '<canvas id="salesChart" style="height: 400px;"></canvas>';
                var salesChartCanvas = document.getElementById("salesChart").getContext("2d");
                var salesChart       = new Chart(salesChartCanvas);

                // Создаем заново
                salesChart.Line(salesChartData, salesChartOptions);
                // Подписываем заголовок графика
                document.getElementById("outputPlotHeader").innerHTML = 'За сутки с интервалом 1ч';
                // document.getElementById("outputPlotHeader").innerHTML = 'За сутки с интервалом 1ч (с ' + dataRes[0].startDate + ' по ' + dataRes[dataRes.length-1].endDate + ')';


                // ++++++++++++++++++++++++++++++++


            }
        });

    }



    // -------------------------------------------------------

    // // Get context with jQuery - using jQuery's .get() method.
    // var salesChartCanvas = $('#salesChart').get(0).getContext('2d');
    // // This will get the first returned node in the jQuery collection.
    // var salesChart       = new Chart(salesChartCanvas);
    //
    // var salesChartData = {
    //   labels  : t_labels, //['J-anuary', 'February', 'March', 'April', 'May', 'June', 'July'],
    //   datasets: [
    //     {
    //       label               : 'Good RESTs', //'Electronics',
    //       fillColor           : 'rgb(210, 214, 222)',
    //       strokeColor         : 'rgb(210, 214, 222)',
    //       pointColor          : 'rgb(210, 214, 222)',
    //       pointStrokeColor    : '#c1c7d1',
    //       pointHighlightFill  : '#fff',
    //       pointHighlightStroke: 'rgb(220,220,220)',
    //       data                : t_itemsGood // [65, 59, 80, 81, 56, 55, 40]
    //     },
    //     {
    //       label               : 'Digital Goods',
    //       fillColor           : 'rgba(60,141,188,0.9)',
    //       strokeColor         : 'rgba(60,141,188,0.8)',
    //       pointColor          : '#3b8bba',
    //       pointStrokeColor    : 'rgba(60,141,188,1)',
    //       pointHighlightFill  : '#fff',
    //       pointHighlightStroke: 'rgba(60,141,188,1)',
    //       data                : t_itemsBad // [28, 48, 40, 19, 86, 27, 90]
    //     }
    //   ]
    // };
    //
    // var salesChartOptions = {
    //   // Boolean - If we should show the scale at all
    //   showScale               : true,
    //   // Boolean - Whether grid lines are shown across the chart
    //   scaleShowGridLines      : false,
    //   // String - Colour of the grid lines
    //   scaleGridLineColor      : 'rgba(0,0,0,.05)',
    //   // Number - Width of the grid lines
    //   scaleGridLineWidth      : 1,
    //   // Boolean - Whether to show horizontal lines (except X axis)
    //   scaleShowHorizontalLines: true,
    //   // Boolean - Whether to show vertical lines (except Y axis)
    //   scaleShowVerticalLines  : true,
    //   // Boolean - Whether the line is curved between points
    //   bezierCurve             : true,
    //   // Number - Tension of the bezier curve between points
    //   bezierCurveTension      : 0.3,
    //   // Boolean - Whether to show a dot for each point
    //   pointDot                : false,
    //   // Number - Radius of each point dot in pixels
    //   pointDotRadius          : 4,
    //   // Number - Pixel width of point dot stroke
    //   pointDotStrokeWidth     : 1,
    //   // Number - amount extra to add to the radius to cater for hit detection outside the drawn point
    //   pointHitDetectionRadius : 20,
    //   // Boolean - Whether to show a stroke for datasets
    //   datasetStroke           : true,
    //   // Number - Pixel width of dataset stroke
    //   datasetStrokeWidth      : 2,
    //   // Boolean - Whether to fill the dataset with a color
    //   datasetFill             : true,
    //   // String - A legend template
    //   legendTemplate          : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<datasets.length; i++){%><li><span style=\'background-color:<%=datasets[i].lineColor%>\'></span><%=datasets[i].label%></li><%}%></ul>',
    //   // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
    //   maintainAspectRatio     : true,
    //   // Boolean - whether to make the chart responsive to window resizing
    //   responsive              : true
    // };
    //
    // // Create the line chart
    // salesChart.Line(salesChartData, salesChartOptions);


    // ---------------------------
    // - END MONTHLY SALES CHART -
    // ---------------------------




    // Получение распределения количества запросов по ip
    function getIpPie() {
        var param = 'host=' + host;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/getCountByUsername',
            type: 'GET',
            data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {



                // currentCountLog = parseInt(data);

                // -------------
                // - PIE CHART -
                // -------------
                // Get context with jQuery - using jQuery's .get() method.
                // var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
                // var pieChart       = new Chart(pieChartCanvas);

                var PieData = [];
                var colorArr = ['#f56954',
                    '#00a65a',
                    '#f39c12',
                    '#00c0ef',
                    '#3c8dbc',
                    '#d2d6de',
                    '#e018eb',
                    '#9c4fa6',
                    '#f39c12',
                    '#55593a',
                    '#2d232a',
                    '#47de30'];
                // var dataKeys = data.keys();
                //
                // for (var i = 0; i < dataKeys.length; i++){
                //     var key = dataKeys.next().value;
                //     PieData.push({
                //         value    : parseInt(data.key),
                //         color    : colorArr[i],
                //         highlight: colorArr[i],
                //         label    : key
                //     })
                // }

                var flagRePie = 0; // флаг, разрешающий (1) или запрещающий(0) перестраивать диаграмму, в цикле проверяем, если все поля те же, то перестраивать не надо
                for (var i = 0; i < data.length; i++){
                    if(oldPie[i] != data[i].count){
                        oldPie[i] = data[i].count;
                        flagRePie = 1;
                    }
                    PieData.push({
                        value    : parseInt(data[i].count),
                        color    : colorArr[i],
                        highlight: colorArr[i],
                        label    : data[i].name + ', [' + data[i].ip + ']'
                    })
                }




                // Create pie or douhnut chart
                // You can switch between pie and douhnut using the method below.
                if (flagRePie = 1) { // если флаг выставлен, то перестраиваем

                    var pieOptions     = {
                        // Boolean - Whether we should show a stroke on each segment
                        segmentShowStroke    : true,
                        // String - The colour of each segment stroke
                        segmentStrokeColor   : '#fff',
                        // Number - The width of each segment stroke
                        segmentStrokeWidth   : 1,
                        // Number - The percentage of the chart that we cut out of the middle
                        percentageInnerCutout: 50, // This is 0 for Pie charts
                        // Number - Amount of animation steps
                        animationSteps       : 100,
                        // String - Animation easing effect
                        animationEasing      : 'easeOutBounce',
                        // Boolean - Whether we animate the rotation of the Doughnut
                        animateRotate        : true,
                        // Boolean - Whether we animate scaling the Doughnut from the centre
                        animateScale         : false,
                        // Boolean - whether to make the chart responsive to window resizing
                        responsive           : true,
                        // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
                        maintainAspectRatio  : false,
                        // String - A legend template
                        legendTemplate       : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<segments.length; i++){%><li><span style=\'background-color:<%=segments[i].fillColor%>\'></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>',
                        // String - A tooltip template
                        tooltipTemplate      : '<%=value %> <%=label%>'
                    };

                    document.getElementById("chartContainer").innerHTML = '&nbsp;';
                    document.getElementById("chartContainer").innerHTML = '<canvas id="pieChart" height="150"></canvas> ';
                    var pieChartCanvas = document.getElementById("pieChart").getContext("2d");

                    // var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
                    var pieChart       = new Chart(pieChartCanvas);

                    pieChart.Doughnut(PieData, pieOptions);


                    // и надо вывести список клиентов
                    var prefix =['<li><i class="fa fa-circle-o text-red"></i> ',
                        '<li><i class="fa fa-circle-o text-green"></i> ',
                        '<li><i class="fa fa-circle-o text-yellow"></i> ',
                        '<li><i class="fa fa-circle-o text-aqua"></i> ',
                        '<li><i class="fa fa-circle-o text-light-blue"></i> ',
                        '<li><i class="fa fa-circle-o text-gray"></i> '
                    ];

                    var clientList = '<ul class="chart-legend clearfix">';
                    var size = 6;
                    if (data.length < 6){
                        size = data.length;
                    }
                    for(var i = 0; i < size; i++){
                        if (data[i].name == 'unnamed host'){
                            clientList += prefix[i] + data[i].ip + '</li>'; // либо имя хоста,
                        } else {
                            clientList += prefix[i] + data[i].name + '</li>'; // либо только его айпи
                        }
                        // clientList += prefix[i] + data[i].name + '<br>' + data[i].ip + '</li>';
                    }
                    clientList += '</ul>';
                    document.getElementById("outputClientList").innerHTML = clientList;


                }
                // -----------------
                // - END PIE CHART -
                // -----------------
            }
        });

    }



    // // -------------
    // // - PIE CHART -
    // // -------------
    // // Get context with jQuery - using jQuery's .get() method.
    // var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
    // var pieChart       = new Chart(pieChartCanvas);
    // var PieData        = [
    //   {
    //     value    : 700,
    //     color    : '#f56954',
    //     highlight: '#f56954',
    //     label    : 'Chrome'
    //   },
    //   {
    //     value    : 500,
    //     color    : '#00a65a',
    //     highlight: '#00a65a',
    //     label    : 'IE'
    //   },
    //   {
    //     value    : 400,
    //     color    : '#f39c12',
    //     highlight: '#f39c12',
    //     label    : 'FireFox'
    //   },
    //   {
    //     value    : 600,
    //     color    : '#00c0ef',
    //     highlight: '#00c0ef',
    //     label    : 'Safari'
    //   },
    //   {
    //     value    : 300,
    //     color    : '#3c8dbc',
    //     highlight: '#3c8dbc',
    //     label    : 'Opera'
    //   },
    //   {
    //     value    : 100,
    //     color    : '#d2d6de',
    //     highlight: '#d2d6de',
    //     label    : 'Navigator'
    //   }
    // ];
    // var pieOptions     = {
    //   // Boolean - Whether we should show a stroke on each segment
    //   segmentShowStroke    : true,
    //   // String - The colour of each segment stroke
    //   segmentStrokeColor   : '#fff',
    //   // Number - The width of each segment stroke
    //   segmentStrokeWidth   : 1,
    //   // Number - The percentage of the chart that we cut out of the middle
    //   percentageInnerCutout: 50, // This is 0 for Pie charts
    //   // Number - Amount of animation steps
    //   animationSteps       : 100,
    //   // String - Animation easing effect
    //   animationEasing      : 'easeOutBounce',
    //   // Boolean - Whether we animate the rotation of the Doughnut
    //   animateRotate        : true,
    //   // Boolean - Whether we animate scaling the Doughnut from the centre
    //   animateScale         : false,
    //   // Boolean - whether to make the chart responsive to window resizing
    //   responsive           : true,
    //   // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
    //   maintainAspectRatio  : false,
    //   // String - A legend template
    //   legendTemplate       : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<segments.length; i++){%><li><span style=\'background-color:<%=segments[i].fillColor%>\'></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>',
    //   // String - A tooltip template
    //   tooltipTemplate      : '<%=value %> <%=label%> users'
    // };
    // // Create pie or douhnut chart
    // // You can switch between pie and douhnut using the method below.
    // pieChart.Doughnut(PieData, pieOptions);
    // // -----------------
    // // - END PIE CHART -
    // // -----------------

    /* jVector Maps
     * ------------
     * Create a world map with markers
     */
    // $('#world-map-markers').vectorMap({
    //   map              : 'world_mill_en',
    //   normalizeFunction: 'polynomial',
    //   hoverOpacity     : 0.7,
    //   hoverColor       : false,
    //   backgroundColor  : 'transparent',
    //   regionStyle      : {
    //     initial      : {
    //       fill            : 'rgba(210, 214, 222, 1)',
    //       'fill-opacity'  : 1,
    //       stroke          : 'none',
    //       'stroke-width'  : 0,
    //       'stroke-opacity': 1
    //     },
    //     hover        : {
    //       'fill-opacity': 0.7,
    //       cursor        : 'pointer'
    //     },
    //     selected     : {
    //       fill: 'yellow'
    //     },
    //     selectedHover: {}
    //   },
    //   markerStyle      : {
    //     initial: {
    //       fill  : '#00a65a',
    //       stroke: '#111'
    //     }
    //   },
    //   markers          : [
    //     { latLng: [41.90, 12.45], name: 'Vatican City' },
    //     { latLng: [43.73, 7.41], name: 'Monaco' },
    //     { latLng: [-0.52, 166.93], name: 'Nauru' },
    //     { latLng: [-8.51, 179.21], name: 'Tuvalu' },
    //     { latLng: [43.93, 12.46], name: 'San Marino' },
    //     { latLng: [47.14, 9.52], name: 'Liechtenstein' },
    //     { latLng: [7.11, 171.06], name: 'Marshall Islands' },
    //     { latLng: [17.3, -62.73], name: 'Saint Kitts and Nevis' },
    //     { latLng: [3.2, 73.22], name: 'Maldives' },
    //     { latLng: [35.88, 14.5], name: 'Malta' },
    //     { latLng: [12.05, -61.75], name: 'Grenada' },
    //     { latLng: [13.16, -61.23], name: 'Saint Vincent and the Grenadines' },
    //     { latLng: [13.16, -59.55], name: 'Barbados' },
    //     { latLng: [17.11, -61.85], name: 'Antigua and Barbuda' },
    //     { latLng: [-4.61, 55.45], name: 'Seychelles' },
    //     { latLng: [7.35, 134.46], name: 'Palau' },
    //     { latLng: [42.5, 1.51], name: 'Andorra' },
    //     { latLng: [14.01, -60.98], name: 'Saint Lucia' },
    //     { latLng: [6.91, 158.18], name: 'Federated States of Micronesia' },
    //     { latLng: [1.3, 103.8], name: 'Singapore' },
    //     { latLng: [1.46, 173.03], name: 'Kiribati' },
    //     { latLng: [-21.13, -175.2], name: 'Tonga' },
    //     { latLng: [15.3, -61.38], name: 'Dominica' },
    //     { latLng: [-20.2, 57.5], name: 'Mauritius' },
    //     { latLng: [26.02, 50.55], name: 'Bahrain' },
    //     { latLng: [0.33, 6.73], name: 'São Tomé and Príncipe' }
    //   ]
    // });

    // var timerId = setInterval(drawStat, 2000);



    function drawStat() {
        var param = "host=" + host; // + '&user_id=' + user_id;

        // Получаем данные через AJAX-запрос и формируем массив для отрисовки графика
        $.ajax({
            url: '/getEntity',
            type: 'GET',
            data: param, //'{"host":"' + param + '"}',
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {

                var items = "";
                for (var i = 0; i < data.length; i++){

                    items += '<pre style="background: #002240; color: white;"><span style="color: #ffee80;">'
                        + data[i].id +' </span><span style="color: #ff9d00;">'
                        + data[i].datetime + ' </span>'
                        + data[i].ip + ' <span style="color: #e1efff;">'
                        + data[i].hostname + '</span><span style="color: #3ad900;"></span><br>'
                        + data[i].status + ' <span style="color: #3ad900;">'
                        + data[i].type + ' </span><span style="color: #e1efff;">'
                        + data[i].rest + ' </span>'
                        + '</pre>';


                }

                document.getElementById("world-map-markers").innerHTML = items;

            }
        });

    }


    // Получение и вывод на страницу количества и процентажа хороших и плохих, а также всего запросов за все время
    function getCountAllAndGoodAndBadRest() {
        var param = "host=" + host;
        $.ajax({
            url: '/getAllAndGoodAndBadCountRest',
            type: 'GET',
            data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {
                // Подписываем количество всех запросов
                document.getElementById("allCountRest").innerHTML = '' + data.allCount;
                // Подписываем количество неудачных запросов
                document.getElementById("badCountRest").innerHTML = '' + data.badCount + ' ~ ' + data.badPercent + '%';
                // Подписываем количество успешных запросов
                document.getElementById("goodCountRest").innerHTML = '' + data.goodCount + ' ~ ' + data.goodPercent + '%';
            }
        });

    }



    // Получение и вывод на страницу текущих папраметров клиентасоотношения типов запросов
    function getCountOfGetPostPutDelete() {
        var param = "host=" + host;
        $.ajax({
            url: '/getCountOfGetPostPutDelete',
            type: 'GET',
            data: param,
            dataType: 'json',
            contentType: "application/json",
            mimeType: 'application/json',
            // async: false,
            success: function (data) {

                var countGet = parseInt(data.GET);
                var countPost = parseInt(data.POST);
                var countPut = parseInt(data.PUT);
                var countDelete = parseInt(data.DELETE);
                var countAll = countGet + countPost + countPut + countDelete;


                // Подписываем соответствующие поля с вычислением процентов
                document.getElementById("outputCountGET").innerHTML = '<b>' + countGet + '</b> ~ ' + parseFloat(countGet / countAll * 100).toFixed(2) + '%';
                document.getElementById("outputCountPOST").innerHTML = '<b>' + countPost + '</b> ~ ' + parseFloat(countPost / countAll * 100).toFixed(2) + '%';
                document.getElementById("outputCountPUT").innerHTML = '<b>' + countPut + '</b> ~ ' + parseFloat(countPut / countAll * 100).toFixed(2) + '%';
                document.getElementById("outputCountDELETE").innerHTML = '<b>' + countDelete + '</b> ~ ' + parseFloat(countDelete / countAll * 100).toFixed(2) + '%';
            }
        });

    }



    /* SPARKLINE CHARTS
     * ----------------
     * Create a inline charts with spark line
     */

    // -----------------
    // - SPARKLINE BAR -
    // -----------------
    $('.sparkbar').each(function () {
        var $this = $(this);
        $this.sparkline('html', {
            type    : 'bar',
            height  : $this.data('height') ? $this.data('height') : '30',
            barColor: $this.data('color')
        });
    });

    // -----------------
    // - SPARKLINE PIE -
    // -----------------
    $('.sparkpie').each(function () {
        var $this = $(this);
        $this.sparkline('html', {
            type       : 'pie',
            height     : $this.data('height') ? $this.data('height') : '90',
            sliceColors: $this.data('color')
        });
    });

    // ------------------
    // - SPARKLINE LINE -
    // ------------------
    $('.sparkline').each(function () {
        var $this = $(this);
        $this.sparkline('html', {
            type     : 'line',
            height   : $this.data('height') ? $this.data('height') : '90',
            width    : '100%',
            lineColor: $this.data('linecolor'),
            fillColor: $this.data('fillcolor'),
            spotColor: $this.data('spotcolor')
        });
    });
});
