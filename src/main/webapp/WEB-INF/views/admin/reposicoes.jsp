<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kairos - Reposições</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reposicoes.css">
</head>

<body>

<aside class="sidebar">

    <div class="logo-area">
        <img src="${pageContext.request.contextPath}/assets/images/logo-kairos.svg"
             alt="Kairos" class="logo">
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

        <a href="${pageContext.request.contextPath}/admin/gondolas" class="menu-item">
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

        <a href="${pageContext.request.contextPath}/admin/reposicoes" class="menu-item active">
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
        <label for="logout-popup" class="logout-button">Sair</label>
    </div>

</aside>

<main class="main-content">

    <section class="search-area">

        <form action="${pageContext.request.contextPath}/admin/reposicoes"
              method="get" class="search-form">

            <input type="text"
                   name="busca"
                   placeholder="Buscar reposição..."
                   class="search-input"
                   value="${fn:escapeXml(param.busca)}">

            <button type="submit" class="search-button">Buscar</button>
        </form>

        <label for="add-reposicao-toggle"
               class="add-reposicao-button"
               onclick="limparFormularioAdicionar()">
            + Adicionar Reposição
        </label>

    </section>

    <section class="content-card">

        <h1>Reposições</h1>

        <div class="table-wrapper">

            <table class="reposicoes-table users-table ${empty reposicoes ? 'table-empty' : ''}">

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Produto</th>
                    <th>Marca</th>
                    <th>Data da reposição</th>
                    <th>Quantidade reposta</th>
                    <th>Motivo da reposição</th>
                    <th>ID da gôndola</th>
                    <th>Ações</th>
                </tr>
                </thead>

                <tbody>

                <c:choose>

                    <c:when test="${empty reposicoes}">

                        <tr>
                            <td colspan="8" class="empty-message">
                                Nenhuma reposição cadastrada.
                            </td>
                        </tr>

                    </c:when>

                    <c:otherwise>

                        <c:forEach var="reposicao" items="${reposicoes}">

                            <tr class="reposicao-row">

                                <td>${reposicao.id}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty reposicao.produto}">
                                            <c:out value="${reposicao.produto.nome}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${reposicao.historicoReposicao.produtoNome}" default="—"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty reposicao.produto}">
                                            <c:out value="${reposicao.produto.marca}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${reposicao.historicoReposicao.produtoMarca}" default="—"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <fmt:formatDate value="${reposicao.dataReposicao}"
                                                    pattern="dd/MM/yyyy 'às' HH:mm"/>
                                </td>

                                <td>${reposicao.quantidadeReposto}</td>

                                <td class="motivo">
                                    <c:out value="${reposicao.motivoReposicao}"/>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty reposicao.gondola}">
                                            ${reposicao.gondola.id}
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${reposicao.historicoReposicao.gondolaId}" default="—"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td class="actions">

                                    <input type="checkbox"
                                           id="details-${reposicao.id}"
                                           class="details-toggle"
                                           onchange="toggleDetails(${reposicao.id})">

                                    <label for="details-${reposicao.id}" class="details-button">
                                        <span class="button-text">Ver mais</span>
                                        <span class="arrow">▼</span>
                                    </label>

                                    <label for="update-reposicao-${reposicao.id}"
                                           class="update-button"
                                           onclick="limparFormularioAtualizacao(${reposicao.id})">
                                        Atualizar
                                    </label>

                                    <label for="delete-reposicao-${reposicao.id}"
                                           class="delete-button">
                                        Deletar
                                    </label>

                                </td>

                            </tr>

                            <tr class="details-row" id="details-row-${reposicao.id}">

                                <td colspan="8">

                                    <div class="details-box">

                                        <div class="details-header">

                                            <h2>Funcionários Associados</h2>

                                            <label for="link-usuario-${reposicao.id}"
                                                   class="link-button"
                                                   onclick="limparFormularioVincular(${reposicao.id})">
                                                Vincular
                                            </label>

                                        </div>

                                        <table class="details-table">

                                            <c:set var="possuiFuncionariosAtivos" value="${false}"/>

                                            <c:forEach var="historicoUsuario"
                                                       items="${reposicao.historicoUsuariosReposicoes}">

                                                <c:if test="${historicoUsuario.vinculoAtivo}">
                                                    <c:set var="possuiFuncionariosAtivos" value="${true}"/>
                                                </c:if>

                                            </c:forEach>

                                            <thead>
                                            <tr>
                                                <th>
                                                    <c:if test="${possuiFuncionariosAtivos}">
                                                        Campo
                                                    </c:if>
                                                </th>

                                                <th>
                                                    <c:if test="${possuiFuncionariosAtivos}">
                                                        Informação
                                                    </c:if>
                                                </th>

                                                <th>
                                                    <c:if test="${possuiFuncionariosAtivos}">
                                                        Ação
                                                    </c:if>
                                                </th>
                                            </tr>
                                            </thead>

                                            <tbody>

                                            <c:forEach var="historicoUsuario"
                                                       items="${reposicao.historicoUsuariosReposicoes}">

                                                <c:if test="${historicoUsuario.vinculoAtivo}">

                                                    <tr>
                                                        <td>ID do funcionário</td>
                                                        <td class="usuario-id">
                                                                ${historicoUsuario.usuarioId}
                                                        </td>
                                                        <td rowspan="4" class="usuario-action-cell">
                                                            <label for="unlink-usuario-${reposicao.id}-${historicoUsuario.usuarioId}"
                                                                   class="unlink-button">
                                                                Desvincular
                                                            </label>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td>Nome</td>
                                                        <td>
                                                            <c:out value="${historicoUsuario.usuarioNome}"/>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td>Sobrenome</td>
                                                        <td>
                                                            <c:out value="${historicoUsuario.usuarioSobrenome}"/>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td>Empresa</td>
                                                        <td>
                                                            <c:out value="${historicoUsuario.empresaNome}"/>
                                                        </td>
                                                    </tr>

                                                </c:if>

                                            </c:forEach>

                                            <c:if test="${not possuiFuncionariosAtivos}">

                                                <tr>
                                                    <td colspan="3" class="empty-message">
                                                        Nenhum funcionário associado a esta reposição.
                                                    </td>
                                                </tr>

                                            </c:if>

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

