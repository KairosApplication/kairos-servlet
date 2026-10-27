<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Kairos - Alertas</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/alertas.css">
</head>

<body>

<!-- SIDEBAR -->

<aside class="sidebar">

    <div class="logo-area">
        <img
                src="${pageContext.request.contextPath}/assets/images/logo-kairos.svg"
                alt="Kairos"
                class="logo"
        >
    </div>

    <nav class="menu">

        <a href="${pageContext.request.contextPath}/admin/usuarios"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/usuario.svg" alt="">
            </span>
            <span>Usuários</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/empresas"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/empresa.svg" alt="">
            </span>
            <span>Empresa</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/setores"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/setor.svg" alt="">
            </span>
            <span>Setor</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/gondolas"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/gondola.svg" alt="">
            </span>
            <span>Gôndola</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/produtos"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/produto.svg" alt="">
            </span>
            <span>Produto</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/reposicoes"
           class="menu-item">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/reposicao.svg" alt="">
            </span>
            <span>Reposição</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/alertas"
           class="menu-item active">
            <span class="menu-icon">
                <img src="${pageContext.request.contextPath}/assets/images/alerta.svg" alt="">
            </span>
            <span>Alerta</span>
        </a>

        <a href="${pageContext.request.contextPath}/admin/compras"
           class="menu-item">
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


<!-- CONTEÚDO PRINCIPAL -->

<main class="main-content">

    <section class="search-area">

        <form
                action="${pageContext.request.contextPath}/admin/alertas"
                method="get"
                class="search-form"
        >

            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar alerta..."
                    class="search-input"
                    value="${param.busca}"
            >

            <button type="submit" class="search-button">
                Buscar
            </button>

        </form>

        <label for="add-alerta-toggle" class="add-user-button">
            + Adicionar Alerta
        </label>

    </section>


    <!-- TABELA -->

    <section class="content-card">

        <h1>Alertas</h1>

        <div class="table-wrapper">

            <table class="alerts-table ${empty alertas ? 'table-empty' : ''}">

                <thead>
                <tr>
                    <th>ID do alerta</th>
                    <th>ID do usuário</th>
                    <th>ID da gôndola</th>
                    <th>ID do produto</th>
                    <th>Data da ruptura</th>
                    <th>Descrição do alerta</th>
                    <th>Status do alerta</th>
                    <th>Data da resolução</th>
                    <th>Ações</th>
                </tr>
                </thead>

                <tbody>

                <c:choose>

                    <c:when test="${empty alertas}">

                        <tr>
                            <td colspan="9" class="empty-message">
                                Nenhum alerta cadastrado.
                            </td>
                        </tr>

                    </c:when>

                    <c:otherwise>

                        <c:forEach var="alerta" items="${alertas}">

                            <tr class="alert-row">

                                <td>${alerta.id}</td>

                                <!-- ID HISTÓRICO DO USUÁRIO -->
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty alerta.historicoAlerta}">
                                            ${alerta.historicoAlerta.usuarioId}
                                        </c:when>
                                        <c:when test="${not empty alerta.usuario}">
                                            ${alerta.usuario.id}
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <!-- ID HISTÓRICO DA GÔNDOLA -->
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty alerta.historicoAlerta}">
                                            ${alerta.historicoAlerta.gondolaId}
                                        </c:when>
                                        <c:when test="${not empty alerta.gondola}">
                                            ${alerta.gondola.id}
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <!-- ID HISTÓRICO DO PRODUTO -->
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty alerta.historicoAlerta}">
                                            ${alerta.historicoAlerta.produtoId}
                                        </c:when>
                                        <c:when test="${not empty alerta.produto}">
                                            ${alerta.produto.id}
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>${alerta.dataRuptura}</td>

                                <td>
                                    <c:out value="${alerta.descricaoAlerta}" />
                                </td>

                                <!-- STATUS FORMATADO -->
                                <td>
                                    <c:choose>
                                        <c:when test="${alerta.statusAlerta == 'CONCLUIDO'}">
                                            Concluído
                                        </c:when>
                                        <c:when test="${alerta.statusAlerta == 'NAO_CONCLUIDO'}">
                                            Não Concluído
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${alerta.statusAlerta}" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${alerta.statusAlerta == 'CONCLUIDO' and not empty alerta.dataResolucao}">
                                            <fmt:formatDate
                                                    value="${alerta.dataResolucao}"
                                                    pattern="dd/MM/yyyy HH:mm"
                                            />
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <td class="actions">

                                    <label
                                            for="update-alerta-${alerta.id}"
                                            class="update-button"
                                            onclick="limparFormularioAtualizacao(${alerta.id})"
                                    >
                                        Atualizar
                                    </label>

                                    <label
                                            for="delete-alerta-${alerta.id}"
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


<!-- POPUPS DE ATUALIZAÇÃO -->

