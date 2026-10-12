function toggleDetails(id) {

    const checkbox = document.getElementById(
        "details-" + id
    );

    const row = document.getElementById(
        "details-row-" + id
    );

    if (!checkbox || !row) {
        return;
    }

    row.style.display = checkbox.checked
        ? "table-row"
        : "none";
}


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
 * Impede envios e cliques múltiplos.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Funciona para adicionar, atualizar,
     * vincular e desvincular produtos.
     */
    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            if (form.dataset.enviado === "true") {

                event.preventDefault();
                return;

            }

            /*
             * Bloqueia os controles somente
             * quando o formulário for válido.
             */
            if (!form.checkValidity()) {
                return;
            }

            form.dataset.enviado = "true";

            bloquearControlesPopup(form);


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
     * Impede cliques múltiplos em links
     * que executam ações, como Deletar.
     */
    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {

        link.addEventListener("click", function (event) {

            if (link.dataset.clicado === "true") {

                event.preventDefault();
                return;

            }

            link.dataset.clicado = "true";

            bloquearControlesPopup(link);

            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";

        });

    });

});


/*
 * Limpa o formulário de adicionar compra.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-compra-form"
    );

    if (!formulario) {
        return;
    }


    /*
     * Não limpa depois que a ação
     * já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }


    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.type !== "hidden") {

            input.removeAttribute("value");
            input.value = "";

        }

    });


    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}


/*
 * Limpa erros e restaura os dados originais
 * da compra no formulário de atualização.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-compra-form-" + id
    );

    if (!formulario) {
        return;
    }


    /*
     * Não restaura os campos depois
     * que a atualização foi confirmada.
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


/*
 * Limpa o formulário de vincular produto.
 *
 * Remove o ID do produto, a quantidade
 * comprada e os erros de validação.
 */
function limparFormularioVincular(id) {

    const formulario = document.getElementById(
        "link-produto-form-" + id
    );

    if (!formulario) {
        return;
    }


    /*
     * Não limpa depois que a ação
     * já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }


    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.type !== "hidden") {

            input.removeAttribute("value");
            input.value = "";

        }

    });


    formulario.querySelectorAll("select").forEach(function (select) {

        select.value = "";

    });


    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });

}


/*
 * Bloqueia os campos do popup depois
 * que a ação foi confirmada.
 *
 * Não utiliza disabled=true nos campos,
 * pois os valores ainda precisam ser
 * enviados pelo formulário.
 */
function bloquearCamposPopup(popup) {

    popup.querySelectorAll(
        "input:not([type='hidden']), select, textarea"
    ).forEach(function (campo) {

        campo.style.opacity = "0.6";
        campo.dataset.bloqueado = "true";


        /*
         * Impede interação pelo mouse.
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