(function() {
  'use strict';
  
  // Aplicar tema ANTES do DOM para evitar flash de tela branca
  const savedTheme = localStorage.getItem('theme') || 'light';
  document.documentElement.setAttribute('data-theme', savedTheme);
  
  document.addEventListener('DOMContentLoaded', function() {
    const themeToggle = document.getElementById('theme-toggle');
    const html = document.documentElement;
    const logoImg = document.getElementById('navbar-logo-img');
    
    // Aplicar tema completo após DOM carregar
    applyTheme(savedTheme);
    
    // Listener do botão
    if (themeToggle) {
      themeToggle.addEventListener('click', function(e) {
        e.preventDefault();
        const currentTheme = html.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        applyTheme(newTheme);
        localStorage.setItem('theme', newTheme);
      });
    }
    
    function applyTheme(theme) {
      // Aplicar data-theme no html e body
      html.setAttribute('data-theme', theme);
      document.body.setAttribute('data-theme', theme);
      
      // Classes para compatibilidade extra
      if (theme === 'dark') {
        html.classList.add('dark');
        document.body.classList.add('dark');
      } else {
        html.classList.remove('dark');
        document.body.classList.remove('dark');
      }
      
      updateThemeIcon(theme);
      updateLogo(theme);
    }
    
    function updateThemeIcon(theme) {
      const sunIcon = document.getElementById('sun-icon');
      const moonIcon = document.getElementById('moon-icon');
      
      if (!sunIcon || !moonIcon) return;
      
      // Dark mode: mostra sol (para voltar ao claro)
      // Light mode: mostra lua (para ir ao escuro)
      if (theme === 'dark') {
        sunIcon.classList.add('active');
        moonIcon.classList.remove('active');
      } else {
        sunIcon.classList.remove('active');
        moonIcon.classList.add('active');
      }
    }
    
    function updateLogo(theme) {
      if (!logoImg) return;
      
      const lightLogo = logoImg.getAttribute('data-light-logo');
      const darkLogo = logoImg.getAttribute('data-dark-logo');
      
      if (!lightLogo || !darkLogo) return;
      
      logoImg.src = theme === 'dark' ? darkLogo : lightLogo;
    }
  });
})();