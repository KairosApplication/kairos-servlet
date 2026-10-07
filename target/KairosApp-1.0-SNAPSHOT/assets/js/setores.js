/*
 * Impede o envio múltiplo dos formulários.
 * Funciona para adicionar e atualizar setor.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Impede o envio múltiplo dos formulários.
     */
    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            /*
             * Bloqueia Cancelar, X e campos do popup
             * antes de enviar a ação.
             */
            bloquearControlesPopup(form);


            if (form.dataset.enviado === "true") {

                event.preventDefault();
                return;

            }

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
     * Impede cliques múltiplos em links que executam ações.
     * Exemplo: Deletar.
     */
    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {

        link.addEventListener("click", function (event) {

            /*
             * Bloqueia Cancelar e X antes de executar
             * a exclusão.
             */
            bloquearControlesPopup(link);


            if (link.dataset.clicado === "true") {

                event.preventDefault();
                return;

            }

            link.dataset.clicado = "true";


            /*
             * Bloqueia o próprio botão Deletar.
             */
            link.style.opacity = "0.6";
            link.style.pointerEvents = "none";
            link.style.cursor = "not-allowed";

        });

    });

});


/*
 * Impede qualquer interação com os botões
 * de cancelar e fechar do popup depois
 * que uma ação foi confirmada.
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
     * Não aplica ao popup de logout.
     */
    const logout = document.getElementById(
        "logout-popup"
    );

    if (logout) {

        const logoutOverlay = logout.nextElementSibling;

        if (logoutOverlay === overlay) {
            return;
        }

    }


    /*
     * Bloqueia Cancelar e X.
     */
    popup.querySelectorAll(
        ".cancel-button, .popup-close"
    ).forEach(function (controle) {

        controle.style.opacity = "0.6";
        controle.style.cursor = "not-allowed";
        controle.dataset.bloqueado = "true";


        /*
         * Impede o clique sem remover
         * o elemento da área do mouse.
         */
        controle.addEventListener("click", function (event) {

            if (controle.dataset.bloqueado === "true") {

                event.preventDefault();
                event.stopPropagation();

            }

        });

    });


    /*
     * Bloqueia os campos do formulário.
     */
    bloquearCamposPopup(popup);

}


/*
 * Bloqueia os campos do popup depois que
 * a ação foi confirmada.
 *
 * Os campos não recebem disabled=true,
 * pois seus valores ainda precisam ser
 * enviados pelo formulário.
 */
function bloquearCamposPopup(popup) {

    popup.querySelectorAll(
        "input:not([type='hidden']), select, textarea"
    ).forEach(function (campo) {

        campo.style.opacity = "0.6";
        campo.dataset.bloqueado = "true";


        /*
         * Impede interação pelo mouse antes
         * mesmo que o evento de click aconteça.
         */
        campo.addEventListener("mousedown", function (event) {

            if (campo.dataset.bloqueado === "true") {

                event.preventDefault();

            }

        });


        /*
         * Impede alterações pelo teclado.
         */
        campo.addEventListener("keydown", function (event) {

            if (campo.dataset.bloqueado === "true") {

                event.preventDefault();

            }

        });


        /*
         * Impede entrada de texto.
         */
        campo.addEventListener("beforeinput", function (event) {

            if (campo.dataset.bloqueado === "true") {

                event.preventDefault();

            }

        });


        /*
         * Impede cliques no campo.
         */
        campo.addEventListener("click", function (event) {

            if (campo.dataset.bloqueado === "true") {

                event.preventDefault();

            }

        });


        /*
         * Impede alterações em selects
         * e outros campos.
         */
        campo.addEventListener("change", function (event) {

            if (campo.dataset.bloqueado === "true") {

                event.preventDefault();

            }

        });

    });

}


/*
 * Limpa o formulário de adicionar setor.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-sector-form"
    );

    if (!formulario) {
        return;
    }


    /*
     * Não limpa o formulário depois que
     * a ação já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }


    formulario.querySelectorAll("input").forEach(function (input) {

        input.removeAttribute("value");
        input.value = "";

    });


    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}


/*
 * Limpa os erros e restaura os dados originais
 * do setor no formulário de atualização.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-sector-form-" + id
    );

    if (!formulario) {
        return;
    }


    /*
     * Não limpa nem restaura os campos depois
     * que a atualização já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }


    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.dataset.original !== undefined) {

            input.value = input.dataset.original;

        }

    });


    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}