<c:forEach var="reposicao" items="${reposicoes}">

    <input type="checkbox"
           id="update-reposicao-${reposicao.id}"
           class="popup-toggle"
        ${idReposicaoAtualizacao == reposicao.id ? 'checked' : ''}>

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Atualizar Reposição</h2>

                <label for="update-reposicao-${reposicao.id}"
                       class="popup-close"
                       onclick="limparFormularioAtualizacao(${reposicao.id})">
                    &times;
                </label>

            </div>

            <form id="update-reposicao-form-${reposicao.id}"
                  action="${pageContext.request.contextPath}/admin/reposicoes"
                  method="post"
                  class="reposicao-form">

                <input type="hidden" name="acao" value="atualizar">
                <input type="hidden" name="id" value="${reposicao.id}">

                <div class="form-grid">

                    <div class="form-field full-width">

                        <label for="quantidade-${reposicao.id}">
                            Quantidade reposta
                        </label>

                        <input type="number"
                               id="quantidade-${reposicao.id}"
                               name="quantidadeReposto"
                               min="1"
                               value="${idReposicaoAtualizacao == reposicao.id ? fn:escapeXml(param.quantidadeReposto) : reposicao.quantidadeReposto}"
                               data-original="${reposicao.quantidadeReposto}">

                        <c:if test="${idReposicaoAtualizacao == reposicao.id && not empty errosAtualizacao.quantidadeReposto}">
                            <div class="campo-erro">
                                <c:out value="${errosAtualizacao.quantidadeReposto}"/>
                            </div>
                        </c:if>

                    </div>

                    <div class="form-field full-width">

                        <label for="motivo-${reposicao.id}">
                            Motivo da reposição
                        </label>

                        <textarea id="motivo-${reposicao.id}"
                                  name="motivoReposicao"
                                  rows="4"
                                  data-original="${fn:escapeXml(reposicao.motivoReposicao)}">${idReposicaoAtualizacao == reposicao.id ? fn:escapeXml(param.motivoReposicao) : fn:escapeXml(reposicao.motivoReposicao)}</textarea>

                        <c:if test="${idReposicaoAtualizacao == reposicao.id && not empty errosAtualizacao.motivoReposicao}">
                            <div class="campo-erro">
                                <c:out value="${errosAtualizacao.motivoReposicao}"/>
                            </div>
                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label for="update-reposicao-${reposicao.id}"
                           class="cancel-button"
                           onclick="limparFormularioAtualizacao(${reposicao.id})">
                        Cancelar
                    </label>

                    <button type="submit" class="save-button">
                        Atualizar Reposição
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>

