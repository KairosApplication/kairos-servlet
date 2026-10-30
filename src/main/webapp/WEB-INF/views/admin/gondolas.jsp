<%@ page import="com.kairos.utils.Formatter" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kairos - Gôndolas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/gondolas.css">

    <style>
        .gondolas-table td.empty-message,
        .details-table td.empty-message {
            text-align: center;
            vertical-align: middle;
            padding: 12px;
            color: #6f8496 !important;
            font-size: 14px;
            font-weight: 400;
        }

        .gondolas-table.table-empty thead th,
        .details-table.table-empty thead th {
            color: var(--green);
        }
    </style>
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
                <img src="${pageContext.request.contextPath}/assets/images/usuario.svg" alt="">
            </span>
            <span>Usuários</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/empresas" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/empresa.svg" alt="">
            </span>
            <span>Empresa</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/setores" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/setor.svg" alt="">
            </span>
            <span>Setor</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/gondolas" class="menu-item active">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/gondola.svg" alt="">
            </span>
            <span>Gôndola</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/produtos" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/produto.svg" alt="">
            </span>
            <span>Produto</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/reposicoes" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/reposicao.svg" alt="">
            </span>
            <span>Reposição</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/alertas" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/alerta.svg" alt="">
            </span>
            <span>Alerta</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/compras" class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/compra.svg" alt="">
            </span>
            <span>Compra</span>
        </a>

    </nav>

    <div class="logout-area">
        <label for="logout-popup" class="logout-button">
            Sair
        </label>
    </div>

</aside>

<main class="main-content">

    <section class="search-area">

        <form
                action="${pageContext.request.contextPath}/admin/gondolas"
                method="get"
                class="search-form"
        >
            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar gôndola..."
                    class="search-input"
                    value="${param.busca}"
            >

            <button type="submit" class="search-button">
                Buscar
            </button>
        </form>

        <label for="add-gondola-toggle" class="add-gondola-button">
            + Adicionar Gôndola
        </label>

    </section>

    <c:if test="${not empty mensagemErro}">
        <div class="mensagem-erro">
                ${mensagemErro}
        </div>
    </c:if>

    <section class="content-card">

        <h1>Gôndolas</h1>

        <div class="table-wrapper">

            <table class="gondolas-table ${empty gondolas ? 'table-empty' : ''}">

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Capacidade máxima</th>
                    <th>Setor</th>
                    <th>Ações</th>
                </tr>
                </thead>

                <tbody>

                <c:choose>

                    <c:when test="${empty gondolas}">

                        <tr>
                            <td colspan="4" class="empty-message">
                                Nenhuma gôndola cadastrada.
                            </td>
                        </tr>

                    </c:when>

                    <c:otherwise>

                        <c:forEach var="gondola" items="${gondolas}">

                            <tr class="gondola-row">

                                <td>
                                        ${gondola.id}
                                </td>

                                <td>
                                        ${gondola.capacidadeMaxima}
                                </td>

                                <td>
                                        ${gondola.setor.nome}
                                </td>

                                <td class="actions-cell">

                                    <div class="actions">

                                        <input
                                                type="checkbox"
                                                id="details-${gondola.id}"
                                                class="details-toggle"
                                        >

                                        <label
                                                for="details-${gondola.id}"
                                                class="details-button"
                                        >
                                            <span class="button-text">
                                                Ver mais
                                            </span>

                                            <span class="arrow">
                                                ▼
                                            </span>
                                        </label>

                                        <label
                                                for="update-gondola-${gondola.id}"
                                                class="update-button"
                                                onclick="limparFormularioAtualizacao(${gondola.id})"
                                        >
                                            Atualizar
                                        </label>

                                        <label
                                                for="delete-gondola-${gondola.id}"
                                                class="delete-button"
                                        >
                                            Deletar
                                        </label>

                                    </div>

                                </td>

                            </tr>

                            <tr
                                    class="details-row"
                                    id="details-row-${gondola.id}"
                            >

                                <td colspan="4">

                                    <div class="details-box">

                                        <div class="details-header">

                                            <h2>Produtos Associados</h2>

                                            <label
                                                    for="link-produto-${gondola.id}"
                                                    class="link-button"
                                            >
                                                Vincular
                                            </label>

                                        </div>

                                        <table class="details-table ${empty produtosPorGondola[gondola.id] ? 'table-empty' : ''}">

                                            <thead>
                                            <tr>
                                                <th>Campo</th>
                                                <th>Informação</th>
                                                <th>Ação</th>
                                            </tr>
                                            </thead>

                                            <tbody>

                                            <c:choose>

                                                <c:when test="${empty produtosPorGondola[gondola.id]}">

                                                    <tr>
                                                        <td colspan="3" class="empty-message">
                                                            Nenhum produto associado a esta gôndola.
                                                        </td>
                                                    </tr>

                                                </c:when>

                                                <c:otherwise>

                                                    <c:forEach
                                                            var="produto"
                                                            items="${produtosPorGondola[gondola.id]}"
                                                    >

                                                        <tr>

                                                            <td>
                                                                ID do produto
                                                            </td>

                                                            <td class="id-produto">
                                                                    ${produto.id}
                                                            </td>

                                                            <td
                                                                    rowspan="4"
                                                                    class="produto-action-cell"
                                                            >

                                                                <label
                                                                        for="unlink-produto-${gondola.id}-${produto.id}"
                                                                        class="unlink-button"
                                                                >
                                                                    Desvincular
                                                                </label>

                                                            </td>

                                                        </tr>

                                                        <tr>

                                                            <td>
                                                                Marca do produto
                                                            </td>

                                                            <td>
                                                                    ${produto.marca}
                                                            </td>

                                                        </tr>

                                                        <tr>

                                                            <td>
                                                                Nome do produto
                                                            </td>

                                                            <td>
                                                                    ${produto.nome}
                                                            </td>

                                                        </tr>

                                                        <tr>

                                                            <td>
                                                                Quantidade em estoque
                                                            </td>

                                                            <td>
                                                                    ${produto.quantidadeEstoque}
                                                            </td>

                                                        </tr>

                                                    </c:forEach>

                                                </c:otherwise>

                                            </c:choose>

                                            </tbody>

                                        </table>

                                    </div>

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

