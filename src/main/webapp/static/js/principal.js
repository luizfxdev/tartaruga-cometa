document.addEventListener('DOMContentLoaded', () => {
    const menuItems = document.querySelectorAll('.hex-content[data-menu-item]');

    menuItems.forEach(item => {
        item.addEventListener('click', event => {
            event.preventDefault();
            const menuItemName = item.querySelector('.title').textContent;
            alert(`O menu "${menuItemName}" está em construção!`);
        });
    });
});
