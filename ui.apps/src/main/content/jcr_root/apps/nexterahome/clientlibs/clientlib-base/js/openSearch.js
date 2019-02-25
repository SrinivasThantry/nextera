function open_search(elmnt) {
    var a = document.getElementById("googleSearch");
    if (a.style.display == "") {
      a.style.display = "none";
      a.style.paddingRight = "";
      elmnt.innerHTML = "&#xe802;";    
    } else {
      a.style.display = "";  
      if (window.innerWidth > 700) {
        a.style.width = "40%";
      } else {
        a.style.width = "80%";
      }
      if (document.getElementById("gsc-i-id1")) {document.getElementById("gsc-i-id1").focus(); }
      elmnt.innerHTML = "<span style='font-family:verdana;font-weight:bold;'>X</span>";
    }
}