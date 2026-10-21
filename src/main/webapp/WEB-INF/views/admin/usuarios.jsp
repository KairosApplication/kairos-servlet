<%@ page import="com.kairos.utils.Formatter" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Kairos - Usuários</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/usuarios.css">

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

        <a href="#" class="menu-item active">

            <span class="menu-icon">
                <img
                        src="${pageContext.request.contextPath}/assets/images/usuario.svg"
                        alt=""
                >
            </span>

            <span>Usuários</span>

        </a>

        <a href="${pageContext.request.contextPath}/admin/empresas" class="menu-item">

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

    <section class="search-area">

        <form
                action="${pageContext.request.contextPath}/admin/usuarios"
                method="get"
                class="search-form"
        >

            <input
                    type="text"
                    name="busca"
                    placeholder="Buscar usuário..."
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
                for="add-user-toggle"
                class="add-user-button"
        >
            + Adicionar Usuário
        </label>

    </section>


    <section class="content-card">

        <h1>Usuários</h1>

        <div class="table-wrapper">

            <table class="users-table ${empty usuarios ? 'table-empty' : ''}">

                <thead>

                <tr>

                    <th>ID</th>
                    <th>CPF</th>
                    <th>Nome</th>
                    <th>Sobrenome</th>
                    <th>Nascimento</th>
                    <th>CEP</th>
                    <th>Tipo de usuário</th>
                    <th>Email</th>
                    <th>Empresa</th>
                    <th>Ações</th>

                </tr>

                </thead>


                <tbody>

                <c:choose>

                    <c:when test="${empty usuarios}">

                        <tr>

                            <td
                                    colspan="10"
                                    class="empty-message"
                            >
                                Nenhum usuário cadastrado.
                            </td>

                        </tr>

                    </c:when>

                    <c:otherwise>

                        <c:forEach
                                var="usuario"
                                items="${usuarios}"
                        >

                            <tr class="user-row">

                                <td>${usuario.id}</td>

                                <td>${usuario.cpfFormatado}</td>

                                <td>${usuario.nome}</td>

                                <td>${usuario.sobrenome}</td>

                                <td>${usuario.dataNascimentoFormatada}</td>

                                <td>${usuario.cepFormatado}</td>

                                <td>${usuario.tipoUsuario}</td>

                                <td>${usuario.email}</td>

                                <td>${usuario.empresa.nome}</td>

                                <td class="actions">

                                    <c:choose>

                                        <c:when test="${usuario.tipoUsuario.name() == 'FUNCIONARIO'}">

                                            <input
                                                    type="checkbox"
                                                    id="details-${usuario.id}"
                                                    class="details-toggle"
                                            >

                                            <label
                                                    for="details-${usuario.id}"
                                                    class="details-button"
                                            >

                                                <span class="button-text">
                                                    Ver mais
                                                </span>

                                                <span class="arrow">
                                                    ▼
                                                </span>

                                            </label>

                                        </c:when>

                                        <c:otherwise>

                                            <span class="details-button-placeholder"></span>

                                        </c:otherwise>

                                    </c:choose>


                                    <label
                                            for="update-user-${usuario.id}"
                                            class="update-button"
                                            onclick="limparFormularioAtualizacao(${usuario.id})"
                                    >
                                        Atualizar
                                    </label>


                                    <label
                                            for="delete-user-${usuario.id}"
                                            class="delete-button"
                                    >
                                        Deletar
                                    </label>

                                </td>

                            </tr>


                            <c:if test="${usuario.tipoUsuario.name() == 'FUNCIONARIO'}">

                                <tr
                                        class="details-row"
                                        id="details-row-${usuario.id}"
                                >

                                    <td colspan="10">

                                        <div class="details-box">

                                            <div class="details-header">

                                                <h2>
                                                    Gôndolas Associadas
                                                </h2>

                                                <label
                                                        for="link-gondola-${usuario.id}"
                                                        class="link-button"
                                                >
                                                    Vincular
                                                </label>

                                            </div>


                                            <table class="details-table ${empty gondolasPorUsuario[usuario.id] ? 'table-empty' : ''}">

                                                <thead>

                                                <tr>

                                                    <th>Campo</th>
                                                    <th>Informação</th>
                                                    <th>Ação</th>

                                                </tr>

                                                </thead>


                                                <tbody>

                                                <c:choose>

                                                    <c:when test="${empty gondolasPorUsuario[usuario.id]}">

                                                        <tr>

                                                            <td
                                                                    colspan="3"
                                                                    class="empty-message"
                                                            >
                                                                Nenhuma gôndola associada a este usuário.
                                                            </td>

                                                        </tr>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <c:forEach
                                                                var="gondola"
                                                                items="${gondolasPorUsuario[usuario.id]}"
                                                        >

                                                            <tr>

                                                                <td>
                                                                    ID da gôndola
                                                                </td>

                                                                <td class="id-gondola">
                                                                        ${gondola.id}
                                                                </td>

                                                                <td
                                                                        rowspan="3"
                                                                        class="gondola-action-cell"
                                                                >

                                                                    <label
                                                                            for="unlink-gondola-${usuario.id}-${gondola.id}"
                                                                            class="unlink-button"
                                                                    >
                                                                        Desvincular
                                                                    </label>

                                                                </td>

                                                            </tr>


                                                            <tr>

                                                                <td>
                                                                    Capacidade máxima da gôndola
                                                                </td>

                                                                <td>
                                                                        ${gondola.capacidadeMaxima}
                                                                </td>

                                                            </tr>


                                                            <tr>

                                                                <td>
                                                                    Nome do setor
                                                                </td>

                                                                <td>
                                                                        ${gondola.setor.nome}
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

                            </c:if>

                        </c:forEach>

                    </c:otherwise>

                </c:choose>

                </tbody>

            </table>

        </div>

    </section>

