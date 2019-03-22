$(document).ready(function() {

    var pagename = "";


    console.log(":::::::::enter:");

    $("#closeid").click(function() {
        $("#zipcodeGate").removeAttr("style");
        $("#zipcodeGate").attr("style", "display: none;");
        $("#zipcodeGate").attr("aria-hidden", "true");
        $("#zipcodeGate").removeClass('show');

    });


    $("#closeidbtn").click(function() {
        $("#zipcodeGate").removeAttr("style");
        $("#zipcodeGate").attr("style", "display: none;");
        $("#zipcodeGate").attr("aria-hidden", "true");
        $("#zipcodeGate").removeClass('show');

    });

    $('#anchorlist > a').click(zipvalidate);
    
    $('#ancherfooterid > a').click(zipvalidate);
    
    $('#advantagezipqry a').click(function(evt) {

        evt.preventDefault();
        zipvalidate(evt);

    });
    $('#footerfaq > a').click(function(event) {
    	console.log("debug");
    	 var text = event.currentTarget.innerText;
    	 
    	 if (text === 'HOME' || text === 'OUR PHILOSOPHY' || text === 'LOGIN') {
    		 return;
    	 }else{
    		 console.log("entered here");
    		 event.preventDefault();
    		 zipvalidate(event);
    	 }
    	 

    });
    $('#advantageid a').click(function(event) {
    	console.log("debug");
   	 var text = event.currentTarget.innerText;
   	 
   	 if (text === 'HOME' || text === 'OUR PHILOSOPHY' || text === 'LOGIN') {
   		 return;
   	 }else{
   		 console.log("entered here");
   		event.preventDefault();
       zipvalidate(event);
   	 }
   	 

   });
    $('#headerfaq > a').click(function(event) {
    	console.log("debug");
   	 var text = event.currentTarget.innerText;
   	 
   	 if (text === 'HOME' || text === 'OUR PHILOSOPHY' || text === 'LOGIN') {
   		 return;
   	 }else{
   		 console.log("entered here");
   		event.preventDefault();
       zipvalidate(event);
   	 }
   	 

   });
   
    
    function zipvalidate(event) {
    	
    	 var text = event.currentTarget.innerText;
    	 text = text.trim();
    	 text = text.toUpperCase();
    	 if (text === 'HOME' || text === 'OUR PHILOSOPHY' || text === 'JOIN US' || text === 'LOGIN') {
    	 }else{
    		 console.log("entered here111");
    		 event.preventDefault(); 
    	 
         pagename = text;
         if(text === 'HAVE A QUESTION?' || text === 'FAQ')
     		pagename = 'FAQ';
         
        $('#zipCode').val("");
        console.log(checkStorage('zipcodeobj', 1));
        if(checkStorage('zipcodeobj', 1)){
        	var obj = JSON.parse(sessionStorage.getItem('zipcodeobj'));
        	console.log(obj.zipcode);
        	submitzip(event,obj.zipcode);
        }else{
        	sessionStorage.removeItem('zipcodeobj');
        	sessionStorage.clear();
        	text = text.trim();
        if (text === 'FAQ' || text === 'SERVICE PLANS' || text === 'FIND A PLAN' || text === 'HAVE A QUESTION?' || text === "service plans" || text === "VIEW SERVICE PLANS" || text === "READY TO ENROLL?") {
        	if(text === 'HAVE A QUESTION?' || text === 'FAQ')
        		pagename = 'FAQ';
            $('#zipcodeGate').addClass('show');
            $("#zipcodeGate").attr("style", "display: block; padding-right: 17px;");


        } else {

            $("#zipcodeGate").removeAttr("style");
            $("#zipcodeGate").attr("style", "display: none;");
            $("#zipcodeGate").removeClass('show');
           

        }
    }
        return;
    	 }
    }

    
    
    
    function checkStorage (key, m) {
        m = m ? m : 1;
    	var item = JSON.parse(sessionStorage.getItem('zipcodeobj'));
        if (item === undefined || item === null) {
          return false;
        }
        if (item.zipcode === '') {
            return false;
          }
        if(item === null)
        	return false;
        
        var expirationDate = new Date(item.created);
        console.log(expirationDate+":::::::::::"+new Date());
        if (expirationDate > new Date()) {
            return true;
          }
        
        return false;
      };
    

    $('#formzipcodesubmit').click(function(event) {
    	
    	submitzip(event, undefined);
    });
    
    function submitzip(event,zip){
    	
    	var zipcode = "";
    	if(zip === undefined){    	
    		zipcode = $('#zipCode').val();
    		
    	}
    	else
    		zipcode = zip;
       
        
       
        if (zipcode.length == 0){
        	alert('Please Enter a Zip Code');
        	  return false;
        }
        
        
        


        $.ajax({

            type: 'GET',
            url: '/bin/zipcodevalidation',
            async: false,
            data: 'zipcode=' + zipcode,
            success: function(responseData) {
            	
            	var hasvalidzipcode = false;
                console.log(JSON.stringify(responseData.validZipcode));

                if (responseData.validZipcode) {

                    var redirectpath = "";


                    if (responseData.service == 'nextEra') {
                        
                        if (pagename === 'SERVICE PLANS')
                            redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-illinois.html';
                        if (pagename === 'FAQ')
                            redirectpath = '/content/nexterahome/en/homestrcture/faq/faq-illinois.html';
                        if(redirectpath === '')
                        	redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-illinois.html';
                        event.preventDefault();
                        pagename="";
                        window.location = redirectpath;
                        hasvalidzipcode=true;

                    } else if (responseData.service == 'FPL') {

                        event.preventDefault();
                        pagename="";
                        window.location = "/content/nexterahome/en/homestrcture/leadercaptureform.html";
                        hasvalidzipcode = false;
                    } else if (responseData.service == 'texas') {

                        if (pagename === 'SERVICE PLANS')
                            redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-texas.html';
                        if (pagename === 'FAQ')
                            redirectpath = '/content/nexterahome/en/homestrcture/faq/faq-texas.html';
                        if(redirectpath === '')
                        	redirectpath = '/content/nexterahome/en/homestrcture/serviceplans/serviceplans-texas.html';
                        event.preventDefault();
                        pagename="";
                        window.location = redirectpath;

                        hasvalidzipcode=true;
                    } else {
                        event.preventDefault();
                        pagename="";
                        window.location = "/content/nexterahome/en/homestrcture/leadercaptureform.html";
                        hasvalidzipcode = false;
                    }


                } else {
                    event.preventDefault();
                    pagename="";
                    window.location = "/content/nexterahome/en/homestrcture/leadercaptureform.html";
                    hasvalidzipcode = false;
                    // alert(responseData.service);
                }
                
                /////////store session storage based on response
                
                if(hasvalidzipcode){
                	
                	
                	var newStorage = {};
                	newStorage.created = new Date(new Date().getTime() + (60000 * 10))
                    newStorage.zipcode = zipcode;        
                    sessionStorage.setItem('zipcodeobj', JSON.stringify(newStorage));
                	
                }

            }

        });

    
    }
});