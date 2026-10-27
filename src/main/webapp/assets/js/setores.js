// Impedir envios e cliques múltiplos
document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

            if (form.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (!form.checkValidity() || event.defaultPrevented) {
                return;
            }

            bloquearControlesPopup(form);

            form.dataset.enviado = "true";

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

            bloquearControlesPopup(link);

            link.dataset.clicado = "true";

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

    const logout = document.getElementById("logout-popup");

    if (logout) {

        const logoutOverlay = logout.nextElementSibling;

        if (logoutOverlay === overlay) {
            return;
        }
    }

    popup.querySelectorAll(".cancel-button, .popup-close").forEach(function (controle) {

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

    bloquearCamposPopup(popup);
}

// Bloquear campos do popup
function bloquearCamposPopup(popup) {

    popup.querySelectorAll("input:not([type='hidden']), select, textarea").forEach(function (campo) {

        campo.style.opacity = "0.6";
        campo.style.cursor = "not-allowed";
        campo.dataset.bloqueado = "true";

        campo.addEventListener("mousedown", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("keydown", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("beforeinput", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("click", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("change", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });
    });
}

// Limpar formulário de cadastro
function limparFormularioAdicionar() {

    const formulario = document.getElementById("add-sector-form");

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

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Restaurar formulário de atualização
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById("update-sector-form-" + id);

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

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}