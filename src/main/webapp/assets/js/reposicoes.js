// Exibe ou oculta os detalhes da reposição
function toggleDetails(id) {
    const checkbox = document.getElementById("details-" + id);
    const row = document.getElementById("details-row-" + id);

    if (!checkbox || !row) {
        return;
    }

    row.style.display = checkbox.checked ? "table-row" : "none";
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

    const checkbox = overlay.previousElementSibling;

    if (checkbox && checkbox.id === "logout-popup") {
        return;
    }

    popup.dataset.bloqueado = "true";

    popup.querySelectorAll(
        ".cancel-button, .popup-close"
    ).forEach(function (controle) {
        controle.dataset.bloqueado = "true";
        controle.style.opacity = "0.6";
        controle.style.cursor = "not-allowed";
        controle.setAttribute("aria-disabled", "true");
    });

    bloquearCamposPopup(popup);
}

// Bloqueia os campos sem impedir o envio dos valores
function bloquearCamposPopup(popup) {
    popup.querySelectorAll(
        "input:not([type='hidden']), select, textarea"
    ).forEach(function (campo) {
        if (campo.dataset.bloqueado === "true") {
            return;
        }

        campo.dataset.bloqueado = "true";
        campo.style.opacity = "0.6";
        campo.style.cursor = "not-allowed";

        [
            "mousedown",
            "keydown",
            "beforeinput",
            "click",
            "change",
            "paste",
            "drop"
        ].forEach(function (tipoEvento) {
            campo.addEventListener(tipoEvento, function (event) {
                if (campo.dataset.bloqueado === "true") {
                    event.preventDefault();
                    event.stopImmediatePropagation();
                }
            });
        });
    });
}

// Limpa o formulário de adicionar reposição
function limparFormularioAdicionar() {
    const formulario = document.getElementById("add-reposicao-form");

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll(
        "input:not([type='hidden'])"
    ).forEach(function (input) {
        input.removeAttribute("value");
        input.value = "";
    });

    formulario.querySelectorAll("textarea").forEach(function (textarea) {
        textarea.value = "Falta de Produtos";
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Restaura os dados originais da reposição
function limparFormularioAtualizacao(id) {
    const formulario = document.getElementById(
        "update-reposicao-form-" + id
    );

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input").forEach(function (input) {
        if (input.dataset.original !== undefined) {
            input.value = input.dataset.original;
        }
    });

    formulario.querySelectorAll("textarea").forEach(function (textarea) {
        if (textarea.dataset.original !== undefined) {
            textarea.value = textarea.dataset.original;
        }
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Limpa o formulário de vincular funcionário
function limparFormularioVincular(id) {
    const formulario = document.getElementById(
        "link-usuario-form-" + id
    );

    if (!formulario || formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll(
        "input:not([type='hidden'])"
    ).forEach(function (input) {
        input.removeAttribute("value");
        input.value = "";
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });
}

// Impede envios e cliques múltiplos
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("form").forEach(function (formulario) {
        formulario.addEventListener("submit", function (event) {
            if (formulario.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (event.defaultPrevented || !formulario.checkValidity()) {
                return;
            }

            formulario.dataset.enviado = "true";

            bloquearControlesPopup(formulario);

            const botao = event.submitter ||
                formulario.querySelector(
                    'button[type="submit"], input[type="submit"]'
                );

            if (botao) {
                botao.dataset.bloqueado = "true";
                botao.style.opacity = "0.6";
                botao.style.cursor = "not-allowed";
                botao.setAttribute("aria-disabled", "true");
            }
        });
    });

    // Impede múltiplos cliques nos links de exclusão
    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {
        link.addEventListener("click", function (event) {
            const overlay = link.closest(".popup-overlay");
            const checkbox = overlay ? overlay.previousElementSibling : null;

            if (checkbox && checkbox.id === "logout-popup") {
                return;
            }

            if (link.dataset.clicado === "true") {
                event.preventDefault();
                return;
            }

            link.dataset.clicado = "true";

            bloquearControlesPopup(link);

            link.dataset.bloqueado = "true";
            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";
            link.setAttribute("aria-disabled", "true");
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
        const checkbox = document.getElementById(label.htmlFor);

        if (!checkbox || !checkbox.classList.contains("popup-toggle")) {
            return;
        }

        label.addEventListener("click", function (event) {
            event.preventDefault();

            if (label.dataset.bloqueado === "true") {
                return;
            }

            const overlay = checkbox.nextElementSibling;
            const popup = overlay ? overlay.querySelector(".popup") : null;

            if (popup && popup.dataset.bloqueado === "true") {
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

            if (checkbox.id === "add-reposicao-toggle") {
                limparFormularioAdicionar();
            } else if (checkbox.id.startsWith("update-reposicao-")) {
                const id = checkbox.id.replace(
                    "update-reposicao-", ""
                );

                limparFormularioAtualizacao(id);
            } else if (checkbox.id.startsWith("link-usuario-")) {
                const id = checkbox.id.replace(
                    "link-usuario-", ""
                );

                limparFormularioVincular(id);
            }

            atualizarScrollPopup();
        });
    });

    atualizarScrollPopup();
});

// Restaura os controles ao retornar para a página
window.addEventListener("pageshow", function () {
    document.querySelectorAll("form").forEach(function (formulario) {
        formulario.dataset.enviado = "false";

        formulario.querySelectorAll(
            'button[type="submit"], input[type="submit"]'
        ).forEach(function (botao) {
            botao.disabled = false;
            botao.dataset.bloqueado = "false";
            botao.style.opacity = "";
            botao.style.cursor = "";
            botao.removeAttribute("aria-disabled");
        });
    });

    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {
        link.dataset.clicado = "false";
        link.dataset.bloqueado = "false";
        link.style.opacity = "";
        link.style.cursor = "";
        link.style.pointerEvents = "";
        link.removeAttribute("aria-disabled");
    });

    document.querySelectorAll(".popup").forEach(function (popup) {
        popup.dataset.bloqueado = "false";

        popup.querySelectorAll(
            ".cancel-button, .popup-close"
        ).forEach(function (controle) {
            controle.dataset.bloqueado = "false";
            controle.style.opacity = "";
            controle.style.cursor = "";
            controle.removeAttribute("aria-disabled");
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