<c:forEach var="reposicao" items="${reposicoes}">

    <input type="checkbox"
           id="link-usuario-${reposicao.id}"
           class="popup-toggle"
        ${idReposicaoVincular == reposicao.id ? 'checked' : ''}>

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Vincular Funcionário</h2>

                <label for="link-usuario-${reposicao.id}"
                       class="popup-close"
                       onclick="limparFormularioVincular(${reposicao.id})">
                    &times;
                </label>

            </div>

            <form id="link-usuario-form-${reposicao.id}"
                  action="${pageContext.request.contextPath}/admin/usuarios-reposicoes"
                  method="post"
                  class="reposicao-form">

                <input type="hidden" name="acao" value="vincular">
                <input type="hidden" name="idReposicao" value="${reposicao.id}">

                <div class="form-grid">

                    <div class="form-field full-width">

                        <label for="idUsuario-${reposicao.id}">
                            ID do funcionário
                        </label>

                        <input type="number"
                               id="idUsuario-${reposicao.id}"
                               name="idUsuario"
                               min="1"
                               value="${idReposicaoVincular == reposicao.id ? idUsuarioVincular : ''}">

                        <c:if test="${idReposicaoVincular == reposicao.id && not empty erroVincularReposicao}">
                            <div class="campo-erro">
                                <c:out value="${erroVincularReposicao}"/>
                            </div>
                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label for="link-usuario-${reposicao.id}"
                           class="cancel-button"
                           onclick="limparFormularioVincular(${reposicao.id})">
                        Cancelar
                    </label>

                    <button type="submit" class="save-button">
                        Vincular
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>

<c:forEach var="reposicao" items="${reposicoes}">

    <c:forEach var="historicoUsuario"
               items="${reposicao.historicoUsuariosReposicoes}">

        <c:if test="${historicoUsuario.vinculoAtivo}">

            <input type="checkbox"
                   id="unlink-usuario-${reposicao.id}-${historicoUsuario.usuarioId}"
                   class="popup-toggle">

            <div class="popup-overlay">

                <div class="popup">

                    <div class="popup-header">

                        <h2>Desvincular Funcionário</h2>

                        <label for="unlink-usuario-${reposicao.id}-${historicoUsuario.usuarioId}"
                               class="popup-close">
                            &times;
                        </label>

                    </div>

                    <p>
                        Você deseja realmente desvincular o funcionário
                        <strong>
                            <c:out value="${historicoUsuario.usuarioNome}"/>
                            <c:out value="${historicoUsuario.usuarioSobrenome}"/>
                        </strong>
                        da reposição
                        <strong>${reposicao.id}</strong>?
                    </p>

                    <form action="${pageContext.request.contextPath}/admin/usuarios-reposicoes"
                          method="post"
                          class="reposicao-form">

                        <input type="hidden" name="acao" value="desvincular">
                        <input type="hidden" name="idUsuario" value="${historicoUsuario.usuarioId}">
                        <input type="hidden" name="idReposicao" value="${reposicao.id}">

                        <div class="popup-actions">

                            <label for="unlink-usuario-${reposicao.id}-${historicoUsuario.usuarioId}"
                                   class="cancel-button">
                                Cancelar
                            </label>

                            <button type="submit" class="unlink-confirm-button">
                                Desvincular
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </c:if>

    </c:forEach>

</c:forEach>

