<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Kairos - Empresas</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/empresas.css">

</head>

<body>

<aside class="sidebar">

    <div class="logo-area">

        <img
                src="${pageContext.request.contextPath}/assets/images/logo-kairos.svg"
                alt="Kairos"
                class="logo"
        >

    </div>


    <nav class="menu">

        <a href="${pageContext.request.contextPath}/admin/usuarios" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/usuario.svg"
                        alt=""
                >
            </span>

            <span>Usuários</span>

        </a>


        <a href="#" class="menu-item active">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/empresa.svg"
                        alt=""
                >
            </span>

            <span>Empresa</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/setores" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/setor.svg"
                        alt=""
                >
            </span>

            <span>Setor</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/gondolas" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/gondola.svg"
                        alt=""
                >
            </span>

            <span>Gôndola</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/produtos" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/produto.svg"
                        alt=""
                >
            </span>

            <span>Produto</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/reposicoes" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/reposicao.svg"
                        alt=""
                >
            </span>

            <span>Reposição</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/alertas" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/alerta.svg"
                        alt=""
                >
            </span>

            <span>Alerta</span>

        </a>


        <a href="${pageContext.request.contextPath}/admin/compras" class="menu-item">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/compra.svg"
                        alt=""
                >
            </span>

            <span>Compra</span>

        </a>

    </nav>


    <div class="logout-area">

        <label
                for="logout-popup"
                class="logout-button"
        >
            Sair
        </label>

    </div>

</aside>


<main class="main-content">


    <!-- ====================================================== -->
    <!-- BUSCA E ADICIONAR -->
    <!-- ====================================================== -->

    <section class="search-area">

        <form
                action="${pageContext.request.contextPath}/admin/empresas"
                method="get"
                class="search-form"
        >

            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar empresa..."
                    class="search-input"
                    value="${param.busca}"
            >

            <button
                    type="submit"
                    class="search-button"
            >
                Buscar
            </button>

        </form>


        <label
                for="add-company-toggle"
                class="add-user-button"
        >
            + Adicionar Empresa
        </label>

    </section>


    <!-- ====================================================== -->
    <!-- TABELA -->
    <!-- ====================================================== -->

    <section class="content-card">

        <h1>Empresas</h1>


        <div class="table-wrapper">

            <table class="users-table">

                <thead>

                <tr>

                    <th>ID</th>
                    <th>Nome</th>
                    <th>CNPJ</th>
                    <th>Tipo de plano</th>
                    <th>Ações</th>

                </tr>

                </thead>


                <tbody>

                <c:forEach
                        var="empresa"
                        items="${empresas}"
                >

                    <tr class="user-row">

                        <td>
                                ${empresa.id}
                        </td>

                        <td>
                                ${empresa.nome}
                        </td>

                        <td>
                                ${empresa.cnpj}
                        </td>

                        <td>
                                ${empresa.tipoPlano}
                        </td>

                        <td class="actions">

                            <!-- ATUALIZAR -->

                            <label
                                    for="update-company-${empresa.id}"
                                    class="update-button"
                            >
                                Atualizar
                            </label>


                            <!-- DELETAR -->

                            <label
                                    for="delete-company-${empresa.id}"
                                    class="delete-button"
                            >
                                Deletar
                            </label>

                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>

    </section>

</main>


<!-- ====================================================== -->
<!-- POPUPS DE ATUALIZAÇÃO -->
<!-- ====================================================== -->

<c:forEach
        var="empresa"
        items="${empresas}"
>

    <input
            type="checkbox"
            id="update-company-${empresa.id}"
            class="popup-toggle"
        ${idEmpresaAtualizacao == empresa.id ? 'checked' : ''}
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Atualizar Empresa
                </h2>

                <label
                        for="update-company-${empresa.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${empresa.id})"
                >
                    &times;
                </label>

            </div>


            <form
                    id="update-company-form-${empresa.id}"
                    action="${pageContext.request.contextPath}/admin/empresas"
                    method="post"
                    class="add-user-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${empresa.id}"
                >

                <div class="form-grid">

                    <div class="form-field full-width">

                        <label for="tipoPlano-${empresa.id}">
                            Tipo de plano
                        </label>

                        <select
                                id="tipoPlano-${empresa.id}"
                                name="tipoPlano"
                                data-original="${empresa.tipoPlano != null ? empresa.tipoPlano.name() : ''}"

                        >

                            <option
                                    value=""
                                ${empty param.tipoPlano && empty empresa.tipoPlano ? 'selected' : ''}
                            >
                                Selecione o tipo de plano
                            </option>

                            <option
                                    value="GRATUITO"
                                ${idEmpresaAtualizacao == empresa.id && param.tipoPlano == 'GRATUITO' || idEmpresaAtualizacao != empresa.id && empresa.tipoPlano.name() == 'GRATUITO' ? 'selected' : ''}
                            >
                                Gratuito
                            </option>

                            <option
                                    value="STANDART"
                                ${idEmpresaAtualizacao == empresa.id && param.tipoPlano == 'STANDART' || idEmpresaAtualizacao != empresa.id && empresa.tipoPlano.name() == 'STANDART' ? 'selected' : ''}
                            >
                                Standart
                            </option>

                            <option
                                    value="PREMIUM"
                                ${idEmpresaAtualizacao == empresa.id && param.tipoPlano == 'PREMIUM' || idEmpresaAtualizacao != empresa.id && empresa.tipoPlano.name() == 'PREMIUM' ? 'selected' : ''}
                            >
                                Premium
                            </option>

                        </select>

                        <c:if test="${idEmpresaAtualizacao == empresa.id and not empty errosAtualizacao.tipoPlano}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.tipoPlano}
                            </div>

                        </c:if>

                    </div>

                </div>


                <div class="popup-actions">

                    <label
                            for="update-company-${empresa.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${empresa.id})"
                    >
                        Cancelar
                    </label>

                    <button
                            type="submit"
                            class="save-button"
                    >
                        Atualizar Empresa
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>


