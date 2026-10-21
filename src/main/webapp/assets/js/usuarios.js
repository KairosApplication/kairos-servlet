
// Exibe ou oculta os detalhes do usuário
function toggleDetails(id) {

    const checkbox = document.getElementById(
        "details-" + id
    );

    const row = document.getElementById(
        "details-row-" + id
    );

    row.style.display = checkbox.checked
        ? "table-row"
        : "none";
}


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

    if (logout) {

        const logoutOverlay = logout.nextElementSibling;

        if (logoutOverlay === overlay) {
            return;
        }

    }

    // Bloqueia os botões de cancelar e fechar
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

    // Bloqueia os campos do formulário
    bloquearCamposPopup(popup);

}


// Impede envios e cliques duplicados
document.addEventListener("DOMContentLoaded", function () {

    // Bloqueia o envio múltiplo de formulários
    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            bloquearControlesPopup(form);

            if (form.dataset.enviado === "true") {

                event.preventDefault();

                return;
            }

            form.dataset.enviado = "true";

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

            bloquearControlesPopup(link);

            if (link.dataset.clicado === "true") {

                event.preventDefault();

                return;
            }

            link.dataset.clicado = "true";

            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";

        });

    });

});


// Limpa o formulário de cadastro
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-user-form"
    );

    if (!formulario) {
        return;
    }

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

    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });
}


// Restaura os dados do formulário de atualização
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-user-form-" + id
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


// Limpa o formulário de vinculação de gôndola
function limparFormularioVincular(id) {

    const formulario = document.getElementById(
        "link-gondola-form-" + id
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

    formulario.querySelectorAll("select").forEach(function (select) {

        select.value = "";

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

    document.querySelectorAll("label[for]").forEach(function (label) {

        const checkbox = document.getElementById(label.htmlFor);

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

    atualizarScrollPopup();

});
