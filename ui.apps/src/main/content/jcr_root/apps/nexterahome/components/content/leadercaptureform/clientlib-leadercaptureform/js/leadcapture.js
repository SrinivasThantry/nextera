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
        var atposition=email.indexOf("@");
        var dotposition=email.lastIndexOf(".");


        var myCheck = $('#myCheck').val();

		 var enteredAllMandatoryFields = true;

		 var phonenumbertype = $('#phonenumbertype').val();
        var phonenumber = $('#phonenumber').val();
        var comments = $('#comments').val();
        

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

		if((email.length==0)||(atposition<1 || dotposition<atposition+2 || dotposition+2>=email.length)){
			$('#email').addClass('error');
             document.getElementById('errorname').innerHTML="Please enter Valid email address";
             document.getElementById("errorname").style.color="red";
			enteredAllMandatoryFields = false;
        }else{
			$('#email').removeClass('error');
        }

        if(phonenumber.length==0){
			$('#phonenumber').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#phonenumber').removeClass('error');
        }

        if(phonenumbertype.length==0 || phonenumbertype=="none"){

			$('#phonenumbertype').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#phonenumbertype').removeClass('error');
        }

         if(!this.form.checkbox.checked)
{
   // alert('You must agree to receive information updates and marketing offers by email.');
    document.getElementById("legalAgreementCopy").style.color="red";
    return false;
}


		if(!enteredAllMandatoryFields)
			return false;

     $.ajax({

            type: 'POST', 
			url:'/bin/leadcapture',
            async: false,
            data: 'FirstName='+ firstName +'&LastName='+ lastName +'&EmailAddress='+ email +'&zipCode='+ zipcode+'&phonenumber='+ phonenumber+'&phonenumbertype='+ phonenumbertype+'&comments='+ comments,
            success: function(responseData){
			console.log(JSON.stringify(responseData.service));

            if(responseData.service != 'No Service available'){

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
                       event.preventDefault();

                window.location = "/content/nexterahome/en/homestrcture/thank-you.html";
            }


            }

        });




    		

    });





});