<!-- ====================================================== -->
<!-- POPUPS DE CONFIRMAÇÃO DE DELETAR -->
<!-- ====================================================== -->

<c:forEach
        var="empresa"
        items="${empresas}"
>

    <input
            type="checkbox"
            id="delete-company-${empresa.id}"
            class="popup-toggle"
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Deletar Empresa
                </h2>

                <label
                        for="delete-company-${empresa.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>


            <p>

                Você deseja realmente deletar a empresa

                <strong>
                        ${empresa.nome}
                </strong>?

            </p>


            <div class="popup-actions">

                <label
                        for="delete-company-${empresa.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>


                <a
                        href="${pageContext.request.contextPath}/admin/empresas?acao=deletar&amp;id=${empresa.id}"
                        class="delete-confirm-button"
                >
                    Deletar
                </a>

            </div>

        </div>

    </div>

</c:forEach>


<!-- ====================================================== -->
<!-- POPUP DE ADICIONAR EMPRESA -->
<!-- ====================================================== -->

<input
        type="checkbox"
        id="add-company-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>


<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>
                Adicionar Empresa
            </h2>

            <label
                    for="add-company-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>


        <form
                id="add-company-form"
                action="${pageContext.request.contextPath}/admin/empresas"
                method="post"
                class="add-user-form"
        >

            <div class="form-grid">


                <div class="form-field">

                    <label for="nome">
                        Nome
                    </label>

                    <input
                            type="text"
                            id="nome"
                            name="nome"
                            value="${param.nome}"
                    >

                    <c:if test="${not empty erros.nome}">

                        <div class="campo-erro">
                                ${erros.nome}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="cnpj">
                        CNPJ
                    </label>

                    <input
                            type="text"
                            id="cnpj"
                            name="cnpj"
                            value="${param.cnpj}"
                    >

                    <c:if test="${not empty erros.cnpj}">

                        <div class="campo-erro">
                                ${erros.cnpj}
                        </div>

                    </c:if>

                </div>


                <div class="form-field full-width">

                    <label for="tipoPlano">
                        Tipo de plano
                    </label>

                    <select
                            id="tipoPlano"
                            name="tipoPlano"
                    >

                        <option
                                value=""
                        ${empty param.tipoPlano ? 'selected' : ''}
                        >
                            Selecione o tipo de plano
                        </option>

                        <option
                                value="GRATUITO"
                        ${param.tipoPlano == 'GRATUITO' ? 'selected' : ''}
                        >
                            Gratuito
                        </option>

                        <option
                                value="STANDART"
                        ${param.tipoPlano == 'STANDART' ? 'selected' : ''}
                        >
                            Standart
                        </option>

                        <option
                                value="PREMIUM"
                        ${param.tipoPlano == 'PREMIUM' ? 'selected' : ''}
                        >
                            Premium
                        </option>

                    </select>


                    <c:if test="${not empty erros.tipoPlano}">

                        <div class="campo-erro">
                                ${erros.tipoPlano}
                        </div>

                    </c:if>

                </div>


            </div>


            <div class="popup-actions">

                <label
                        for="add-company-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>

                <button
                        type="submit"
                        class="save-button"
                >
                    Adicionar Empresa
                </button>

            </div>

        </form>

    </div>

</div>


<!-- ====================================================== -->
<!-- POPUP DE LOGOUT -->
<!-- ====================================================== -->

<input
        type="checkbox"
        id="logout-popup"
        class="popup-toggle"
>


<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>
                Confirmar saída
            </h2>

            <label
                    for="logout-popup"
                    class="popup-close"
            >
                &times;
            </label>

        </div>


        <p>
            Você deseja realmente sair da página do administrador?
        </p>


        <div class="popup-actions">

            <label
                    for="logout-popup"
                    class="cancel-button"
            >
                Cancelar
            </label>

            <a
                    href="${pageContext.request.contextPath}/logout"
                    class="delete-confirm-button"
            >
                Sair
            </a>

        </div>

    </div>

</div>


<script
        src="${pageContext.request.contextPath}/assets/js/empresas.js">
</script>

</body>

</html>