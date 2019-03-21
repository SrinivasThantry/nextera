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
    	 if (text === 'HOME' || text === 'OUR PHILOSOPHY') {
    	 }else{
    		 console.log("entered here111");
    		 event.preventDefault(); 
    	 
         pagename = text;
         if(text === 'HAVE A QUESTION?' || text === 'FAQ')
     		pagename = 'FAQ';
         
        $('#zipCode').val("");
        if(checkStorage('zipcodeobj', 10)){
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

    
    
    function compareDates(date1, date2, time) {
        time = time ? time : 'm';
        var dateStart     = new Date(date1),
            dateEnd       = date2 != '' ? new Date(date2) : new Date(),
            one_min       = 1000 * 60,
            one_hour      = one_min * 60,
            one_day       = one_hour * 24,
            date1_ms      = dateStart.getTime(),
            date2_ms      = dateEnd.getTime(),
            difference_ms = date2_ms - date1_ms;
        if (time === 'd') {
          return Math.floor(difference_ms / one_day);
        } else if (time === 'h') {
          return Math.floor(difference_ms / one_hour);
        }
        return Math.floor(difference_ms / one_min);
      }
    
    
    function checkStorage (key, m) {
        m = m ? m : 20;
    	var item = JSON.parse(sessionStorage.getItem('zipcodeobj'));
        if (item === undefined || item === null) {
          return false;
        }
        if (item.zipcode === '') {
            return false;
          }
        if(item === null)
        	return false;
        if (compareDates(item.created, new Date().toISOString(), 'm') > m) {
          return false;
        }
        return true;
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
                    newStorage.created = new Date().toISOString();
                    newStorage.zipcode = zipcode;        
                    sessionStorage.setItem('zipcodeobj', JSON.stringify(newStorage));
                	
                }

            }

        });

    
    }
});