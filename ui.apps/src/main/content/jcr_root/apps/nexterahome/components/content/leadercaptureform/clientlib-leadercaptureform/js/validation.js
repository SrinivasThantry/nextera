 function getUrlVars() {
        var vars = {};
        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
            vars[key] = value;
        });
        return vars;
	}

$(document).ready(function(){


    $("#form1Zipcode").on('input',function(e){
		$(this).val($(this).val().replace(/[^0-9]/g,''));
    });

    $("#form2Zipcode").on('input',function(e){
		$(this).val($(this).val().replace(/[^0-9]/g,''));
    });

    $("#phonenumber").on('input',function(e){
		$(this).val($(this).val().replace(/[^0-9]/g,''));
    });

   $('#reEnterEmail').parent().siblings('.error-label').hide();

    $('#phonenumbertype').blur(function(){
        var phonenumbertype = $('#phonenumbertype').val();
        if(phonenumbertype=="none"){
          //  document.getElementById("stateVal").style.display="block";
			$('#phonenumbertype').addClass('error');

			//enteredAllMandatoryFields = false;
        }else{
			$('#phonenumbertype').removeClass('error');
        }
    })


    $('#MailingAddressState').blur(function(){
    var state = $('#MailingAddressState').val();
        if(state=="none"){
          //  document.getElementById("stateVal").style.display="block";
			$('#MailingAddressState').addClass('error');

			//enteredAllMandatoryFields = false;
        }else{
			$('#MailingAddressState').removeClass('error');
        }
    })


    $('#submit').click(function(event){


		var plan = getUrlVars()["plan"];

         var zipcode = $('#form1Zipcode').val();
         var firstName = $('#firstName').val();


		 var lastName = $('#lastName').val();

		 var form1Zipcode = $('#form1Zipcode').val();

		 var email = $('#email').val();

		 var enteredAllMandatoryFields = true;




        if(firstName.length==0){
			$('#firstName').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#firstName').removeClass('error');
        }
		if(lastName.length==0){
			$('#lastName').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#lastName').removeClass('error');
        }

		if(form1Zipcode.length==0 || form1Zipcode.length<5){
			$('#form1Zipcode').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#form1Zipcode').removeClass('error');
        }

		if(email.length==0){
			$('#email').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#email').removeClass('error');
        }


     $.ajax({

            type: 'GET', 
			url:'/bin/zipcodevalidation',
            async: false,
            data: 'zipcode='+ zipcode,
            success: function(responseData){
			console.log(JSON.stringify(responseData.validZipcode));

            if(responseData.validZipcode){

            	if(responseData.service=='nextEra'){
         			 	 event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/serviceplans/serviceplans-illinois.html";
        		}else if(responseData.service=='FPL'){

            			event.preventDefault();
            			window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

        		}else if(responseData.service=='texas'){
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/serviceplans/serviceplans-texas.html";

                    }else{
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }


            }else{

                //alert(responseData.service);
            }


            }

        });




    		

    });





});
