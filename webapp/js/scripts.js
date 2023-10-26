// 질문의 답변 생성 /qna/show.jsp
$(".answerWrite input[type=submit]").click((event) => {
  event.preventDefault(); // submit 이 자동으로 동작하는 것을 막는다.

  const queryString = $("form[name=answer]").serialize();

  $.ajax({
    type: 'post',
    url: '/api/qna/addAnswer',
    data: queryString,
    dataType: 'json',
    success: function(result) {
      const answerTemplate = $("#answerTemplate").html();
      const template = answerTemplate.format(result.writer, new Date(result.createdDate), result.contents, result.answerId);

      $(".qna-comment-slipp-articles").prepend(template);
    },
    error: console.log
  })
});

// 답변 삭제 /qna/show.jsp
$(".qna-comment").on("click", ".form-delete", deleteAnswer);

function deleteAnswer(e) {
  e.preventDefault();

  const deleteBtn = $(this);
  const queryString = deleteBtn.closest("form").serialize();

  $.ajax({
    type : 'post',
    url: '/api/qna/deleteAnswer',
    data: queryString,
    dataType : 'json',
    success: function(result) {
      if(result.status) {
        deleteBtn.closest('article').remove();
      }
    },
    error: console.log
  });
}

String.prototype.format = function() {
  const args = arguments;
  return this.replace(/{\d+}/g, function(match, number) {
    console.log(match, number); // match는 자체를 반환하고, number는 인덱스인가?
    return typeof args[number] != "undefined"
        ? args[number]
        : match;
  })
}

$(document).ready(function(){/* jQuery toggle layout */
  $('#btnToggle').click(function(){
    if ($(this).hasClass('on')) {
      $('#main .col-md-6').addClass('col-md-4').removeClass('col-md-6');
      $(this).removeClass('on');
    }
    else {
      $('#main .col-md-4').addClass('col-md-6').removeClass('col-md-4');
      $(this).addClass('on');
    }
  });


});

