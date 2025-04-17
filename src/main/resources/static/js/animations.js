// Animation configurations
const animations = {
    // Animation for news posts
    newsPost: {
        targets: '.news-post',
        translateY: [50, 0],
        opacity: [0, 1],
        duration: 800,
        delay: anime.stagger(100),
        easing: 'easeOutExpo'
    },

    // Animation for project cards
    projectCard: {
        targets: '.project-card',
        scale: [0.95, 1],
        opacity: [0, 1],
        duration: 600,
        delay: anime.stagger(100),
        easing: 'easeOutExpo'
    },

    // Animation for buttons
    buttonHover: {
        scale: 1.05,
        duration: 200,
        easing: 'easeOutExpo'
    },

    // Animation for modal
    modalOpen: {
        targets: '#projectDetailsModal .modal-content',
        scale: [0.9, 1],
        opacity: [0, 1],
        duration: 400,
        easing: 'easeOutExpo'
    },

    // Animation for user avatars
    avatarHover: {
        scale: 1.1,
        duration: 300,
        easing: 'easeOutExpo'
    },

    // Animation for search bar
    searchBarFocus: {
        scale: 1.02,
        duration: 300,
        easing: 'easeOutExpo'
    }
};

// Initialize animations when document is ready
document.addEventListener('DOMContentLoaded', function() {
    // Animate news posts
    anime(animations.newsPost);

    // Animate project cards
    anime(animations.projectCard);

    // Add hover effects to buttons
    document.querySelectorAll('.btn').forEach(button => {
        button.addEventListener('mouseenter', () => {
            anime({
                targets: button,
                ...animations.buttonHover
            });
        });
        button.addEventListener('mouseleave', () => {
            anime({
                targets: button,
                scale: 1,
                duration: 200,
                easing: 'easeOutExpo'
            });
        });
    });

    // Add hover effects to avatars
    document.querySelectorAll('.avatar, .participant-avatar').forEach(avatar => {
        avatar.addEventListener('mouseenter', () => {
            anime({
                targets: avatar,
                ...animations.avatarHover
            });
        });
        avatar.addEventListener('mouseleave', () => {
            anime({
                targets: avatar,
                scale: 1,
                duration: 300,
                easing: 'easeOutExpo'
            });
        });
    });

    // Add focus effect to search bar
    const searchInput = document.querySelector('input[type="search"]');
    if (searchInput) {
        searchInput.addEventListener('focus', () => {
            anime({
                targets: searchInput,
                ...animations.searchBarFocus
            });
        });
        searchInput.addEventListener('blur', () => {
            anime({
                targets: searchInput,
                scale: 1,
                duration: 300,
                easing: 'easeOutExpo'
            });
        });
    }

    // Animate modal when opened
    const projectModal = document.getElementById('projectDetailsModal');
    if (projectModal) {
        projectModal.addEventListener('show.bs.modal', () => {
            anime(animations.modalOpen);
        });
    }
}); 