<c:forEach var="reposicao" items="${reposicoes}">

    <input type="checkbox"
           id="delete-reposicao-${reposicao.id}"
           class="popup-toggle">

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Deletar Reposição</h2>

                <label for="delete-reposicao-${reposicao.id}"
                       class="popup-close">
                    &times;
                </label>

            </div>

            <p>
                Você deseja realmente deletar a reposição
                <strong>${reposicao.id}</strong>?
            </p>

            <div class="popup-actions">

                <label for="delete-reposicao-${reposicao.id}"
                       class="cancel-button">
                    Cancelar
                </label>

                <a href="${pageContext.request.contextPath}/admin/reposicoes?acao=deletar&amp;id=${reposicao.id}"
                   class="delete-confirm-button">
                    Deletar
                </a>

            </div>

        </div>

    </div>

</c:forEach>

<input type="checkbox"
       id="add-reposicao-toggle"
       class="popup-toggle"
${not empty erros ? 'checked' : ''}>

<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>Adicionar Reposição</h2>

            <label for="add-reposicao-toggle"
                   class="popup-close"
                   onclick="limparFormularioAdicionar()">
                &times;
            </label>

        </div>

        <form id="add-reposicao-form"
              action="${pageContext.request.contextPath}/admin/reposicoes"
              method="post"
              class="reposicao-form">

            <input type="hidden" name="acao" value="adicionar">

            <div class="form-grid">

                <div class="form-field">

                    <label for="produto">ID do produto</label>

                    <input type="number"
                           id="produto"
                           name="produtoId"
                           min="1"
                           value="${fn:escapeXml(param.produtoId)}">

                    <c:if test="${not empty erros.produtoId}">
                        <div class="campo-erro">
                            <c:out value="${erros.produtoId}"/>
                        </div>
                    </c:if>

                </div>

                <div class="form-field">

                    <label for="gondola">ID da gôndola</label>

                    <input type="number"
                           id="gondola"
                           name="gondolaId"
                           min="1"
                           value="${fn:escapeXml(param.gondolaId)}">

                    <c:if test="${not empty erros.gondolaId}">
                        <div class="campo-erro">
                            <c:out value="${erros.gondolaId}"/>
                        </div>
                    </c:if>

                </div>

                <div class="form-field">

                    <label for="dataReposicao">
                        Data da reposição
                    </label>

                    <input type="datetime-local"
                           id="dataReposicao"
                           name="dataReposicao"
                           value="${fn:escapeXml(param.dataReposicao)}">

                    <c:if test="${not empty erros.dataReposicao}">
                        <div class="campo-erro">
                            <c:out value="${erros.dataReposicao}"/>
                        </div>
                    </c:if>

                </div>

                <div class="form-field">

                    <label for="quantidadeReposto">
                        Quantidade reposta
                    </label>

                    <input type="number"
                           id="quantidadeReposto"
                           name="quantidadeReposto"
                           min="1"
                           value="${fn:escapeXml(param.quantidadeReposto)}">

                    <c:if test="${not empty erros.quantidadeReposto}">
                        <div class="campo-erro">
                            <c:out value="${erros.quantidadeReposto}"/>
                        </div>
                    </c:if>

                </div>

                <div class="form-field full-width">

                    <label for="motivoReposicao">
                        Motivo da reposição
                    </label>

                    <textarea id="motivoReposicao"
                              name="motivoReposicao"
                              rows="4">${not empty erros ? fn:escapeXml(param.motivoReposicao) : 'Falta de Produtos'}</textarea>

                    <c:if test="${not empty erros.motivoReposicao}">
                        <div class="campo-erro">
                            <c:out value="${erros.motivoReposicao}"/>
                        </div>
                    </c:if>

                </div>

            </div>

            <div class="popup-actions">

                <label for="add-reposicao-toggle"
                       class="cancel-button"
                       onclick="limparFormularioAdicionar()">
                    Cancelar
                </label>

                <button type="submit" class="save-button">
                    Adicionar Reposição
                </button>

            </div>

        </form>

    </div>

</div>

<input type="checkbox"
       id="logout-popup"
       class="popup-toggle">

<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>Confirmar saída</h2>

            <label for="logout-popup" class="popup-close">
                &times;
            </label>

        </div>

        <p>Você deseja realmente sair da página do administrador?</p>

        <div class="popup-actions">

            <label for="logout-popup" class="cancel-button">
                Cancelar
            </label>

            <a href="${pageContext.request.contextPath}/logout"
               class="delete-confirm-button">
                Sair
            </a>

        </div>

    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/reposicoes.js"></script>

</body>
</html>
