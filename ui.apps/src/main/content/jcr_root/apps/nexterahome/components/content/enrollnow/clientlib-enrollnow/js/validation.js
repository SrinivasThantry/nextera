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
      	 var promocode = $('#promocode').val();
         var phonenumbertype = $('#phonenumbertype').val();
		 var phonenumber = $('#phonenumber').val();
		 var mailingAddress = $('#mailingAddress').val();
		 var mailingAddressOptional = $('#mailingAddressOptional').val();
		 var form1Zipcode = $('#form1Zipcode').val();
		 var city = $('#city').val();
		 var state = $('#MailingAddressState').val();
		 var email = $('#email').val();
		 var reEnterEmail = $('#reEnterEmail').val();
		 var serviceAddressChk = $('#serviceAddressChk').is(':checked')
		 var legalAgreementCopy = $('#legalAgreementCopy').val();
		 var serviceAddressSct = $('#serviceAddressSct').val();
		 var firstNameSecondary = $('#firstNameSecondary').val();
		 var lastNameSecondary = $('#lastNameSecondary').val();
         var promocodeSecondary = $('#promocodeSecondary').val();
         var phonenumbertypeSecondary = $('#phonenumbertypeSecondary').val();
		 var phonenumberSecondary = $('#phonenumberSecondary').val();
		 var mailingAddressSecondary = $('#mailingAddressSecondary').val();
		 var mailingAddressOptionalSecondary = $('#mailingAddressOptionalSecondary').val();
		 var form2Zipcode = $('#form2Zipcode').val();
		 var citySecondary = $('#citySecondary').val();
		 var stateSecondary = $('#stateSecondary').val();
		 var emailSecondary = $('#emailSecondary').val();	  
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
		if(mailingAddress.length==0){
			$('#mailingAddress').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#mailingAddress').removeClass('error');
        }
        if(phonenumbertype=="none"){
			$('#phonenumbertype').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#phonenumbertype').removeClass('error');
        }
		if(phonenumber.length==0){
			$('#phonenumber').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#phonenumber').removeClass('error');
        }
		if(form1Zipcode.length==0 || form1Zipcode.length<5){
			$('#form1Zipcode').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#form1Zipcode').removeClass('error');
        }
		if(city.length==0){
			$('#city').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#city').removeClass('error');
        }
		if(state=="none"){
          //  document.getElementById("stateVal").style.display="block";
			$('#MailingAddressState').addClass('error');

			enteredAllMandatoryFields = false;
        }else{
			$('#MailingAddressState').removeClass('error');
        }
		if(email.length==0){
			$('#email').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#email').removeClass('error');
        }
		if(reEnterEmail.length==0){
			$('#reEnterEmail').addClass('error');
			enteredAllMandatoryFields = false;
        }else{
			$('#reEnterEmail').removeClass('error');
        }
        if(email!=reEnterEmail){
			$('#reEnterEmail').addClass('error');
            $('#reEnterEmail').parent().siblings('.error-label').show();
            enteredAllMandatoryFields = false;
        }else{
			$('#reEnterEmail').removeClass('error');
            $('#reEnterEmail').parent().siblings('.error-label').hide();
        }

		if(serviceAddressChk){

				if(firstNameSecondary.length==0){
					$('#firstNameSecondary').addClass('error');
					enteredAllMandatoryFields = false;
				}else{
					$('#firstNameSecondary').removeClass('error');
        		}
				if(lastNameSecondary.length==0){
					$('#lastNameSecondary').addClass('error');
					enteredAllMandatoryFields = false;
				}else{
					$('#lastNameSecondary').removeClass('error');
       			}
				if(mailingAddressSecondary.length==0){
					$('#mailingAddressSecondary').addClass('error');
					enteredAllMandatoryFields = false;
				}else{
					$('#mailingAddressSecondary').removeClass('error');
       			}
       			 if(phonenumbertypeSecondary=="none"){
					$('#phonenumbertypeSecondary').addClass('error');
					enteredAllMandatoryFields = false;
      		    }else{
					$('#phonenumbertypeSecondary').removeClass('error');
        	   }
				if(phonenumberSecondary.length==0){
					$('#phonenumberSecondary').addClass('error');
					enteredAllMandatoryFields = false;
        	   }else{
					$('#phonenumberSecondary').removeClass('error');
       		   }
				if(form2Zipcode.length==0 || form2Zipcode.length<5){
					$('#form2Zipcode').addClass('error');
					enteredAllMandatoryFields = false;
				}else{
					$('#form2Zipcode').removeClass('error');
        		}
				if(citySecondary.length==0){
					$('#citySecondary').addClass('error');
					enteredAllMandatoryFields = false;
				}else{
					$('#citySecondary').removeClass('error');
        		}
				if(stateSecondary=="none"){
          //  document.getElementById("stateVal").style.display="block";
			$('#stateSecondary').addClass('error');

			enteredAllMandatoryFields = false;
        }else{
			$('#stateSecondary').removeClass('error');
        }
				/*if(emailSecondary.length==0){
					$('#emailSecondary').parent().addClass('error');
					enteredAllMandatoryFields = false;
				}
        else{
					$('#emailSecondary').parent().removeClass('error');
       			 }*/

		}


        var PlanName="";
        var MarketingProgramId="";
        var Deductible="";
        if(plan=="Healthy%20Appliances%20Plan"){
            PlanName="HealthyAppliances";
            MarketingProgramId="8602";
            Deductible="125";
        }else if(plan=='Healthy%20Systems%20Plan'){
            PlanName="HealthySystems";
            MarketingProgramId="8605";
            Deductible="125";
        }else if(plan=="Healthy%20Home%20Plan"){
            PlanName="HealthyHome";
            MarketingProgramId="8599";
            Deductible="125";
        }
	var IsMailingAddressSameasCoverageAddress = !serviceAddressChk;
	if(enteredAllMandatoryFields){
         if(serviceAddressChk){
			firstName = firstNameSecondary;
            lastName = lastNameSecondary;
			promocode = promocodeSecondary;
            phonenumbertype = phonenumbertypeSecondary;
            phonenumber = phonenumberSecondary;
            mailingAddress = mailingAddressSecondary;
            mailingAddressOptional = mailingAddressOptionalSecondary;
			form1Zipcode = form2Zipcode;
            city = citySecondary;
            state = stateSecondary;
           // email = emailSecondary;
        }
         
     $.ajax({

            type: 'POST', 
			url:'/bin/enrollnowformsubmit',
            async: false,
            data: 'FirstName='+ firstName +'&LastName='+ lastName +'&EmailAddress='+ email +'&ConfirmEmailAddress='+ reEnterEmail +'&MarketingOptIn=true'+'&AddressLine1='+ mailingAddress +'&AddressLine2='+ mailingAddressOptional + '&ZipCode='+ form1Zipcode + '&City='+ city + '&State='+ state + '&IsMailingAddressSameasCoverageAddress='+ IsMailingAddressSameasCoverageAddress +'&PlanName='+ PlanName +'&MarketingProgramId='+ MarketingProgramId +'&Deductible='+ Deductible + '&promocode='+ promocode +'&phonenumbertype='+ phonenumbertype +'&phonenumber='+ phonenumber+'&CustomerIdentifier=121354',
            success: function(responseData){
			console.log(JSON.stringify(responseData.postCustomerData));
			var obj = JSON.parse(responseData.postCustomerData);
            if(obj.message == 'Success'){

            	if(responseData.service=='nextEra'){
         			 	window.open("https://model-nextera.assurant.com/home?s="+obj.successId+'&plan='+plan);
            			event.preventDefault();
            			location.reload();
        		}else if(responseData.service=='FPL'){

            			event.preventDefault();
            			window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

        		}else{
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }

            }else{
                console.log(obj.message);
                alert(obj.message);
            }


            }

        });






    		// Do an Ajax call to validate next Era zipcode - change this to jquery ajax
                 var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                     var obj = JSON.parse(this.responseText);
                     if(obj.service!=='No Service available'){
                         if(obj.service=="nextEra"){
							$('#submitRedirect').attr('href','https://www.assurant.com/');
                         }else if(obj.service=="FPL"){
							$('#submitRedirect').attr('href','/content/nexterahome/en/homestrcture/thank-you-fpl.html');
                         }
                     }else{
                         $('#submitRedirect').attr('href','/content/nexterahome/en/homestrcture/thank-you.html');
                     }
                 }
                };
              xhttp.open("POST", requestUrl ,data,false);
              xhttp.send();
    

	}else{
			// Stopping the submit action
			//event.preventDefault();
        return false;
	}


	//sample nextEra zipcode 76267
    //sample  FPL zipcode 32044    



    });





});
