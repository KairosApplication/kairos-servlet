function toggleDetails(id) {

    const checkbox = document.getElementById("details-" + id);
    const row = document.getElementById("details-row-" + id);

    if (!checkbox || !row) {
        return;
    }

    row.style.display = checkbox.checked ? "table-row" : "none";
}

// Bloqueia os controles do popup após confirmar uma ação
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
    const logout = document.getElementById("logout-popup");

    if (logout && logout.nextElementSibling === overlay) {
        return;
    }

    // Bloqueia os botões de cancelar e fechar
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

    // Bloqueia os campos do formulário
    bloquearCamposPopup(popup);
}

// Impede envios e cliques múltiplos
document.addEventListener("DOMContentLoaded", function () {

    // Bloqueia formulários após o primeiro envio válido
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

    // Impede cliques múltiplos em links de exclusão
    document.querySelectorAll("a.delete-confirm-button").forEach(function (link) {

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

// Limpa o formulário de adicionar compra
function limparFormularioAdicionar() {

    const formulario = document.getElementById("add-compra-form");

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.type !== "hidden") {

            input.removeAttribute("value");
            input.value = "";
        }
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Restaura os dados originais do formulário de atualização
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById("update-compra-form-" + id);

    if (!formulario || formulario.dataset.enviado === "true") {
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

// Limpa o formulário de vincular produto
function limparFormularioVincular(id) {

    const formulario = document.getElementById("link-produto-form-" + id);

    if (!formulario || formulario.dataset.enviado === "true") {
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

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Bloqueia os campos do popup após confirmar uma ação
function bloquearCamposPopup(popup) {

    popup.querySelectorAll(
        "input:not([type='hidden']):not([type='submit']), select, textarea"
    ).forEach(function (campo) {

        campo.dataset.bloqueado = "true";
        campo.style.opacity = "0.6";
        campo.style.cursor = "not-allowed";
        campo.style.caretColor = "transparent";

        // Mantém os valores disponíveis para envio
        if (campo.tagName === "INPUT" || campo.tagName === "TEXTAREA") {
            campo.readOnly = true;
        }

        // Impede interações pelo mouse
        campo.addEventListener("mousedown", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("click", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
                event.stopPropagation();
            }
        });

        // Impede alterações pelo teclado
        campo.addEventListener("keydown", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        // Impede digitação e colagem
        campo.addEventListener("beforeinput", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("paste", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        campo.addEventListener("drop", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });

        // Impede alterações em campos de seleção
        campo.addEventListener("change", function (event) {

            if (campo.dataset.bloqueado === "true") {
                event.preventDefault();
            }
        });
    });
}
