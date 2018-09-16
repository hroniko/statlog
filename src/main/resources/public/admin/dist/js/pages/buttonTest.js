
// Убираем галочку
var btns = document.getElementsByClassName('contracts__list-item');
var btn_check = btns[1];
btn_check.className = 'contracts__list-item';

// и обратно галочку

var btns = document.getElementsByClassName('contracts__list-item');
var btn_check = btns[1];
btn_check.className = 'contracts__list-item contracts__list-item_checked';




var names = document.getElementsByClassName('one roe-table-cell');
var amounts = document.getElementsByClassName('two roe-table-cell');
var sum = 0;
for (var i = 0; i < names.length; i++){
    if (names[i].innerText.indexOf('Total') >= 0){
        try {
            sum += parseFloat(amounts[i+1].innerText.replace('RM', '').replace(',', ''));
        }catch (e) {
        //
        }

    }
}


var label = undefined;
while (!label) {
    try {
        label = document.getElementById('payNowId');
        label.innerText = 'RM' + '000.00';
        if (label){
            ROE.findNrcWithWait();
        }
    }catch (e) {
        //
    }
}


//////


