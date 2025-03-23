(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    
    // Initiate the wowjs
    new WOW().init();


    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 1) {
            $('.sticky-top').css('top', '0px');
        } else {
            $('.sticky-top').css('top', '-100px');
        }
    });
    
    
    // Dropdown on mouse hover
    const $dropdown = $(".dropdown");
    const $dropdownToggle = $(".dropdown-toggle");
    const $dropdownMenu = $(".dropdown-menu");
    const showClass = "show";
    
    $(window).on("load resize", function() {
        if (this.matchMedia("(min-width: 992px)").matches) {
            $dropdown.hover(
            function() {
                const $this = $(this);
                $this.addClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "true");
                $this.find($dropdownMenu).addClass(showClass);
            },
            function() {
                const $this = $(this);
                $this.removeClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "false");
                $this.find($dropdownMenu).removeClass(showClass);
            }
            );
        } else {
            $dropdown.off("mouseenter mouseleave");
        }
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Header carousel
    $(".header-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        items: 1,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-chevron-left"></i>',
            '<i class="bi bi-chevron-right"></i>'
        ]
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        center: true,
        margin: 24,
        dots: true,
        loop: true,
        nav : false,
        responsive: {
            0:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            }
        }
    });
    

    // Sidebar Toggle
    document.addEventListener('DOMContentLoaded', function() {
        const sidebar = document.querySelector('.sidebar');
        const contentWrapper = document.querySelector('.content-wrapper');
        const navbar = document.querySelector('.navbar');
        const toggleBtn = document.getElementById('toggle-sidebar');
        
        // Toggle sidebar on button click
        toggleBtn.addEventListener('click', function() {
            sidebar.classList.toggle('collapsed');
            contentWrapper.classList.toggle('sidebar-collapsed');
            navbar.classList.toggle('sidebar-collapsed');
            
            // Force recalculation of layout
            setTimeout(() => {
                window.dispatchEvent(new Event('resize'));
            }, 300);
        });
        
        // Handle responsive behavior
        function handleResize() {
            const width = window.innerWidth;
            
            if (width <= 768) {
                sidebar.classList.add('collapsed');
                contentWrapper.classList.add('sidebar-collapsed');
                navbar.classList.add('sidebar-collapsed');
                
                // Adjust navbar width for mobile
                navbar.style.width = '100%';
            } else {
                if (!sidebar.classList.contains('collapsed')) {
                    contentWrapper.style.width = `calc(100% - 250px)`;
                    navbar.style.left = '250px';
                } else {
                    contentWrapper.style.width = `calc(100% - 70px)`;
                    navbar.style.left = '70px';
                }
                
                // Reset navbar width for desktop
                navbar.style.width = '';
            }
        }
        
        // Listen for window resize
        window.addEventListener('resize', handleResize);
        
        // Initial check
        handleResize();
    });

})(jQuery);
function switchTab(tab) {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const tabs = document.querySelectorAll('.auth-tab');
    
    if (tab === 'login') {
        registerForm.style.opacity = 0;
        setTimeout(() => {
            loginForm.style.display = 'block';
            registerForm.style.display = 'none';
            setTimeout(() => {
                loginForm.style.opacity = 1;
            }, 50);
        }, 200);
        tabs[0].classList.add('active');
        tabs[1].classList.remove('active');
    } else {
        loginForm.style.opacity = 0;
        setTimeout(() => {
            loginForm.style.display = 'none';
            registerForm.style.display = 'block';
            setTimeout(() => {
                registerForm.style.opacity = 1;
            }, 50);
        }, 200);
        tabs[0].classList.remove('active');
        tabs[1].classList.add('active');
    }
}

// Add loading animation
window.addEventListener('load', function() {
    setTimeout(function() {
        document.getElementById('spinner').classList.remove('show');
    }, 1000);
});
