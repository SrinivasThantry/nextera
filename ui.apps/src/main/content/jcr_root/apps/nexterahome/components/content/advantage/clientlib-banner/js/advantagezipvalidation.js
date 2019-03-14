
$(document).ready(function(){
$('.disclaimer a').attr('data-toggle', 'modal').attr('data-target', '#zipcodeGate');
    $("#zipadvantage").on('input',function(e){
		$(this).val($(this).val().replace(/[^0-9]/g,''));
    });


    console.log(":::::::::::::::::::::::");
 $('#advantagezip').click(function(event){

     var zipcode = $('#zipadvantage').val();

     

      /* $.ajax({

            type: 'GET', 
			url:'/bin/zipcodevalidation',
            async: false,
            data: 'zipcode='+ zipcode,
            success: function(responseData){
			console.log(JSON.stringify(responseData.validZipcode));

            if(responseData.validZipcode){

            	if(responseData.service=='nextEra'){
         			 	 event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/faq-illinois.html";
        		}else if(responseData.service=='FPL'){

            			event.preventDefault();
            			window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

        		}else if(responseData.service=='texas'){
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/faq-texas.html";

                    }else{
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }


            }else{

                alert(responseData.service);
            }


            }

        });*/

 });
});