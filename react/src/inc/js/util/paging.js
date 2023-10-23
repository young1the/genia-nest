function paging(totalData, dataPerPage, pageCount, currentPage, pagingId) {
    currentPage = parseInt(currentPage);

    let totalPage = Math.ceil(totalData / dataPerPage); //총 페이지 수

    if (totalPage < pageCount) {
        pageCount = totalPage;
    }

    let pageGroup = Math.ceil(currentPage / pageCount); // 페이지 그룹
    let last = pageGroup * pageCount; //화면에 보여질 마지막 페이지 번호

    if (last > totalPage) {
        last = totalPage;
    }

    let first = 1; //화면에 보여질 첫번째 페이지 번호
    if (pageGroup > 1) {
        first = first + (pageGroup - 1) * pageCount;
    }
    let next = currentPage + 1;
    let prev = currentPage - 1;

    let pageHtml = "";

    if (prev > 0) {
        pageHtml += '<a href="javascript:void(0);" class="pprev"><img src="/images/page_pprev.svg" alt="처음"></a>';
        pageHtml += '<a href="javascript:void(0);" class="prev"><img src="/images/page_prev.svg" alt="이전"></a>';
    }

    //페이징 번호 표시
    for (let i = first; i <= last; i++) {
        if (currentPage == i) {
            pageHtml +=
                '<a href="javascript:void(0);" class="on">' + i +'</a>';
        } else {
            pageHtml += '<a href="javascript:void(0);">' + i +'</a>';
        }
    }

    if (next <= totalPage) {
        pageHtml += '<a href="javascript:void(0);" class="next"><img src="/images/page_next.svg" alt="다음"></a>';
        pageHtml += '<a href="javascript:void(0);" class="nnext"><img src="/images/page_nnext.svg" alt="끝"></a>';
    }

    $("#" + pagingId).html(pageHtml);

    //페이징 번호 클릭 이벤트
    $("#" + pagingId + " a").click(function () {
        let pageClass = $(this).attr("class");
        let selectedPage = $(this).text();

        if (pageClass === "nnext") selectedPage = totalPage;
        if (pageClass === "next") selectedPage = next;
        if (pageClass === "prev") selectedPage = prev;
        if (pageClass === "pprev") selectedPage = 1;

        //글 목록 표시 재호출
        movePage(selectedPage, pagingId);
    });
}