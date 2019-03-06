
$(document).ready(function(){

 $('#howitworkzip').click(function(event){

     var zipcode = $('#ziphowitworks').val();


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
                        window.location = "/content/nexterahome/en/homestrcture/serviceplans-illinois.html";
        		}else if(responseData.service=='FPL'){

            			event.preventDefault();
            			window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

        		}else if(responseData.service=='texas'){
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/serviceplans-texas.html";

                    }else{
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }


            }else{
            	event.preventDefault();
                window.location = "/content/nexterahome/en/homestrcture/thank-you.html";
              //  alert(responseData.service);
            }


            }

        });

 });
});