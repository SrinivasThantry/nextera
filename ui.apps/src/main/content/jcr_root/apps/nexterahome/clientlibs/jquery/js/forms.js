$(document).ready(function(){

//material contact form animation
var floatingField = $('.material-form .floating-field').find('.form-control');
var formItem = $('.material-form .input-block').find('.form-control');

//##case 1 for default style
//on focus
formItem.focus(function() {
  $(this).parent('.input-block').addClass('focus');
});
//removing focusing
formItem.blur(function() {
  $(this).parent('.input-block').removeClass('focus');
});

//##case 2 for floating style
//initiating field
floatingField.each(function() {
  var targetItem = $(this).parent();
  if ($(this).val()) {
    $(targetItem).addClass('has-value');
  }
});

//on typing
floatingField.blur(function() {
  $(this).parent('.input-block').removeClass('focus');
  //if value is not exists
  if ($(this).val().length == 0) {
    $(this).parent('.input-block').removeClass('has-value');
  }else{
      $(this).parent('.input-block').addClass('has-value');
  }
});





// show/hide additional sections

$('#serviceAddressChk').click(function() {
    if( $('#serviceAddressChk').is(':checked')) {
        $("#serviceAddressSct").show();
    } else {
        $("#serviceAddressSct").hide();
    }
});  

});