<c:forEach var="alerta" items="${alertas}">

    <fmt:formatDate
            value="${alerta.dataResolucao}"
            pattern="yyyy-MM-dd'T'HH:mm"
            var="dataResolucaoFormatada"
    />

    <input
            type="checkbox"
            id="update-alerta-${alerta.id}"
            class="popup-toggle"
        ${idAlertaAtualizacao == alerta.id ? 'checked' : ''}
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Atualizar Alerta</h2>

                <label
                        for="update-alerta-${alerta.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${alerta.id})"
                >
                    &times;
                </label>

            </div>

            <form
                    id="update-alerta-form-${alerta.id}"
                    action="${pageContext.request.contextPath}/admin/alertas"
                    method="post"
                    class="add-alerta-form update-alerta-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${alerta.id}"
                >

                <div class="form-grid">

                    <!-- DESCRIÇÃO -->

                    <div class="form-field full-width">

                        <label for="descricao-${alerta.id}">
                            Descrição do alerta
                        </label>

                        <textarea
                                id="descricao-${alerta.id}"
                                name="descricaoAlerta"
                                data-original="<c:out value='${alerta.descricaoAlerta}' />"
                        ><c:out value="${idAlertaAtualizacao == alerta.id ? param.descricaoAlerta : alerta.descricaoAlerta}" /></textarea>

                        <c:if test="${idAlertaAtualizacao == alerta.id and not empty errosAtualizacao.descricaoAlerta}">
                            <div class="campo-erro">
                                <c:out value="${errosAtualizacao.descricaoAlerta}" />
                            </div>
                        </c:if>

                    </div>


                    <!-- STATUS -->

                    <div class="form-field full-width">

                        <label for="status-${alerta.id}">
                            Status do alerta
                        </label>

                        <select
                                id="status-${alerta.id}"
                                name="statusAlerta"
                                data-original="${alerta.statusAlerta}"
                        >

                            <option
                                    value="CONCLUIDO"
                                ${idAlertaAtualizacao == alerta.id
                                        ? (param.statusAlerta == 'CONCLUIDO' ? 'selected' : '')
                                        : (alerta.statusAlerta == 'CONCLUIDO' ? 'selected' : '')}
                            >
                                Concluído
                            </option>

                            <option
                                    value="NAO_CONCLUIDO"
                                ${idAlertaAtualizacao == alerta.id
                                        ? (param.statusAlerta == 'NAO_CONCLUIDO' ? 'selected' : '')
                                        : (alerta.statusAlerta == 'NAO_CONCLUIDO' ? 'selected' : '')}
                            >
                                Não Concluído
                            </option>

                        </select>

                        <c:if test="${idAlertaAtualizacao == alerta.id and not empty errosAtualizacao.statusAlerta}">
                            <div class="campo-erro">
                                <c:out value="${errosAtualizacao.statusAlerta}" />
                            </div>
                        </c:if>

                    </div>


                    <!-- DATA DA RESOLUÇÃO -->

                    <div
                            class="form-field full-width campo-resolucao"
                            id="campoDataResolucao-${alerta.id}"
                            style="${idAlertaAtualizacao == alerta.id
                                    ? (param.statusAlerta == 'CONCLUIDO' ? '' : 'display: none;')
                                    : (alerta.statusAlerta == 'CONCLUIDO' ? '' : 'display: none;')}"
                    >

                        <label for="data-resolucao-${alerta.id}">
                            Data da resolução
                        </label>

                        <input
                                type="datetime-local"
                                id="data-resolucao-${alerta.id}"
                                name="dataResolucao"
                                data-original="${dataResolucaoFormatada}"
                                value="${idAlertaAtualizacao == alerta.id ? param.dataResolucao : dataResolucaoFormatada}"
                            ${idAlertaAtualizacao == alerta.id
                                    ? (param.statusAlerta == 'CONCLUIDO' ? 'required' : '')
                                    : (alerta.statusAlerta == 'CONCLUIDO' ? 'required' : '')}
                        >

                        <c:if test="${idAlertaAtualizacao == alerta.id and not empty errosAtualizacao.dataResolucao}">
                            <div class="campo-erro">
                                <c:out value="${errosAtualizacao.dataResolucao}" />
                            </div>
                        </c:if>

                    </div>

                </div>

                <div class="popup-actions">

                    <label
                            for="update-alerta-${alerta.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${alerta.id})"
                    >
                        Cancelar
                    </label>

                    <button type="submit" class="save-button">
                        Atualizar Alerta
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>


<!-- POPUPS DE EXCLUSÃO -->

<c:forEach var="alerta" items="${alertas}">

    <input
            type="checkbox"
            id="delete-alerta-${alerta.id}"
            class="popup-toggle"
    >

    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>Deletar Alerta</h2>

                <label
                        for="delete-alerta-${alerta.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>

            <p>
                Você deseja realmente deletar o alerta
                <strong>${alerta.id}</strong>?
            </p>

            <div class="popup-actions">

                <label
                        for="delete-alerta-${alerta.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>

                <a
                        href="${pageContext.request.contextPath}/admin/alertas?acao=deletar&amp;id=${alerta.id}"
                        class="delete-confirm-button"
                >
                    Deletar
                </a>

            </div>

        </div>

    </div>

