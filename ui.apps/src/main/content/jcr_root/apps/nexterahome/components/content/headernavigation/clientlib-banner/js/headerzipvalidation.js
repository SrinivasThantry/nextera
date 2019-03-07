
$(document).ready(function(){

    var pagename = "";


    console.log(":::::::::enter:");

    $("#closeid").click(function(){
 		   $("#zipcodeGate").removeAttr("style");
           $("#zipcodeGate").attr("style", "display: none;");
         	$("#zipcodeGate").attr("aria-hidden", "true");
           $("#zipcodeGate").removeClass('show');

     });


 	$("#closeidbtn").click(function(){
 		   $("#zipcodeGate").removeAttr("style");
           $("#zipcodeGate").attr("style", "display: none;");
         	$("#zipcodeGate").attr("aria-hidden", "true");
           $("#zipcodeGate").removeClass('show');

     });

	$('a[href="#load_links"]').click(function(event){
	   $('#zipCode').val("");
       var text = event.currentTarget.innerText;
       console.dir("test"+event);
		pagename = text;
       if(text === 'FAQ' || text === 'SERVICE PLANS'){
			$('#zipcodeGate').addClass('show');
            $("#zipcodeGate").attr("style", "display: block; padding-right: 17px;");

       }else{

           $("#zipcodeGate").removeAttr("style");
           $("#zipcodeGate").attr("style", "display: none;");
           $("#zipcodeGate").removeClass('show');
           if(text === 'HOME'){

               event.preventDefault();
               window.location = "/content/nexterahome/en/homestrcture/home.html";

           }else if(text === 'OUR PHILOSOPHY'){
 			   event.preventDefault();
               window.location = "/content/nexterahome/en/homestrcture/ourphilosophy.html";


           }

       }



   })


 $('#formzipcodesubmit').click(function(event){

     var zipcode = $('#zipCode').val();

     	if(zipcode.length == 0)
            return;



       $.ajax({

            type: 'GET', 
			url:'/bin/zipcodevalidation',
            async: false,
            data: 'zipcode='+ zipcode,
            success: function(responseData){
			console.log(JSON.stringify(responseData.validZipcode));

            if(responseData.validZipcode){

           var redirectpath = "";



             if(responseData.service=='nextEra'){
 					if(pagename === 'SERVICE PLANS')
           				 redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-illinois.html';
          		 	if(pagename === 'FAQ')
          				 redirectpath = '/content/nexterahome/en/homestrcture/faq/faq-illinois.html';
         			 	 event.preventDefault();
                        window.location = redirectpath;
        		}else if(responseData.service=='FPL'){

            			event.preventDefault();
            			window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

        		}else if(responseData.service=='texas'){

                    if(pagename === 'SERVICE PLANS')
           				 redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-texas.html';
          		 	if(pagename === 'FAQ')
          				 redirectpath = '/content/nexterahome/en/homestrcture/faq/faq-texas.html';
         			 	 event.preventDefault();
                        window.location = redirectpath;


                    }else{
                        event.preventDefault();
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }


            }else{
            	event.preventDefault();
                window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

               // alert(responseData.service);
            }


            }

        });

 });
});