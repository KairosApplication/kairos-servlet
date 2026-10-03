<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Kairos - Setores</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/setores.css">

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

        <a
                href="${pageContext.request.contextPath}/admin/usuarios"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/usuario.svg"
                        alt=""
                >

            </span>

            <span>Usuários</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/empresas"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/empresa.svg"
                        alt=""
                >

            </span>

            <span>Empresa</span>

        </a>


        <a
                href="#"
                class="menu-item active"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/setor.svg"
                        alt=""
                >

            </span>

            <span>Setor</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/gondolas"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/gondola.svg"
                        alt=""
                >

            </span>

            <span>Gôndola</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/produtos"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/produto.svg"
                        alt=""
                >

            </span>

            <span>Produto</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/reposicoes"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/reposicao.svg"
                        alt=""
                >

            </span>

            <span>Reposição</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/alertas"
                class="menu-item"
        >

            <span class="menu-icon">

                <img
                        src="${pageContext.request.contextPath}/assets/images/alerta.svg"
                        alt=""
                >

            </span>

            <span>Alerta</span>

        </a>


        <a
                href="${pageContext.request.contextPath}/admin/compras"
                class="menu-item"
        >

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
                action="${pageContext.request.contextPath}/admin/setores"
                method="get"
                class="search-form"
        >

            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar setor..."
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
                for="add-sector-toggle"
                class="add-user-button"
        >
            + Adicionar Setor
        </label>

    </section>


    <!-- ====================================================== -->
    <!-- TABELA -->
    <!-- ====================================================== -->

    <section class="content-card">

        <h1>Setores</h1>


        <div class="table-wrapper">

            <table class="users-table">

                <thead>

                <tr>

                    <th>ID</th>
                    <th>Nome</th>
                    <th>Categoria</th>
                    <th>Ações</th>

                </tr>

                </thead>


                <tbody>

                <c:forEach
                        var="setor"
                        items="${setores}"
                >

                    <tr class="user-row">

                        <td>
                                ${setor.id}
                        </td>

                        <td>
                                ${setor.nome}
                        </td>

                        <td>
                                ${setor.categoria}
                        </td>

                        <td class="actions">

                            <!-- ATUALIZAR -->

                            <label
                                    for="update-sector-${setor.id}"
                                    class="update-button"
                            >
                                Atualizar
                            </label>


                            <!-- DELETAR -->

                            <label
                                    for="delete-sector-${setor.id}"
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
        var="setor"
        items="${setores}"
>

    <input
            type="checkbox"
            id="update-sector-${setor.id}"
            class="popup-toggle"
        ${idSetorAtualizacao == setor.id ? 'checked' : ''}
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Atualizar Setor
                </h2>

                <label
                        for="update-sector-${setor.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${setor.id})"
                >
                    &times;
                </label>

            </div>


            <form
                    id="update-sector-form-${setor.id}"
                    action="${pageContext.request.contextPath}/admin/setores"
                    method="post"
                    class="add-user-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${setor.id}"
                >


                <div class="form-grid">


                    <!-- NOME -->

                    <div class="form-field">

                        <label for="nome-${setor.id}">
                            Nome
                        </label>

                        <input
                                type="text"
                                id="nome-${setor.id}"
                                name="nome"
                                value="${idSetorAtualizacao == setor.id && not empty param.nome ? param.nome : setor.nome}"
                                data-original="${setor.nome}"
                        >

                        <c:if test="${idSetorAtualizacao == setor.id and not empty errosAtualizacao.nome}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.nome}
                            </div>

                        </c:if>

                    </div>


                    <!-- CATEGORIA -->

                    <div class="form-field">

                        <label for="categoria-${setor.id}">
                            Categoria
                        </label>

                        <input
                                type="text"
                                id="categoria-${setor.id}"
                                name="categoria"
                                value="${idSetorAtualizacao == setor.id && not empty param.categoria ? param.categoria : setor.categoria}"
                                data-original="${setor.categoria}"
                        >

                        <c:if test="${idSetorAtualizacao == setor.id and not empty errosAtualizacao.categoria}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.categoria}
                            </div>

                        </c:if>

                    </div>


                </div>


                <div class="popup-actions">

                    <label
                            for="update-sector-${setor.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${setor.id})"
                    >
                        Cancelar
                    </label>


                    <button
                            type="submit"
                            class="save-button"
                    >
                        Atualizar Setor
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
        var="setor"
        items="${setores}"
>

    <input
            type="checkbox"
            id="delete-sector-${setor.id}"
            class="popup-toggle"
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Deletar Setor
                </h2>

                <label
                        for="delete-sector-${setor.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>


            <p>

                Você deseja realmente deletar o setor

                <strong>
                        ${setor.nome}
                </strong>?

            </p>


            <div class="popup-actions">

                <label
                        for="delete-sector-${setor.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>


                <a
                        href="${pageContext.request.contextPath}/admin/setores?acao=deletar&amp;id=${setor.id}"
                        class="delete-confirm-button"
                >
                    Deletar
                </a>

            </div>

        </div>

    </div>

</c:forEach>


<!-- ====================================================== -->
<!-- POPUP DE ADICIONAR SETOR -->
<!-- ====================================================== -->

<input
        type="checkbox"
        id="add-sector-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>


<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>
                Adicionar Setor
            </h2>

            <label
                    for="add-sector-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>


        <form
                id="add-sector-form"
                action="${pageContext.request.contextPath}/admin/setores"
                method="post"
                class="add-user-form"
        >

            <div class="form-grid">


                <!-- NOME -->

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


                <!-- CATEGORIA -->

                <div class="form-field">

                    <label for="categoria">
                        Categoria
                    </label>

                    <input
                            type="text"
                            id="categoria"
                            name="categoria"
                            value="${param.categoria}"
                    >

                    <c:if test="${not empty erros.categoria}">

                        <div class="campo-erro">
                                ${erros.categoria}
                        </div>

                    </c:if>

                </div>


            </div>


            <div class="popup-actions">

                <label
                        for="add-sector-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>


                <button
                        type="submit"
                        class="save-button"
                >
                    Adicionar Setor
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
        src="${pageContext.request.contextPath}/assets/js/setores.js">
</script>

</body>

</html>