</c:forEach>


<!-- POPUP DE ADIÇÃO -->

<input
        type="checkbox"
        id="add-alerta-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>

<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>Adicionar Alerta</h2>

            <label
                    for="add-alerta-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>

        <form
                id="add-alerta-form"
                action="${pageContext.request.contextPath}/admin/alertas"
                method="post"
                class="add-alerta-form"
        >

            <div class="form-grid">

                <!-- USUÁRIO -->

                <div class="form-field">

                    <label for="idUsuario">
                        ID do usuário
                    </label>

                    <input
                            type="number"
                            id="idUsuario"
                            name="idUsuario"
                            min="1"
                            value="${param.idUsuario}"
                    >

                    <c:if test="${not empty erros.idUsuario}">
                        <div class="campo-erro">
                            <c:out value="${erros.idUsuario}" />
                        </div>
                    </c:if>

                </div>


                <!-- GÔNDOLA -->

                <div class="form-field">

                    <label for="idGondola">
                        ID da gôndola
                    </label>

                    <input
                            type="number"
                            id="idGondola"
                            name="idGondola"
                            min="1"
                            value="${param.idGondola}"
                    >

                    <c:if test="${not empty erros.idGondola}">
                        <div class="campo-erro">
                            <c:out value="${erros.idGondola}" />
                        </div>
                    </c:if>

                </div>


                <!-- PRODUTO -->

                <div class="form-field">

                    <label for="idProduto">
                        ID do produto
                    </label>

                    <input
                            type="number"
                            id="idProduto"
                            name="idProduto"
                            min="1"
                            value="${param.idProduto}"
                    >

                    <c:if test="${not empty erros.idProduto}">
                        <div class="campo-erro">
                            <c:out value="${erros.idProduto}" />
                        </div>
                    </c:if>

                </div>


                <!-- DATA DA RUPTURA -->

                <div class="form-field">

                    <label for="dataRuptura">
                        Data da ruptura
                    </label>

                    <input
                            type="date"
                            id="dataRuptura"
                            name="dataRuptura"
                            value="${param.dataRuptura}"
                    >

                    <c:if test="${not empty erros.dataRuptura}">
                        <div class="campo-erro">
                            <c:out value="${erros.dataRuptura}" />
                        </div>
                    </c:if>

                </div>


                <!-- DESCRIÇÃO -->

                <div class="form-field full-width">

                    <label for="descricaoAlerta">
                        Descrição do alerta
                    </label>

                    <textarea
                            id="descricaoAlerta"
                            name="descricaoAlerta"
                    ><c:out value="${param.descricaoAlerta}" /></textarea>

                    <c:if test="${not empty erros.descricaoAlerta}">
                        <div class="campo-erro">
                            <c:out value="${erros.descricaoAlerta}" />
                        </div>
                    </c:if>

                </div>


                <!-- STATUS -->

                <div class="form-field full-width">

                    <label for="statusAlerta">
                        Status do alerta
                    </label>

                    <select
                            id="statusAlerta"
                            name="statusAlerta"
                    >

                        <option
                                value=""
                        ${empty param.statusAlerta ? 'selected' : ''}
                        >
                            Selecione o status do alerta
                        </option>

                        <option
                                value="CONCLUIDO"
                        ${param.statusAlerta == 'CONCLUIDO' ? 'selected' : ''}
                        >
                            Concluído
                        </option>

                        <option
                                value="NAO_CONCLUIDO"
                        ${param.statusAlerta == 'NAO_CONCLUIDO' ? 'selected' : ''}
                        >
                            Não Concluído
                        </option>

                    </select>

                    <c:if test="${not empty erros.statusAlerta}">
                        <div class="campo-erro">
                            <c:out value="${erros.statusAlerta}" />
                        </div>
                    </c:if>

                </div>


                <!-- DATA DA RESOLUÇÃO -->

                <div
                        class="form-field full-width campo-resolucao"
                        id="campoDataResolucao"
                        style="${param.statusAlerta == 'CONCLUIDO' ? '' : 'display: none;'}"
                >

                    <label for="dataResolucao">
                        Data da resolução
                    </label>

                    <input
                            type="datetime-local"
                            id="dataResolucao"
                            name="dataResolucao"
                            value="${param.dataResolucao}"
                    ${param.statusAlerta == 'CONCLUIDO' ? 'required' : ''}
                    >

                    <c:if test="${not empty erros.dataResolucao}">
                        <div class="campo-erro">
                            <c:out value="${erros.dataResolucao}" />
                        </div>
                    </c:if>

                </div>

            </div>

            <div class="popup-actions">

                <label
                        for="add-alerta-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>

                <button type="submit" class="save-button">
                    Adicionar Alerta
                </button>

            </div>

        </form>

    </div>

</div>


<!-- POPUP DE SAÍDA -->

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


<script
        src="${pageContext.request.contextPath}/assets/js/alertas.js">
</script>

</body>
</html>
