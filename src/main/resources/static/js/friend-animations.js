// Friend animations
document.addEventListener('DOMContentLoaded', function() {
    initializeModalAnimations();
    initializeTabAnimations();
    initializeSearchFormAnimations();
    initializeResultAnimations();
    initializeRequestAnimations();
    initializeButtonHoverEffects();
});

function initializeModalAnimations() {
    const modal = document.getElementById('friendRequestModal');
    if (modal) {
        modal.addEventListener('show.bs.modal', function() {
            anime({
                targets: '.modal-content',
                scale: [0.8, 1],
                opacity: [0, 1],
                duration: 300,
                easing: 'easeOutElastic(1, .8)'
            });
        });
    }
}

function initializeTabAnimations() {
    const tabs = document.querySelectorAll('[data-bs-toggle="tab"]');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const target = document.querySelector(this.dataset.bsTarget);
            anime({
                targets: target,
                translateX: [100, 0],
                opacity: [0, 1],
                duration: 300,
                easing: 'easeOutQuad'
            });
        });
    });
}

function initializeSearchFormAnimations() {
    const searchForm = document.querySelector('.search-form');
    if (searchForm) {
        anime({
            targets: searchForm,
            translateY: [-20, 0],
            opacity: [0, 1],
            duration: 400,
            delay: 200
        });
    }
}

function initializeResultAnimations() {
    const resultCard = document.querySelector('.result-card');
    if (resultCard) {
        anime({
            targets: '.result-card',
            scale: [0.9, 1],
            opacity: [0, 1],
            duration: 400,
            delay: 300
        });

        anime({
            targets: '.result-avatar',
            scale: [0, 1],
            rotate: [180, 0],
            duration: 600,
            delay: 400
        });

        anime({
            targets: ['.result-name', '.result-phone'],
            translateX: [-20, 0],
            opacity: [0, 1],
            duration: 400,
            delay: 500
        });

        anime({
            targets: '.add-friend-btn',
            scale: [0.8, 1],
            opacity: [0, 1],
            duration: 400,
            delay: 600
        });
    }
}

function initializeRequestAnimations() {
    const requestItems = document.querySelectorAll('.request-item');
    requestItems.forEach((item, index) => {
        anime({
            targets: item,
            translateX: [-50, 0],
            opacity: [0, 1],
            duration: 400,
            delay: index * 100
        });

        anime({
            targets: item.querySelector('.request-avatar'),
            scale: [0, 1],
            rotate: [180, 0],
            duration: 600,
            delay: index * 100 + 100
        });

        anime({
            targets: [item.querySelector('.request-name'), item.querySelector('.request-message')],
            translateX: [-20, 0],
            opacity: [0, 1],
            duration: 400,
            delay: index * 100 + 200
        });

        anime({
            targets: item.querySelector('.request-actions'),
            scale: [0.8, 1],
            opacity: [0, 1],
            duration: 400,
            delay: index * 100 + 300
        });
    });
}

function initializeButtonHoverEffects() {
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            anime({
                targets: this,
                scale: 1.05,
                duration: 200
            });
        });

        button.addEventListener('mouseleave', function() {
            anime({
                targets: this,
                scale: 1,
                duration: 200
            });
        });
    });
} 