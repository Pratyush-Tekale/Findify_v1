const links = document.querySelectorAll('a[href^="#"]');

links.forEach(link => {

    link.addEventListener("click", function(e){

        const target = document.querySelector(this.getAttribute("href"));

        if(target){

            e.preventDefault();

            target.scrollIntoView({

                behavior:"smooth"

            });

        }

    });

});

window.addEventListener("scroll",()=>{

    const header=document.querySelector("header");

    if(window.scrollY>40){

        header.style.boxShadow="0 5px 18px rgba(0,0,0,.18)";

    }

    else{

        header.style.boxShadow="none";

    }

});

const year=new Date().getFullYear();

const copyright=document.getElementById("copyright");

if(copyright){

    copyright.innerHTML=`© ${year} FINDIFY — COLLEGE PROJECT`;

}
/* ===========================
   LOGIN REQUIRED LINKS
=========================== */

const protectedLinks = document.querySelectorAll(".login-required");

protectedLinks.forEach(function(link) {

    link.addEventListener("click", function(e) {

        if (!isLoggedIn) {

            e.preventDefault();

            alert("Please log in first.");

            window.location.href = "login.jsp";
        }

    });

});