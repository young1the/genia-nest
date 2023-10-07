function initPagination(grid,perPage = null){
    const pagination = grid.getPagination();
    if(perPage !== null){
        pagination.setItemsPerPage(parseInt(perPage));
    }else{
        pagination.setItemsPerPage(grid.getPerPage());
    }
    pagination.reset();
}

function convertFormToCamel(element){
    const formData = new FormData(element);
    let params = {};
    formData.forEach(function (value, key) {
        params[convertSnakeToCamel(key)] = value;
    });
    return params;
}

const convertSnakeToCamel = (s) => {
    return s.replace(/([-_][a-z])/ig, ($1) => {
        return $1.toUpperCase()
            .replace('-', '')
            .replace('_', '');
    });
};

function getSecondaryCodeList(primaryCode){
    let result = {};

}

// ajax 공통호출
const ajaxCall = (type, url, param, callback) => {
    $.ajax({
        url: url,
        type : type,
        data : param,
        success: function (data) {
            return callback(data);
        },
        error : function (xhr, status, error) {
            alert("관리자에게 문의 하세요.");
            console.log(xhr, status, error);
        }
    });
}

// object null check
const isEmptyObj = (obj) =>  {
    return obj.constructor === Object && Object.keys(obj).length === 0;
}

//checkbox 전체선택, 해제
const allCheck = (allBox,checkBoxesName = '') => {
    const checkBoxes = document.getElementsByName(checkBoxesName);
    checkBoxes.forEach((checkBox) => {
        checkBox.checked = allBox.checked;
    });
}


// 날짜
function dateParse(str, hours){
    const date = new Date(str);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);

    // 시간
    if (hours === 'Y') {
        const hours =('0' + (date.getHours() + 1)).slice(-2);
        const minutes =('0' + (date.getMinutes() + 1)).slice(-2);
        const seconds =('0' + (date.getSeconds() + 1)).slice(-2);

        return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds ;
    } else {
        return year + '-' + month + '-' + day;
    }
}

// 테이블 번호 (역순)
function rowNumber(totalCount, perPage, currentPage, index){
    return totalCount - ((currentPage - 1) * perPage + index);
}

//전체선택 (페이지 단위 x)
function pageAllCheck(){
    let chkImg = $('input[type=checkbox][name=checkRow]');
    let checkAllButton = document.getElementById('check_all');
    if (checkAllButton.innerText === '전체 선택') {
        checkAllButton.innerText = '전체 해제';
        chkImg.prop('checked', true);
    }else {
        checkAllButton.innerText ='전체 선택';
        chkImg.prop('checked', false);
    }
}

// 페이지 벗어나면 검색 조건 session 삭제
/**
 * @param mappingKeyword 특정 메뉴 공통 mapping keword(ex. exam일 때는 '/exam')
 * @param storageKey page에서 사용하는 검색 조건을 저장한 storage Key 이름 (ex. 'examSearch')
 */
function searchOptionSessionRemove(mappingKeyword, storageKey){
    let prevURL = document.referrer; // 현재 페이지를 접근한 이전 페이지의 url
    if( !(prevURL.includes(mappingKeyword)) ){ // 다른 메뉴에서 접근했다면 입력한 스토리지 키, 값 삭제
        window.sessionStorage.removeItem(storageKey);
    }
}

function awaitExplorerAccessKey(){
    return fetch('/system/user/find-explorer-access-key',{
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => {
        return response.json();
    }).catch((data) => {
        console.log(data);
        alert("에러 발생");
    });
}

function guid() {
    function _s4() {
        return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
    }
    return _s4() + _s4() + '-' + _s4() + '-' + _s4() + '-' + _s4() + '-' + _s4() + _s4() + _s4();
}

// 준비중 alert
function prepareAlert() {
    alert('오픈 준비 중입니다.');
}

// 3자리 마다 콤마
function numberWithCommas(str) {
    return str.toString()
        .replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}