</main>


<c:forEach
        var="usuario"
        items="${usuarios}"
>

    <input
            type="checkbox"
            id="update-user-${usuario.id}"
            class="popup-toggle"
        ${idUsuarioAtualizacao == usuario.id ? 'checked' : ''}
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Atualizar Usuário
                </h2>

                <label
                        for="update-user-${usuario.id}"
                        class="popup-close"
                        onclick="limparFormularioAtualizacao(${usuario.id})"
                >
                    &times;
                </label>

            </div>


            <form
                    id="update-user-form-${usuario.id}"
                    action="${pageContext.request.contextPath}/admin/usuarios"
                    method="post"
                    class="add-user-form"
            >

                <input
                        type="hidden"
                        name="id"
                        value="${usuario.id}"
                >


                <div class="form-grid">

                    <div class="form-field">

                        <label for="nome-${usuario.id}">
                            Nome
                        </label>

                        <input
                                type="text"
                                id="nome-${usuario.id}"
                                name="nome"
                                value="${idUsuarioAtualizacao == usuario.id ? param.nome : usuario.nome}"
                                data-original="${usuario.nome}"
                        >

                        <c:if test="${idUsuarioAtualizacao == usuario.id and not empty errosAtualizacao.nome}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.nome}
                            </div>

                        </c:if>

                    </div>


                    <div class="form-field">

                        <label for="sobrenome-${usuario.id}">
                            Sobrenome
                        </label>

                        <input
                                type="text"
                                id="sobrenome-${usuario.id}"
                                name="sobrenome"
                                data-original="${usuario.sobrenome}"
                                value="${idUsuarioAtualizacao == usuario.id ? param.sobrenome : usuario.sobrenome}"
                        >

                        <c:if test="${idUsuarioAtualizacao == usuario.id and not empty errosAtualizacao.sobrenome}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.sobrenome}
                            </div>

                        </c:if>

                    </div>


                    <div class="form-field">

                        <label for="cep-${usuario.id}">
                            CEP
                        </label>

                        <input
                                type="text"
                                id="cep-${usuario.id}"
                                name="cep"
                                data-original="${usuario.cep}"
                                value="${idUsuarioAtualizacao == usuario.id ? param.cep : usuario.cep}"
                        >

                        <c:if test="${idUsuarioAtualizacao == usuario.id and not empty errosAtualizacao.cep}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.cep}
                            </div>

                        </c:if>

                    </div>


                    <div class="form-field">

                        <label for="email-${usuario.id}">
                            Email
                        </label>

                        <input
                                type="email"
                                id="email-${usuario.id}"
                                name="email"
                                data-original="${usuario.email}"
                                value="${idUsuarioAtualizacao == usuario.id ? param.email : usuario.email}"
                        >

                        <c:if test="${idUsuarioAtualizacao == usuario.id and not empty errosAtualizacao.email}">

                            <div class="campo-erro">
                                    ${errosAtualizacao.email}
                            </div>

                        </c:if>

                    </div>

                </div>


                <div class="popup-actions">

                    <label
                            for="update-user-${usuario.id}"
                            class="cancel-button"
                            onclick="limparFormularioAtualizacao(${usuario.id})"
                    >
                        Cancelar
                    </label>

                    <button
                            type="submit"
                            class="save-button"
                    >
                        Atualizar Usuário
                    </button>

                </div>

            </form>

        </div>

    </div>

