// id 필터 검색
/*function searchId(){
    $('.filter_box').removeClass('open');
    searchForm();
    $('.filter_box').find('input[type=text]').val('');
    $('.filter_box').find('textarea').val('');
}*/

// id 종류 선택 시 특정번호 name 변경
function itemTypeSearch(element){
    $('#specificId').attr('name', element.value)
}

// id 범위 영역 선택 시 라디오 체크
function radioCheck(element){
    if (element.id === 'start_item_id' || element.id === 'end_item_id'){
        $('input[type=radio][value=range]').prop('checked', true);
    } else if (element.id === 'specificId'){
        $('input[type=radio][value=specific]').prop('checked', true);
    }
}

// id 필터 닫기
/*function closeSearchId(){
    $('.filter_box').removeClass('open');
    $('.filter_box').find('input[type=text]').val('');
    $('.filter_box').find('textarea').val('');
    $('.filter_box').find('input[type="radio"][name="search_type"][value="all"]').prop("checked","checked");
}*/

// ID 필터 창에서 다른 영역 클릭하면 닫히도록
$(document).mouseup(function (e){
    let LayerPopup = $(".filter_box");
    if(LayerPopup.has(e.target).length === 0){
        // 창 닫기
        LayerPopup.removeClass("open");
        // 초기화
        /*$('.filter_box').find('input[type=text]').val('');
        $('.filter_box').find('textarea').val('');
        $('.filter_box').find('input[type="radio"][name="search_type"][value="all"]').prop("checked","checked");*/
    }
});