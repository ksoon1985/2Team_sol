
$(function() {
            //input을 datepicker로 선언
            $("#datepicker").datepicker({
            	onSelect: function(date) {
            		
            		var selectDate = date.split('-'),
            			dateYear = selectDate[0],
            			dateMonth = selectDate[1],
            			dateDay = selectDate[2];
            		
            		var setDate = dateYear + " 년 " + dateMonth + " 월 " + dateDay + " 일 ";
            		$('#selected_date').text(setDate);
            		
            		var param_date1 = dateYear.substr(2,4) + "/" + dateMonth +"/"+dateDay;
            		
            		$('#date1').val(param_date1);
            		
                }
                ,dateFormat: 'yy-mm-dd' //Input Display Format 변경
                ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
                ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
                ,changeYear: true //콤보박스에서 년 선택 가능
                ,changeMonth: true //콤보박스에서 월 선택 가능                
                ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
                ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
                ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
                ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
                ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
                ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
                ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
                ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
                ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
                ,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
                ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                
            });                    
            
            //초기값을 오늘 날짜로 설정
           $('#datepicker').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)            
});

// 결제하기 버튼 클릭시 
$('#send_money').click(function(e){
	e.preventDefault();
	
	var IMP = window.IMP;
	IMP.init('imp89291970');
	var msg;
	var mem_name   	 = $('#name').val(), 	 	// 멤버 이름
		total_price  = $('#price').val(), 		// 총 금액
		mem_no 		 = $('#mem_no').val(), 		// 멤버 넘버
		mem_email    = $('#mem_email').val(), 	// 멤버 이메일
		mem_tel 	 = $('#phone').val(), 		// 멤버 전화번호
		res_no		 = $('#res_no').val(), 		// 레스토랑 번호
		b_date  	 = $('#date1').val(), 		// 방문할 날짜
		comment		 = $('#comment').val(); 	// 요청사항
	
	
	IMP.request_pay({
		// 이 변수들을 활용하는 정해진 방법이 있는듯... 아직 모르겠음 
        pg : 'kakaopay',
        pay_method : 'card',
        merchant_uid : 'merchant_' + new Date().getTime(),
        name : '음식점 : 결제테스트',
        amount : total_price,
        buyer_email : mem_email,
        buyer_name : mem_name,
        buyer_tel : mem_tel,
	    
	    //모바일의 redirect_url , callback처리 하지 않음
	    //m_redirect_url : 'http://www.naver.com'
	}, function(rsp) {
        if ( rsp.success ) {
            //[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
            jQuery.ajax({
                url: "/custom/bookingProc", //cross-domain error가 발생하지 않도록 주의해주세요
                type: 'POST',
                dataType: 'json',
                contentType : "application/x-www-form-urlencoded; charset=utf-8",
                data: {
                    res_no : res_no,
                    mem_no : mem_no,
                    date1 : b_date,
                    price : total_price,
                    content : comment
                    //기타 필요한 데이터가 있으면 추가 전달
                }
            }).done(function(data) {
                //[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우
            	alert('done !');
            	
                if ( everythings_fine ) {
                    msg = '결제가 완료되었습니다.';
                    msg += '\n고유ID : ' + rsp.imp_uid;
                    msg += '\n상점 거래ID : ' + rsp.merchant_uid;
                    msg += '\결제 금액 : ' + rsp.paid_amount;
                    msg += '카드 승인번호 : ' + rsp.apply_num;
                    
                    //msg += '\n상점 거래ID : ' + rsp.merchant_uid;
                    alert(msg);
                } else {
                    //[3] 아직 제대로 결제가 되지 않았습니다.
                    //[4] 결제된 금액이 요청한 금액과 달라 결제를 자동취소처리하였습니다.
                	alert("오류 !!!");
                }
            }).fail(function(data){
            	alert("fail !")
            });
            //성공시 이동할 페이지
            //location.href='/';
        } else {
            msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            //실패시 이동할 페이지
            //location.href="<%=request.getContextPath()%>/order/payFail";
            alert(msg);
        }
	    
	    
	});
});

// 메뉴에서 +버튼 클릭시 가격이 오르는 함수
function add(node,price){
	
	// 수량 증가
	var num_el = $(node).siblings('span').children('#menu_num');
	var num = num_el.text();
	
	num = Number(num) + 1;
	
	$(num_el).text(num);
	
	// 금액 증가
	var total = $('#price_total').text();
	total = Number(total) + price;
	$('#price_total').text(total);
	
	$('input[name=price]').val(total);
}

//메뉴에서 -버튼 클릭시 가격이 감소하는 함수
function minus(node,price){
	
	// 수량 감소 
	var num_el = $(node).siblings('span').children('#menu_num');
	var num = num_el.text();
	
	num = Number(num);
	if(num == 0 )
	{
		return faluse;
	}
	
	num = num - 1;
	$(num_el).text(num);
	
	// 금액 감소 
	var total = $('#price_total').text();
	total = Number(total) - price;
	$('#price_total').text(total);
	$('input[name=price]').val(total);
}

/*
function f_ChangeMonth( pYyyyMmDd ) {
	var frm = document.frm;
	var gv_save_yyyymmdd = "";
	
	$.ajaxSetup({ async:false }); 
	
	gv_save_yyyymmdd = pYyyyMmDd;

	$.ajaxSetup({ async:true }); 
}			
*/

//--일자 선택에 대한 처리 (pYyyyMmDd - 상품일자, pSdSeq - 일정SEQ, pSdTimeuseYn - 일정의 시간 사용여부, pSdEndHhmi - 일정의 종료시간)
/*
function f_SelectDate( pYyyyMmDd, pSdSeq, pSdTimeuseYn, pSdEndHhmi , weekName, id) {
	valueReset(); //날짜&시간 선택시 전에 선택했던 항목 초기화
	
	var frm = document.frm;
	$(".calendar-data a").removeClass("on");
	$("#"+id).addClass("on");
	
	var selectDay = pYyyyMmDd.substr(0,4)+ '년 ' + pYyyyMmDd.substr(4,2) + '월 ' + pYyyyMmDd.substr(6) + "일(" + weekName + ")";
	$("#selectDay").text(selectDay);
}
*/
/*
function valueReset(){
	$(".price total").text("0");
	$(".sumText").text("");

}
*/

