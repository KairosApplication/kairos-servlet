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


/*
 * Impede o envio múltiplo dos formulários.
 * Funciona para adicionar, atualizar,
 * vincular e desvincular.
 */
document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll("form").forEach(function (form) {

        form.addEventListener("submit", function (event) {

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


    /*
     * Impede cliques múltiplos em links que executam ações.
     * Exemplo: Deletar.
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

            link.style.opacity = "0.6";
            link.style.pointerEvents = "none";
            link.style.cursor = "not-allowed";

        });

    });

});


/*
 * Limpa o formulário de adicionar reposição.
 *
 * Remove os valores digitados,
 * restaura o motivo padrão
 * e remove os erros de validação.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-reposicao-form"
    );

    if (!formulario) {
        return;
    }

    formulario.querySelectorAll("input").forEach(function (input) {

        if (input.type !== "hidden") {

            input.removeAttribute("value");
            input.value = "";

        }

    });

    formulario.querySelectorAll("textarea").forEach(function (textarea) {

        textarea.value = "Falta de Produtos";

    });

    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });
}


/*
 * Limpa erros e restaura os dados originais
 * da reposição no formulário de atualização.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-reposicao-form-" + id
    );

    if (!formulario) {
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

    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });
}


/*
 * Limpa o formulário de vincular funcionário.
 *
 * Remove o ID digitado e os erros de validação.
 */
function limparFormularioVincular(id) {

    const formulario = document.getElementById(
        "link-usuario-form-" + id
    );

    if (!formulario) {
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