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
 * Funciona para criar, atualizar, vincular e desvincular.
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
 * Limpa o formulário de adicionar gôndola.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-gondola-form"
    );

    if (!formulario) {
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


/*
 * Limpa erros e restaura os dados originais
 * da gôndola no formulário de atualização.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-gondola-form-" + id
    );

    if (!formulario) {
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

    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });
}


/*
 * Limpa o formulário de vincular produto.
 *
 * É executada ao clicar em Cancelar
 * ou no X do popup.
 */
function limparFormularioVincular(id) {

    const formulario = document.getElementById(
        "link-produto-form-" + id
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

    formulario.querySelectorAll("select").forEach(function (select) {

        select.value = "";

    });

    formulario.querySelectorAll(
        ".campo-erro"
    ).forEach(function (erro) {

        erro.remove();

    });
}