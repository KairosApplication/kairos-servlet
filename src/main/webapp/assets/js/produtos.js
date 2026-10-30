// Bloqueia os controles do popup após a confirmação
function bloquearControlesPopup(elemento) {

    const popup = elemento.closest(".popup");

    if (!popup) {
        return;
    }

    const overlay = popup.closest(".popup-overlay");

    if (!overlay) {
        return;
    }

    // Ignora o popup de logout
    const logout = document.getElementById(
        "logout-popup"
    );

    if (logout && logout.nextElementSibling === overlay) {
        return;
    }

    popup.dataset.bloqueado = "true";

    // Bloqueia os botões de cancelar e fechar
    popup.querySelectorAll(
        ".cancel-button, .popup-close"
    ).forEach(function (controle) {

        controle.style.opacity = "0.6";
        controle.style.cursor = "not-allowed";
        controle.dataset.bloqueado = "true";

    });

    // Bloqueia os campos do formulário
    bloquearCamposPopup(popup);

}


// Impede envios e cliques duplicados
document.addEventListener("DOMContentLoaded", function () {

    // Bloqueia o envio múltiplo de formulários
    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            if (form.dataset.enviado === "true") {

                event.preventDefault();
                return;

            }

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

    // Bloqueia cliques múltiplos em links de exclusão
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

    // Impede o fechamento de popups bloqueados
    document.querySelectorAll(
        ".popup-close, .cancel-button"
    ).forEach(function (controle) {

        controle.addEventListener("click", function (event) {

            if (controle.dataset.bloqueado === "true") {

                event.preventDefault();
                event.stopImmediatePropagation();

            }

        }, true);

    });

});


// Limpa o formulário de cadastro
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-product-form"
    );

    if (!formulario) {
        return;
    }

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


// Restaura os dados do formulário de atualização
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-product-form-" + id
    );

    if (!formulario) {
        return;
    }

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


// Bloqueia os campos sem impedir o envio dos valores
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


// Mantém a posição da página ao abrir e fechar popups
document.addEventListener("DOMContentLoaded", function () {

    let posicaoScroll = 0;

    function atualizarScrollPopup() {

        const popupAberto = document.querySelector(
            ".popup-toggle:checked"
        );

        if (popupAberto) {

            if (document.body.style.position !== "fixed") {

                posicaoScroll = window.scrollY;

                document.body.style.position = "fixed";
                document.body.style.top = `-${posicaoScroll}px`;
                document.body.style.width = "100%";

            }

        } else if (document.body.style.position === "fixed") {

            document.body.style.position = "";
            document.body.style.top = "";
            document.body.style.width = "";

            window.scrollTo(0, posicaoScroll);

        }

    }

    // Controla a abertura e o fechamento dos popups
    document.querySelectorAll("label[for]").forEach(function (label) {

        const checkbox = document.getElementById(
            label.htmlFor
        );

        if (!checkbox || !checkbox.classList.contains("popup-toggle")) {
            return;
        }

        label.addEventListener("click", function (event) {

            event.preventDefault();

            if (label.dataset.bloqueado === "true") {
                return;
            }

            checkbox.checked = !checkbox.checked;

            atualizarScrollPopup();

        });

    });

    // Fecha os popups ao clicar fora, quando permitido
    document.querySelectorAll(".popup-overlay").forEach(function (overlay) {

        overlay.addEventListener("click", function (event) {

            if (event.target !== overlay) {
                return;
            }

            const popup = overlay.querySelector(".popup");

            if (popup && popup.dataset.bloqueado === "true") {
                return;
            }

            const checkbox = overlay.previousElementSibling;

            if (!checkbox || !checkbox.classList.contains("popup-toggle")) {
                return;
            }

            checkbox.checked = false;

            if (checkbox.id === "add-product-toggle") {

                limparFormularioAdicionar();

            } else if (checkbox.id.startsWith("update-product-")) {

                const id = checkbox.id.replace(
                    "update-product-",
                    ""
                );

                limparFormularioAtualizacao(id);

            }

            atualizarScrollPopup();

        });

    });

    atualizarScrollPopup();

});


// Restaura os controles ao retornar para a página
window.addEventListener("pageshow", function () {

    document.querySelectorAll("form").forEach(function (form) {

        form.dataset.enviado = "false";

        const botao = form.querySelector(
            'button[type="submit"], input[type="submit"]'
        );

        if (botao) {

            botao.disabled = false;
            botao.style.opacity = "";
            botao.style.cursor = "";

        }

    });

    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {

        link.dataset.clicado = "false";

        link.style.opacity = "";
        link.style.cursor = "";
        link.style.pointerEvents = "";

    });

    document.querySelectorAll(".popup").forEach(function (popup) {

        popup.dataset.bloqueado = "false";

        popup.querySelectorAll(
            ".cancel-button, .popup-close"
        ).forEach(function (controle) {

            controle.dataset.bloqueado = "false";
            controle.style.opacity = "";
            controle.style.cursor = "";

        });

        popup.querySelectorAll(
            "input:not([type='hidden']), select, textarea"
        ).forEach(function (campo) {

            campo.dataset.bloqueado = "false";
            campo.style.opacity = "";
            campo.style.cursor = "";

        });

    });

});