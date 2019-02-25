
$(document).ready(function(){

    console.log(":::::::::::::::::::::::");
 $('#zipsubmit').click(function(event){

     var zipcode = $('#zip').val();

     

       $.ajax({

            type: 'GET', 
			url:'/bin/zipcodevalidation',
            async: false,
            data: 'zipcode='+ zipcode,
            success: function(responseData){
			console.log(JSON.stringify(responseData.validZipcode));

            if(responseData.validZipcode){

            	if(responseData.service=='nextEra'){
         			 	window.open("https://model-nextera.assurant.com/home?s="+responseData.postCustomerData.successId+'&plan='+plan);
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

                alert(responseData.service);
            }


            }

        });

 });
});