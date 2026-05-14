/* =====================================================================
   RocketSim - Lightweight client script
   ---------------------------------------------------------------------
   Adds two small behaviours on top of the otherwise server-rendered UI:
     1) Mobile hamburger menu toggle.
     2) "Are you sure?" confirmation on any delete-form submission
        (forms that carry the data-confirm attribute).
   ===================================================================== */
(function () {
    'use strict';

    // 1) Hamburger toggle
    var toggle = document.getElementById('navToggle');
    var nav    = document.getElementById('topNav');
    if (toggle && nav) {
        toggle.addEventListener('click', function () {
            nav.classList.toggle('open');
        });
    }

    // 2) Confirm before submitting any form marked data-confirm
    var forms = document.querySelectorAll('form[data-confirm]');
    forms.forEach(function (f) {
        f.addEventListener('submit', function (e) {
            var msg = f.getAttribute('data-confirm') || 'Are you sure?';
            if (!window.confirm(msg)) {
                e.preventDefault();
            }
        });
    });

    // 3) Auto-dismiss alert banners after 5 seconds (gentle fade)
    var alerts = document.querySelectorAll('.alert.alert-success, .toast.success');
    alerts.forEach(function (el) {
        setTimeout(function () {
            el.style.transition = 'opacity .5s ease';
            el.style.opacity = '0';
            setTimeout(function () { el.remove(); }, 500);
        }, 5000);
    });
})();
