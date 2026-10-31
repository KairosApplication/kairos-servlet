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

    const logout = document.getElementById("logout-popup");

    if (logout && logout.nextElementSibling === overlay) {
        return;
    }

    popup.querySelectorAll(".cancel-button, .popup-close").forEach(function (controle) {

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

    bloquearCamposPopup(popup);
}

// Bloqueia os campos sem impedir o envio dos valores
function bloquearCamposPopup(popup) {

    popup.querySelectorAll("input:not([type='hidden']), select, textarea").forEach(function (campo) {

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

// Exibe a data de resolução apenas para alertas concluídos
function atualizarCampoResolucao(formulario) {

    if (!formulario) {
        return;
    }

    const status = formulario.querySelector('[name="statusAlerta"]');
    const campoResolucao = formulario.querySelector(".campo-resolucao");

    if (!status || !campoResolucao) {
        return;
    }

    const concluido = status.value === "CONCLUIDO";

    campoResolucao.style.display = concluido ? "flex" : "none";

    const inputResolucao = campoResolucao.querySelector('[name="dataResolucao"]');

    if (inputResolucao) {
        inputResolucao.required = concluido;
    }
}

// Configura a exibição da data de resolução nos formulários
function configurarCamposResolucao() {

    document.querySelectorAll(".add-alerta-form, .update-alerta-form").forEach(function (formulario) {

        const status = formulario.querySelector('[name="statusAlerta"]');

        if (!status) {
            return;
        }

        atualizarCampoResolucao(formulario);

        status.addEventListener("change", function () {
            atualizarCampoResolucao(formulario);
        });

    });
}

// Limpa os campos e erros do formulário de adição
function limparFormularioAdicionar() {

    const formulario = document.getElementById("add-alerta-form");

    if (!formulario) {
        return;
    }

    if (formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input:not([type='hidden']), textarea").forEach(function (campo) {
        campo.value = "";
    });

    formulario.querySelectorAll("select").forEach(function (select) {
        select.selectedIndex = 0;
    });

    formulario.querySelectorAll(".campo-erro").forEach(function (erro) {
        erro.remove();
    });

    atualizarCampoResolucao(formulario);
}

// Restaura os valores originais e limpa os erros da atualização
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById("update-alerta-form-" + id);

    if (!formulario) {
        return;
    }

    if (formulario.dataset.enviado === "true") {
        return;
    }

    formulario.querySelectorAll("input:not([type='hidden']), textarea").forEach(function (campo) {

        if (campo.dataset.original !== undefined) {
            campo.value = campo.dataset.original;
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

    atualizarCampoResolucao(formulario);
}

// Configura os formulários e impede confirmações duplicadas
document.addEventListener("DOMContentLoaded", function () {

    configurarCamposResolucao();

    // Bloqueia o botão de busca após o envio
    document.querySelectorAll(".search-form").forEach(function (formulario) {

        formulario.addEventListener("submit", function (event) {

            if (formulario.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (!formulario.checkValidity() || event.defaultPrevented) {
                return;
            }

            formulario.dataset.enviado = "true";

            const botao = formulario.querySelector(".search-button");

            if (botao) {
                botao.disabled = true;
                botao.style.opacity = "0.6";
                botao.style.cursor = "not-allowed";
            }

        });

    });

    // Bloqueia os botões dos formulários após o envio
    document.querySelectorAll(".add-alerta-form, .update-alerta-form").forEach(function (formulario) {

        formulario.addEventListener("submit", function (event) {

            if (formulario.dataset.enviado === "true") {
                event.preventDefault();
                return;
            }

            if (!formulario.checkValidity()) {
                return;
            }

            if (event.defaultPrevented) {
                return;
            }

            bloquearControlesPopup(formulario);

            formulario.dataset.enviado = "true";

            const botao = formulario.querySelector('button[type="submit"], input[type="submit"]');

            if (botao) {
                botao.disabled = true;
                botao.style.opacity = "0.6";
                botao.style.cursor = "not-allowed";
            }

        });

    });

    // Impede confirmações duplicadas de exclusão
    document.querySelectorAll("a.delete-confirm-button").forEach(function (link) {

        link.addEventListener("click", function (event) {

            if (link.dataset.clicado === "true") {
                event.preventDefault();
                return;
            }

            bloquearControlesPopup(link);

            link.dataset.clicado = "true";
            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";

        });

    });

});
