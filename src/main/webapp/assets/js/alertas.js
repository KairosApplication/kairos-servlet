
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
    const logout = document.getElementById("logout-popup");

    if (logout && logout.nextElementSibling === overlay) {
        return;
    }

    /*
     * Bloqueia Cancelar e X.
     */
    popup.querySelectorAll(
        ".cancel-button, .popup-close"
    ).forEach(function (controle) {

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

    /*
     * Bloqueia os campos do popup.
     *
     * Os campos não recebem disabled=true,
     * para que seus valores continuem sendo
     * enviados pelo formulário.
     */
    bloquearCamposPopup(popup);
}


/*
 * Bloqueia os campos do popup depois que
 * a ação foi confirmada.
 */
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


/*
 * Exibe ou oculta o campo de data de resolução
 * conforme o status selecionado.
 *
 * A data só aparece quando o status é CONCLUIDO.
 */
function atualizarCampoResolucao(formulario) {

    if (!formulario) {
        return;
    }

    const status = formulario.querySelector(
        '[name="statusAlerta"]'
    );

    const campoResolucao = formulario.querySelector(
        ".campo-resolucao"
    );

    if (!status || !campoResolucao) {
        return;
    }

    const concluido = status.value === "CONCLUIDO";

    campoResolucao.style.display = concluido ? "flex" : "none";

    const inputResolucao = campoResolucao.querySelector(
        '[name="dataResolucao"]'
    );

    if (inputResolucao) {

        /*
         * A data é obrigatória somente quando
         * o alerta estiver concluído.
         */
        inputResolucao.required = concluido;

    }

}


/*
 * Configura os campos de resolução
 * dos formulários de adicionar e atualizar.
 */
function configurarCamposResolucao() {

    document.querySelectorAll(
        ".add-alerta-form, .update-alerta-form"
    ).forEach(function (formulario) {

        const status = formulario.querySelector(
            '[name="statusAlerta"]'
        );

        if (!status) {
            return;
        }

        /*
         * Define o estado inicial do campo.
         */
        atualizarCampoResolucao(formulario);

        /*
         * Atualiza o campo sempre que o status mudar.
         */
        status.addEventListener("change", function () {

            atualizarCampoResolucao(formulario);

        });

    });

}


/*
 * Limpa o formulário de adicionar alerta
 * quando o usuário cancela a operação.
 */
function limparFormularioAdicionar() {

    const formulario = document.getElementById(
        "add-alerta-form"
    );

    if (!formulario) {
        return;
    }

    /*
     * Não limpa o formulário depois que
     * a ação já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }

    /*
     * Limpa os campos de entrada.
     */
    formulario.querySelectorAll(
        "input:not([type='hidden']), textarea"
    ).forEach(function (campo) {

        campo.value = "";

    });

    /*
     * Retorna os selects à primeira opção.
     */
    formulario.querySelectorAll("select").forEach(
        function (select) {

            select.selectedIndex = 0;

        }
    );

    /*
     * Remove as mensagens de erro.
     */
    formulario.querySelectorAll(".campo-erro").forEach(
        function (erro) {

            erro.remove();

        }
    );

    /*
     * Atualiza a visibilidade da data de resolução.
     */
    atualizarCampoResolucao(formulario);

}


/*
 * Limpa os erros e restaura os valores
 * originais do formulário de atualização.
 *
 * Os valores originais devem ser definidos
 * no JSP usando data-original.
 */
function limparFormularioAtualizacao(id) {

    const formulario = document.getElementById(
        "update-alerta-form-" + id
    );

    if (!formulario) {
        return;
    }

    /*
     * Não restaura os campos depois que
     * a atualização já foi confirmada.
     */
    if (formulario.dataset.enviado === "true") {
        return;
    }

    /*
     * Restaura os valores originais dos campos.
     */
    formulario.querySelectorAll(
        "input:not([type='hidden']), textarea"
    ).forEach(function (campo) {

        if (campo.dataset.original !== undefined) {

            campo.value = campo.dataset.original;

        }

    });

    /*
     * Restaura os valores originais dos selects.
     */
    formulario.querySelectorAll("select").forEach(
        function (select) {

            if (select.dataset.original !== undefined) {

                select.value = select.dataset.original;

            }

        }
    );

    /*
     * Remove as mensagens de erro.
     */
    formulario.querySelectorAll(".campo-erro").forEach(
        function (erro) {

            erro.remove();

        }
    );

    /*
     * Atualiza a visibilidade da data de resolução.
     */
    atualizarCampoResolucao(formulario);

}


/*
 * Configura o comportamento dos formulários
 * depois que o documento for carregado.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Configura a exibição da data de resolução.
     */
    configurarCamposResolucao();


    /*
     * Impede envios duplicados nos formulários
     * de adicionar e atualizar alertas.
     *
     * Não interfere no formulário de pesquisa.
     */
    document.querySelectorAll(
        ".add-alerta-form, .update-alerta-form"
    ).forEach(function (formulario) {

        formulario.addEventListener("submit", function (event) {

            /*
             * Impede um novo envio se o formulário
             * já tiver sido enviado.
             */
            if (formulario.dataset.enviado === "true") {

                event.preventDefault();
                return;

            }

            /*
             * Não bloqueia o formulário caso
             * exista algum campo inválido.
             */
            if (!formulario.checkValidity()) {
                return;
            }

            /*
             * Não bloqueia se outro código
             * já tiver cancelado o envio.
             */
            if (event.defaultPrevented) {
                return;
            }

            /*
             * Bloqueia a interação com o popup.
             */
            bloquearControlesPopup(formulario);

            /*
             * Registra que o formulário foi enviado.
             */
            formulario.dataset.enviado = "true";

            /*
             * Desabilita o botão de envio para
             * evitar cliques repetidos.
             */
            const botao = formulario.querySelector(
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
     * Impede cliques múltiplos nos links
     * de confirmação de exclusão.
     */
    document.querySelectorAll(
        "a.delete-confirm-button"
    ).forEach(function (link) {

        link.addEventListener("click", function (event) {

            /*
             * Impede uma segunda exclusão.
             */
            if (link.dataset.clicado === "true") {

                event.preventDefault();
                return;

            }

            /*
             * Bloqueia os controles do popup.
             */
            bloquearControlesPopup(link);

            /*
             * Registra o clique.
             */
            link.dataset.clicado = "true";

            link.style.opacity = "0.6";
            link.style.cursor = "not-allowed";

        });

    });

});