<c:forEach var="gondola" items="${gondolas}">

    <input
            type="checkbox"
            id="update-gondola-${gondola.id}"
            class="popup-toggle"
        ${idGondolaAtualizacao == gondola.id ? 'checked' : ''}
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Atualizar Gôndola</h2>

                <label
                        for="update-gondola-${gondola.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${gondola.id})"
                >
                    &times;
                </label>

            </div>

            <form
                    id="update-gondola-form-${gondola.id}"
                    action="${pageContext.request.contextPath}/admin/gondolas"
                    method="post"
                    class="add-gondola-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${gondola.id}"
                >

                <div class="form-grid">

                    <div class="form-field full-width">

                        <label for="capacidade-${gondola.id}">
                            Capacidade máxima
                        </label>

                        <input
                                type="number"
                                id="capacidade-${gondola.id}"
                                name="capacidadeMaxima"
                                min="1"
                                data-original="${gondola.capacidadeMaxima}"
                                value="${idGondolaAtualizacao == gondola.id ? param.capacidadeMaxima : gondola.capacidadeMaxima}"
                        >

                        <c:if test="${idGondolaAtualizacao == gondola.id and not empty errosAtualizacao.capacidadeMaxima}">
                            <div class="campo-erro">
                                    ${errosAtualizacao.capacidadeMaxima}
                            </div>
                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label
                            for="update-gondola-${gondola.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${gondola.id})"
                    >
                        Cancelar
                    </label>

                    <button type="submit" class="save-button">
                        Atualizar Gôndola
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>

<c:forEach var="gondola" items="${gondolas}">

    <input
            type="checkbox"
            id="link-produto-${gondola.id}"
            class="popup-toggle"
        ${idGondolaVincularProduto == gondola.id ? 'checked' : ''}
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Vincular Produto</h2>

                <label
                        for="link-produto-${gondola.id}"
                        class="popup-close"
                        onclick="limparFormularioVincular(${gondola.id})"
                >
                    &times;
                </label>

            </div>

            <form
                    id="link-produto-form-${gondola.id}"
                    action="${pageContext.request.contextPath}/admin/gondolas-produtos"
                    method="post"
                    class="add-gondola-form"
            >

                <input
                        type="hidden"
                        name="acao"
                        value="vincular"
                >

                <input
                        type="hidden"
                        name="idGondola"
                        value="${gondola.id}"
                >

                <div class="form-grid">

                    <div class="form-field">

                        <label for="marca-produto-${gondola.id}">
                            Marca do produto
                        </label>

                        <input
                                type="text"
                                id="marca-produto-${gondola.id}"
                                name="marcaProduto"
                                value="${idGondolaVincularProduto == gondola.id ? marcaProdutoVincular : ''}"
                        >

                        <c:if test="${idGondolaVincularProduto == gondola.id and not empty erroMarcaVincular}">
                            <div class="campo-erro">
                                    ${erroMarcaVincular}
                            </div>
                        </c:if>

                    </div>

                    <div class="form-field">

                        <label for="nome-produto-${gondola.id}">
                            Nome do produto
                        </label>

                        <input
                                type="text"
                                id="nome-produto-${gondola.id}"
                                name="nomeProduto"
                                value="${idGondolaVincularProduto == gondola.id ? nomeProdutoVincular : ''}"
                        >

                        <c:if test="${idGondolaVincularProduto == gondola.id and not empty erroProdutoVincular}">
                            <div class="campo-erro">
                                    ${erroProdutoVincular}
                            </div>
                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label
                            for="link-produto-${gondola.id}"
                            class="cancel-button"
                            onclick="limparFormularioVincular(${gondola.id})"
                    >
                        Cancelar
                    </label>

                    <button type="submit" class="save-button">
                        Vincular Produto
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>

