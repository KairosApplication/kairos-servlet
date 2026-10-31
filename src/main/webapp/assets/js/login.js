/*
 * Alterna a visibilidade da senha.
 */
function toggleSenha() {

    const input = document.getElementById("senha");
    const icon = document.getElementById("eyeIcon");

    /*
     * Impede a alteração após o bloqueio.
     */
    if (input.dataset.bloqueado === "true") {
        return;
    }

    /*
     * Alterna entre mostrar e ocultar a senha.
     */
    if (input.type === "password") {
        input.type = "text";
        icon.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';
    } else {
        input.type = "password";
        icon.innerHTML = '<path d="M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a18.5 18.5 0 0 1 5.06-5.94"></path><path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
    }

}


/*
 * Configura o bloqueio do formulário de login.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Impede envios duplicados.
     */
    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            if (form.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (!form.checkValidity() || event.defaultPrevented) {
                return;
            }

            /*
             * Bloqueia os campos do login.
             */
            bloquearCamposLogin(form);

            form.dataset.enviado = "true";

            /*
             * Bloqueia o botão de confirmação.
             */
            const botao = form.querySelector(
                'button[type="submit"], input[type="submit"]'
            );

            if (botao) {

                botao.disabled = true;
                botao.style.opacity = "0.6";
                botao.style.cursor = "not-allowed";

            }

        });

    });

});


/*
 * Bloqueia os campos após a confirmação.
 */
function bloquearCamposLogin(form) {

    form.querySelectorAll(
        "input:not([type='hidden'])"
    ).forEach(function (campo) {

        if (campo.dataset.bloqueado === "true") {
            return;
        }

        campo.style.opacity = "0.6";
        campo.style.cursor = "not-allowed";
        campo.dataset.bloqueado = "true";

        /*
         * Impede alterações sem desabilitar os campos.
         */
        [
            "mousedown",
            "keydown",
            "beforeinput",
            "click",
            "change",
            "paste",
            "drop"
        ].forEach(function (tipo) {

            campo.addEventListener(tipo, function (event) {

                if (campo.dataset.bloqueado === "true") {
                    event.preventDefault();
                    event.stopPropagation();
                }

            });

        });

    });

}