</c:forEach>


<c:forEach
        var="usuario"
        items="${usuarios}"
>

    <c:if test="${usuario.tipoUsuario.name() == 'FUNCIONARIO'}">

        <input
                type="checkbox"
                id="link-gondola-${usuario.id}"
                class="popup-toggle"
            ${idUsuarioVincular == usuario.id ? 'checked' : ''}
        >


        <div class="popup-overlay">

            <div class="popup">

                <div class="popup-header">

                    <h2>
                        Vincular Gôndola
                    </h2>

                    <label
                            for="link-gondola-${usuario.id}"
                            class="popup-close"
                            onclick="limparFormularioVincular(${usuario.id})"
                    >
                        &times;
                    </label>

                </div>


                <form
                        id="link-gondola-form-${usuario.id}"
                        action="${pageContext.request.contextPath}/admin/usuarios-gondolas"
                        method="post"
                        class="add-user-form"
                >

                    <input
                            type="hidden"
                            name="acao"
                            value="vincular"
                    >

                    <input
                            type="hidden"
                            name="idUsuario"
                            value="${usuario.id}"
                    >


                    <div class="form-grid">

                        <div class="form-field full-width">

                            <label for="id-gondola-${usuario.id}">
                                ID da gôndola
                            </label>

                            <input
                                    type="number"
                                    id="id-gondola-${usuario.id}"
                                    name="idGondola"
                                    min="1"
                                    value="${idUsuarioVincular == usuario.id ? idGondolaVincular : ''}"
                                    required
                            >


                            <c:if test="${idUsuarioVincular == usuario.id and not empty erroVincularGondola}">

                                <div class="campo-erro">
                                        ${erroVincularGondola}
                                </div>

                            </c:if>

                        </div>

                    </div>


                    <div class="popup-actions">

                        <label
                                for="link-gondola-${usuario.id}"
                                class="cancel-button"
                                onclick="limparFormularioVincular(${usuario.id})"
                        >
                            Cancelar
                        </label>

                        <button
                                type="submit"
                                class="save-button"
                        >
                            Vincular Gôndola
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </c:if>

</c:forEach>


<c:forEach
        var="usuario"
        items="${usuarios}"
>

    <c:if test="${usuario.tipoUsuario.name() == 'FUNCIONARIO'}">

        <c:forEach
                var="gondola"
                items="${gondolasPorUsuario[usuario.id]}"
        >

            <input
                    type="checkbox"
                    id="unlink-gondola-${usuario.id}-${gondola.id}"
                    class="popup-toggle"
            >


            <div class="popup-overlay">

                <div class="popup">

                    <div class="popup-header">

                        <h2>
                            Desvincular Gôndola
                        </h2>

                        <label
                                for="unlink-gondola-${usuario.id}-${gondola.id}"
                                class="popup-close"
                        >
                            &times;
                        </label>

                    </div>


                    <p>
                        Você deseja realmente desvincular a gôndola
                            ${gondola.id}
                        deste usuário?
                    </p>


                    <form
                            action="${pageContext.request.contextPath}/admin/usuarios-gondolas"
                            method="post"
                            class="add-user-form"
                    >

                        <input
                                type="hidden"
                                name="acao"
                                value="desvincular"
                        >

                        <input
                                type="hidden"
                                name="idUsuario"
                                value="${usuario.id}"
                        >

                        <input
                                type="hidden"
                                name="idGondola"
                                value="${gondola.id}"
                        >


                        <div class="popup-actions">

                            <label
                                    for="unlink-gondola-${usuario.id}-${gondola.id}"
                                    class="cancel-button"
                            >
                                Cancelar
                            </label>

                            <button
                                    type="submit"
                                    class="unlink-confirm-button"
                            >
                                Desvincular
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </c:forEach>

    </c:if>

</c:forEach>


<c:forEach
        var="usuario"
        items="${usuarios}"
>

    <input
            type="checkbox"
            id="delete-user-${usuario.id}"
            class="popup-toggle"
    >


    <div class="popup-overlay">

        <div class="popup">

            <div class="popup-header">

                <h2>
                    Deletar Usuário
                </h2>

                <label
                        for="delete-user-${usuario.id}"
                        class="popup-close"
                >
                    &times;
                </label>

            </div>


            <p>
                Você deseja realmente deletar o usuário
                <strong>
                        ${usuario.nome} ${usuario.sobrenome}
                </strong>?
            </p>


            <div class="popup-actions">

                <label
                        for="delete-user-${usuario.id}"
                        class="cancel-button"
                >
                    Cancelar
                </label>

                <a
                        href="${pageContext.request.contextPath}/admin/usuarios?acao=deletar&amp;id=${usuario.id}"
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
        id="add-user-toggle"
        class="popup-toggle"
