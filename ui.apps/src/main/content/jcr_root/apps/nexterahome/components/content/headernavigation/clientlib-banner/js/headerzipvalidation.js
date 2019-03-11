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
    $('#footerfaq a').click(function(evt) {

        evt.preventDefault();
        zipvalidate(evt);

    });
    
    
    function zipvalidate(event) {

        $('#zipCode').val("");
        var text = event.currentTarget.innerText;
        console.dir("test" + event);
        pagename = text;
        //HAVE A QUESTION?
        if (text === 'FAQ' || text === 'SERVICE PLANS' || text === 'FIND A PLAN' || text === 'HAVE A QUESTION?') {
            //event.currentTarget.className = "nav-item active";
        	if(text === 'HAVE A QUESTION?' || text === 'FAQ')
        		pagename = 'FAQ';
            $('#zipcodeGate').addClass('show');
            $("#zipcodeGate").attr("style", "display: block; padding-right: 17px;");


        } else {

            $("#zipcodeGate").removeAttr("style");
            $("#zipcodeGate").attr("style", "display: none;");
            $("#zipcodeGate").removeClass('show');
            if (text === 'HOME') {

                event.preventDefault();
                window.location = "/content/nexterahome/en/homestrcture/home.html";

            } else if (text === 'OUR PHILOSOPHY') {
                event.preventDefault();
                window.location = "/content/nexterahome/en/homestrcture/ourphilosophy.html";


            }

        }
        return;
    }



    $('#formzipcodesubmit').click(function(event) {
        var zipcode = $('#zipCode').val();
       
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

                    } else if (responseData.service == 'FPL') {

                        event.preventDefault();
                        pagename="";
                        window.location = "/content/nexterahome/en/homestrcture/thank-you-fpl.html";

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


                    } else {
                        event.preventDefault();
                        pagename="";
                        window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    }


                } else {
                    event.preventDefault();
                    pagename="";
                    window.location = "/content/nexterahome/en/homestrcture/thank-you.html";

                    // alert(responseData.service);
                }


            }

        });

    });
});