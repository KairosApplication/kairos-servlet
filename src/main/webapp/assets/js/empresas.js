/*
 * Configura o bloqueio dos formulários e botões.
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
             * Bloqueia os controles do popup.
             */
            bloquearControlesPopup(form);

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


    /*
     * Impede exclusões duplicadas.
     */
    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {

        link.addEventListener("click", function (event) {

            if (link.dataset.clicado === "true") {
                event.preventDefault();
                return;
            }

            /*
             * Bloqueia os controles do popup.
             */
            bloquearControlesPopup(link);

            link.dataset.clicado = "true";

            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";

        });

    });

});


/*
 * Bloqueia os controles do popup.
 */
function bloquearControlesPopup(elemento) {

    const popup = elemento.closest(".popup");

    if (!popup) {
        return;
    }

    const overlay = popup.closest(".popup-overlay");

    if (!overlay) {
        return;
    }

    /*
     * Não bloqueia o popup de logout.
     */
    const logout = document.getElementById(
        "logout-popup"
    );

    if (logout && logout.nextElementSibling === overlay) {
        return;
    }

    /*
     * Bloqueia Cancelar e X.
     */
    popup.querySelectorAll(
        ".cancel-button, .popup-close"
    ).forEach(function (controle) {

        if (controle.dataset.bloqueado === "true") {
            return;
        }

        controle.style.opacity = "0.6";
        controle.style.cursor = "not-allowed";
        controle.dataset.bloqueado = "true";

        controle.addEventListener("click", function (event) {

            if (controle.dataset.bloqueado === "true") {
                event.preventDefault();
                event.stopPropagation();
            }

        });

    });

    /*
     * Bloqueia os campos do popup.
     */
    bloquearCamposPopup(popup);

}


/*
 * Bloqueia os campos após a confirmação.
 */
function bloquearCamposPopup(popup) {

    popup.querySelectorAll(
        "input:not([type='hidden']), select, textarea"
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


/*
 * Limpa o formulário de adicionar empresa.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-company-form"
    );

    if (!formulario) {
        return;
    }

    /*
     * Não limpa após a confirmação.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input").forEach(function (input) {

        input.removeAttribute("value");
        input.value = "";

    });

    formulario.querySelectorAll("select").forEach(function (select) {

        select.value = "";

    });

    /*
     * Remove os erros de validação.
     */
    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}


/*
 * Restaura os dados originais da empresa.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-company-form-" + id
    );

    if (!formulario) {
        return;
    }

    /*
     * Não restaura após a confirmação.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }

    /*
     * Restaura os inputs.
     */
    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.dataset.original !== undefined) {

            input.value = input.dataset.original;

        }

    });

    /*
     * Restaura os selects.
     */
    formulario.querySelectorAll("select").forEach(function (select) {

        if (select.dataset.original !== undefined) {

            select.value = select.dataset.original;

        }

    });

    /*
     * Remove os erros de validação.
     */
    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}