${not empty erros ? 'checked' : ''}
>


<div class="popup-overlay">

    <div class="popup">

        <div class="popup-header">

            <h2>
                Adicionar Usuário
            </h2>

            <label
                    for="add-user-toggle"
                    class="popup-close"
                    onclick="limparFormularioAdicionar()"
            >
                &times;
            </label>

        </div>


        <form
                id="add-user-form"
                action="${pageContext.request.contextPath}/admin/usuarios"
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
                            value="${not empty erros ? param.nome : ''}"
                    >

                    <c:if test="${not empty erros.nome}">

                        <div class="campo-erro">
                                ${erros.nome}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="sobrenome">
                        Sobrenome
                    </label>

                    <input
                            type="text"
                            id="sobrenome"
                            name="sobrenome"
                            value="${not empty erros ? param.sobrenome : ''}"
                    >

                    <c:if test="${not empty erros.sobrenome}">

                        <div class="campo-erro">
                                ${erros.sobrenome}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="cpf">
                        CPF
                    </label>

                    <input
                            type="text"
                            id="cpf"
                            name="cpf"
                            value="${not empty erros ? param.cpf : ''}"
                    >

                    <c:if test="${not empty erros.cpf}">

                        <div class="campo-erro">
                                ${erros.cpf}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="nascimento">
                        Data de nascimento
                    </label>

                    <input
                            type="date"
                            id="nascimento"
                            name="nascimento"
                            value="${not empty erros ? param.nascimento : ''}"
                    >

                    <c:if test="${not empty erros.nascimento}">

                        <div class="campo-erro">
                                ${erros.nascimento}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="cep">
                        CEP
                    </label>

                    <input
                            type="text"
                            id="cep"
                            name="cep"
                            value="${not empty erros ? param.cep : ''}"
                    >

                    <c:if test="${not empty erros.cep}">

                        <div class="campo-erro">
                                ${erros.cep}
                        </div>

                    </c:if>

                </div>


                <div class="form-field">

                    <label for="email">
                        Email
                    </label>

                    <input
                            type="email"
                            id="email"
                            name="email"
                            value="${not empty erros ? param.email : ''}"
                    >

                    <c:if test="${not empty erros.email}">

                        <div class="campo-erro">
                                ${erros.email}
                        </div>

                    </c:if>

                </div>


                <div class="form-field full-width">

                    <label for="tipoUsuario">
                        Tipo de usuário
                    </label>

                    <select
                            id="tipoUsuario"
                            name="tipoUsuario"
                    >

                        <option
                                value=""
                        ${empty erros or empty param.tipoUsuario ? 'selected' : ''}
                        >
                            Selecione o tipo de usuário
                        </option>

                        <option
                                value="FUNCIONARIO"
                        ${not empty erros and param.tipoUsuario == 'FUNCIONARIO' ? 'selected' : ''}
                        >
                            Funcionário
                        </option>

                        <option
                                value="GERENTE"
                        ${not empty erros and param.tipoUsuario == 'GERENTE' ? 'selected' : ''}
                        >
                            Gerente
                        </option>

                    </select>

                    <c:if test="${not empty erros.tipoUsuario}">

                        <div class="campo-erro">
                                ${erros.tipoUsuario}
                        </div>

                    </c:if>

                </div>


                <div class="form-field full-width">

                    <label for="empresa">
                        Nome da empresa
                    </label>

                    <input
                            type="text"
                            id="empresa"
                            name="nomeEmpresa"
                            value="${not empty erros ? param.nomeEmpresa : ''}"
                    >

                    <c:if test="${not empty erros.empresa}">

                        <div class="campo-erro">
                                ${erros.empresa}
                        </div>

                    </c:if>

                </div>

            </div>


            <div class="popup-actions">

                <label
                        for="add-user-toggle"
                        class="cancel-button"
                        onclick="limparFormularioAdicionar()"
                >
                    Cancelar
                </label>

                <button
                        type="submit"
                        class="save-button"
                >
                    Adicionar Usuário
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
        src="${pageContext.request.contextPath}/assets/js/usuarios.js">
</script>

</body>

</html>