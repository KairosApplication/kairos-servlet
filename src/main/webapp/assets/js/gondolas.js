function toggleDetails(id) {
    const checkbox = document.getElementById("details-" + id);
    const row = document.getElementById("details-row-" + id);

    if (!checkbox || !row) {
        return;
    }

    row.style.display = checkbox.checked ? "table-row" : "none";
}

// Impedir envios e cliques múltiplos
document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            if (form.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (event.defaultPrevented || !form.checkValidity()) {
                return;
            }

            form.dataset.enviado = "true";

            bloquearControlesPopup(form);

            const botao = form.querySelector('button[type="submit"], input[type="submit"]');

            if (botao) {
                botao.disabled = true;
                botao.style.opacity = "0.6";
                botao.style.cursor = "not-allowed";
            }

        });

    });

    document.querySelectorAll("a.delete-confirm-button").forEach(function (link) {

        link.addEventListener("click", function (event) {

            if (link.dataset.clicado === "true") {
                event.preventDefault();
                return;
            }

            link.dataset.clicado = "true";

            bloquearControlesPopup(link);

            link.style.opacity = "0.6";
            link.style.pointerEvents = "none";
            link.style.cursor = "not-allowed";

        });

    });

});

// Bloquear controles do popup
function bloquearControlesPopup(elemento) {
    const popup = elemento.closest(".popup");

    if (!popup) {
        return;
    }

    const overlay = popup.closest(".popup-overlay");

    if (!overlay) {
        return;
    }

    const checkbox = overlay.previousElementSibling;

    if (checkbox && checkbox.id === "logout-popup") {
        return;
    }

    popup.querySelectorAll(".cancel-button, .popup-close").forEach(function (controle) {

        controle.dataset.bloqueado = "true";
        controle.style.opacity = "0.6";
        controle.style.cursor = "not-allowed";

        controle.addEventListener("click", function (event) {

            if (controle.dataset.bloqueado === "true") {
                event.preventDefault();
                event.stopImmediatePropagation();
            }

        }, true);

    });

    bloquearCamposPopup(popup);
}

// Bloquear campos do popup
function bloquearCamposPopup(popup) {

    popup.querySelectorAll("input:not([type='hidden']), select, textarea").forEach(function (campo) {

        campo.dataset.bloqueado = "true";
        campo.style.opacity = "0.6";
        campo.style.cursor = "not-allowed";

        ["mousedown", "keydown", "beforeinput", "click", "change"].forEach(function (tipoEvento) {

            campo.addEventListener(tipoEvento, function (event) {

                if (campo.dataset.bloqueado === "true") {
                    event.preventDefault();
                    event.stopImmediatePropagation();
                }

            });

        });

    });

}

// Limpar formulário de adicionar
function limparFormularioAdicionar() {
    const formulario = document.getElementById("add-gondola-form");

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input:not([type='hidden'])").forEach(function (input) {
        input.removeAttribute("value");
        input.value = "";
    });

    formulario.querySelectorAll("select").forEach(function (select) {
        select.value = "";
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Limpar formulário de atualização
function limparFormularioAtualizacao(id) {
    const formulario = document.getElementById("update-gondola-form-" + id);

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.dataset.original !== undefined) {
            input.value = input.dataset.original;
        }

    });

    formulario.querySelectorAll("select").forEach(function (select) {

        if (select.dataset.original !== undefined) {
            select.value = select.dataset.original;
        }

    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Limpar formulário de vincular produto
function limparFormularioVincular(id) {
    const formulario = document.getElementById("link-produto-form-" + id);

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input:not([type='hidden'])").forEach(function (input) {
        input.removeAttribute("value");
        input.value = "";
    });

    formulario.querySelectorAll("select").forEach(function (select) {
        select.value = "";
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}