<c:forEach var="gondola" items="${gondolas}">

    <c:forEach var="produto" items="${produtosPorGondola[gondola.id]}">

        <input
                type="checkbox"
                id="unlink-produto-${gondola.id}-${produto.id}"
                class="popup-toggle"
        >

        <div class="popup-overlay">

            <div class="popup">

                <div class="popup-header">

                    <h2>Desvincular Produto</h2>

                    <label
                            for="unlink-produto-${gondola.id}-${produto.id}"
                            class="popup-close"
                    >
                        &times;
                    </label>

                </div>

                <p>
                    Você deseja realmente desvincular o produto
                    <strong>${produto.nome}</strong>
                    desta gôndola?
                </p>

                <form
                        action="${pageContext.request.contextPath}/admin/gondolas-produtos"
                        method="post"
                        class="add-gondola-form"
                >

                    <input
                            type="hidden"
                            name="acao"
                            value="desvincular"
                    >

                    <input
                            type="hidden"
                            name="idGondola"
                            value="${gondola.id}"
                    >

                    <input
                            type="hidden"
                            name="nomeProduto"
                            value="${produto.nome}"
                    >

                    <input
                            type="hidden"
                            name="marcaProduto"
                            value="${produto.marca}"
                    >

                    <div class="popup-actions">

                        <label
                                for="unlink-produto-${gondola.id}-${produto.id}"
                                class="cancel-button"
                        >
                            Cancelar
                        </label>

                        <button type="submit" class="unlink-confirm-button">
                            Desvincular
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </c:forEach>

</c:forEach>

<c:forEach var="gondola" items="${gondolas}">

    <input
            type="checkbox"
            id="delete-gondola-${gondola.id}"
            class="popup-toggle"
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Deletar Gôndola</h2>

                <label
                        for="delete-gondola-${gondola.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>

            <p>
                Você deseja realmente deletar a gôndola
                <strong>${gondola.id}</strong>?
            </p>

            <div class="popup-actions">

                <label
                        for="delete-gondola-${gondola.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>

                <a
                        href="${pageContext.request.contextPath}/admin/gondolas?acao=deletar&amp;id=${gondola.id}"
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
        id="add-gondola-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>

<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>Adicionar Gôndola</h2>

            <label
                    for="add-gondola-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>

        <form
                id="add-gondola-form"
                action="${pageContext.request.contextPath}/admin/gondolas"
                method="post"
                class="add-gondola-form"
        >

            <div class="form-grid">

                <div class="form-field">

                    <label for="capacidadeMaxima">
                        Capacidade máxima
                    </label>

                    <input
                            type="number"
                            id="capacidadeMaxima"
                            name="capacidadeMaxima"
                            min="1"
                            value="${not empty erros ? param.capacidadeMaxima : ''}"
                    >

                    <c:if test="${not empty erros.capacidadeMaxima}">
                        <div class="campo-erro">
                                ${erros.capacidadeMaxima}
                        </div>
                    </c:if>

                </div>

                <div class="form-field">

                    <label for="nomeSetor">
                        Nome do setor
                    </label>

                    <input
                            type="text"
                            id="nomeSetor"
                            name="nomeSetor"
                            value="${not empty erros ? param.nomeSetor : ''}"
                    >

                    <c:if test="${not empty erros.setor}">
                        <div class="campo-erro">
                                ${erros.setor}
                        </div>
                    </c:if>

                </div>

            </div>

            <div class="popup-actions">

                <label
                        for="add-gondola-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>

                <button type="submit" class="save-button">
                    Adicionar Gôndola
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

            <h2>Confirmar saída</h2>

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

<script src="${pageContext.request.contextPath}/assets/js/gondolas.js"></script>

</body>
</html>
