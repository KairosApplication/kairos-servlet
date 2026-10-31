<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Kairos - Produtos</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/produtos.css">

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
                href="${pageContext.request.contextPath}/admin/setores"
                class="menu-item"
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
                href="#"
                class="menu-item active"
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

    <section class="search-area">

        <form
                action="${pageContext.request.contextPath}/admin/produtos"
                method="get"
                class="search-form"
        >

            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar produto..."
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
                for="add-product-toggle"
                class="add-user-button"
        >
            + Adicionar Produto
        </label>

    </section>

    <c:if test="${not empty mensagemErro}">

        <div class="mensagem-erro">
                ${mensagemErro}
        </div>

    </c:if>

    <section class="content-card">

        <h1>Produtos</h1>

        <div class="table-wrapper">

            <table class="users-table ${empty produtos ? 'table-empty' : ''}">

                <thead>

                <tr>
                    <th>ID</th>
                    <th>Marca</th>
                    <th>Nome</th>
                    <th>Quantidade no estoque</th>
                    <th>Ações</th>
                </tr>

                </thead>

                <tbody>

                <c:choose>

                    <c:when test="${empty produtos}">

                        <tr>
                            <td colspan="5" class="empty-message">
                                Nenhum produto cadastrado.
                            </td>
                        </tr>

                    </c:when>

                    <c:otherwise>

                        <c:forEach
                                var="produto"
                                items="${produtos}"
                        >

                            <tr class="user-row">

                                <td>
                                        ${produto.id}
                                </td>

                                <td>
                                        ${produto.marca}
                                </td>

                                <td>
                                        ${produto.nome}
                                </td>

                                <td>
                                        ${produto.quantidadeEstoque}
                                </td>

                                <td class="actions">

                                    <label
                                            for="update-product-${produto.id}"
                                            class="update-button"
                                    >
                                        Atualizar
                                    </label>

                                    <label
                                            for="delete-product-${produto.id}"
                                            class="delete-button"
                                    >
                                        Deletar
                                    </label>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:otherwise>

                </c:choose>

                </tbody>

            </table>

        </div>

    </section>

</main>

<c:forEach
        var="produto"
        items="${produtos}"
>

    <input
            type="checkbox"
            id="update-product-${produto.id}"
            class="popup-toggle"
        ${idProdutoAtualizacao == produto.id ? 'checked' : ''}
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Atualizar Produto
                </h2>

                <label
                        for="update-product-${produto.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${produto.id})"
                >
                    &times;
                </label>

            </div>

            <form
                    id="update-product-form-${produto.id}"
                    action="${pageContext.request.contextPath}/admin/produtos"
                    method="post"
                    class="add-user-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${produto.id}"
                >

                <div class="form-grid">

                    <div class="form-field full-width">

                        <label for="quantidadeEstoque-${produto.id}">
                            Quantidade no estoque
                        </label>

                        <input
                                type="number"
                                id="quantidadeEstoque-${produto.id}"
                                name="quantidadeEstoque"
                                min="0"
                                value="${idProdutoAtualizacao == produto.id && not empty param.quantidadeEstoque ? param.quantidadeEstoque : produto.quantidadeEstoque}"
                                data-original="${produto.quantidadeEstoque}"
                        >

                        <c:if test="${idProdutoAtualizacao == produto.id and not empty errosAtualizacao.quantidadeEstoque}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.quantidadeEstoque}
                            </div>

                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label
                            for="update-product-${produto.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${produto.id})"
                    >
                        Cancelar
                    </label>

                    <button
                            type="submit"
                            class="save-button"
                    >
                        Atualizar Produto
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>

<c:forEach
        var="produto"
        items="${produtos}"
>

    <input
            type="checkbox"
            id="delete-product-${produto.id}"
            class="popup-toggle"
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Deletar Produto
                </h2>

                <label
                        for="delete-product-${produto.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>

            <p>

                Você deseja realmente deletar o produto

                <strong>
                        ${produto.nome}
                </strong>?

            </p>

            <div class="popup-actions">

                <label
                        for="delete-product-${produto.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>

                <a
                        href="${pageContext.request.contextPath}/admin/produtos?acao=deletar&amp;id=${produto.id}"
                        class="delete-confirm-button"
                >
                    Deletar
                </a>

            </div>

        </div>

    </div>

</c:forEach>

<input
        type="checkbox"
        id="add-product-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>

<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>
                Adicionar Produto
            </h2>

            <label
                    for="add-product-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>

        <form
                id="add-product-form"
                action="${pageContext.request.contextPath}/admin/produtos"
                method="post"
                class="add-user-form"
        >

            <div class="form-grid">

                <div class="form-field">

                    <label for="marca">
                        Marca
                    </label>

                    <input
                            type="text"
                            id="marca"
                            name="marca"
                            value="${not empty erros ? param.marca : ''}"
                    >

                    <c:if test="${not empty erros.marca}">

                        <div class="campo-erro">
                                ${erros.marca}
                        </div>

                    </c:if>

                </div>

                <div class="form-field">

                    <label for="nome">
                        Nome
                    </label>

                    <input
                            type="text"
                            id="nome"
                            name="nome"
                            value="${not empty erros ? param.nome : ''}"
                    >

                    <c:if test="${not empty erros.nome}">

                        <div class="campo-erro">
                                ${erros.nome}
                        </div>

                    </c:if>

                </div>

                <div class="form-field full-width">

                    <label for="quantidadeEstoque">
                        Quantidade no estoque
                    </label>

                    <input
                            type="number"
                            id="quantidadeEstoque"
                            name="quantidadeEstoque"
                            min="0"
                            value="${not empty erros ? param.quantidadeEstoque : ''}"
                    >

                    <c:if test="${not empty erros.quantidadeEstoque}">

                        <div class="campo-erro">
                                ${erros.quantidadeEstoque}
                        </div>

                    </c:if>

                </div>

            </div>

            <div class="popup-actions">

                <label
                        for="add-product-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>

                <button
                        type="submit"
                        class="save-button"
                >
                    Adicionar Produto
                </button>

            </div>

        </form>

    </div>

</div>

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
        src="${pageContext.request.contextPath}/assets/js/produtos.js">
</script>

</body>

</html>
