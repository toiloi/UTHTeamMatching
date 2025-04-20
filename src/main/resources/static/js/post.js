function deletePost(button) {
    if (!confirm('Bạn có chắc chắn muốn xóa bài viết này?')) {
        return;
    }

    const articleId = button.getAttribute('data-article-id');
    fetch('/post/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `articleId=${articleId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Remove the post from the UI
            button.closest('.news-post').remove();
            // Show success message
            alert(data.message);
        } else {
            alert(data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi xóa bài viết');
    });
} 