$(function () {
  //menu
  function checkCurrentGnb() {
    var _className = $("#wrap").attr("class"),
      _afterStr = _className ? _className.split(" ") : "",
      _gnbMenuLink = $(".nav > li");

    switch (_afterStr[0]) {
      case "page03_01":
        _gnbMenuLink.eq(0).find("> a").addClass("active");
        _gnbMenuLink
          .eq(0)
          .find(".depth2")
          .find("li")
          .eq(0)
          .find("a")
          .addClass("active");
        break;
      case "page04_01":
        _gnbMenuLink.eq(1).find("> a").addClass("active");
        _gnbMenuLink
          .eq(1)
          .find(".depth2")
          .find("li")
          .eq(0)
          .find("a")
          .addClass("active");
        break;

      case "page03":
        _gnbMenuLink.eq(2).addClass("active");
        break;
      case "page04":
        _gnbMenuLink.eq(3).addClass("active");

        break;
    }
  }

  function menuUI() {
    let menuWrap = $(".nav");
    let menu = $(".nav li");
    let menuBtn = $(".nav li a");
    let _header = $(".header");
    let subMenu = $(".depth2");
    let dim = $(".dim");

    menuBtn.on("mouseover", function () {
      let _this = $(this);
      let _thisMenu = $(this).parents("li");

      _header.addClass("active");
      subMenu.addClass("on");
      dim.show();

      _thisMenu.siblings("li").find("> a").removeClass("active");
      _thisMenu.siblings("li").find(subMenu).find("a").removeClass("active");
      _thisMenu.find("> a").addClass("active");
      _this.addClass("active");
    });

    _header.on("mouseleave", function () {
      _header.removeClass("active");
      subMenu.removeClass("on");
      menuBtn.removeClass("active");
      dim.hide();

      checkCurrentGnb();
    });
  }

  $(document).on("ready", function () {
    checkCurrentGnb();
    menuUI();
  });

  // tab
  let tabBtn = $(".ui-tab-btn");

  function tabUI() {
    let _this = $(this);
    let _cnt = $(this).parents(".tab-wrap").find(".contents");
    let _idx = $(this).index();

    if (_this.parents("div").hasClass("contents")) {
      if (!_this.hasClass("active")) {
        _this.siblings().removeClass("active");
        _this.addClass("active");
        _this.parents(".contents").find(".content").removeClass("on");
        _this.parents(".contents").find(".content").eq(_idx).addClass("on");
      }
    } else {
      if (!_this.hasClass("active")) {
        _this.siblings().removeClass("active");
        _this.addClass("active");
        _cnt.removeClass("on");
        _cnt.eq(_idx).addClass("on");
      }
    }
  }

  tabBtn.on("click", tabUI);

  // paging
  let pageBtn = $(".page");

  function pageFunc() {
    let _this = $(this);

    if (!_this.hasClass("active")) {
      pageBtn.removeClass("active");
      _this.addClass("active");
    }
  }

  pageBtn.on("click", pageFunc);

  // select
  let selectBtn = $(".select-btn");

  function selectUI() {
    let _this = $(this);
    let _cnt = $(this).next();

    if (!_this.hasClass("active")) {
      _this.addClass("active");
      _cnt.stop().slideDown("fast");
    } else {
      _cnt.stop().slideUp("fast", function () {
        _this.removeClass("active");
      });
    }
  }
  selectBtn.on("click", selectUI);

  // popup
  let _dim = $(".dim-pop");
  let _html = $("html , body");
  let popBtn = $(".pop-btn");
  let closePop = $(".pop-close");

  function popFunc() {
    let _this = $(this);
    let popData = _this.data("pop");

    _html.css("overflow", "hidden");
    _dim.fadeIn();

    $(".pop-wrap[data-pop='" + popData + "']").show();

    console.log($(".pop-wrap[data-pop='" + popData + "']"));
  }

  function popClose() {
    let _this = $(this);
    $(".pop-wrap").hide();
    _html.css("overflow", "auto");
    _dim.fadeOut();
  }

  popBtn.on("click", popFunc);
  closePop.on("click", popClose);

  // checkbox
  let chkAll = $(".allCheck");

  function checkFunc() {
    let _this = $(this);

    if (_this.prop("checked")) {
      _this.parents("table").find("input[type=checkbox]").prop("checked", true);
    } else {
      _this
        .parents("table")
        .find("input[type=checkbox]")
        .prop("checked", false);
    }
  }

  chkAll.on("click", checkFunc);

  // accordion
  let accBtn = $(".acc-btn");

  function accFunc() {
    let _this = $(this);

    _this.toggleClass("active");

    if (_this.hasClass("active")) {
      _this.next(".cnt").stop().slideDown("fast");
    } else {
      _this.next(".cnt").stop().slideUp("fast");
    }
  }

  accBtn.on("click", accFunc);

  // 검색 조건 박스 토글
  $(".btn-toggle").on("click", function (e) {
    e.preventDefault();
    $(this).toggleClass("close");
    $(this).parent().parent().next(".toggle-box").slideToggle(0);
    if ($(this).hasClass("close")) {
      $(this).parents(".search-box").css("border-radius", 0);
    } else {
      $(this).parents(".search-box").css("border-radius", "4px");
    }
  });

  // 	search_toggle_box 펼쳐진 상태
  $(".toggle-box.slidedown").slideDown();

  // click button
  let tblBtn = $(".search-type .table-wrap tbody tr");

  function btnClickFunc() {
    let _this = $(this);

    if (!_this.hasClass("active")) {
      _this.parents("tbody").find("tr").removeClass("active");
      _this.addClass("active");
    }

  }

  tblBtn.on("click", btnClickFunc);

  // page drag
  $(function () {
    $("#pdf-viewer-type01").sortable();
    $("#pdf-viewer-type01").disableSelection();

    $("#pdf-viewer-type02").sortable();
    $("#pdf-viewer-type02").disableSelection();
  });
});