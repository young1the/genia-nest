/*
* 사용 중인 화면
* 시스템 관리 > 평가 속성 관리
* 시스템 관리 > 성취 기준 관리
*
* */

function setCreateSelectBox(selectBoxName, item, changeFunc) {
    let currentDiv = document.getElementById("div_" + selectBoxName);
    let selectBox = document.createElement("select");
    let option = document.createElement("option");

    currentDiv.innerHTML = '';

    // 셀렉트박스 정보 설정
    selectBox.id = "select_" + selectBoxName;
    selectBox.className = "select_box";
    selectBox.name = selectBoxName;
    selectBox.style.width = "100%";

    // 옵션 초기값 셋팅
    option.value = "";
    option.text = "-선택-";
    selectBox.appendChild(option);


    // 옵션값 셋팅
    for (let i = 0; i < item.length; i++) {
        let option = document.createElement("option");
        option.value = item[i][selectBoxName];
        // 교육과정 ~ 영역
        if(selectBoxName.includes('Code')) {
            option.text = item[i][selectBoxName + 'Name'] != undefined? item[i][selectBoxName + 'Name'] : item[i][selectBoxName.replace('Code', 'Name')];
        // 과목
        } else if(selectBoxName.includes('Id')) {
            option.text = item[i][selectBoxName + 'Name'] != undefined? item[i][selectBoxName + 'Name'] : item[i][selectBoxName.replace('Id', 'Name')];
        }

        selectBox.appendChild(option);
    }

    currentDiv.appendChild(selectBox);

    // onChange 이벤트 셋팅
    if(changeFunc != undefined){
        $('#' + selectBox.id).on("change", function (){changeFunc();})
    }
}

// 셀렉트박스 초기화
function initSelectBox(selectBoxName) {
    let divList = document.getElementById("create_select_box_list").querySelectorAll("div");
    let reset = false;

    for (let i = 1; i < divList.length; i++) {
        if(divList[i].id == 'div_' + selectBoxName){
            reset = true;
        }

        if(reset) {
            divList[i].innerHTML = '<select style="width: 100%;"><option value="">-선택-</option></select>';
        }
    }
}

// 학교급 목록 가져오기
function getSchoolLevel(e) {
    let curriculumCode = e.value;
    let url = "/system/subject/school-level-select";
    let data = {"curriculumCode": curriculumCode};

    $.ajax({
        url: url,
        type: "post",
        data: data,
        async: false,
        success: function (result) {
            initSelectBox('schoolLevelCode');
            if(result.length) {
                setCreateSelectBox('schoolLevelCode', result, getGrade);
            }
        },
        error: function () {
            alert('데이터 요청을 실패하였습니다');
            return false;
        }
    });
}

// 학년 목록 가져오기
function getGrade() {
    let curriculumCode = $("#select_curriculumCode").val();
    let schoolLevelCode = $("#select_schoolLevelCode").val();

    let url = "/system/subject/grade-select";
    let data = {
        "curriculumCode": curriculumCode,
        "schoolLevelCode": schoolLevelCode
    };

    $.ajax({
        url: url,
        type: "post",
        data: data,
        async: false,
        success: function (result) {
            initSelectBox('gradeCode');
            if(result.length) {
                setCreateSelectBox('gradeCode', result, getArea);
            }
        },
        error: function () {
            alert('데이터 요청을 실패하였습니다');
            return false;
        }
    });
}

// 과목 영역 목록 가져오기
function getArea() {
    let curriculumCode = $("#select_curriculumCode").val();
    let schoolLevelCode = $("#select_schoolLevelCode").val();
    let gradeCode = $("#select_gradeCode").val();

    let url = "/system/subject/subject-area-select";
    let data = {
        "curriculumCode": curriculumCode,
        "schoolLevelCode": schoolLevelCode,
        "gradeCode": gradeCode
    };

    $.ajax({
        url: url,
        type: "post",
        data: data,
        async: false,
        success: function (result) {
            initSelectBox('areaCode');
            if(result.length) {
                setCreateSelectBox('areaCode', result, getSubject);
            }
        },
        error: function () {
            alert('데이터 요청을 실패하였습니다');
            return false;
        }
    });
}

// 과목 목록 가져오기
function getSubject() {
    let curriculumCode = $("#select_curriculumCode").val();
    let schoolLevelCode = $("#select_schoolLevelCode").val();
    let gradeCode = $("#select_gradeCode").val();
    let areaCode = $("#select_areaCode").val();

    let url = "/system/subject/subject-select";
    let data = {
        "curriculumCode": curriculumCode,
        "schoolLevelCode": schoolLevelCode,
        "gradeCode": gradeCode,
        "areaCode": areaCode
    };

    $.ajax({
        url: url,
        type: "post",
        data: data,
        async: false,
        success: function (result) {
            initSelectBox('subjectId');
            if(result.length) {
                setCreateSelectBox('subjectId', result);
            }
        },
        error: function () {
            alert('데이터 요청을 실패하였습니다');
            return false;
        }
    });
}