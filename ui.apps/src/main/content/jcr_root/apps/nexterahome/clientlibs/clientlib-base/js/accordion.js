$(document).ready(function($) { 
    $('#accordion').find('.title-wrap').click(function(){

      //Expand or collapse this panel
      $(this).next().slideToggle('fast');

      if($('.accordion-content').length > 0){
        $(this).find('.open-icon').toggle();
        $(this).find('.close-icon').toggle();
      }

      //Hide the other panels
      $(".accordion-content").not($(this).next()).slideUp('fast');
      $('.open-icon').not($(this).find(".open-icon")).show();
      $('.close-icon').not($(this).find(".close-icon")).hide();
    });
  });